package main
 
import (
	"bufio"
	"log"
	"os"
	"net/url"
	"testing"
	"fmt"
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
		
		_, err :=url.Parse(eachline)
		if err != nil {
		    exceptions+="\n{\"url\":\""+eachline+"\", \"exception\":\""+err.Error()+"\"}"
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
