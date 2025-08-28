package com.example.dynamicSQLTest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EScholarships {
    MANUTENTION("BECA DE MANUTENCION"),
    CONAFE("CONAFE"),
    WRITING_THE_FUTURE("JOVENES ESCRIBIENDO EL FUTURO"),
    SINGLE_MOTHER("MADRE SOLTERA"),
    OTHER_SCHOLARSHIP("OTRA BECA"),
    TALENT_SEEDS("SEMILLAS TALENTO"),
    OTHER("OTRO");

    private final String description;
}
