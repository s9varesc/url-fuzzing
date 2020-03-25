#!/usr/bin/ruby

require_relative './testuri/uri'

IO.foreach("../exampleURLs/plainURLs"){|block| 
	begin 
	    uri = URI(block) 
	    puts uri
	rescue URI::InvalidURIError => e
	
	end
}
