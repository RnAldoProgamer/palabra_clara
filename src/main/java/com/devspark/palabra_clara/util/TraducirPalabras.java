package com.devspark.palabra_clara.util;

import com.devspark.palabra_clara.model.TraduccionResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;


@Component
public class TraducirPalabras {

    public GenericResponse traducirTexto(String texto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(StaticConstants.API, StaticConstants.API_KEY_GROQ);
        headers.set(StaticConstants.API_HOST, StaticConstants.API_HOST_KEY);
        headers.set(StaticConstants.CONTENT_TYPE, StaticConstants.CONTENT_TYPE_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        List<TraduccionResponseBean> traducciones = new ArrayList<>();

        try {
            for (String idioma : StaticConstants.IDIOMAS) {

                Map<String, String> requestBody = new HashMap<>();
                requestBody.put(StaticConstants.JSON_COLUMNA_TRADUCCION_IDIOMA, idioma);
                requestBody.put(StaticConstants.JSON_COLUMNA_TEXTO, texto);

                String jsonBody = objectMapper.writeValueAsString(requestBody);

                HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

                ResponseEntity<String> jsonResponse = new RestTemplate().exchange(
                        StaticConstants.URL,
                        HttpMethod.POST,
                        entity,
                        String.class
                );


                Map<String, Object> response = objectMapper.readValue(
                        jsonResponse.getBody(),
                        Map.class
                );

                TraduccionResponseBean traduccion = new TraduccionResponseBean();
                traduccion.setLenguajeOrigen(idioma);
                traduccion.setTextoTraducido((String) response.get(StaticConstants.JSON_COLUMNA_TRADUCCION));
                traducciones.add(traduccion);

            }

            return new GenericResponse(
                    StaticConstants.CODIGO_OK,
                    StaticConstants.MENSAJE_TRADUCIR_OK,
                    traducciones
            );
        } catch (Exception e) {
            return new GenericResponse(
                    StaticConstants.CODIGO_ERROR,
                    StaticConstants.MENSAJE_TRADUCIR_ERROR,
                    e.getMessage()
            );
        }
    }
}
