package com.devspark.palabra_clara.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensajeGroqBean {

    @JsonAlias("role")
    private String rol;
    @JsonAlias("content")
    private String contenido;
}
