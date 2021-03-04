#!/usr/bin/ruby

require_relative './testuri/uri'
exceptions=""
IO.foreach("../urls/plainURLs"){|block| 
	begin 
		baseandrel=block[0..-2].split("<");
		if baseandrel.length()>1
			uri=URI.join(baseandrel[0], baseandrel[1]);
		else
			uri = URI(block[0..-2]);
		end 
	    
	    
	rescue Exception => e
	   #URI::InvalidURIError
	   exceptions+="\n{\"url\":\""+block[0..-2]+"\", \"exception\":\""+e.message+"\"}";
	end
}
    File.open('RubyExceptions.txt', 'w') do |f|  
      f.write(exceptions[0..-1])  
    end  
