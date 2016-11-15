package com.example.dam32_corral.ejercicio_03;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Calculadora extends AppCompatActivity {
    private TextView resultado;
    private TextView ventana;
    private List<Object> operacion = new ArrayList<Object>();
    private String mostrar = "";
    private boolean limpiar = false;
    private static double resAnterior = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculadora);
    }

    public void calculadora(View view) {
        resultado = (TextView) findViewById(R.id.tvResultado);
        ventana = (TextView) findViewById(R.id.tvResultadoNumerico);
        String descripcion = (String) view.getContentDescription();
        char tipo = descripcion.charAt(0);
        if(tipo == 'n') {
            mostrar += descripcion.substring(2, descripcion.length());
            int num = Integer.parseInt(descripcion.substring(2, descripcion.length()));
            Numero digito = new Numero(num);
            operacion.add(digito);
        } else if(tipo == 'f') {
            String accion = descripcion.substring(2, descripcion.length());
            if(accion.equals("sum")) {
                if(operacion.size() == 0) {
                    mostrar += "Ans";
                    operacion.add(new Numero(resAnterior));
                }
                mostrar += "+";
                operacion.add(new Accion(Accion.SUMAR));
            } else if(accion.equals("res")) {
                if(operacion.size() == 0) {
                    mostrar += "Ans";
                    operacion.add(new Numero(resAnterior));
                }
                mostrar += "-";
                operacion.add(new Accion(Accion.RESTAR));
            } else if(accion.equals("mult")) {
                if(operacion.size() == 0) {
                    mostrar += "Ans";
                    operacion.add(new Numero(resAnterior));
                }
                mostrar += "*";
                operacion.add(new Accion(Accion.MULTIPLICAR));
            } else if(accion.equals("div")) {
                if(operacion.size() == 0) {
                    mostrar += "Ans";
                    operacion.add(new Numero(resAnterior));
                }
                mostrar += "/";
                operacion.add(new Accion(Accion.DIVIDIR));
            } else if(accion.equals("sin")) {
                mostrar += "sin(";
                operacion.add(new AccionEsp(AccionEsp.SIN));
                operacion.add(new Especial(Especial.PAR_IZQ));
            } else if(accion.equals("cos")) {
                mostrar += "cos(";
                operacion.add(new AccionEsp(AccionEsp.COS));
                operacion.add(new Especial(Especial.PAR_IZQ));
            } else if(accion.equals("tan")) {
                mostrar += "tan(";
                operacion.add(new AccionEsp(AccionEsp.TAN));
                operacion.add(new Especial(Especial.PAR_IZQ));
            } else if(accion.equals("raiz")) {
                mostrar += "sqrt(";
                operacion.add(new AccionEsp(AccionEsp.RAIZ));
                operacion.add(new Especial(Especial.PAR_IZQ));
            } else if(accion.equals("calc")) {
                mostrar += "=";
                calcular(operacion, ventana);
                limpiar = true;
            } else if(accion.equals("punto")) {
                mostrar += ".";
                operacion.add(new Especial(Especial.DECIMAL));
            } else if(accion.equals("par_izq")) {
                mostrar += "(";
                operacion.add(new Especial(Especial.PAR_IZQ));
            } else if(accion.equals("par_drc")) {
                mostrar += ")";
                operacion.add(new Especial(Especial.PAR_DRC));
            } else if(accion.equals("c")) {
                operacion.clear();
                mostrar = "";
            } else if(accion.equals("ce")) {
                if(operacion.size() > 0 && mostrar.length() > 0) {
                    operacion.remove(operacion.size()-1);
                    mostrar = mostrar.substring(0, mostrar.length()-1);
                }
            }
        }
        if(mostrar.length() == 1 && operacion.size() == 1 && resAnterior != 0.0 && 2 == 1) {
            Object tmp = operacion.get(0);
            operacion.set(0, new Numero(resAnterior));
            operacion.add(tmp);
            mostrar = resAnterior+"";
        }
        resultado.setText(mostrar);
        if(limpiar) {
            limpiar = false;
            operacion.clear();
            mostrar = "";
        }
    }

    public static void calcular(List<Object> operacion, TextView texto) {
        double res = procesarCalculo(operacion);
        resAnterior = res;
        texto.setText(res+"");
    }

    public static double procesarCalculo(List<Object> operacion) {
        List<Object> borrar = new ArrayList<Object>();
        Accion ac = null;
        boolean prioridad = buscarPrioridad(operacion);
        int opEspecial = buscarOpEspecial(operacion);
        int[] parentesis = buscarParentesis(operacion);

        if(opEspecial != -1) {
            List<Object> subOperacion = new ArrayList<Object>();
            for(int i=opEspecial+1; i<operacion.size(); i++) {
                subOperacion.add(operacion.get(i));
                borrar.add(operacion.get(i));
            }
            double resSubEsp = procesarCalculo(subOperacion);
            AccionEsp accionEsp = (AccionEsp) operacion.get(opEspecial);
            double resEsp = accionEsp.calcular(resSubEsp);
            operacion.set(opEspecial, new Numero(resEsp));
        } else if(parentesis[0] != -1 && parentesis[1] != -1) {
            List<Object> subOperacion = new ArrayList<Object>();
            for(int i = parentesis[0]+1; i<parentesis[1]; i++) {
                subOperacion.add(operacion.get(i));
                borrar.add(operacion.get(i));
            }
            borrar.add(operacion.get(parentesis[1]));
            double resPar = procesarCalculo(subOperacion);
            operacion.set(parentesis[0], new Numero(resPar));
        } else {
            for(int i=0; i<operacion.size(); i++) {
                Object obj = operacion.get(i);
                if(obj.getClass().equals(Accion.class)) {
                    ac = (Accion) obj;
                    if(prioridad && ac.getPrioridad() != 2) {
                        continue;
                    }
                    List<Object> nums1 = getNumsIzq(i, operacion);
                    List<Object> nums2 = getNumsDrc(i, operacion);
                    double valor1 = calcularValor(nums1);
                    double valor2 = calcularValor(nums2);
                    double res = ac.calcular(valor1, valor2);
                    Numero resNum = new Numero(res);
                    operacion.set(i, resNum);
                    for(Object num : nums1) {
                        borrar.add(num);
                    }
                    for(Object num : nums2) {
                        borrar.add(num);
                    }
                    break;
                }
            }
        }
        for(Object obj : borrar) {
            operacion.remove(obj);
        }
        for(Object obj : operacion) {
            if(obj.getClass().equals(Accion.class)) {
                return procesarCalculo(operacion);
            }
        }
        return calcularValor(operacion);
    }

    private static boolean buscarPrioridad(List<Object> operacion) {
        for(Object obj : operacion) {
            if(obj.getClass().equals(Accion.class)) {
                Accion ac = (Accion) obj;
                if(ac.getPrioridad() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] buscarParentesis(List<Object> operacion) {
        int[] parentesis = {-1, -1};
        for(int i=0; i<operacion.size(); i++) {
            if(operacion.get(i).getClass().equals(Especial.class)) {
                Especial esp = (Especial) operacion.get(i);
                if(esp.getTipo() == Especial.PAR_IZQ && parentesis[0] == -1) {
                    parentesis[0] = i;
                } else if(esp.getTipo() == Especial.PAR_DRC) {
                    parentesis[1] = i;
                }
            }
        }
        return parentesis;
    }

    private static int buscarOpEspecial(List<Object> operacion) {
        int res = -1;
        for(int i=0; i<operacion.size(); i++) {
            if(operacion.get(i).getClass().equals(AccionEsp.class)) {
                return i;
            }
        }
        return res;
    }

    private static double calcularValor(List<Object> nums) {
        double res = 0.0;
        double numerales = 0.0;
        double decimales = 0.0;
        int numDecimales = 0;
        boolean decimal = false;
        for(Object obj : nums) {
            if(obj.getClass().equals(Numero.class)) {
                Numero num = (Numero) obj;
                if(!decimal) {
                    numerales *= 10;
                    numerales += num.getValor();
                } else {
                    decimales *= 10;
                    decimales += num.getValor();
                    numDecimales++;
                }

            } else if(obj.getClass().equals(Especial.class)) {
                Especial esp = (Especial) obj;
                if(esp.getTipo() == Especial.DECIMAL) {
                    decimal = true;
                }
            }
        }
        if(numDecimales == 0) {
            numDecimales++;
        }
        decimales = decimales / (10*numDecimales);
        res = numerales+decimales;
        return res;
    }

    private static List<Object> getNumsIzq(int pos, List<Object> operacion) {
        List<Object> nums = new ArrayList<Object>();
        int limite = 0;
        for(int i=pos-1; i>=0; i--) {
            if (operacion.get(i).getClass().equals(Accion.class)) {
                limite = i + 1;
                break;
            }
        }
        for(int i=limite; i<pos; i++) {
            nums.add(operacion.get(i));
        }
        return nums;
    }

    private static List<Object> getNumsDrc(int pos, List<Object> operacion) {
        List<Object> nums = new ArrayList<Object>();
        int limite = operacion.size()-1;
        for(int i=pos+1; i<operacion.size(); i++) {
            if(operacion.get(i).getClass().equals(Accion.class)) {
                limite = i-1;
                break;
            }
        }
        for(int i=pos+1; i<=limite; i++) {
            nums.add(operacion.get(i));
        }
        return nums;
    }

    public void salir(View view) {
        finish();
    }
}
