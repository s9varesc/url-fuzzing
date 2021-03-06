
#include "Poco/URI.h"	
#include <iostream>
#include <exception>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>





 

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
	std::string exceptions;
 
	// Get the contents of file in a vector
	bool result = getFileContent("../urls/plainURLs", vecOfStr);
 
	if(result)
	{
		for(std::string & line : vecOfStr){
			try {
			    line=line.substr(0,line.length());
			} catch(const std::exception& e){
				
			}	
			//split line into base and relative	
			std::stringstream inputcopy;
			inputcopy<<line;
			std::string segment;
			std::vector<std::string> baseandrel;
			while(std::getline(inputcopy, segment, '<')){
			   baseandrel.push_back(segment);
			}	
			//parse urls
			try
			{
				if(baseandrel.size()>1){
					Poco::URI base(baseandrel[0]);
					Poco::URI rel(base, baseandrel[1]);
				}
				else{
					Poco::URI uri1(line);
				}
			    
			}
			catch(const std::exception& e)
			{
			    exceptions.append("\n{\"url\":\"");
			    exceptions.append(line);
    			    exceptions.append("\", \"exception\":\"");
			    exceptions.append(e.what());
			    exceptions.append("\"}");
			}
			//std::cout<<uri1<<std::endl;
		}
	}
	std::string exceptionsfinal;
	exceptionsfinal.append(exceptions.substr(0,exceptions.length()));
        std::ofstream os("CppExceptions.txt");  
	if (!os) { 
	    std::cerr<<"Error writing to ..."<<std::endl; 
	} else {  
  	    os << exceptionsfinal;  
	}
}  
	

