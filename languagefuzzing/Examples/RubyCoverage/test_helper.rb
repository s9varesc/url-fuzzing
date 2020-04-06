#!/usr/bin/ruby

require 'simplecov'

SimpleCov.start do
  
  add_filter "TestRubyMain.rb"
  add_filter "testuri/uri.rb"
  add_filter "testuri/uri/common.rb"
  add_filter "testuri/uri/ftp.rb"
  add_filter "testuri/uri/generic.rb"
  add_filter "testuri/uri/http.rb"
  add_filter "testuri/uri/https.rb"
  add_filter "testuri/uri/ldap.rb"
 add_filter "testuri/uri/ldaps.rb"
  add_filter "testuri/uri/mailto.rb"
  track_files "testuri/uri/*_parser"
   
end

#require_relative '/usr/lib/ruby/2.5.0/uri'
require_relative './TestRubyMain'


