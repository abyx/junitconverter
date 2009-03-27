lexer grammar Java;
@members {
  protected boolean enumIsKeyword = true;
  protected boolean assertIsKeyword = true;
  
  private StringBuilder string = new StringBuilder();
  public String getAll() { return string.toString(); }
  private void addString(String text) {
  	string.append(text);
  }
}
@header {
package junitconverter;
}

T25 : 'package' ;
T26 : ';' ;
T27 : 'import' ;
T28 : 'static' ;
T29 : '.' ;
T30 : '*' ;
T31 : 'public' ;
T32 : 'protected' ;
T33 : 'private' ;
T34 : 'abstract' ;
T35 : 'final' ;
T36 : 'strictfp' ;
T37 : 'class' ;
T38 : 'extends' ;
T39 : 'implements' ;
T40 : '<' ;
T41 : ',' ;
T42 : '>' ;
T43 : '&' ;
T44 : '{' ;
T45 : '}' ;
T46 : 'interface' ;
T47 : 'void' ;
T48 : '[' ;
T49 : ']' ;
T50 : 'throws' ;
T51 : '=' ;
T52 : 'native' ;
T53 : 'synchronized' ;
T54 : 'transient' ;
T55 : 'volatile' ;
T56 : 'boolean' ;
T57 : 'char' ;
T58 : 'byte' ;
T59 : 'short' ;
T60 : 'int' ;
T61 : 'long' ;
T62 : 'float' ;
T63 : 'double' ;
T64 : '?' ;
T65 : 'super' ;
T66 : '(' ;
T67 : ')' ;
T68 : '...' ;
T69 : 'this' ;
T70 : 'null' ;
T71 : 'true' ;
T72 : 'false' ;
T73 : '@' ;
T74 : 'default' ;
T75 : ':' ;
T76 : 'if' ;
T77 : 'else' ;
T78 : 'for' ;
T79 : 'while' ;
T80 : 'do' ;
T81 : 'try' ;
T82 : 'finally' ;
T83 : 'switch' ;
T84 : 'return' ;
T85 : 'throw' ;
T86 : 'break' ;
T87 : 'continue' ;
T88 : 'catch' ;
T89 : 'case' ;
T90 : '+=' ;
T91 : '-=' ;
T92 : '*=' ;
T93 : '/=' ;
T94 : '&=' ;
T95 : '|=' ;
T96 : '^=' ;
T97 : '%=' ;
T98 : '||' ;
T99 : '&&' ;
T100 : '|' ;
T101 : '^' ;
T102 : '==' ;
T103 : '!=' ;
T104 : 'instanceof' ;
T105 : '+' ;
T106 : '-' ;
T107 : '/' ;
T108 : '%' ;
T109 : '++' ;
T110 : '--' ;
T111 : '~' ;
T112 : '!' ;
T113 : 'new' ;

// $ANTLR src "src/junitconverter/Java.g" 999
HexLiteral : '0' ('x'|'X') HexDigit+ IntegerTypeSuffix? {addString(getText());};

// $ANTLR src "src/junitconverter/Java.g" 1001
DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) IntegerTypeSuffix?  {addString(getText());};

// $ANTLR src "src/junitconverter/Java.g" 1003
OctalLiteral : '0' ('0'..'7')+ IntegerTypeSuffix?  {addString(getText());};

// $ANTLR src "src/junitconverter/Java.g" 1005
fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

// $ANTLR src "src/junitconverter/Java.g" 1008
fragment
IntegerTypeSuffix : ('l'|'L') ;

// $ANTLR src "src/junitconverter/Java.g" 1011
FloatingPointLiteral
    :   ('0'..'9')+ '.' ('0'..'9')* Exponent? FloatTypeSuffix?  {addString(getText());}
    |   '.' ('0'..'9')+ Exponent? FloatTypeSuffix?  {addString(getText());}
    |   ('0'..'9')+ Exponent FloatTypeSuffix?  {addString(getText());}
    |   ('0'..'9')+ FloatTypeSuffix  {addString(getText());}
    ;

// $ANTLR src "src/junitconverter/Java.g" 1018
fragment
Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

// $ANTLR src "src/junitconverter/Java.g" 1021
fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D') ;

// $ANTLR src "src/junitconverter/Java.g" 1024
CharacterLiteral
    :   '\'' ( EscapeSequence | ~('\''|'\\') ) '\''  {addString(getText());}
    ;

// $ANTLR src "src/junitconverter/Java.g" 1028
StringLiteral
    :  '"' ( EscapeSequence | ~('\\'|'"') )* '"'  {addString(getText());}
    ;

// $ANTLR src "src/junitconverter/Java.g" 1032
fragment
EscapeSequence
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UnicodeEscape
    |   OctalEscape
    ;

// $ANTLR src "src/junitconverter/Java.g" 1039
fragment
OctalEscape
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

// $ANTLR src "src/junitconverter/Java.g" 1046
fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

// $ANTLR src "src/junitconverter/Java.g" 1051
ENUM:   'enum' {if (!enumIsKeyword) $type=Identifier;  {addString(getText());}}
    ;
    
// $ANTLR src "src/junitconverter/Java.g" 1054
ASSERT
    :   'assert' {if (!assertIsKeyword) $type=Identifier;  {addString(getText());}}
    ;
    
// $ANTLR src "src/junitconverter/Java.g" 1058
Identifier 
    :   Letter (Letter|JavaIDDigit)*  {addString(getText());}
    ;

// $ANTLR src "src/junitconverter/Java.g" 1062
/**I found this char range in JavaCC's grammar, but Letter and Digit overlap.
   Still works, but...
 */
fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

// $ANTLR src "src/junitconverter/Java.g" 1082
fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

// $ANTLR src "src/junitconverter/Java.g" 1101
WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN; addString(getText());}
    ;

// $ANTLR src "src/junitconverter/Java.g" 1104
COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN; addString(getText());} 
    ;

// $ANTLR src "src/junitconverter/Java.g" 1108
LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;  {addString(getText());}}
    ;
