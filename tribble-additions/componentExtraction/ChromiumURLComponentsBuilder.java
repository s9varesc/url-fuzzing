package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/***
* concrete implementation of a ComponentsBuilder for the livingstandard-url grammar and the Chromium URL format
*/
public class ChromiumURLComponentsBuilder extends ComponentsBuilder {

	ArrayList<String> InternalComponentNames=new ArrayList<String>();
    HashMap<String, String> translation=new HashMap<>();
    String format = "chromium";
    URLComponentsUtil util=new URLComponentsUtil();


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

      //components which need no further processing
    	translation.put("port", "URLport"); 
    	translation.put("query", "URLquery");
    	translation.put("ref", "URLfragment");
    }

    /***
    * @return returns the name of the specified format that this ComponentBuilder will produce 
    */
    public String getComponentFormat(){
      return format;
    }

    /***
    *
    * @return components and their contents in the Chromium component format
    */
    public String buildRepresentation(){
    	//build actual representation
    	//{input, scheme, username, password, host, port, path, query, ref}
    	//{"http://user:pass@foo:21/bar;par?b#c", "http", "user", "pass",    "foo",       21, "/bar;par","b",          "c"},
    	//{"http:foo.com",                        "http", "",  "",      "foo.com",    -1, "",      "",        ""}

    	Map<String, String> components=buildMapping(); 
      	String result="{";
      	result+= "\""+fixEscaping(""+components.get("input"))+"\",";
        result+= "\""+fixEscaping(""+components.get("scheme"))+"\",";
      	result+= "\""+fixEscaping(""+components.get("username"))+"\",";
      	result+= "\""+fixEscaping(""+components.get("password"))+"\",";
        String tmp=components.get("host");
        if (tmp != NULL){
          if (tmp.startsWith("[") && tmp.endsWith("]")){ //ipv6: remove leading zeros and convert ipv4 pieces
            tmp=tmp.subSequence(1, tmp.length()-1).toString(); 
            tmp="["+util.formatIPv6(tmp)+"]";
          }
        }
      	result+= "\""+fixEscaping(""+tmp)+"\",";
      	result+= fixEscaping(""+components.get("port"))+",";
      	result+= "\""+fixEscaping(""+components.get("path"))+"\",";
      	result+= "\""+fixEscaping(""+components.get("query"))+"\",";
      	result+= "\""+fixEscaping(""+components.get("ref"))+"\"";
      	result +="}";

      	String res=result.replaceAll("\"null\"", "\"\"");
      	result=res;
      	return result;

    }

    /***
    * 
    * @return the given string with escaped quotationmarks and backsalshes
    */
    private String fixEscaping(String original){
      String res=original.replaceAll("\\\\", "\\\\\\\\"); 
      return res.replaceAll("\"", "\\\\\"");
    }

    /***
    * uses the dictionary created by DictExtractor to create a mapping of component name (in chr formatting) and component content
    * @return a mapping of component name to component content
    */
    private Map<String, String> buildMapping(){
    	//build mapping between grammar names and component names
    	Map<String, String> components=new HashMap<>();
      	for(String name:translation.keySet()){
        	String content=dict.get(translation.get(name));
          if (name=="port"){
            if (content != null && !content.isEmpty()){
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