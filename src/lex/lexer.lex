//import sym.*;               // definition of terminals returned by scanner
package lex;

import java_cup.runtime.*;  // definition of scanner/parser interface

/* semantic value of token returned by scanner */
class TokenValue {          
  public int lineBegin;
  public int charBegin;
  public String text;
  public String filename;   

  TokenValue() {
  }

  TokenValue(String text, int lineBegin, int charBegin, String filename) {
    this.text = text; 
    this.lineBegin = lineBegin; 
    this.charBegin = charBegin;
    this.filename = filename;
  }

  public String toString() { 
    return text;
  }

  public boolean toBoolean() {
    return Boolean.valueOf(text).booleanValue();  
  }
  
  public int toInt() {
    return Integer.valueOf(text).intValue();
  }

  public double toDouble() {
    return Double.valueOf(text).doubleValue();
  }
}

%%

%class Lexer
%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol



%eofval{
  return new Symbol(sym.EOF, null);
%eofval}
%{
  public String sourceFilename;
  
  public void init(){};
%}
%line
%char
%public

ALPHA=[A-Za-z_]
DIGITO=[0-9]
ALPHA_NUMERIC={ALPHA}|{DIGITO}
ID={ALPHA}({ALPHA_NUMERIC})*
ENTEROPOS=({DIGITO})+
WHITE_SPACE=([\ \n\r\t\f])+

%%

<YYINITIAL> {ENTEROPOS} {
  return new Symbol(sym.ENTEROPOS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> {ENTEROPOS}("."{ENTEROPOS})? {
  return new Symbol(sym.FLOTANTE, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> {ID} {
  return new Symbol(sym.ID, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}  

<YYINITIAL> ({ALPHA_NUMERIC}|" ")({ALPHA_NUMERIC}|" "|":")* {
  return new Symbol(sym.CADENA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "," {
  return new Symbol(sym.COMA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "\'" {
  return new Symbol(sym.APOSTROFE, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "\(" {
  return new Symbol(sym.APARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "\)" {
  return new Symbol(sym.CPARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> ";" {
  return new Symbol(sym.PUNTOYCOMA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "=" {
  return new Symbol(sym.IGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "SUMA" {
  return new Symbol(sym.KW_SUMA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "MULT" {
  return new Symbol(sym.KW_MULT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "PROM" {
  return new Symbol(sym.KW_PROM, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "DATO" {
  return new Symbol(sym.KW_DATO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "\+" {
  return new Symbol(sym.MAS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "\-" {
  return new Symbol(sym.MENOS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "\*" {
  return new Symbol(sym.POR, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "\/" {
  return new Symbol(sym.DIV, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}


<YYINITIAL> {WHITE_SPACE} { }


<YYINITIAL> . {
  return new Symbol(sym.error, null);
}
