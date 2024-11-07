package com.devspark.palabra_clara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PalabraResponseBean {

    private String palabra;

    private String path;

    private List<VarianteRequestBean> variantes;
}
