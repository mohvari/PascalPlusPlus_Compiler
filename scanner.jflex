/*User Code*/
import java.util.*;
%%

/*Options and declarations*/

%class Scanner
%standalone
%line
%char
%column

whiteSpace = [ \t\f\n]+
positiveReal = [0-9]+\.[0-9]*
postiveInteger = [0-9]+
id = [a-zA-Z][a-zA-Z|_|0-9]*
keywords = array|boolean|begin|char|do|else|end|false|function|procedure|if|integer|of|real|return|string|true|while
unary_op = ~
binary_op = \+|\*|\/|&|\^|\||and|or|%

%%

/*Lexical Rules*/
{real} {System.out.println("float");}

{int} { System.out.println("int");}

{keywords} {System.out.println("keyword!");}

{id} {System.out.println(yytext());}

{whiteSpace} {}

. {System.out.println("Other");}