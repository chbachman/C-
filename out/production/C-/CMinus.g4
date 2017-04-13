// Define a grammar called Hello
grammar CMinus;
init : line*;
line  : func | statement;


// com.chbachman.cminus.representation.Function Declaration
func: 'func' ID '('parameterList?')' funcReturn? '{' (funcStatement)* '}' ;
parameterList: parameter (',' parameter)* ;
parameter: type ID ;
funcReturn: '->' type ;

type: ID ;

statement:
    print |
    functionCall |
    variable ;

funcStatement:
    statement |
    ret ;

variable: (VARIABLE_DECLARATION? ID '=' value) | (VARIABLE_DECLARATION ID ':' type) ;
functionCall: ID '(' (value (',' value)*)? ')';
print: 'print' '(' value ')' ;
ret: 'return' value ;

value: ID | literal | functionCall ;

ID : [a-zA-Z_]+ [a-zA-Z0-9_]* ;

// Literals
literal: STRING | INT | FLOAT ;
STRING : '"' (~'"'|'\\"')* '"' ;
INT : [0-9]+ ;
FLOAT: [0-9]+'.'[0-9]+ ;
VARIABLE_DECLARATION: 'var' ;

// Removals (C Style Comments)
COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~'\n'* '\n' -> channel(HIDDEN) ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines