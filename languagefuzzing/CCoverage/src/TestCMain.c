#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <uriparser/Uri.h>

int
main(void)
{
    FILE *fp;
    char *line = NULL;
    size_t len = 0;
    ssize_t read;

    #define ADD_SPACE 1500
    char* exceptions;
    int max_size=ADD_SPACE;

    exceptions=(char*)malloc(sizeof(char)*max_size);
    if (exceptions == NULL){
	exit(EXIT_FAILURE);
    }
  

    fp = fopen("../../urls/plainURLs", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

    char before[] = "\n{ url:\"";
    char middle[] = "\",\n exception:\"";
    char end[] = "\"},";
    

    while ((read = getline(&line, &len, fp)) != -1) {
        //printf("%s", line);
	line[strlen(line)-1]="\0";
	UriParserStateA stateA;
	UriUriA uriA;
	stateA.uri = &uriA;
	
	char * addstr;
	int addsize=strlen(before)+strlen(middle)+strlen(end);
	
	int rc;
	

	if ((rc=uriParseUriA(&stateA, line)) != URI_SUCCESS) {
    	    //write url + rc to string
		char rcstr[6];
		sprintf(rcstr, "%i",stateA.errorCode);
		addsize+=strlen(line)+strlen(rcstr);
		addstr=(char*)calloc(addsize+1,sizeof(char));
		strcat(addstr,before);
		strcat(addstr,line);
		strcat(addstr,middle);
		strcat(addstr,rcstr);
		strcat(addstr,end);
		//printf("%s", addstr);
		if (strlen(exceptions)+addsize >= max_size){
		    max_size+=ADD_SPACE;
		    exceptions=realloc(exceptions, max_size*sizeof(char));
		}
		strcat(exceptions,addstr);

	} else{
		uriFreeUriMembersA(&uriA);
	}

    }

   FILE *nfp = fopen("CExceptions.txt", "w");
   if (nfp != NULL)
   {
       exceptions[strlen(exceptions)-1]="\0";
       fputs(exceptions, nfp);
       fclose(nfp);
       
   }
   free(line);
   exit(EXIT_SUCCESS);
}
