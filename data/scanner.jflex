/*User Code*/
import java.util.*;
import java.io.FileWriter;
%%

/*Options and declarations*/

%class Scanner
%standalone
%line
%char
%column

whiteSpace = [ \t\f\n]+
real = [0-9]+\.[0-9]*
number = [0-9]+
id = [a-zA-Z][a-zA-Z|_|0-9]*
keywords = array|boolean|begin|char|do|else|end|false|function|procedure|if|integer|of|real|return|string|true|while
unary_op = \~
binary_op = \+|\*|\/|&|\^|\||and|or|%
minus_op = -

%{
    FileWriter fileWriter = new FileWriter("../data/scanner_output.txt");
%}

%eof{
    fileWriter.close();
%eof}


%%

/*Lexical Rules*/
{real} {
    fileWriter.write("");

}

{number} {}

{keywords} {}

{id} {fileWriter.write("id, %s\n", yytext());}

{unary_op} {}

{binary_op} {}

{minus_op} {}

{whiteSpace} {}

. {System.out.println("Other");}