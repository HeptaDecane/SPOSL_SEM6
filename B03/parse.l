%{
	#include<stdio.h>
	int lines = 0;
	int space = 0;
	int alnum = 0;
	int special_chars = 0;
%}

%%
"\n" {lines++;}
(" ")+ {space++;}
[A-Za-z0-9] {alnum++;}
[$&+,:;=?@#|"'<>.^*()%!-] {special_chars++;}
. ;
%%

	int main(int argc,char* argv[]){
		yyin=fopen(argv[1],"r");
		yylex();
		printf("Lines: %d\n",lines);
		printf("Spaces: %d\n",space);
		printf("Words: %d\n",lines+space);
		printf("Alphanumeric: %d\n",alnum);
		printf("Special Chars: %d\n",special_chars);
		return 0;
	}

	int yywrap(void){
	 return 1;
	}
	
