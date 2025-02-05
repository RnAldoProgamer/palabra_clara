package com.devspark.palabra_clara.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaGroqBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String object;
    private long created;
    private String model;
    @JsonAlias("choices")
    private List<EleccionGroqBean> eleccionGroqEntities;
    private String systemFingerprint;

}
