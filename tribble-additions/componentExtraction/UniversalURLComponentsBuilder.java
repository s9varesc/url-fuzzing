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
    int max_dict_entries=5;

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
        if(id>=max_dict_entries){
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


    /***
    *  method to access components outside the basic prepared components,
    *  utilizes an additional string to select the correct instantiation in case a rule is used more than once
    *  note1: at most max_dict_entries are present
    *  note2: using a high level rule to access components is preferable
    *
    * @param grammarrule the rule whose instantiation is requested
    * @param containedIn a string which contains the desired content (returned content is a substring of containedIn)
    * @return the instantiation of the specified rule
    */
    public String getSpecialComponentContent(String grammarrule, String containedIn){ 
        int candidates=0;
        ArrayList<String> candidatekeys=new ArrayList<String>();
        for(String key :dict.keySet()){
            if(key.startsWith(grammarrule)){
                candidates++;
                candidatekeys.add(key);
            }
        }
        // check if multiple entries
        if(candidates<=1){
            return dict.get(grammarrule);
        }
        if(containedIn != null && !(containedIn.equals(""))){
            //collect all candidate contents and compare them to containedIn 
            for(String ck : candidatekeys){
                String content=dict.get(ck);
                if(containedIn.contains(content)){
                    return content;
                }
            }

        } 
        //containedIn is empty or none of the candidates match
    	return null;
    }


    /***
    * prepare the components of the produced base URL
    * note: normalization and formatting are applied when combining base and relative URL
    ***/
    private void prepareBaseComponents(){ //TODO enforce lowercase?
        //get full base URL
        String base="";
        boolean specialbase=false;
        boolean filebase=false;
        String tmp=dict.get("specialBaseURL");
        if(tmp != null){
            base=tmp;
            specialbase=true;
        }
        else{
            tmp=dict.get("fileBaseURL");
            if(tmp != null){
                base=tmp;
                filebase=true;
            }
            else{
                base=dict.get("otherBaseURL");
            }
        }
        components.put("base", base);
        // collect base components
        String bscheme="";
        if(specialbase){
            bscheme=getSpecialComponentContent("URLspecialSchemeNotFile", base);
        }
        else{
            if(filebase){
                bscheme=getSpecialComponentContent("URLschemeFile", base);
            }
            else{
                bscheme=getSpecialComponentContent("URLnonSpecialScheme", base);
            }
        }
        components.put("base_scheme", bscheme);
        String bhost="";
        String ophost=getSpecialComponentContent("opaqueHost", base);
        String d=getSpecialComponentContent("domain", base); 
        String ip=getSpecialComponentContent("ipAddress", base);

        if(ip!=null){
            if(ip.startsWith("[") && ip.endsWith("]")){
                tmp=ip.subSequence(1, ip.length()-1).toString(); 
                bhost="["+util.formatIPv6(tmp)+"]";
            }   
        }
        else{
            if(ophost!=null){
                bhost=ophost;
            }
            else{
                bhost=d;
            }
        }
        components.put("base_host", bhost.toLowerCase());
        String bp=getSpecialComponentContent("URLport", base); //TODO port digits could be in ip
        if(bp!=null){
            components.put("base_port", bp);
        }
        String bpa="";
        String pa=getSpecialComponentContent("pathAbsoluteURL", base);
        String panW=getSpecialComponentContent("pathAbsoluteNonWindowsFileURL", base);
        String prsl=getSpecialComponentContent("pathRelativeSchemelessURL", base);
        //TODO check standard again about nonspecial URLs
        for(String p:Arrays.asList(pa, panW, prsl)){
            if(p!=null){
                bpa=p;
            }
        }
        String dl=getSpecialComponentContent("windowsDriveLetter", bpa);

        if(specialbase || filebase){
            components.put("base_path", util.normalizePath(bpa, dl));
        }
        else{
            components.put("base_path", bpa);
        }
        
        String bq=null;
        if(specialbase || filebase){
            bq=getSpecialComponentContent("URLSpecialquery", base);
        }
        else{
            bq=getSpecialComponentContent("URLquery", base);
        }
        if(bq != null){
            components.put("base_query", bq);
        }
        
        String bf=getSpecialComponentContent("URLfragment", base);
        if(bf!=null){
            components.put("base_fragment", bf);
        }

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
        components.put("port", dict.get("URLport")); 
        components.put("fragment", dict.get("URLfragment"));
        components.put("input", dict.get("absoluteURLwithFragment")); 


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