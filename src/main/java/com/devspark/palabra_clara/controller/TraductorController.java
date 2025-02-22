package com.devspark.palabra_clara.controller;

import com.devspark.palabra_clara.model.PalabraRequestBean;
import com.devspark.palabra_clara.services.IPalabraService;
import com.devspark.palabra_clara.util.GenericResponse;
import com.devspark.palabra_clara.util.ObtenerPalabras;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class TraductorController {

    private final IPalabraService iPalabraService;

    public TraductorController(IPalabraService iPalabraService) {
        this.iPalabraService = iPalabraService;
    }

    @GetMapping("/obtenerPalabras")
    public GenericResponse obtnerPalabras(){
        return ObtenerPalabras.main();
        }

    @GetMapping("/obtenerPalabrasCaed")
    public List<String> getAllPalabrasAndVariantes() {
        return iPalabraService.obtenerTodasLasPalabras();
    }

    @PostMapping("guardar/palabra")
    public GenericResponse guardarPalabra(@ModelAttribute PalabraRequestBean palabraRequest, @RequestParam Map<String, MultipartFile> video) throws EncoderException, IOException {
        return iPalabraService.guardarPalabra(palabraRequest, video);
    }

    @PostMapping("actualizar/palabra")
    public GenericResponse actualizarPalabra(@ModelAttribute PalabraRequestBean palabraRequest, @RequestParam Map<String, MultipartFile> video) throws EncoderException, IOException {
        return iPalabraService.actualizarPalabra(palabraRequest, video);
    }

    @GetMapping("/descargarVideo/{palabra}")
    public ResponseEntity<Resource> descargarVideo(@PathVariable String palabra) {
        return iPalabraService.descargarVideo(palabra);
    }

    @PostMapping("/convertirTextoAVoz")
    public ResponseEntity<byte[]> convertirTextoAVoz(@RequestBody String palabra) {
        return iPalabraService.convertirTextoAVoz(palabra);
    }

    @PostMapping("/traducirPalabraGroq")
    public GenericResponse traducirPalabraGroq(@RequestBody String palabra) {
        return iPalabraService.traducirPalabraGroq(palabra);
    }

    @PostMapping("traducir/braile")
    public GenericResponse traducirBraile(@RequestBody String texto){
        return iPalabraService.traducirBraile(texto);
    }
}
