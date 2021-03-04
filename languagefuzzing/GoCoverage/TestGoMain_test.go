package main
 
import (
	"bufio"
	"log"
	"os"
	"net/url"
	"testing"
	"fmt"
	"strings"
	//"io/ioutil"
)
func TestURLs(t *testing.T) {
	
	//file reading from https://webdamn.com/read-file-line-by-line-using-golang/
	readFile, err := os.Open("../urls/plainURLs")
 	
	if err != nil {
		log.Fatalf("failed to open file: %s", err)
	}
 
	fileScanner := bufio.NewScanner(readFile)
	fileScanner.Split(bufio.ScanLines)
	var fileTextLines []string
 
	for fileScanner.Scan() {
		fileTextLines = append(fileTextLines, fileScanner.Text())
	}
 
	readFile.Close()
 	exceptions:=""
	for _, eachline := range fileTextLines {
		//split eachline into base and relative
		baseandrel:= strings.Split(eachline, "<")

		if len(baseandrel)>1 {
			base, err1:=url.Parse(baseandrel[0])
			rel, err2:=url.Parse(baseandrel[1])
			res:=base.ResolveReference(rel)

			if err1 != nil || err2 != nil {
			    exceptions+="\n{\"url\":\""+eachline+"\", \"exception\":\""+err.Error()+"\"}"
			}
		} else {
			_, err1 :=url.Parse(eachline)

			if err1 != nil {
			    exceptions+="\n{\"url\":\""+eachline+"\", \"exception\":\""+err.Error()+"\"}"
			}
		}
		
		

	}
	
	file, err := os.Create("GoExceptions.txt")
    	if err != nil {
	    fmt.Println(err)
            return
    	}
    	
    	file.WriteString(exceptions)
	file.Sync()
	file.Close()

}
