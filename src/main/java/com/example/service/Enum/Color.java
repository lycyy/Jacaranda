package com.example.service.Enum;

public enum Color {
    A("#cdb4db"),
    B("#ffafcc"),
    C("#ffcaca"),
    D("#fb6f92"),
    E("#449dd1"),
    F("#bccef8"),
    G("#a2d2ff"),
    H("#74c69d");
    private final String value;

    Color(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
