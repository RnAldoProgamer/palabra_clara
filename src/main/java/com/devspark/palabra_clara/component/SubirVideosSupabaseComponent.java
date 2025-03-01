package com.devspark.palabra_clara.component;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.devspark.palabra_clara.util.StaticConstants.*;

public class SubirVideosSupabaseComponent {

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
      return "https://ufloszpkhtuczintyaya.supabase.co/storage/v1/object/public/" + SUPABASE_BUCKET + "/" + key;
    } else {
      throw new IOException("Error al subir el archivo a Supabase: " + response.getBody());
    }
  }
}
