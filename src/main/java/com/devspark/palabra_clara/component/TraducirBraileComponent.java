package com.devspark.palabra_clara.component;

import com.devspark.palabra_clara.model.RespuestaGroqBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TraducirBraileComponent {

  public String traducirBraile(String texto){
    try {
      String apiKey = "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"; // Reemplaza con tu clave de API
      String url = "https://api.groq.com/openai/v1/chat/completions"; // Reemplaza con el endpoint real de la API de Groq

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + apiKey);
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

      Map<String, Object> requestBodyBrailleSimplificado = new HashMap<>();
      requestBodyBrailleSimplificado.put("model", "llama-3.1-8b-instant");
      requestBodyBrailleSimplificado.put("messages", new Object[] {
          new HashMap<String, String>() {{
            put("role", "user");
            put("content", "Vas a actuar como traductor. Te proporcionaré una palabra o frase en español, y necesito que me la traduzcas a lenguaje braille en un formato JSON simplificado, fácil de procesar programáticamente.  El JSON debe ser un array de objetos. Cada objeto representará una letra o código especial del braille, y tendrá dos campos:\n\n" +
                "- `letra`:  La letra original en español (o los códigos especiales 'MAYUSCULA_BRAILLE' para indicar mayúscula, y 'espacio_braille' para indicar un espacio).\n" +
                "- `puntos`: Un array de números enteros. Estos números representan los puntos que están realzados para esa letra braille, utilizando la numeración estándar de 6 puntos braille (1, 2, 3 en la columna izquierda y 4, 5, 6 en la columna derecha).\n\n" +
                "Por ejemplo:\n" +
                "- La letra 'a' (⠁) se representaría como: `{\"letra\": \"a\", \"puntos\": [1]}`\n" +
                "- La letra 'b' (⠃) se representaría como: `{\"letra\": \"b\", \"puntos\": [1, 2]}`\n" +
                "- Un espacio se representaría como: `{\"letra\": \"espacio_braille\", \"puntos\": []}`\n" +
                "- Mayúscula se indicaría con: `{\"letra\": \"MAYUSCULA_BRAILLE\", \"puntos\": [4, 6]}` seguido del objeto de la letra en minúscula.\n\n" +
                "Devuélveme solo el JSON con la traducción al braille en este formato simplificado, sin ningún texto adicional.\n\n" +
                "El JSON generado debe ser válido y estar bien formado.\n\n" +
                "Ejemplo de formato JSON esperado:\n" +
                "```json\n" +
                "{\n" +
                "  \"es\": \"Patata\",\n" +
                "  \"braille_simplificado\": [\n" +
                "    {\"letra\": \"P\", \"puntos\": [1, 2, 3, 4, 6]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]},\n" +
                "    {\"letra\": \"t\", \"puntos\": [2, 3, 5, 6]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]}\n" +
                "  ]\n" +
                "}\n" +
                "```\n\n" +
                "Otro ejemplo:\n" +
                "```json\n" +
                "{\n" +
                "  \"es\": \"Casa\",\n" +
                "  \"braille_simplificado\": [\n" +
                "    {\"letra\": \"MAYUSCULA_BRAILLE\", \"puntos\": [4, 6]},\n" +
                "    {\"letra\": \"c\", \"puntos\": [1, 4]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]},\n" +
                "    {\"letra\": \"s\", \"puntos\": [2, 3, 4]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]}\n" +
                "  ]\n" +
                "}\n" +
                "```\n\n" +
                "Ejemplo con frase larga:\n" +
                "```json\n" +
                "{\n" +
                "  \"es\": \"Fuerza Regida\",\n" +
                "  \"braille_simplificado\": [\n" +
                "    {\"letra\": \"F\", \"puntos\": [1, 2, 4, 6]},\n" +
                "    {\"letra\": \"u\", \"puntos\": [1, 3, 6]},\n" +
                "    {\"letra\": \"e\", \"puntos\": [1, 5]},\n" +
                "    {\"letra\": \"r\", \"puntos\": [1, 2, 3, 5]},\n" +
                "    {\"letra\": \"z\", \"puntos\": [1, 3, 5, 6]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]},\n" +
                "    {\"letra\": \"espacio_braille\", \"puntos\": []},\n" +
                "    {\"letra\": \"R\", \"puntos\": [2, 3, 5]},\n" +
                "    {\"letra\": \"e\", \"puntos\": [1, 5]},\n" +
                "    {\"letra\": \"g\", \"puntos\": [1, 2, 4, 5]},\n" +
                "    {\"letra\": \"i\", \"puntos\": [2, 4]},\n" +
                "    {\"letra\": \"d\", \"puntos\": [1, 4, 5]},\n" +
                "    {\"letra\": \"a\", \"puntos\": [1]}\n" +
                "  ]\n" +
                "}\n" +
                "```\n\n" +
                "Es importante que solo devuelvas el JSON con la traducción en el formato simplificado, sin ningún texto adicional. No incluyas ninguna explicación o comentario, solo el JSON.\n\n" +
                "La palabra en español es: " + texto);
          }}
      });

      ObjectMapper objectMapper = new ObjectMapper();
      String jsonBody = objectMapper.writeValueAsString(requestBodyBrailleSimplificado);

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
