/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import javax.swing.JOptionPane;

/**
 *
 * @author darkparadox
 */
public class ListaDoble {
    private NodoDoble inicio, fin;
    public ListaDoble(){
        inicio=fin=null;
    }
    // Metodo para saber cuando la lista está vacía
    public boolean estVacia(){
        return inicio==null;
    }
    // Metodo para agreagr nodos al final
    public void agregarAlFinal(int el){
        if(!estVacia()){
            fin=new NodoDoble(el, null, fin);
            fin.anterior.siguiente = fin;
        }
        else{
            inicio=fin=new NodoDoble(el);
        }
    }
    // Metodo para agreagr nodos al inicio
    public void agregarAlInicio(int el){
        if(!estVacia()){
            inicio=new NodoDoble(el, inicio, null);
            inicio.siguiente.anterior = inicio;
        }
        else{
            inicio=fin=new NodoDoble(el);
        }
    }
    // Método para mostrar la lista de inicio a fin
    public void mostrarListaInicioFin(){
        if(!estVacia()){
            String datos="<=>";
            NodoDoble auxiliar = inicio;
            while(auxiliar!=null){
                datos = datos + "[" + auxiliar.dato + "]<=>";
                auxiliar = auxiliar.siguiente;
                System.out.print(datos);
            }
            JOptionPane.showMessageDialog(null, datos, "Mostrando la lista de inicio a Fin", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // Método para mostrar la lista de fin a Inicio
    public void mostrarListaFinInicio(){
        if(!estVacia()){
            String datos="<=>";
            NodoDoble auxiliar = fin;
            while(auxiliar!=null){
                datos = datos + "[" + auxiliar.dato + "]<=>";
                auxiliar = auxiliar.anterior;
            }
            System.out.print(datos);
            //JOptionPane.showMessageDialog(null, datos, "Mostrando la lista de inicio a Fin", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
