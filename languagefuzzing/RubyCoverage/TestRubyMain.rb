#!/usr/bin/ruby

require_relative './testuri/uri'

IO.foreach("../urls/plainURLs"){|block| 
	begin 
	    uri = URI(block) 
	    
	rescue URI::InvalidURIError => e
	
	end
}
