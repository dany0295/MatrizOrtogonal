package lex;

import java_cup.runtime.*;
import java.util.*;
import electric_sheet.*;
/* Preliminaries to set up and use the scanner.  
init with {: scanner.init();    :}
scan with {: return scanner.next_token();:};
*/
parser code {:	
                private static int modo = 0; // 0: aritmetica 1: funcion o asignacion
                // lista de numeros para evaluar funciones con listas de numeros
                protected LinkedList<Double> listaNumeros = new LinkedList<Double>(); 
                /*Coordenadas para */
                private static int colx0; //columna en x
                private static int fily0; //columna en y
                private static int colx1; //columna en x
                private static int fily1; //columna en y
                private static String idHoja; //Hoja en la que se evaluara
                private static String expr; //Hoja en la que se evaluara
                private Kreator creador;
                /*public Lexer scanner;*/
		private static String retval;
		public void setRetval(String s){
			retval = s;
		}
                /**Asigna la clase creadora de datos*/
                public void setCreador(Kreator kr){
                    creador = kr;
                }
                /**Devuelve la clase creadora de datos*/
                public Kreator getCreador(){
                    return creador;
                }
		/**Retorna expresion aritmetica evaluada*/
		public String getEvaluatedExpr(){
			return retval;
		}
                public static int getX0(){
                    return colx0;
                    }
                public static int getY0(){
                    return fily0;
                }
                public static void setX0(int x){
                    colx0 = x;
                }
                public static void setY0(int y){
                    fily0 = y;
                }
                public static int getX1(){
                    return colx1;
                    }
                public static int getY1(){
                    return fily1;
                }
                public static void setX1(int x){
                    colx1 = x;
                }
                public static void setY1(int y){
                    fily1 = y;
                }
                public static String getIdHoja(){
                    return idHoja;
                }
                public static void setIdHoja(String id){
                    idHoja = new String(id);
                }
                public static String getExpr(){
                    return expr;
                }
                public static void setExpr(String id){
                    expr = new String(id);
                }
                public static int getModo(){
                    return modo;
                }
                public static void setModo(int x){
                    modo = x;
                }
:};

/* Terminals (tokens returned by the scanner). */
terminal           COMA, APOSTROFE, APARENT, CPARENT, PUNTOYCOMA;
terminal           IGUAL, /*KW_SUMA, KW_MULT, KW_PROM, */KW_DATO, WHITE_SPACE;
terminal           MAS,MENOS,POR,DIV,UMENOS;
terminal Double    FLOTANTE;
terminal Integer   ENTEROPOS;
terminal String    CADENA,ID,EXPR;

/* Non-terminals */
non terminal           inicio, file_in, params, rango, coords0 ,coords1, asignacion;
non terminal Double    expr, operacion , serie_num, numero;
non terminal String    evaluacion;

/* Precedences */

precedence left    POR;
precedence left    DIV;
precedence left	   MAS;
precedence left    MENOS;
precedence right    UMENOS;
/* The grammar */
inicio ::=	inicio file_in {:parser.getCreador().insertarDatos(parser.getIdHoja(),
                                                        parser.getY0(),parser.getX0(),parser.getExpr());:}
                | file_in {:parser.getCreador().insertarDatos(parser.getIdHoja(),
                                                        parser.getY0(),parser.getX0(),parser.getExpr());:}
                | evaluacion:e {:parser.setRetval(new String(e.toString()));:}
		;

file_in ::= 	ID:id APARENT EXPR:hoja COMA 
				EXPR:y COMA
				EXPR:x COMA
				EXPR:ex
			CPARENT PUNTOYCOMA
                        {:
                        if (id.equalsIgnoreCase("Dato")){
                        parser.setIdHoja(new String(hoja));
                        parser.setX0(Integer.valueOf(x).intValue());
                        parser.setY0(Integer.valueOf(y).intValue());
                        parser.setExpr(new String(ex));
                        }
                        :}
		;
evaluacion ::= 	IGUAL operacion:o1 {:RESULT = new String(new Double(o1.doubleValue()).toString());:}
		| FLOTANTE:f {:RESULT = new String(new Double(f.doubleValue()).toString());:}
                | MENOS FLOTANTE:f {:RESULT = new String(new Double( 0 - f.doubleValue()).toString() );:}
                %prec UMENOS
                | ENTEROPOS:e {:RESULT = new String(new Integer(e.intValue()).toString());:}
                | MENOS ENTEROPOS:e {:RESULT = new String(new Integer( 0 - e.intValue() ).toString() );:}
                %prec UMENOS
                | ID:id {:RESULT = new String(id);:}
                | CADENA:c {: parser.setModo(0);parser.setExpr(new String(c));RESULT = new String(c);:}
                //| WHITE_SPACE {:RESULT = new String("");:}
                ;
		
operacion ::=	ID:id APARENT rango CPARENT {:
                    RESULT = new Double(1);
                    if (id.equalsIgnoreCase("SUMA"))
                        parser.setModo(2);
                    else if (id.equalsIgnoreCase("MULT"))
                        parser.setModo(4);
                    else if (id.equalsIgnoreCase("PROM"))
                        parser.setModo(6);
                :}
                | ID:id APARENT serie_num CPARENT {:
                /* Se implementara un algoritmo similar al aplicado en electric_sheet.tda.Operacion */
                    double retval = 0;
                    Double readed = new Double(0);
                    if (id.equalsIgnoreCase("SUMA")){
                        while(parser.listaNumeros.size() > 0){
                            readed = (Double) parser.listaNumeros.poll();
                            retval += readed.doubleValue();
                        }     
                    }
                    else if (id.equalsIgnoreCase("MULT")){
                        if (parser.listaNumeros.size()>0){
                            retval = 1;
                            while(parser.listaNumeros.size() > 0){
                                readed = (Double) parser.listaNumeros.poll();
                                retval *= readed.doubleValue();
                            }
                        }
                    }
                    else if (id.equalsIgnoreCase("PROM")){
                        int numdatos = parser.listaNumeros.size();
                        retval = 0;
                        while(parser.listaNumeros.size() > 0){
                            readed = (Double) parser.listaNumeros.poll();
                            retval += readed.doubleValue();
                        }
                        if (numdatos > 0)
                            retval = retval / numdatos;
                        else
                            retval = 0;
                    }
                    parser.setModo(0);
                    RESULT = retval;
                :}
		| asignacion {:RESULT = new Double(2);parser.setModo(1);:}
		| expr:e {:RESULT = e.doubleValue();parser.setModo(0);:}
		;


serie_num ::=	serie_num COMA numero:f {:parser.listaNumeros.add(new Double(f));:}
		| numero:f {:parser.listaNumeros.add(new Double(f));:}
		;

rango	::=	/*CADENA*/ID:hoja COMA coords0 COMA coords1 {:parser.setIdHoja(new String(hoja));:}
		;


asignacion ::=	 coords0
		;

coords0	::=	APARENT ENTEROPOS:x COMA ENTEROPOS:y CPARENT
                {:  if (x.intValue()>0 && y.intValue()>0){
                        parser.setX0(new Integer(x.intValue()));
                        parser.setY0(new Integer(y.intValue()));
                    }
                :}
		;
coords1	::=	APARENT ENTEROPOS:x COMA ENTEROPOS:y CPARENT
                {:  parser.setX1(new Integer(x.intValue()));
                    parser.setY1(new Integer(y.intValue()));:}
		;


expr ::=	numero:n {:RESULT = new Double(n.doubleValue());:}
		| expr:e1 MAS numero:e2
		{:RESULT = new Double(e1.doubleValue() + e2.doubleValue());:}
		| expr:e1 MENOS numero:e2
		{:RESULT = new Double(e1.doubleValue() - e2.doubleValue());:}
		| expr:e1 POR numero:e2
		{:RESULT = new Double(e1.doubleValue() * e2.doubleValue());:}
		| expr:e1 DIV numero:e2
		{:
		if(e2.doubleValue() != 0)
			RESULT = new Double(e1.doubleValue() * e2.doubleValue());
		else
			RESULT = new Double(0);
		:}
		;
numero::=       FLOTANTE:f {:RESULT=f.doubleValue();:}
                | ENTEROPOS:e {:RESULT = (double) e.intValue();:}
                | MENOS FLOTANTE:e1
		{:RESULT = new Double(0 - e1.doubleValue());:}
                | MENOS ENTEROPOS:e1
		{:RESULT = new Double(0 - (double) e1.intValue());:}
                ;



