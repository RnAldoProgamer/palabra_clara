package com.devspark.palabra_clara.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TextoVozComponent {

    @Value("${elevenlabs.api.key}")
    private String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(TextoVozComponent.class);

    public ResponseEntity<byte[]> textoVoz(String texto) {
        try {
            String url = "https://api.elevenlabs.io/v1/text-to-speech/EXAVITQu4vr4xnSDxMaL";

            logger.info("Procesando texto a voz: {}", texto);
            logger.info("URL: {}", url);
            logger.info("API Key: {}", apiKey);
            // Configuración de encabezados
            HttpHeaders headers = new HttpHeaders();
            headers.set("xi-api-key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construcción del cuerpo de la solicitud
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model_id", "eleven_multilingual_v2");
            requestBody.put("text", texto);

            Map<String, Object> voiceSettings = new HashMap<>();
            voiceSettings.put("use_speaker_boost", false);
            voiceSettings.put("similarity_boost", 1);
            voiceSettings.put("stability", 1);

            requestBody.put("voice_settings", voiceSettings);

            // Conversión a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // Ejecución de la solicitud
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                byte[].class
            );

            // Verificación de respuesta exitosa
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(response.getBody());
            } else {
                logger.error("Error en la API: {}", response.getStatusCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error en la API: " + response.getStatusCode()).getBytes());
            }
        } catch (HttpClientErrorException e) {
            logger.error("Error en la API: {} - Headers: {} - Body: {}",
                e.getStatusCode(),
                e.getResponseHeaders(),
                e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Error en la API: " + e.getStatusCode()).getBytes());
        } catch (Exception e) {
            logger.error("Error al procesar texto a voz", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Error al traducir el texto: " + e.getMessage()).getBytes());
        }
    }
}
