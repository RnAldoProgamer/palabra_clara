package com.devspark.palabra_clara.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObtenerPalabras {

    private ObtenerPalabras() {
        throw new UnsupportedOperationException(StaticConstants.CONSTRUCTORES_CONSTANTES_NO_INICIALIZADA);
    }

    public static GenericResponse main() {
        return new GenericResponse(StaticConstants.CODIGO_OK, StaticConstants.MENSAJE_TRADUCIR_OK, convertirAJson(leerPalabras(StaticConstants.PATH_PALABRAS_TODAS)));
    }

    public static List<String> leerPalabras(String archivo) {
        List<String> palabras = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                palabras.add(linea.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return palabras;
    }

    public static String convertirAJson(List<String> palabras) {
        Gson gson = new Gson();
        return gson.toJson(palabras);
    }
}
