package com.example.dynamicSQLTest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EHouseholdServices {
    DRINKING_WATER("Agua Potable"),
    DRAINAGE("Drenaje"),
    ELECTRICITY("Energía Eléctrica"),
    GAS_INSTALLATION("Instalación de Gas"),
    INTERNET("Internet"),
    WASHING_MACHINE("Lavadora de Ropa"),
    MICROWAVE("Microondas"),
    REFRIGERATOR("Refrigerador"),
    CELL_PHONE("Telefono Celular"),
    LANDLINE_PHONE("Telefono Fijo"),
    TELEVISION("Televisión");

    private final String description;
}
