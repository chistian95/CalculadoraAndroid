package com.example.dam32_corral.ejercicio_03;

/**
 * Created by dam32-Corral on 23/09/2016.
 */
public class Especial {
    public static final int DECIMAL = 0;
    public static final int PAR_IZQ = 1;
    public static final int PAR_DRC = 2;

    private int tipo;

    public Especial(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }
}
