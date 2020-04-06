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

   fp = fopen("../exampleURLs/plainURLs", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

   while ((read = getline(&line, &len, fp)) != -1) {
        printf("%s", line);
	UriParserStateA stateA;
	UriUriA uriA;
	stateA.uri = &uriA;
	
	
	if (uriParseUriA(&stateA, line) != URI_SUCCESS) {
    	    /* Failure (no need to call uriFreeUriMembersA) */
	} else{
		uriFreeUriMembersA(&uriA);
	}

    }

   free(line);
   exit(EXIT_SUCCESS);
}
