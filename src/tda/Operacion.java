/*
 * Operacion.java
 *
 * Creada el 10 de marzo de 2006, 05:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tda;

import java.util.*;

/**
 * Clase que agrupa operaciones a realizar con un conjunt ode datos dados en forma 
 * de una lista enlazada
 * @author Erik Giron (200313492)
 *
 */
public class Operacion {
    private static float retval;
    private static Dato tmpDato = null;
    /** Crea una nueva instancia de Operacion */
    public Operacion() {
        
    }
    /**Realiza una sumatoria
     @param datos Lista enlazada que representa el conjunto de datos a operar
     @return Sumatoria*/
    public static double sumatoria(LinkedList datos){
        retval = 0;
        while(datos.size() > 0){
            tmpDato =(Dato) datos.poll();
            retval += tmpDato.getValor();
        }
        return retval;       
    }
    /**Realiza una multiplicatoria
     * @param datos Lista enlazada que representa el conjunto de datos a operar
     * @return Multiplicatoria de la serie de numeros*/
    public static double multiplicatoria(LinkedList datos){
        if (datos.size()>0){
            retval = 1;
            while(datos.size() > 0){
                tmpDato =(Dato) datos.poll();
                retval *= tmpDato.getValor();
            }
            return retval;
        }
        return 0;
    }
    /**Realiza una media aritmetica
     * @param datos Lista enlazada que representa el conjunto de datos a operar
     * @return Media*/
    public static double media(LinkedList datos){
        int numdatos = datos.size();
        retval = 0;
        while(datos.size() > 0){
            tmpDato =(Dato) datos.poll();
            retval += tmpDato.getValor();
        }
        return (retval / numdatos);
    }
}
