package com.example.dynamicSQLTest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EScholarships {
    MANUTENTION("Beca de Manutención"),
    CONAFE("CONAFE"),
    WRITING_THE_FUTURE("Jóvenes Escribiendo el Futuro"),
    SINGLE_MOTHER("Madre Soltera"),
    OTHER_SCHOLARSHIP("Otra Beca"),
    TALENT_SEEDS("Semillas Talento"),
    OTHER("Otro");

    private final String description;
}
