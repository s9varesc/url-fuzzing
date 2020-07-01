package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChromiumURLComponentsBuilder extends ComponentsBuilder {

	ArrayList<String> InternalComponentNames=new ArrayList<String>();
    HashMap<String, String> translation=new HashMap<>();
    String format = "chromium";


    public ChromiumURLComponentsBuilder(){
    	this.InternalComponentNames.add("input");
    	this.InternalComponentNames.add("scheme");
    	this.InternalComponentNames.add("username");
    	this.InternalComponentNames.add("password");
    	this.InternalComponentNames.add("host");
    	this.InternalComponentNames.add("port");
    	this.InternalComponentNames.add("path");
    	this.InternalComponentNames.add("query");
    	this.InternalComponentNames.add("ref");

    	/*this.InternalComponentNames.add("inner_scheme"); //TODO only used for filesystem URLs
    	this.InternalComponentNames.add("inner_username");
    	this.InternalComponentNames.add("inner_password");
    	this.InternalComponentNames.add("inner_host");
    	this.InternalComponentNames.add("inner_port");
    	this.InternalComponentNames.add("inner_path"); */

    	translation.put("port", "URLport"); //TODO cant be used whith filesystem URL
    	translation.put("query", "URLquery");
    	translation.put("ref", "URLfragment");
    }
    public String getComponentFormat(){
      return format;
    }

    public String buildRepresentation(){//TODO needs to be changed when using filesystem URL
    	//build actual representation
    	//{input, scheme, username, password, host, port, path, query, ref}
    	//{"http://user:pass@foo:21/bar;par?b#c", "http", "user", "pass",    "foo",       21, "/bar;par","b",          "c"},
    	//{"http:foo.com",                        "http", NULL,  NULL,      "foo.com",    -1, NULL,      NULL,        NULL}

    	Map<String, String> components=buildMapping();
      	String result="{";
      	result+= "\""+components.get("input")+"\",";
        result+= "\""+components.get("scheme")+"\",";
      	result+= "\""+components.get("username")+"\",";
      	result+= "\""+components.get("password")+"\",";
      	result+= "\""+components.get("host")+"\",";
      	result+= components.get("port")+",";
      	result+= "\""+components.get("path")+"\",";
      	result+= "\""+components.get("query")+"\",";
      	result+= "\""+components.get("ref")+"\"";
      	result +="}";

      	String res=result.replaceAll("\"null\"", "NULL");
      	result=res;
      	return result;

    }


    private Map<String, String> buildMapping(){
    	//build mapping between grammar names and component names
    	Map<String, String> components=new HashMap<>();
      	for(String name:translation.keySet()){
        	String content=dict.get(translation.get(name));
          if (name=="port"){
            if (content != null || content != ""){
              components.put(name, content);
            }
            else{
              components.put(name, "-1");
            }
          	
          }
          else{
            components.put(name, content);
          }
      	}

      	//build input = whole URL
      	String url=dict.get("relativeURLwithFragment");
      	if(url !=null){
        	components.put("input", url);
      	}
      	else{
        	components.put("input", dict.get("absoluteURLwithFragment"));
      	}
      	String input=components.get("input");
		
		//build path
      	String pa=dict.get("pathAbsoluteURL");
      	String panW=dict.get("pathAbsoluteNonWindowsFileURL");
      	String prsl=dict.get("pathRelativeSchemelessURL");

      	for (String content: Arrays.asList(panW, pa, prsl)){
        	if(content !=null){
          		components.put("path", content);
        	}
      	}

		//build host
      	String ophost=dict.get("opaqueHost");
      	String d=dict.get("domain");
      	if(ophost !=null){
        	components.put("host", ophost.toLowerCase());
      	}
      	else{
        	if(d !=null){
          		components.put("host", d.toLowerCase());
        	}
      	}

      	//build scheme
      	String specialnf=dict.get("URLspecialSchemeNotFile");
      	String nonspecial=dict.get("URLnonSpecialScheme");
      	String file=dict.get("URLschemeFile");

      	for (String content: Arrays.asList(specialnf, nonspecial, file)){
        	if(content !=null){
        	  	components.put("scheme", content.toLowerCase());
        	}
      	}

      	//build username and password
      	String userinfo = dict.get("userinfo");
        int colon=-1;
        if (userinfo != null){
          colon=userinfo.indexOf(":");
        }

      	if(colon==-1){
      		components.put("username", userinfo);
      	}
      	else {
      		components.put("username", userinfo.substring(0, colon));
      		components.put("password", userinfo.substring(colon + 1));
      	}
      	


      	return components;
    }

}