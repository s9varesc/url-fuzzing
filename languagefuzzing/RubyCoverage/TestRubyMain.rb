#!/usr/bin/ruby

require_relative './testuri/uri'
exceptions=""
IO.foreach("../urls/plainURLs"){|block| 
	begin 
	    uri = URI(block) 
	    
	rescue URI::InvalidURIError => e
	   exceptions+="\n{ url:\""+block+"\",\n exception:\""+e.message+"\"},";
	end
}
    File.open('RubyExceptions.txt', 'w') do |f|  
      f.write("["+exceptions[0..-2]+"]")  
    end  
