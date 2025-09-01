package com.example.dynamicSQLTest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ECivilStates {
    SOLTERO("Soltero"),
    CASADO("Casado"),
    DIVORCIADO("Divorciado"),
    UNION_LIBRE("Unión Libre"),
    PADRE_MADRE_SOLTERO("Padre/Madre soltero(a)"),
    OTRO("Otro");

    private final String description;
}
