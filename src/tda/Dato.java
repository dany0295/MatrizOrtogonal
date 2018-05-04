/*
 * Datos.java
 *
 * Creada el 27 de febrero de 2006, 05:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tda;
import lex.*;
import java.io.*;
import electric_sheet.*;
/**
 * Clase que almacena los datos guardados en las celdas de la hoja 
 * electronica.
 * @author Erik Giron (200313492)
 *
 */
public class Dato {
    
    private static boolean debugear = false;
    private Matriz mat; //puntero a la matriz a la que pertenece
    
    private Matriz.Nodo nodo; //puntero al nodo donde se encuentra el dato
    /**Expresion que representa al dato*/
    private String expr;
    /** Valor numerico (si hubiese) del dato */
    private double valor;
    /** Crea una nueva instancia de Datos */
    public Dato() {
        expr = "";
        valor = 0;
    }
    public Dato(String e, double v,Matriz m){
        expr = new String(e);
        valor = v;
        mat = m;
    }
    
    public Dato(String e, double v,Matriz m,Matriz.Nodo n){
        this(e,v,m);
        this.nodo = n;
    }
    public Dato(String e){
        expr = new String(e);
    }
    /**Retorna fila donde se encuentra el dato*/
    public int getRow(){
        return nodo.getRow();
    }
    /**Retorna columna donde se encuentra el dato*/
    public int getCol(){
        return nodo.getCol();
    }
    /**Asigna expresion a dato*/
    public void setExpr(String e){
        expr = new String(e);        
    }
    /**Asigna puntero a la expresion*/
    public void setPtrExpr(String e){
        expr = e;
    }
    /**
     * @return Expresion a evaluar*/
    public String getExpr(){
        return expr;
    }
    
    /**Asigna valor numerico para acelerar los calculos*/
    public void setValor(double v){
        valor = v;
    }
    
    /**@return Valor de coma flotante que representa el valor numerico del dato*/
    public double getValor(){
        return valor;
    }
    /**Retorna representacion de cadena de la expresion ya evaluada*/
    public String toString(){
        return eval();
    }
    /**establece puntero hacia la matriz a donde pertenece*/
    public void setMatriz(Matriz m){
        this.mat = m;
    }
    /**Devuelve referencia de la matriz donde se encuentra el dato*/
    public Matriz getMatriz(){
        return mat;
    }
    /**establece puntero hacia el nodo donde esta el dato*/
    public void setNodo(Matriz.Nodo n){
        this.nodo = n;
    }
    /**Devuelve referencia del nodo donde se encuentra el dato*/
    public Matriz.Nodo getNodo(){
        return nodo;
    }
    
    
    /**Retorna la expresion evaluada para la celda*/
    public String eval(){
      String ret = new String();
      int x0,y0,x1,y1,modo;
      String idHoja;
      Dato tmpDato;
      boolean huboError = false;
      Sheet tmpSheet;
      
      StringReader st = new StringReader(expr);
      /* create a parsing object */
      parser parser_obj = new parser(new Lexer(st)); 
      parser_obj.setModo(0);
      /* open input files, etc. here */
      java_cup.runtime.Symbol parse_tree = null;

      try {
          if(debugear)
            parse_tree = parser_obj.debug_parse();
          else
            parse_tree = parser_obj.parse();
        } catch (Exception e) {
        /* do cleanup here - - possibly rethrow e */
          System.out.println("Algo pasa " + e.toString());
          huboError = true;
        } finally {
	/* do close out here */
      }
      /*Si no hubo error Obtiene valor evaluado segun el tipo de expresion*/
      if(!huboError){
          ret = parser_obj.getEvaluatedExpr();
          modo = parser.getModo();
          switch (modo){
              case 0: /*si es una expresion aritmetica*/
                      try{ // si es un numero
                        this.valor = Double.valueOf(ret);
                        this.expr = "=" + parser_obj.getEvaluatedExpr();
                      }
                      catch(Exception e){ // si es una cadena
                        this.valor = 0;
                        this.expr = parser_obj.getEvaluatedExpr();
                      }
                  break;
              case 1: /*Si es una asignacion*/
                      x0 = parser.getX0();
                      y0 = parser.getY0();

                      mat.irA(x0,y0); // apunta al nodo especificado
                      tmpDato = mat.datoActual();
                      if (tmpDato!=null){ // si existe el nodo asigna
                          this.setPtrExpr(tmpDato.getExpr()); //enlaza la expresion del dato anterior con esta
                          this.valor = tmpDato.getValor(); //obtiene el valor
                          ret = this.expr;//String.valueOf(valor);
                      }
                  break;
              case 2: /*si es una sumatoria*/
                    x0 = parser.getX0();
                    y0 = parser.getY0();
                    x1 = parser.getX1();
                    y1 = parser.getY1();
                    idHoja = parser.getIdHoja();
                    this.expr = ("=suma(" +
                                idHoja + "," +
                                "(" + String.valueOf(x0) +","+ String.valueOf(y0) +")" + "," +
                                "(" + String.valueOf(x1) +","+ String.valueOf(y1) +")" +
                                ")");
                    tmpSheet = mat.getWorkplace().getSheet(idHoja);
                    if (tmpSheet != null){
                        this.valor = Operacion.sumatoria(tmpSheet.getHoja().getTabla().getSubset(
                                                            x0,y0,x1,y1));
                    }
                    
                    // Transitorio
                    ret = String.valueOf(valor); 
                  break;
              case 4: /*si es una multiplicatoria*/
                    x0 = parser.getX0();
                    y0 = parser.getY0();
                    x1 = parser.getX1();
                    y1 = parser.getY1();
                    idHoja = parser.getIdHoja();
                    this.expr = ("=mult(" +
                                idHoja + "," +
                                "(" + String.valueOf(x0) +","+ String.valueOf(y0) +")" + "," +
                                "(" + String.valueOf(x1) +","+ String.valueOf(y1) +")" +
                                ")");
                    tmpSheet = mat.getWorkplace().getSheet(idHoja);
                    if (tmpSheet != null){
                        this.valor = Operacion.multiplicatoria(tmpSheet.getHoja().getTabla().getSubset(
                                                            x0,y0,x1,y1));
                    }
                    
                    
                    // Transitorio
                    ret = String.valueOf(valor); 
                  break;
              case 6:/*si es un promedio*/
                    x0 = parser.getX0();
                    y0 = parser.getY0();
                    x1 = parser.getX1();    
                    y1 = parser.getY1();
                    
                    idHoja = parser.getIdHoja();
                    this.expr = ("=prom(" +
                                idHoja + "," +
                                "(" + String.valueOf(x0) +","+ String.valueOf(y0) +")" + "," +
                                "(" + String.valueOf(x1) +","+ String.valueOf(y1) +")" +
                                ")");
                    tmpSheet = mat.getWorkplace().getSheet(idHoja);
                    if (tmpSheet != null){
                        this.valor = Operacion.media(tmpSheet.getHoja().getTabla().getSubset(
                                                            x0,y0,x1,y1));
                    }
                    

                    // Transitorio
                    ret = String.valueOf(valor); 
                  break;
              default:
                  
                  break;
          }
//          
//          if( parser.getModo() == 0){/*si es una expresion aritmetica*/
//              try{
//                this.valor = Double.valueOf(ret);
//              }catch(Exception e){
//                this.valor = 0;
//              }
//          }
//         /*si es una funcion*/
//          else if (parser.getModo() == 4 || parser.getModo() == 8 
//                    || parser.getModo() == 16){
//            x0 = parser.getX0();
//            y0 = parser.getY0();
//            x1 = parser.getX1();
//            y1 = parser.getY1();
//            
//            // Transitorio
//            ret = String.valueOf(valor); 
//          }
//        /*Si es una asignacion*/
//          else if (parser.getModo() == 2){ 
//            x0 = parser.getX0();
//            y0 = parser.getY0();
//            
//            mat.irA(x0,y0); // apunta al nodo especificado
//            tmpDato = mat.datoActual();
//            if (tmpDato!=null){ // si existe el nodo asigna
//                this.setPtrExpr(tmpDato.getExpr()); //enlaza la expresion del dato anterior con esta
//                this.valor = tmpDato.getValor(); //obtiene el valor
//                ret = String.valueOf(valor);
//            }
//          } 
      }
      else{ /**Si hubo error de parse*/
        valor = 0;
        ret = new String(" Error: Imposible evaluar expresion");
      }

      return ret;
    }
}
