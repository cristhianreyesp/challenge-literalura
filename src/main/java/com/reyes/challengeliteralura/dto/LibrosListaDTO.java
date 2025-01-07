package com.reyes.challengeliteralura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibrosListaDTO(
        @JsonAlias("results") List<LibroDTO> resultados
) {
}
