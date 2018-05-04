/*
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Formularios;
import Clases.Dato;


/**
 * Clase generadora de hojas que se van leyendo en el archivo de entrada
 * @author 
 *
 */
public class CreadorHojas {
    private frmEspacioTrabajo wp = null;
    
    public CreadorHojas() {
    }

    public void setWorkplace(frmEspacioTrabajo w){
        wp = w;
    }
    public frmEspacioTrabajo getWorkplace(frmEspacioTrabajo w){
        return wp;
    }
    /**Inserta un dato, si no existe la hoja, la crea,
     *@param row Fila
     *@param col Columna
     *@param expr Expresion a insertar en la fila y columna dada
     */
    public int insertarDatos(String idHoja,int row, int col,String expr){
        Dato dato = new Dato();
        dato.setExpr(expr); // asigna expresion
        // si se logro insertar la hoja asigna los datos
        wp.insertarHoja(idHoja);
        wp.getSheet(idHoja).getHoja().getTabla().insertar(dato,col,row);
        
        return 0;
    }
    /**Abre archivo dado por el parametro*/
//    Kreator(String filename){
//        try{
//            setFileName(filename);
//        }catch(Exception e){};
//    }
    /**Asigna un archivo de entrada*/
//    Kreator(FileReader f){
//        fr = f;
//    }
    /**Asigna filename al streamreader*/
    

    /** Crea una nueva instancia de Kreator */
    
    
    CreadorHojas(frmEspacioTrabajo w){
        wp = w;
    }
}
