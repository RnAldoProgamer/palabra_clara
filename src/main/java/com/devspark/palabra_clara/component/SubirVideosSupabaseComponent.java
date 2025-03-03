package com.devspark.palabra_clara.component;

import com.devspark.palabra_clara.repository.PalabraRepository;
import com.devspark.palabra_clara.services.PalabraServiceImpl;
import com.devspark.palabra_clara.util.StaticConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.devspark.palabra_clara.util.StaticConstants.*;

@Component
public class SubirVideosSupabaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(PalabraServiceImpl.class);
  private final PalabraRepository palabraRepository;

  @Autowired
  public SubirVideosSupabaseComponent(PalabraRepository palabraRepository) {
    this.palabraRepository = palabraRepository;
  }

  public String uploadFileToSupabaseS3(File file, String fileName) throws IOException {
    // Obtener el nombre del video sin la extensión
    String videoName;
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex != -1) {
      videoName = fileName.substring(0, dotIndex); // Ejemplo: "video" de "video.mp4"
    } else {
      videoName = fileName; // Si no hay extensión, usa el nombre completo
    }

    // Construir la clave con la "carpeta" (prefijo)
    String key = videoName + "/" + fileName; // Ejemplo: "video/video.mp4"

    // Construir la URL completa para la API de Supabase
    String url = SUPABASE_URL + SUPABASE_BUCKET + "/" + key;

    // Configurar los encabezados HTTP
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + SUPABASE_JWT_TOKEN);
    headers.set("apikey", SUPABASE_API_KEY); // Necesario junto con el token
    headers.set("Content-Type", "application/octet-stream"); // Tipo MIME genérico para archivos binarios

    // Leer el archivo como bytes
    byte[] fileBytes = Files.readAllBytes(file.toPath());

    // Crear el cuerpo de la solicitud con los datos del archivo
    HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);

    // Enviar la solicitud a Supabase usando RestTemplate
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.POST,
        requestEntity,
        String.class
    );

    // Verificar si la subida fue exitosa
    if (response.getStatusCode().is2xxSuccessful()) {
      // Devolver la URL pública del archivo subido
      return "https://ufloszpkhtuczintyaya.supabase.co/storage/v1/object/" + SUPABASE_BUCKET + "/" + key;
    } else {
      throw new IOException("Error al subir el archivo a Supabase: " + response.getBody());
    }
  }

  /**
   * Método para descargar un video almacenado en Supabase Storage
   * basado en una URL de Supabase.
   *
   * @param url URL completa del video en Supabase Storage
   * @return ResponseEntity con el recurso de video para descarga
   */
  public ResponseEntity<Resource> descargarVideoDeSupabase(String url) {
    try {
      // Validar que la URL pertenezca a Supabase
      if (!url.contains("supabase.co/storage")) {
        logger.error("URL no válida para Supabase Storage");
        return ResponseEntity.badRequest().body(null);
      }

      // Configurar los encabezados HTTP para la solicitud a Supabase
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.set("Authorization", "Bearer " + SUPABASE_JWT_TOKEN);
      requestHeaders.set("apikey", SUPABASE_API_KEY);

      // Crear la entidad de la solicitud
      HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);

      // Crear RestTemplate para realizar la solicitud HTTP
      RestTemplate restTemplate = new RestTemplate();

      // Configurar para recibir el archivo como array de bytes
      ResponseEntity<byte[]> supabaseResponse = restTemplate.exchange(
          url,
          HttpMethod.GET,
          requestEntity,
          byte[].class
      );

      // Verificar si la descarga fue exitosa
      if (supabaseResponse.getStatusCode().is2xxSuccessful() && supabaseResponse.getBody() != null) {
        // Obtener el nombre del archivo de la URL
        String fileName = extraerNombreArchivo(url);

        // Crear un recurso a partir de los bytes descargados
        ByteArrayResource resource = new ByteArrayResource(supabaseResponse.getBody());

        // Configurar los encabezados de la respuesta
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, String.format(StaticConstants.CONTENT_DISPOSITION, fileName));

        // Devolver la respuesta con el video
        return ResponseEntity.ok()
            .headers(responseHeaders)
            .contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType(StaticConstants.TIPO_VIDEO))
            .body(resource);
      } else {
        logger.error("Error al descargar el video de Supabase: " + supabaseResponse.getStatusCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    } catch (Exception e) {
      logger.error(StaticConstants.MENSAJE_DESCARGAR_ERROR, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }



  /**
   * Método auxiliar para extraer el nombre del archivo de la URL de Supabase
   *
   * @param url URL completa del video en Supabase Storage
   * @return Nombre del archivo con extensión
   */
  private String extraerNombreArchivo(String url) {
    // Extraer el nombre del archivo de la URL
    String[] partes = url.split("/");
    String nombreCompleto = partes[partes.length - 1];

    // Decodificar en caso de que tenga caracteres URL-encoded
    try {
      nombreCompleto = URLDecoder.decode(nombreCompleto, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      logger.warn("No se pudo decodificar el nombre del archivo", e);
    }

    return nombreCompleto;
  }
}
