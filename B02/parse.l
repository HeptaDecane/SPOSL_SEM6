%{
	#include<stdio.h>
	
	typedef struct String{
		char characters[64];
	} String;
	
	String symbol_table[128];
	int table_index = 0;
	
	void insert_symbol(String symbol);
	int fg = 0;
	int comment_open = 0;
	int declaration = 0;
	int parenthesis_open = 0;
	
%}

DIGIT [0-9]
NUMBER {DIGIT}+
REAL {DIGIT}*[.]{NUMBER}
TEXT [A-Za-z]
KEYWORDS "main"|"public"|"System"|"out"|"println"|"class"|"static"|"this"
DATATYPE "int"|"float"|"double"|"long"|"void"|"String"
CONDITIONAL "if"|"else"|"else if"|"switch"|"case"
ITERATIVE "for"|"while"|"do"
PREPROCESSOR "import"[^\n]*";"
STRING_LITERAL \"([^\\\"]|\\.)*\"
SC ";"
IDENTIFIER {TEXT}({DIGIT}|{TEXT}|"_")*
NONIDENTIFIER {DIGIT}({TEXT}|{DIGIT}|"_")*
ARITH_OP "+"|"-"|"/"|"%"|"*";
LOGICAL_OP "&&"|"||"|"!"|"!="
REL_OP "<"|">"|"<="|">="|"=="
UNARY "++"|"--"
ACCESS "public"|"private"|"protected"|""
%%
[\n\t]+ ;
{PREPROCESSOR} {if(comment_open==0) printf("%s\t==> PREPROCESSOR\n",yytext);}
{CONDITIONAL} {if(comment_open==0) printf("%s\t==> CONDITIONAL\n",yytext);}
{ITERATIVE} {if(comment_open==0)printf("%s\t==> ITERATIVE CONSTRUCT\n",yytext);}
{DATATYPE} {
	if(comment_open==0){
		printf("%s\t==> DATATYPE\n",yytext);
		declaration = 1;
	}
}
{ACCESS} {if(comment_open==0) printf("%s\t==> ACCESS SPECIFIER\n",yytext);}
{KEYWORDS} {if(comment_open==0) printf("%s\t==> KEYWORDS\n",yytext);}
{IDENTIFIER} {
	if(comment_open==0){
		printf("%s\t==> IDENTIFIER\n",yytext);
		if(declaration==1 && !parenthesis_open){
			String symbol;
			strcpy(symbol.characters,yytext);
			insert_symbol(symbol);
		}
	}
}
{STRING_LITERAL} {if(comment_open==0) printf("%s\t==> STRING LITERAL\n",yytext);}
{REAL} {if(comment_open==0) printf("%s\t==> REAL CONSTANT\n",yytext);}
{NUMBER} {if(comment_open==0) printf("%s\t==> CONSTAINT INTEGER\n",yytext);}
{NONIDENTIFIER} {if(comment_open==0) printf("%s\t==> NONIDENTIFIER\n",yytext);}
{SC} {
	if(comment_open==0){
		printf("%s\t==> DELIMITER\n",yytext);
		declaration = 0;
	}
}
{UNARY} {if(comment_open==0) printf("%s\t==> UNARY OP\n",yytext);}
{ARITH_OP} {if(comment_open==0) printf("%s\t==> ARITHMETIC OPERATOR\n",yytext);}
{LOGICAL_OP} {if(comment_open==0) printf("%s\t==> LOGICAL OP\n",yytext);}
{REL_OP} {if(comment_open==0) printf("%s\t==> RELATIONAL OP\n",yytext);}
"=" {if(comment_open==0) printf("%s\t==> ASSIGNMENT OP\n",yytext);}
"{"  {if(comment_open==0) printf("%s\t==> BLOCK BEGIN\n",yytext);}
"}"  {if(comment_open==0) printf("%s\t==> BLOCK END\n",yytext);}
"("  {
	if(comment_open==0){
		printf("%s\t==> PARANTHESIS BEGIN\n",yytext);
		parenthesis_open = 1;
	}
}
")"  {
	if(comment_open==0) {
		printf("%s\t==> PARENTHESIS END\n",yytext);
		parenthesis_open--;
		declaration = 0;
	}
}
\s*"//".* ;
"/*" {comment_open=1;}
"*/" {comment_open=0;}
. ;
%%

	int main(int argc,char* argv[]){
		yyin=fopen(argv[1],"r");
		yylex();
		printf("\n\nINDEX\tSYMBOL\n");
		for(int i=0;i<table_index;i++){
			printf("%d\t%s\n",i,symbol_table[i].characters);
		}
		fclose(yyin);
	}
	
	void insert_symbol(String symbol){
		int i;
		for(i=0; i<table_index; i++){
			if(strcmp(symbol_table[i].characters,symbol.characters)==0){
				break;
			}
		}
		if(i==table_index){
			symbol_table[i] = symbol;
			table_index++;
		}
	}
	
	int yywrap(){
		return 1;
	}

