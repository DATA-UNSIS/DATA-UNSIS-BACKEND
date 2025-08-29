package com.example.dynamicSQLTest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EHouseholdServices {
    DRINKING_WATER("AGUA POTABLE"),
    DRAINAGE("DRENAJE"),
    ELECTRICITY("ENERGIA ELECTRICA"),
    GAS_INSTALLATION("INSTALACION GAS"),
    INTERNET("INTERNET"),
    WASHING_MACHINE("LAVADORA ROPA"),
    MICROWAVE("MICROONDAS"),
    REFRIGERATOR("REFRIGERADOR"),
    CELL_PHONE("TELEFONO CELULAR"),
    LANDLINE_PHONE("TELEFONO FIJO"),
    TELEVISION("TELEVISION");

    private final String description;
}
