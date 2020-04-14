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

   fp = fopen("../../urls/plainURLs", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

   while ((read = getline(&line, &len, fp)) != -1) {
        //printf("%s", line);
	UriParserStateA stateA;
	UriUriA uriA;
	stateA.uri = &uriA;
	
	int rc;
	if ((rc=uriParseUriA(&stateA, line)) != URI_SUCCESS) {
    	    //write url + rc to string
	} else{
		uriFreeUriMembersA(&uriA);
	}

    }

   FILE *nfp = fopen("CExceptions.txt", "w");
   if (nfp != NULL)
   {
       fputs("write test", nfp);
       fclose(nfp);
       
   }
   free(line);
   exit(EXIT_SUCCESS);
}
