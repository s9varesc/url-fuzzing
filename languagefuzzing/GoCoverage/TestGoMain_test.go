package main
 
import (
	"bufio"
	"log"
	"os"
	"net/url"
	"testing"
	"fmt"
	"io/ioutil"
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
		
		u, err :=url.Parse(eachline)
		if err != nil {
		    exceptions+="\n{ url:\""+eachline+"\",\n exception:\""+err+"\"},"
		}

	}
	
	err = ioutil.WriteFile("GoExceptions.txt", "["+exceptions[:1]+"]", 0644)
    	if err != nil {
            panic(err)
    	}
}
