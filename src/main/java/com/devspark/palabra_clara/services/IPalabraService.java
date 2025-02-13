package com.devspark.palabra_clara.services;

import com.devspark.palabra_clara.model.PalabraRequestBean;
import com.devspark.palabra_clara.util.GenericResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IPalabraService {

    GenericResponse guardarPalabra(PalabraRequestBean palabraRequest, Map<String, MultipartFile> video) throws EncoderException, IOException;

    GenericResponse traducirPalabra(String palabra);

    ResponseEntity<Resource> descargarVideo(String filename);

    GenericResponse actualizarPalabra(PalabraRequestBean palabraRequestBean, Map<String, MultipartFile> video) throws EncoderException, IOException;

    List<String> obtenerTodasLasPalabras();

    ResponseEntity<byte[]> convertirTextoAVoz(String palabra);

    GenericResponse traducirPalabraGroq(String palabra);

    GenericResponse traducirBraile(String texto);
}
