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
public class EleccionGroqBean {

    private int index;
    @JsonAlias("message")
    private MensajeGroqBean mensajeGroqBean;
    @JsonAlias("logprobs")
    private Object problemasRegistro;
    @JsonAlias("finish_reason")
    private String terminarRazon;
}
