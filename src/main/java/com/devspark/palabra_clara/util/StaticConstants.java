package com.devspark.palabra_clara.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StaticConstants {
    private StaticConstants (){
        throw new IllegalStateException("Utility class");
    }

    /*
     * Constantes de respuesta: codigo
     */
    public static final int CODIGO_OK = 0;
    public static final int CODIGO_ERROR = 1;
    public static final int CODIGO_ADVERTENCIA = 2;

    /*
    * Constantes de respuesta: mensaje
    * Operacion: traducción
    */

    public static final String MENSAJE_TRADUCIR_OK = "La traducción se realizó correctamente";
    public static final String MENSAJE_TRADUCIR_ERROR = "Error al realizar la traducción: Revise la conexión con la base de datos";
    public static final String MENSAJE_TRADUCIR_ADVERTENCIA = "El texto no existe";

    /*
    * Constantes de Traductor
    * Operacion: traducción
    */

    public static final List<String> IDIOMAS = Collections.unmodifiableList(Arrays.asList("en", "fr", "de"));
    public static final String URL = "https://openl-translate.p.rapidapi.com/translate";
    public static final String FORMATO_BODY_REQUEST = "\"{\\\"target_lang\\\":\\\"%s\\\",\\\"text\\\":\\\"%s\\\"}\"";
    public static final String API = "x-rapidapi-key";
    public static final String API_HOST = "x-rapidapi-host";
    public static final String API_HOST_KEY = "openl-translate.p.rapidapi.com";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";


    /*
    * Constantes de Actualizar Palabra
    * Operacion: Actualizar Palabra
    */
    public static final String MENSAJE_VIDEO_NULL = "El video es obligatorio para actualizar la palabra";
    public static final String MENSAJE_ACTUALIZAR_PALABRA_ERROR = "No se encontró la palabra o variantes para actualizar";

    /*
    * Constantes de Comprimir Video
    * Operacion: Comprimir Video
    */

    public static final String RUTA_CARPETA_VIDEOS = "C://videosPalabraClara";
    public static final String ERROR_CREAR_DIRECTORIO = "No se pudo crear el directorio de salida: ";
    public static final String TERMINACION_TMP = ".tmp";
    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String PUNTO_DOT = ".";
    public static final String MENSAJE_ERROR_COMPRIMIR_VIDEO = "Error al comprimir el video";

    /*
    * Constantes de Descargar Video
    * Operacion: Descargar Video
    */
    public static final String MENSAJE_DESCARGAR_ADVERTENCIA = "El archivo no existe";
    public static final String MENSAJE_DESCARGAR_ERROR = "Error al descargar el archivo";
    public static final String MENSAJE_DESCARGAR_OK = "El video se encontro";
    public static final String TIPO_VIDEO = "video/webm";
    public static final String CONTENT_DISPOSITION = "attachment; filename=\"%s\"";

    /*
    * Constantes de Traductor
    * Operacion: traducción
    */
    public static final String PALABRA_TRADUCCION = "traduccion";
    public static final String PALABRA_VIDEO_EXISTE = "video";

    /*
    * Constantes de Guardar Palabra
    * Operacion: guardar palabra
    */
    public static final String PALABRA_VIDEO = "video";
    public static final String ERROR_PALABRA_NULL = "La solicitud de palabra es null";
    public static final String ERROR_VIDEO_NULL = "Archivo de video no encontrado";
    public static final String ERROR_VIDEO_VACIO = "Archivo de video vacío";
    public static final String MENSAJE_GUARDAR_PALABRA_OK = "La palabra se guardó correctamente";
    public static final String MENSAJE_GUARDAR_PALABRA_ERROR = "Error al guardar la palabra";
    /*
    * Rutas de archivos
    */
    public static final String PATH_VIDEOS = "src/main/resources/static/videos/";
    public static final String PATH_PALABRAS_TODAS = "src/main/resources/static/palabras/0_palabras_todas.txt";

    /*
    * Nombres de tablas
    */
    public static final String TABLA_PALABRA = "palabra";
    public static final String TABLA_VARIANTE = "variante";

    /*
    * Nombres de columnas
    */
    public static final String COLUMNA_UNION_PALABRA_VARIANTE = "id_palabra";
    public static final String COLUMNA_UNION_PALABRA_REFERENCIADA = "id";
    public static final String JSON_COLUMNA_TRADUCCION = "translatedText";
    public static final String JSON_COLUMNA_TRADUCCION_IDIOMA = "target_lang";
    public static final String JSON_COLUMNA_TEXTO = "text";

    /*
    * Constantes de Configuraciones
    * Operacion: configuraciones
    */
    public static final String CODEC_MP4 = "libx264";
    public static final String CODEC_WEBM = "libvpx";
    public static final String CODEC_LOW_QUALITY_MP4 = "libx264";
    public static final String CODEC_HD_MP4 = "libx264";
    public static final String CODEC_HD_WEBM = "libvpx";
    public static final String CODEC_LOW_QUALITY_WEBM = "libvpx";
    public static final String CODEC_LOW_QUALITY_HD_MP4 = "libx264";
    public static final String CODEC_LOW_QUALITY_HD_WEBM = "libvpx";
    public static final String CODEC_AUDIO_MP4 = "aac";
    public static final String CODEC_AUDIO_WEBM = "libvorbis";
    public static final String CODEC_AUDIO_LOW_QUALITY_MP4 = "aac";
    public static final String CODEC_AUDIO_LOW_QUALITY_WEBM = "libvorbis";
    public static final String TIPO_EXTENSION_MP4 = "mp4";
    public static final String TIPO_EXTENSION_WEBM = "webm";

    /*
    * Constructores Constantes
    * Operacion: constantes
    */
    public static final String CONSTRUCTORES_CONSTANTES_NO_INICIALIZADA = "Utility class cannot be instantiated";
    public static final String NO_INIZIALIZADO_TEST = "notImplemented() cannot be performed because ...";
}
