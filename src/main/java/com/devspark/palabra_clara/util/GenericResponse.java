package com.devspark.palabra_clara.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse{

    private Integer codigo;
    private String mensaje;
    private Object respuesta;

}
