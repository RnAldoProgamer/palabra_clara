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
            requestBody.put("model", "llama-3.3-70b-versatile");
            requestBody.put("messages", new Object[] {
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content",
                        "Vas a actuar como traductor. Te proporcionaré una palabra o frase en español, y necesito que me la traduzcas al inglés, alemán, francés, portugués y náhuatl tlaxcalteco.\n" +
                            "- Para el alemán, proporciona el artículo y la traducción de la palabra por separado.\n" +
                            "- Para el náhuatl tlaxcalteco (clave \"nah\"), además de la palabra y la descripción, incluye un subcampo \"variants\" con otras variantes dialectales que puedan usarse.\n" +
                            "- Para el portugués (clave \"pt\"), incluye la palabra traducida y su descripción en portugués.\n\n" +
                            "Devuélveme solo el JSON con las traducciones, descripciones y variantes, sin ningún texto adicional.\n" +
                            "El JSON generado debe ser válido y bien formado. Evita errores comunes como:\n" +
                            "- Faltan comas entre elementos.\n" +
                            "- Llaves o corchetes mal cerrados.\n" +
                            "- Caracteres inesperados o mal ubicados.\n\n" +
                            "Ejemplo (Patata):\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"es\": { \"word\": \"Patata\", \"desc\": \"Tubérculo comestible de origen sudamericano\" },\n" +
                            "  \"en\": { \"word\": \"Potato\", \"desc\": \"Edible tuber native to South America\" },\n" +
                            "  \"de\": { \"art\": \"Die\", \"trans\": \"Kartoffel\", \"desc\": \"Essbare Knolle aus Südamerika\" },\n" +
                            "  \"fr\": { \"word\": \"Pomme de terre\", \"desc\": \"Tubercule comestible originaire d'Amérique du Sud\" },\n" +
                            "  \"pt\": { \"word\": \"Batata\", \"desc\": \"Tubérculo comestível de origem sul-americana\" },\n" +
                            "  \"nah\": { \"word\": \"Chīxpatli\", \"desc\": \"Tlātokayotl nēwatl tōnali\", \"variants\": [\"Tialli\",\"Pialli\"] }\n" +
                            "}\n" +
                            "```\n\n" +
                            "Ejemplo (Casa):\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"es\": { \"word\": \"Casa\", \"desc\": \"Edificio para vivienda\" },\n" +
                            "  \"en\": { \"word\": \"House\", \"desc\": \"Building for living\" },\n" +
                            "  \"de\": { \"art\": \"Das\", \"trans\": \"Haus\", \"desc\": \"Gebäude zum Wohnen\" },\n" +
                            "  \"fr\": { \"word\": \"Maison\", \"desc\": \"Bâtiment pour habiter\" },\n" +
                            "  \"pt\": { \"word\": \"Casa\", \"desc\": \"Edifício destinado à habitação\" },\n" +
                            "  \"nah\": { \"word\": \"Calli\", \"desc\": \"Lugar donde vive la gente\", \"variants\": [\"Nēmalli\",\"Tēttēn\"] }\n" +
                            "}\n" +
                            "```\n\n" +
                            "Ejemplo (Perro):\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"es\": { \"word\": \"Perro\", \"desc\": \"Mamífero doméstico leal\" },\n" +
                            "  \"en\": { \"word\": \"Dog\", \"desc\": \"Loyal domestic mammal\" },\n" +
                            "  \"de\": { \"art\": \"Der\", \"trans\": \"Hund\", \"desc\": \"Treues Haussäugetier\" },\n" +
                            "  \"fr\": { \"word\": \"Chien\", \"desc\": \"Mammifère domestique fidèle\" },\n" +
                            "  \"pt\": { \"word\": \"Cão\", \"desc\": \"Mamífero doméstico leal\" },\n" +
                            "  \"nah\": { \"word\": \"Itzcuintli\", \"desc\": \"Cuānahuac tlālli ahmōtlī\", \"variants\": [\"Itzcuintin\",\"Itzcohualloni\"] }\n" +
                            "}\n" +
                            "```\n\n" +
                            "Ejemplo (Gato):\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"es\": { \"word\": \"Gato\", \"desc\": \"Felino doméstico independiente\" },\n" +
                            "  \"en\": { \"word\": \"Cat\", \"desc\": \"Independent domestic feline\" },\n" +
                            "  \"de\": { \"art\": \"Der\", \"trans\": \"Kater\", \"desc\": \"Unabhängige Hauskatze\" },\n" +
                            "  \"fr\": { \"word\": \"Chat\", \"desc\": \"Félin domestique indépendant\" },\n" +
                            "  \"pt\": { \"word\": \"Gato\", \"desc\": \"Felino doméstico independente\" },\n" +
                            "  \"nah\": { \"word\": \"Miztli\", \"desc\": \"Mīztli nōn tlālli\", \"variants\": [\"Miztli tlatoani\",\"Miztli ahmo cēnca\"] }\n" +
                            "}\n" +
                            "```\n\n" +
                            "Ejemplo (Fuerza Regida):\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"es\": { \"word\": \"Fuerza Regida\", \"desc\": \"Grupo musical de corridos tumbados\" },\n" +
                            "  \"en\": { \"word\": \"Regulated Force\", \"desc\": \"Musical group of tumbado corridos\" },\n" +
                            "  \"de\": { \"art\": \"Die\", \"trans\": \"Regulierte Kraft\", \"desc\": \"Musikgruppe von Tumbado-Corridos\" },\n" +
                            "  \"fr\": { \"word\": \"Force Régulée\", \"desc\": \"Groupe musical de corridos tumbados\" },\n" +
                            "  \"pt\": { \"word\": \"Força Regulada\", \"desc\": \"Grupo musical de corridos tumbados\" },\n" +
                            "  \"nah\": { \"word\": \"Tlamantli Tepākualli\", \"desc\": \"Kwikni katal kws Tumbado-Corridos\", \"variants\": [\"Tlamantli Tepākualli\",\"Tlamantli Cualankatl\"] }\n" +
                            "}\n" +
                            "```\n\n" +
                            "La palabra en español es:" + texto);
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
