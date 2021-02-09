package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
* concrete implementation of a ComponentsBuilder for the livingstandard-url grammar producing a component representation 
  not specialized on a particular input format
  other component builders use this class to create their representations whenever possible to avoid code duplications

*/
public class UniversalURLComponentsBuilder extends UniversalComponentsBuilder {

	ArrayList<String> InternalComponentNames=new ArrayList<String>();
    HashMap<String, String> translation=new HashMap<>();
    String format = "basicComponents";
    URLComponentsUtil util=new URLComponentsUtil();
    HashMap<String, String> components=new HashMap<>();

    public UniversalURLComponentsBuilder(){
    	super();
    	this.InternalComponentNames.add("scheme");
    	this.InternalComponentNames.add("host");
    	this.InternalComponentNames.add("port");
    	this.InternalComponentNames.add("path");
    	this.InternalComponentNames.add("query");
    	this.InternalComponentNames.add("fragment");
    	
    	this.InternalComponentNames.add("input"); 

    	//components which need no further processing
        translation.put("port", "URLport");
        translation.put("fragment", "URLfragment");
        translation.put("input", "absoluteURLwithFragment"); 


    }
    

    /***
    * creates a dictonary of basic component names and component contents which can be accessed by
    * more specialized component builders
    */
    public void prepareComponents(){ //TODO
    	// populate this.components from raw dict and apply necessary transformations i.e. escaping, ipv6 formatting, 
    	// path formatting, ...

    	// copy easy contents to the correct place
    	for(String key:translation.keySet()){
    		components.put(key, dict.get(translation.get(key)));
    	}

    	// prepare scheme
    	String specialnf=dict.get("URLspecialSchemeNotFile");
    	String nonspecial=dict.get("URLnonSpecialScheme");
    	String file=dict.get("URLschemeFile");

    	for (String content: Arrays.asList(specialnf, nonspecial, file)){
    	  if(content !=null){
    	    components.put("scheme", content.toLowerCase());
    	  }
    	}

    	// prepare host
    	String ophost=dict.get("opaqueHost");
    	String d=dict.get("domain"); 
    	String ip=dict.get("ipAddress");
    	String reshost="";
    	String tmp="";
    	if(ophost !=null){
    	  reshost=ophost.toLowerCase();
    	}
    	else{
    	  if(d !=null){
    	    reshost=d.toLowerCase();
    	  }
    	  else{
    	    if (ip!=null){
    	      reshost=ip.toLowerCase();	
    	      // in case of ipv6 address format the parts
    	      if(ip.startsWith("[") && ip.endsWith("]")){
    	      	tmp=ip.subSequence(1, ip.length()-1).toString(); 
    	      	reshost="["+util.formatIPv6(tmp)+"]";
    	      }	
    	      
    	    }
    	  }
    	}
    	components.put("host", reshost);

    	// prepare path
    	String pa=dict.get("pathAbsoluteURL");
    	String panW=dict.get("pathAbsoluteNonWindowsFileURL");
    	String prsl=dict.get("pathRelativeSchemelessURL");
    	String pathcontent="";
    	if (panW != null){
    	  pa=null; //pathAbsoluteNonWindowsFileURL contains pathAbsoluteURL
    	}
    	for (String content: Arrays.asList(panW, pa, prsl)){
    	  if(content !=null){
    	    components.put("path", content);  //TODO formatting
    	  }
    	}

    	// prepare query
    	String query;
    	String qs=dict.get("URLSpecialquery");
    	String qns=dict.get("URLquery");

    	if(qs != null){
    	  query=qs; 
    	}
    	else{
    	  query=qns; 
    	}
    	components.put("query", query);
    	
    	return;
    }


    public String getComponentContents(String component){
    	//TODO escape?
    	return components.get(component); 
    }


    public String getSpecialComponentContent(String grammarrule){
    	return dict.get(grammarrule);
    }


}