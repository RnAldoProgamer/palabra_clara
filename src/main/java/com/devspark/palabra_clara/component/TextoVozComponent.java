package com.devspark.palabra_clara.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TextoVozComponent {

    private static final Logger logger = LoggerFactory.getLogger(TextoVozComponent.class);
    public ResponseEntity<byte[]> textoVoz(String texto) {
        try {
            String apiKey = "sk_eea734ef621e8c52284b06c73e384ffda24a977750ada3eb";
            String url = "https://api.elevenlabs.io/v1/text-to-speech/EXAVITQu4vr4xnSDxMaL";

            HttpHeaders headers = new HttpHeaders();
            headers.set("xi-api-key", apiKey);
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model_id", "eleven_multilingual_v2");
            requestBody.put("text", texto);

            Map<String, Object> voiceSettings = new HashMap<>();
            voiceSettings.put("use_speaker_boost", false);
            voiceSettings.put("similarity_boost", 1);
            voiceSettings.put("stability", 1);

            requestBody.put("voice_settings", voiceSettings);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(response.getBody());
        } catch (Exception e) {
            logger.error("Error al procesar texto a voz", e); // Loguea la excepción completa
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al traducir el texto: " + e.getMessage().getBytes()).getBytes());
        }
    }
}
