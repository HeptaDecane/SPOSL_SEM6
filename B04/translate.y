%{
#include<stdio.h>
void yyerror(char*);
int yylex();
%}
%token INT FLOAT CHAR BOOL BLVAL CHVAL ID NL REAL NUM COM DL OP

%%
s: forint|forfloat|forchar|forbool
;
forint: INT varint DL  {printf("Valid integer declaration\n"); return 0;}
;
forfloat: FLOAT varfloat DL  {printf("Valid float declaration\n"); return 0;}
;
forchar: CHAR varchar DL {printf("Valid char declaration\n"); return 0;}
;
forbool: BOOL varbool DL {printf("Valid boolean declaration\n"); return 0;}
;

varint: ID | ID COM varint | ID OP NUM | ID OP NUM COM varint |
;
varfloat: ID | ID COM varfloat | ID OP REAL | ID OP REAL COM varfloat |
;
varchar: ID | ID COM varchar | ID OP CHVAL | ID OP CHVAL COM varchar |
;
varbool: ID | ID COM varbool | ID OP BLVAL | ID OP BLVAL COM varbool |
;
%%
void yyerror(char *s){
    fprintf(stderr, "Error: %s\n",s);
}

int main(){
	printf("Enter the exp: ");
    yyparse();
    return 0;    
}
