package com.devspark.palabra_clara.component;

import com.devspark.palabra_clara.model.RespuestaGroqBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TraducirPalabraComponent {

    public String traducirPalabra(String texto){
        try {
            String apiKey = "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"; // Reemplaza con tu clave de API
            String url = "https://api.groq.com/openai/v1/chat/completions"; // Reemplaza con el endpoint real de la API de Groq

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.1-8b-instant");
            requestBody.put("messages", new Object[] {
                    new HashMap<String, String>() {{
                        put("role", "user");
                        put("content", "Vas a actuar como traductor. Te proporcionaré una palabra o frase en español, y necesito que me la traduzcas al inglés, alemán y francés. Para el alemán, por favor, proporciona el artículo y la traducción de la palabra por separado. Devuélveme solo el JSON con las traducciones, sin ningún texto adicional.\n\n" +
                                "El JSON generado debe ser válido y estar bien formado. Evita errores comunes como: \n" +
                                "- Faltan comas entre elementos.\n" +
                                "- Llaves o corchetes mal cerrados.\n" +
                                "- Caracteres inesperados o mal ubicados.\n\n" +
                                "Ejemplo de formato JSON esperado:\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"es\": \"Patata\",\n" +
                                "  \"en\": \"Potato\",\n" +
                                "  \"de\": {\n" +
                                "    \"art\": \"Die\",\n" +
                                "    \"trans\": \"Kartoffel\"\n" +
                                "  },\n" +
                                "  \"fr\": \"Pomme de terre\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "Otro ejemplo:\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"es\": \"Casa\",\n" +
                                "  \"en\": \"House\",\n" +
                                "  \"de\": {\n" +
                                "    \"art\": \"Das\",\n" +
                                "    \"trans\": \"Haus\"\n" +
                                "  },\n" +
                                "  \"fr\": \"Maison\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "Y otro más:\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"es\": \"Perro\",\n" +
                                "  \"en\": \"Dog\",\n" +
                                "  \"de\": {\n" +
                                "    \"art\": \"Der\",\n" +
                                "    \"trans\": \"Hund\"\n" +
                                "  },\n" +
                                "  \"fr\": \"Chien\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "Otro ejemplo adicional:\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"es\": \"Gato\",\n" +
                                "  \"en\": \"Cat\",\n" +
                                "  \"de\": {\n" +
                                "    \"art\": \"Der\",\n" +
                                "    \"trans\": \"Kater\"\n" +
                                "  },\n" +
                                "  \"fr\": \"Chat\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "Ejemplo con frase larga:\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"es\": \"Fuerza Regida\",\n" +
                                "  \"en\": \"Regulated Force\",\n" +
                                "  \"de\": {\n" +
                                "    \"art\": \"Die\",\n" +
                                "    \"trans\": \"Regulierte Kraft\"\n" +
                                "  },\n" +
                                "  \"fr\": \"Force Régulée\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "Es importante que solo devuelvas el JSON con las traducciones, sin ningún texto adicional. No incluyas ninguna explicación o comentario, solo el JSON.\n\n" +
                                "La palabra en español es: " + texto);
                    }}
            });

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Parsear la respuesta
            RespuestaGroqBean groqResponse = objectMapper.readValue(response.getBody(), RespuestaGroqBean.class);
            return groqResponse.getEleccionGroqEntities().get(0).getMensajeGroqBean().getContenido();
        } catch (Exception e) {
            return "Error al obtener la respuesta: " + e.getMessage();
        }
    }
}
