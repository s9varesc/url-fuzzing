package main
 
import (
	"bufio"
	"log"
	"os"
	"net/url"
	"testing"
)
func TestURLs(t *testing.T) {
	readFile, err := os.Open("../exampleURLs/plainURLs")
 
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
 
	for _, eachline := range fileTextLines {
		
		url.Parse(eachline)

	}
}
