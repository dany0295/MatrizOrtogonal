/*
 * Hoja.java
 *
 * Creada el 3 de marzo de 2006, 09:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Clases;

import Formularios.frmEspacioTrabajo;
import Clases.Dato;
import javax.swing.table.*;
/**
 * Modelo de tabla que implementa una matriz ortogonal como DDE para
 * representar las series de datos
 * @author Erik Giron (200313492)
 *
 */
public class Hoja extends AbstractTableModel {
    
    //private int max_col = (int) Math.pow(2,14) ; //almacena la columna maxima que se ha alcanzado
    //private int max_row = (int) Math.pow(2,18) ;
    
    private int max_col = (int) 21 ; //almacena la columna maxima que se ha alcanzado
    private int max_row = (int) 21 ;
    
    private String id = new String();
    private Matriz tabla = new Matriz();
    
    private Clases.Dato datoAInsertar = new Dato();
    public void setId(String sId){
        this.id = sId;
    }
    /**Devuelve el id de la hoja
     @return Identificador de la hoja*/
    public String getId(){
        return this.id;
    }
    /** Crea una nueva instancia de Hoja */
    public Hoja() {
        id = "Hoja1";
    }
    /**Constructor especificando el String*/
    public Hoja(String ID) {
        id = ID;
    }
    /**Vacia la matriz*/
    public void Borrar(){
        tabla.vaciar();
    }
    /**Obtiene tabla de la hoja*/
    public Matriz getTabla(){
        return this.tabla;
    }
    /**Inserta una nueva tabla en la hoja*/
    public void setTabla(Matriz m){
        tabla = m;
    }
    /**Devuelve el maximo numero de filas
     * @return filas*/
    public int getRowCount(){
        return max_row;
    }
    /**Devuelve el maximo numero de columnas
     * @return columnas*/
    public int getColumnCount(){
        return max_col;
    }
    /**Retorna valor al indice establecido*/
    public Object getValueAt(int row, int column){
        if (row == 0 && column == 0){
            return "";
        }
        else if (row == 0 && column > 0){
            return column;
        }
        else if (column == 0 && row > 0){
            return row;
        }
        else{
            this.tabla.irA(column,row);
            if(this.tabla.datoActual() != null)
                return this.tabla.stringActual();    
            else
                return "";
        }
        
    }
    /**Retorna verdadero para celdas que se pueden editar*/
    public boolean isCellEditable(int rowIndex, int columnIndex){
        if(rowIndex ==0 || columnIndex ==0)
            return false;
        else
            return true;
    }
    /**Ingresa o actualiza dato*/
    public void setValueAt(Object value, int row, int col) {
        datoAInsertar.setExpr(value.toString());
        this.tabla.insertar(datoAInsertar,col,row);
        fireTableCellUpdated(row, col);
    }
    /**Asigna referencia a la matriz del workplace donde se encuentra contenida*/
    public void setWorkplace(frmEspacioTrabajo wp){
        tabla.setWorkplace(wp);
    }
}
