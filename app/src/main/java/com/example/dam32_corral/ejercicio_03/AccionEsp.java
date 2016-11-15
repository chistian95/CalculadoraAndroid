package com.example.dam32_corral.ejercicio_03;

/**
 * Created by dam32-Corral on 27/09/2016.
 */
public class AccionEsp {
    public static final int SIN = 0;
    public static final int COS = 1;
    public static final int TAN = 2;
    public static final int RAIZ = 3;

    private int tipo;

    public AccionEsp(int tipo) {
        this.tipo = tipo;
    }

    public double calcular(double valor1) {
        double res = 0.0;
        int tmp = (int) (Math.round(valor1));
        res = Math.toRadians(tmp);
        switch (tipo) {
            case SIN:
                res = Math.sin(res);
                break;
            case COS:
                res = Math.cos(res);
                break;
            case TAN:
                res = Math.tan(res);
                break;
            case RAIZ:
                res = Math.sqrt(res);
                break;
        }
        return res;
    }

    public int getTipo() {
        return tipo;
    }
}
