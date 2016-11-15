package com.example.dam32_corral.ejercicio_03;

/**
 * Created by dam32-Corral on 22/09/2016.
 */
public class Accion {
    public static final int SUMAR = 0;
    public static final int RESTAR = 1;
    public static final int MULTIPLICAR = 2;
    public static final int DIVIDIR = 3;

    private int tipo;
    private int prioridad;

    public Accion(int tipo) {
        this.tipo = tipo;
        prioridad = 0;
        switch(tipo) {
            case SUMAR:
            case RESTAR:
                prioridad = 1;
                break;
            case MULTIPLICAR:
            case DIVIDIR:
                prioridad = 2;
                break;
        }
    }

    public double calcular(double valor1, double valor2) {
        double res = 0.0;
        switch (tipo) {
            case SUMAR:
                res = valor1 + valor2;
                break;
            case RESTAR:
                res = valor1 - valor2;
                break;
            case MULTIPLICAR:
                res = valor1 * valor2;
                break;
            case DIVIDIR:
                res = valor1 / valor2;
                break;
        }
        return res;
    }

    public int getTipo() {
        return tipo;
    }

    public int getPrioridad() {
        return prioridad;
    }
}
