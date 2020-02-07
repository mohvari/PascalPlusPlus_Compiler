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

comment_multi_line = [<\-\-][\S\s]*[\-\->]
white_space = [ |\t|\n]+
real = [0-9]+\.[0-9]*
number = [0-9]+
string = \"[a-zA-Z]*\"
character = \'[a-zA-z]\'
id = [a-zA-Z][a-zA-Z|_|0-9]*
keyword = array|begin|do|else|end|false|function|procedure|if|of|return|true|while
type = integer|real|string|char|boolean
unary_op = \~
binary_op = \+|\*|\/|&|\^|\||and|or|%|=|>=|>|<=|<|<>
minus_op = -
colon_assign = :=
colon = :
comma = ,
comment = [\-\-|//][^\n]*

%{
%}

%eof{
%eof}


%%

/*Lexical Rules*/

{comment_multi_line} {
    System.out.printf("comment_multi, %s\n", yytext());
}

{comment} {
    System.out.printf("comment, %s\n", yytext());
}

{real} {
    System.out.printf("real, %s\n", yytext());
}

{number} {
    System.out.printf("number, %s\n", yytext());
}

{string} {
    System.out.printf("string, %s\n", yytext());
}

{character} {
    System.out.printf("character, %s\n", yytext());
}

{keyword} {
    System.out.printf("keyword, %s\n", yytext());
}

{type} {
    System.out.printf("type, %s\n", yytext());
}

{id} {
    System.out.printf("id, %s\n", yytext());
}

{unary_op} {
    System.out.printf("unary_op, %s\n", yytext());
}

{colon_assign} {System.out.printf("colon_assign, %s\n", yytext());}

{colon} {System.out.printf(":, %s\n", yytext());}


{comma} {System.out.printf("comma, %s\n", yytext());}

{binary_op} {
    System.out.printf("binary_op, %s\n", yytext());
}

{minus_op} {
    System.out.printf("minus_op, %s\n", yytext());
}


{white_space} {System.out.println("White Space or New Line");}


. {System.out.println("Other");}