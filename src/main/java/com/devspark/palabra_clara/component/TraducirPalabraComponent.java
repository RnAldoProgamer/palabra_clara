package com.devspark.palabra_clara.component;

import com.devspark.palabra_clara.model.RespuestaGroqBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
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
                    put("content", "Vas a actuar como traductor. Te proporcionaré una palabra o frase en español, y necesito que me la traduzcas al inglés, alemán y francés. Para el alemán, por favor, proporciona el artículo y la traducción de la palabra por separado. Además, incluye una breve descripción de la palabra en cada idioma (español, inglés, alemán y francés). Devuélveme solo el JSON con las traducciones y descripciones, sin ningún texto adicional.\n\n" +
                        "El JSON generado debe ser válido y estar bien formado. Evita errores comunes como: \n" +
                        "- Faltan comas entre elementos.\n" +
                        "- Llaves o corchetes mal cerrados.\n" +
                        "- Caracteres inesperados o mal ubicados.\n\n" +
                        "Ejemplo de formato JSON esperado:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"es\": {\n" +
                        "    \"word\": \"Patata\",\n" +
                        "    \"desc\": \"Tubérculo comestible de origen sudamericano\"\n" +
                        "  },\n" +
                        "  \"en\": {\n" +
                        "    \"word\": \"Potato\",\n" +
                        "    \"desc\": \"Edible tuber native to South America\"\n" +
                        "  },\n" +
                        "  \"de\": {\n" +
                        "    \"art\": \"Die\",\n" +
                        "    \"trans\": \"Kartoffel\",\n" +
                        "    \"desc\": \"Essbare Knolle aus Südamerika\"\n" +
                        "  },\n" +
                        "  \"fr\": {\n" +
                        "    \"word\": \"Pomme de terre\",\n" +
                        "    \"desc\": \"Tubercule comestible originaire d'Amérique du Sud\"\n" +
                        "  }\n" +
                        "}\n" +
                        "```\n\n" +
                        "Otro ejemplo:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"es\": {\n" +
                        "    \"word\": \"Casa\",\n" +
                        "    \"desc\": \"Edificio para vivienda\"\n" +
                        "  },\n" +
                        "  \"en\": {\n" +
                        "    \"word\": \"House\",\n" +
                        "    \"desc\": \"Building for living\"\n" +
                        "  },\n" +
                        "  \"de\": {\n" +
                        "    \"art\": \"Das\",\n" +
                        "    \"trans\": \"Haus\",\n" +
                        "    \"desc\": \"Gebäude zum Wohnen\"\n" +
                        "  },\n" +
                        "  \"fr\": {\n" +
                        "    \"word\": \"Maison\",\n" +
                        "    \"desc\": \"Bâtiment pour habiter\"\n" +
                        "  }\n" +
                        "}\n" +
                        "```\n\n" +
                        "Y otro más:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"es\": {\n" +
                        "    \"word\": \"Perro\",\n" +
                        "    \"desc\": \"Mamífero doméstico leal\"\n" +
                        "  },\n" +
                        "  \"en\": {\n" +
                        "    \"word\": \"Dog\",\n" +
                        "    \"desc\": \"Loyal domestic mammal\"\n" +
                        "  },\n" +
                        "  \"de\": {\n" +
                        "    \"art\": \"Der\",\n" +
                        "    \"trans\": \"Hund\",\n" +
                        "    \"desc\": \"Treues Haussäugetier\"\n" +
                        "  },\n" +
                        "  \"fr\": {\n" +
                        "    \"word\": \"Chien\",\n" +
                        "    \"desc\": \"Mammifère domestique fidèle\"\n" +
                        "  }\n" +
                        "}\n" +
                        "```\n\n" +
                        "Otro ejemplo adicional:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"es\": {\n" +
                        "    \"word\": \"Gato\",\n" +
                        "    \"desc\": \"Felino doméstico independiente\"\n" +
                        "  },\n" +
                        "  \"en\": {\n" +
                        "    \"word\": \"Cat\",\n" +
                        "    \"desc\": \"Independent domestic feline\"\n" +
                        "  },\n" +
                        "  \"de\": {\n" +
                        "    \"art\": \"Der\",\n" +
                        "    \"trans\": \"Kater\",\n" +
                        "    \"desc\": \"Unabhängige Haskatze\"\n" +
                        "  },\n" +
                        "  \"fr\": {\n" +
                        "    \"word\": \"Chat\",\n" +
                        "    \"desc\": \"Félin domestique indépendant\"\n" +
                        "  }\n" +
                        "}\n" +
                        "```\n\n" +
                        "Ejemplo con frase larga:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"es\": {\n" +
                        "    \"word\": \"Fuerza Regida\",\n" +
                        "    \"desc\": \"Grupo musical de corridos tumbados\"\n" +
                        "  },\n" +
                        "  \"en\": {\n" +
                        "    \"word\": \"Regulated Force\",\n" +
                        "    \"desc\": \"Musical group of tumbado corridos\"\n" +
                        "  },\n" +
                        "  \"de\": {\n" +
                        "    \"art\": \"Die\",\n" +
                        "    \"trans\": \"Regulierte Kraft\",\n" +
                        "    \"desc\": \"Musikgruppe von Tumbado-Corridos\"\n" +
                        "  },\n" +
                        "  \"fr\": {\n" +
                        "    \"word\": \"Force Régulée\",\n" +
                        "    \"desc\": \"Groupe musical de corridos tumbados\"\n" +
                        "  }\n" +
                        "}\n" +
                        "```\n\n" +
                        "Es importante que solo devuelvas el JSON con las traducciones y descripciones, sin ningún texto adicional. No incluyas ninguna explicación o comentario, solo el JSON.\n\n" +
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
