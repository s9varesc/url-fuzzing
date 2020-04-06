
#include "Poco/URI.h"	
#include <iostream>
#include <fstream>
#include <string>
#include <vector>





 

bool getFileContent(std::string fileName, std::vector<std::string> & vecOfStrs)
{
 
	// Open the File
	std::ifstream in(fileName.c_str());
 
	// Check if object is valid
	if(!in)
	{
		std::cerr << "Cannot open the File : "<<fileName<<std::endl;
		return false;
	}
 
	std::string str;
	// Read the next line from File untill it reaches the end.
	while (std::getline(in, str))
	{
		// Line contains string of length > 0 then save it in vector
		if(str.size() > 0)
			vecOfStrs.push_back(str);
	}
	//Close The File
	in.close();
	return true;
}
 
 
int main()
{
	
	std::vector<std::string> vecOfStr;
 
	// Get the contents of file in a vector
	bool result = getFileContent("../urls/plainURLs", vecOfStr);
 
	if(result)
	{
		// Print the vector contents
		for(std::string & line : vecOfStr){
			//const char *c = line.c_str();
			//parse urls
			Poco::URI uri1(line);
			
			//std::cout<<uri1<<std::endl;
		}
	}
}
