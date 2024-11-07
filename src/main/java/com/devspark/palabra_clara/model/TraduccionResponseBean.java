package com.devspark.palabra_clara.model;

import com.devspark.palabra_clara.util.StaticConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraduccionResponseBean {

    @JsonProperty(StaticConstants.JSON_COLUMNA_TRADUCCION)
    private String textoTraducido;

    private String lenguajeOrigen;

    public void setLenguajeOrigen(String idioma) {
        this.lenguajeOrigen = idioma;
    }
}
