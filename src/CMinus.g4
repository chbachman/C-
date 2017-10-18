// Define a grammar called Hello
grammar CMinus;
init : statements EOF ;

statements: (statement | func | struct)* ;

func: 'func' ID '('parameterList?')' funcReturn? '{' codeBlock '}' ;
parameterList: parameter (',' parameter)* ;
parameter: ID':' type ;
funcReturn: '->' type ;

struct: 'struct' ID '{' classBlock '}' ;

classBlock: (variable ';' | func | initBlock)*;

initBlock: 'init' '('parameterList?')' '{' codeBlock '}';

type: ID ;

// Code Blocks will declare a new scope, and when exiting, remove said scope.
codeBlock: (statement)* ;

statement:
    (( print
      | functionCall
      | variable
      | ret
      | assignment
      ) ';') |
    ( control
      );

variable: ('var' ID '=' value) | ('var' ID ':' type) ;
assignment: identifier '=' value ;
functionCall: ((segment) ('.'(segment))* '.')? funcSegment ;
argumentList: argument (',' argument)* ;
argument: ID':' value ;
print: PRINT '(' value ')' ;
ret: RETURN value ;

identifier: (segment) ('.'(segment))* ;
segment: idSegment | funcSegment ;
idSegment: ID ;
funcSegment: ID '(' argumentList? ')' ;

value:
    functionCall
    | identifier
    | literal
    | assignment
    | paren='(' value ')'
    |<assoc=right> value op='^' value
    | value op='*' value
    | value op='/' value
    | value op='+' value
    | value op='-' value
    | value op='%' value
    | value op='==' value
    ;


control:
    ifStatement
    | forStatement
    ;

ifStatement:
    'if' value '{' codeBlock '}' ('else if' value '{' codeBlock '}')*? ('else' '{' codeBlock '}')?;

forStatement: 'for' range '{' codeBlock '}' ;
range: ID 'in' value'...'value ;

// Literals
literal: STRING | INT | FLOAT | BOOL;
BOOL : 'true' | 'false' ;
STRING : '"' (~'"'|'\\"')* '"' ;
INT : [0-9]+ ;
FLOAT: [0-9]+'.'[0-9]+ ;

// Reserved Keywords
PRINT: 'print' ;
RETURN: 'return' ;

ID : [a-zA-Z_]+ [a-zA-Z0-9_]*;

// Removals (C Style Comments)
COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~'\n'* '\n' -> channel(HIDDEN) ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
