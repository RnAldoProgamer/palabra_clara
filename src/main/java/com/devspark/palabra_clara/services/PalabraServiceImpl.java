package com.devspark.palabra_clara.services;

import com.devspark.palabra_clara.component.SubirVideosSupabaseComponent;
import com.devspark.palabra_clara.component.TextoVozComponent;
import com.devspark.palabra_clara.component.TraducirBraileComponent;
import com.devspark.palabra_clara.component.TraducirPalabraComponent;
import com.devspark.palabra_clara.entity.PalabraEntity;
import com.devspark.palabra_clara.entity.VarianteEntity;
import com.devspark.palabra_clara.model.PalabraRequestBean;
import com.devspark.palabra_clara.model.TraduccionPathResponseBean;
import com.devspark.palabra_clara.model.VarianteRequestBean;
import com.devspark.palabra_clara.repository.PalabraRepository;
import com.devspark.palabra_clara.repository.VarianteRepository;
import com.devspark.palabra_clara.util.ConfiguracionesCalidad;
import com.devspark.palabra_clara.util.GenericResponse;
import com.devspark.palabra_clara.util.StaticConstants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PalabraServiceImpl implements IPalabraService{

    private final PalabraRepository palabraRepository;
    private final VarianteRepository varianteRepository;
    private final TextoVozComponent textoVozComponent;
    private final TraducirPalabraComponent traducirPalabraComponent;
    private static final Logger logger = LoggerFactory.getLogger(PalabraServiceImpl.class);
    private final TraducirBraileComponent traducirBraileComponent;
    private final Encoder ffmpegEncoder;
    private final SubirVideosSupabaseComponent subirVideosSupabaseComponent;

    @Autowired
    public PalabraServiceImpl(PalabraRepository palabraRepository, VarianteRepository varianteRepository, TextoVozComponent textoVozComponent, TraducirPalabraComponent traducirPalabraComponent, TraducirBraileComponent traducirBraileComponent, Encoder ffmpegEncoder, SubirVideosSupabaseComponent subirVideosSupabaseComponent) {
        this.palabraRepository = palabraRepository;
        this.varianteRepository = varianteRepository;
        this.textoVozComponent = textoVozComponent;
        this.traducirPalabraComponent = traducirPalabraComponent;
        this.traducirBraileComponent = traducirBraileComponent;
        this.ffmpegEncoder = ffmpegEncoder;
        this.subirVideosSupabaseComponent = subirVideosSupabaseComponent;
    }

    public GenericResponse traducirPalabraGroq(String palabra) {
        try {
        Optional<PalabraEntity> palabraEntity = palabraRepository.findByPalabraIgnoreCase(palabra);
        String respuesta = traducirPalabraComponent.traducirPalabra(palabra);
        if (palabraEntity.isPresent()) {
            return new GenericResponse(0, StaticConstants.MENSAJE_TRADUCIR_OK, new TraduccionPathResponseBean(palabraEntity.get().getPath() != null, respuesta));
        }

        Optional<VarianteEntity> varianteEntity = varianteRepository.findByVarianteIgnoreCase(palabra);
        if (varianteEntity.isPresent()) {
            return new GenericResponse(0, StaticConstants.MENSAJE_TRADUCIR_OK, new TraduccionPathResponseBean(varianteEntity.get().getPalabra().getPath() != null, respuesta));
        }
        return new GenericResponse(0, StaticConstants.MENSAJE_TRADUCIR_OK, new TraduccionPathResponseBean(false, respuesta));
        } catch (Exception e) {
            return new GenericResponse(1, StaticConstants.MENSAJE_TRADUCIR_ERROR, e.getMessage());
        }
    }

    @Override
    public GenericResponse traducirBraile(String texto) {
        try {
            String respuesta = traducirBraileComponent.traducirBraile(texto);
            return new GenericResponse(0, StaticConstants.MENSAJE_TRADUCIR_OK, respuesta);
        } catch (Exception e) {
            return new GenericResponse(1, StaticConstants.MENSAJE_TRADUCIR_ERROR, e.getMessage());
        }
    }

    @Override
    public GenericResponse guardarPalabra(PalabraRequestBean palabraRequest, Map<String, MultipartFile> video) {
        try {
            if (palabraRequest == null) {
                throw new IllegalArgumentException(StaticConstants.ERROR_PALABRA_NULL);
            }

            if (video != null && video.containsKey(StaticConstants.PALABRA_VIDEO)) {
                palabraRequest.setPath(comprimirVideo(video.get(StaticConstants.PALABRA_VIDEO),ConfiguracionesCalidad.WEBM,palabraRequest));
            }

            return new GenericResponse(0,StaticConstants.MENSAJE_GUARDAR_PALABRA_OK,palabraRepository.save(beanToEntity(palabraRequest)));

        } catch (IOException | IllegalArgumentException e) {
            return new GenericResponse(1, StaticConstants.MENSAJE_GUARDAR_PALABRA_ERROR, e.getMessage());
        }
    }

    @Override
    public GenericResponse actualizarPalabra(PalabraRequestBean palabraRequest, Map<String, MultipartFile> video) {
        try {
            if (palabraRequest == null) {
                throw new IllegalArgumentException(StaticConstants.ERROR_PALABRA_NULL);
            }

            if (video == null || !video.containsKey(StaticConstants.PALABRA_VIDEO)) {
                throw new IllegalArgumentException(StaticConstants.MENSAJE_VIDEO_NULL);
            }

            Optional<PalabraEntity> palabraEntity = palabraRepository.findByPalabraIgnoreCase(beanToEntity(palabraRequest).getPalabra());
            if (palabraEntity.isPresent()) {
                PalabraRequestBean palabraRequestBean = entityToBan(palabraEntity);
                palabraRequestBean.setPath(comprimirVideo(video.get(StaticConstants.PALABRA_VIDEO),ConfiguracionesCalidad.WEBM,palabraRequest));
                return new GenericResponse(0,StaticConstants.MENSAJE_GUARDAR_PALABRA_OK,palabraRepository.save(beanToEntity(palabraRequestBean)));
            }

            List<VarianteEntity> variantesEncontradas = new ArrayList<>();
            for (VarianteRequestBean variante : palabraRequest.getVariantes()) {
                Optional<VarianteEntity> varianteEntity = varianteRepository.findByVarianteIgnoreCase(variante.getVariante());
                if (varianteEntity.isPresent()) {
                    variantesEncontradas.add(varianteEntity.get());
                }
            }

            if (!variantesEncontradas.isEmpty()) {
                List<VarianteRequestBean> variantesActualizadas = variantesEncontradas.stream()
                        .map(varianteEntity -> {
                            VarianteRequestBean varianteRequest = new VarianteRequestBean();
                            varianteRequest.setVariante(varianteEntity.getVariante());
                            return varianteRequest;
                        })
                        .toList();

                palabraRequest.setVariantes(variantesActualizadas);
                palabraRequest.setPath(comprimirVideo(video.get(StaticConstants.PALABRA_VIDEO),ConfiguracionesCalidad.WEBM,palabraRequest));

                return new GenericResponse(0,StaticConstants.MENSAJE_GUARDAR_PALABRA_OK,palabraRepository.save(beanToEntity(palabraRequest)));
            }

            return new GenericResponse(1, StaticConstants.MENSAJE_ACTUALIZAR_PALABRA_ERROR, null);

        } catch (Exception e) {
            return new GenericResponse(1, StaticConstants.MENSAJE_GUARDAR_PALABRA_ERROR, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> descargarVideo(String palabra) {
        try {
            Optional<PalabraEntity> palabraEntityOpt = palabraRepository.findByPalabraIgnoreCase(palabra);

            if (palabraEntityOpt.isPresent()) {
                String path = palabraEntityOpt.get().getPath();
                Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
                Resource resource = new UrlResource(fileStorageLocation.toUri());

                if (resource.exists()) {
                    String fileName = fileStorageLocation.getFileName().toString();
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(StaticConstants.CONTENT_DISPOSITION, fileName));

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentLength(resource.contentLength())
                            .contentType(MediaType.parseMediaType(StaticConstants.TIPO_VIDEO))
                            .body(resource);
                } else {
                    logger.error(StaticConstants.MENSAJE_DESCARGAR_ADVERTENCIA);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            logger.error(StaticConstants.MENSAJE_DESCARGAR_ERROR, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public List<String> obtenerTodasLasPalabras() {
        return palabraRepository.findAllPalabrasYVariantes();
    }

    @Override
    public ResponseEntity<byte[]> convertirTextoAVoz(String palabra) {
        return textoVozComponent.textoVoz(palabra);
    }

//    Funciones de Ayuda

    public String comprimirVideo(MultipartFile inputVideo, ConfiguracionesCalidad format, PalabraRequestBean palabra) throws IOException {
        String tempDirectoryPath = StaticConstants.RUTA_CARPETA_TEMPORAL;
        File tempDirectory = new File(tempDirectoryPath);

        if (!tempDirectory.exists() && !tempDirectory.mkdirs()) {
            throw new IOException(StaticConstants.ERROR_CREAR_DIRECTORIO + tempDirectoryPath);
        }

        File source = File.createTempFile("video_", StaticConstants.TERMINACION_TMP);
        String fileName = palabra.getPalabra() + StaticConstants.PUNTO_DOT + format.getExtension();
        File target = new File(tempDirectoryPath + File.separator + fileName);

        try {
            inputVideo.transferTo(source);

            EncodingAttributes attrs = new EncodingAttributes()
                .setVideoAttributes(format.getVideoAttributes())
                .setOutputFormat(format.getExtension());

            // Usamos el encoder inyectado
            ffmpegEncoder.encode(new MultimediaObject(source), target, attrs);

            if (!target.exists() || target.length() == 0) {
                throw new IOException(StaticConstants.MENSAJE_ERROR_COMPRIMIR_VIDEO);
            }

            return subirVideosSupabaseComponent.uploadFileToSupabaseS3(target, fileName);

        } catch (EncoderException e) {
            logger.error(StaticConstants.MENSAJE_ERROR_COMPRIMIR_VIDEO, e);
            throw new IOException("Error en la compresión: " + e.getMessage());
        } finally {
            cleanTempFile(source);
            cleanTempFile(target);
        }
    }

    private void cleanTempFile(File file) {
        try {
            if (file != null && file.exists()) {
                Files.delete(file.toPath());
            }
        } catch (IOException e) {
            logger.warn("Error eliminando temporal: {}", file.getAbsolutePath(), e);
        }
    }

    private PalabraEntity beanToEntity(PalabraRequestBean palabraRequest) {
        return new ModelMapper().map(palabraRequest, PalabraEntity.class);
    }

    private PalabraRequestBean entityToBan(Optional<PalabraEntity> palabraEntity){
        return new ModelMapper().map(palabraEntity,PalabraRequestBean.class);
    }

}