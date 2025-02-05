package com.devspark.palabra_clara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraduccionPathResponseBean {

    private Boolean videoExiste;
    private String traduccion;
}
