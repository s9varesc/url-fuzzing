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

    HashMap<String, String> basecomponents=new HashMap<>();
    HashMap<String, String> relcomponents=new HashMap<>();

    public UniversalURLComponentsBuilder(){
    	super();
    	this.InternalComponentNames.add("scheme");
    	this.InternalComponentNames.add("host");
    	this.InternalComponentNames.add("port");
    	this.InternalComponentNames.add("path");
    	this.InternalComponentNames.add("query");
    	this.InternalComponentNames.add("fragment");
    	
    	this.InternalComponentNames.add("input"); 

    	


    }

    @Override
    public void addComponent(String name, String content){
        // on the URL grammar specialized: handling duplicates
        addAndKeepOldEntry(name, 0, content);
    }

    private void addAndKeepOldEntry(String name,int id, String content){
        if(id>=5){
            // relevant rules are not present that often, and we are not interested 
            // in keeping track of single digits or characters 
            return; 
        }
        //check for other entries for the same rule
        String suffix=(id!=0 ? String.valueOf(id) : "");
        String oldcontent=dict.get(name+suffix);
        if(oldcontent!=null ){
            if(! oldcontent.equals(content)){
                //save old entry with smaller id
                dict.put(name+String.valueOf(id), oldcontent);
                //remove old entry but keep the original name as key: content of rule and rule0 should be identical
                if(id!=0){
                    dict.remove(name+suffix);
                }
                // try to place the new entry with a higher id
                id++;
                addAndKeepOldEntry(name, id, content);
                return;
            }
            else{
                //no need to save the same content more than once
                return;
            }
            
        }
        else{
            dict.put(name+suffix, content);
        }
        return;
    }
    

    /***
    * creates a dictonary of basic component names and component contents which can be accessed by
    * more specialized component builders
    */
    public void prepareComponents(){ 
    	//determine which method to use
        if(dict.get("baseAndRelativeURL")!=null){
            // there are base and/or relative URLs present
            prepareBaseComponents();
            prepareRelativeComponents();
            combineBaseAndRelativeComponents();
        }
        else{
            // there is only a absolute URL present
            prepareBasicComponents();
        }
        System.out.println(components);
    	return;
    }


    public String getSpecialComponentContent(String grammarrule, String containedIn){ //TODO
        int candidates=0;
        for(String key :dict.keySet()){
            if(key.startsWith(grammarrule)){
                candidates++;
            }
        }
        // check if multiple entries
        if(candidates<=1){
            return dict.get(grammarrule);
        }
        if(containedIn != null && !(containedIn.equals(""))){
            //collect all candidate contents and compare them to containedIn 
        } 
        //containedIn is empty or none of the candidates match
    	return null;
    }


    /***
    * prepare the components of the produced base URL
    * note: normalization and formatting are applied when combining base and relative URL
    ***/
    private void prepareBaseComponents(){
        return;
    }

    /***
    * dual to prepareBaseComponents; prepare the components of the relative URL
    ***/
    private void prepareRelativeComponents(){
        return;
    }

    /***
    * utilizes the prepared components of base and relative URL to create the final component representation
    ***/
    private void combineBaseAndRelativeComponents(){
        components.put("input", dict.get("baseAndRelativeURL"));
        return;
    }


    /***
    * prepares the components of an absolute URL without base
    ***/
    private void prepareBasicComponents(){
        // populate this.components from raw dict and apply necessary transformations i.e. ipv6 formatting, 
        // path formatting, ...

        //components which need no further processing
        components.put("port", "URLport"); 
        components.put("fragment", "URLfragment");
        components.put("input", "absoluteURLwithFragment"); 


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
              
            }
          }
        }
           if(dict.get("ipv6address")!=null){
               String inp=reshost;
               // in case of ipv6 address format the parts
               if(inp.startsWith("[") && inp.endsWith("]")){
                   tmp=inp.subSequence(1, inp.length()-1).toString(); 
                   reshost="["+util.formatIPv6(tmp)+"]";
               }   
               
           }
        components.put("host", reshost);

        // prepare path
        String pa=dict.get("pathAbsoluteURL");
        String panW=dict.get("pathAbsoluteNonWindowsFileURL");
        String prsl=dict.get("pathRelativeSchemelessURL");
           // include all leading slashes in non special urls without host
           String rel=dict.get("schemeRelativeURL"); 
           String ohp=dict.get("opaqueHostAndPort");
           if(nonspecial!=null){
               if(rel!=null){
                   if(ohp !=null){
                       pa=(! ohp.equals("") ? pa : rel);
                   }
                   
               }
       
           }
        String pathcontent="";
           String driveletter=dict.get("windowsDriveLetter");
        /*if (panW != null){
          pa=null; //pathAbsoluteNonWindowsFileURL contains pathAbsoluteURL
        }*/
        for (String content: Arrays.asList(panW, pa, prsl)){
          if(content !=null){ // only normalize path if a special scheme is used
            components.put("path", (nonspecial !=null ? content : util.normalizePath(content, driveletter)));  
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
        
    }

}