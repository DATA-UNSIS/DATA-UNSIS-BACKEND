package com.example.dynamicSQLTest.DTOs.enums;

import lombok.Getter;

@Getter
public enum ETitles {
    MAJOR_DISTRIBUTION (1, "Siempre se mandarian todas las carreras"),
    ECONOMIC_LEVEL (2, "Muestra una evaluacion de los ingesos, esto se tiene que checar de cuanto seria el rango de nivel bajo, medio o alto"),
    SCHOLARSHIPS_REQUESTED (3, "Cuantas personas solicitaron cada beca"),
    HOUSEHOLD_SERVICES (4, "Cuantas personas tienen tv, microondas, etc"),
    CIVIL_STATE (5, "Cuantos son casados divorciados o otros");

    private final int id;
    private final String description;

    ETitles(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
