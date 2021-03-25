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
    public String getSpecialComponentContent(String grammarrule, String containedIn){  //TODO
        ArrayList<String> candidatekeys=getAllCandidates(grammarrule);
        if(containedIn != null && !(containedIn.equals(""))){
            //collect all candidate contents and compare them to containedIn 
            String longestmatch="";
            for(String ck : candidatekeys){
                String content=dict.get(ck);
                if(containedIn.contains(content)){ 
                    if(content.length()>=longestmatch.length()){ //make sure to return the best match
                        longestmatch=content; 
                    }
                }
            }
            return (longestmatch.equals("")? null : longestmatch);

        } 
        //containedIn is empty or none of the candidates match
    	return null;
    }

    private ArrayList<String> getAllCandidates(String grammarrule){
        ArrayList<String> candidatekeys=new ArrayList<String>();
        for(String key :dict.keySet()){
            try{
                if(key.equals(grammarrule) || (key.startsWith(grammarrule) && Character.isDigit(key.charAt(grammarrule.length()))) ){
                    candidatekeys.add(key); 
                }
            }
            catch (Exception e){
                // char at might throw string index out of bounds
                continue;
            }
            
        }
        return candidatekeys;
    }


    /***
    * prepare the components of the produced base URL
    * note: normalization and formatting are applied when combining base and relative URL
    ***/
    private void prepareBaseComponents(){ 
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
        components.put("base_scheme", bscheme.toLowerCase());
        String bhost=prepareHost(base);
        if(bhost != null){
            components.put("base_host", util.encodeHost(bhost.toLowerCase()));
        }
        
        String bp=getSpecialComponentContent("URLport", base); 
        if(bp!=null){
            components.put("base_port", bp);
        }
        String bpa=preparePath(base);
        String dl=getSpecialComponentContent("windowsDriveLetter", bpa);

        components.put("base_path", util.escapeUnicodeChars(bpa, util.PATH_PERCENT_ENCODE));
        components.put("base_driveletter", dl); //needed for normalization later
        
        String bq=null;
        boolean special=false;
        if(specialbase || filebase){
            bq=getSpecialComponentContent("URLSpecialquery", base);
            bq=util.escapeUnicodeChars(bq, util.SPECIAL_QUERY_PERCENT_ENCODE);
        }
        else{
            bq=getSpecialComponentContent("URLquery", base);
            bq=util.escapeUnicodeChars(bq, util.QUERY_PERCENT_ENCODE);
        }
        if(bq != null){
            components.put("base_query", bq);
        }
        
        String bf=getSpecialComponentContent("URLfragment", base);
        if(bf!=null){
            components.put("base_fragment", util.escapeUnicodeChars(bf, util.FRAGMENT_PERCENT_ENCODE));
        }

        return;
    }

    /***
    * dual to prepareBaseComponents; prepare the components of the relative URL
    ***/
    private void prepareRelativeComponents(){
        String bar=dict.get("baseAndRelativeURL");
        String rel;
        boolean scheme=false;
        rel=dict.get("absoluteURLwithFragment"); //base is special/file/other/absolute
        if(rel!=null){
            scheme=true;
        }
        else{
            String sprel=dict.get("specialRelativeURL");
            String firel=dict.get("fileRelativeURL");
            String otrel=dict.get("otherRelativeURL");
            for(String r: Arrays.asList(sprel, firel, otrel)){
                if(r!=null){
                    rel=r;
                }
            }
        }
        components.put("relative", rel);
        // prepare scheme
        if(scheme){
            String rscheme=null;
            String sp=getSpecialComponentContent("URLspecialSchemeNotFile", rel);
            String fi=getSpecialComponentContent("URLschemeFile", rel);
            String ot=getSpecialComponentContent("URLnonSpecialScheme", rel);
            
            for(String s: Arrays.asList(sp, fi, ot)){
                if(s != null && rel.startsWith(s+":")){
                    rscheme=s;
                }
            }
            if(rscheme!= null){
                components.put("relative_scheme", rscheme.toLowerCase());
                //fill dict entries so base components are ignored
                components.put("relative_path", ""); 
                components.put("relative_query", "");
                components.put("relative_fragment", "");
            }
            
        }
        // prepare host and port
        String rhost=prepareHost(rel);
        if(rhost != null){
            components.put("relative_host", util.encodeHost(rhost.toLowerCase())); //TODO use for base host and basic host too
            String rp=getSpecialComponentContent("URLport", rel); 
            if(rp!=null){
                if(rel.contains(":"+rp)){ //avoid adding a port if the digit appears in the ip address
                    components.put("relative_port", rp);
                }
                
            }
        }
        //prepare path
        String rpath=preparePath(rel); 
        String dl=getSpecialComponentContent("windowsDriveLetter", rpath); 
        components.put("relative_driveletter", dl); //needed for normalization later
        if(rpath != null){ 
            components.put("relative_path", util.escapeUnicodeChars( rpath, util.PATH_PERCENT_ENCODE));
           
        }

        // prepare query
        String rq=prepareQuery(rel);
        if(rq != null){
            components.put("relative_query", rq);
        }
        // prepare fragment
        String rf=getSpecialComponentContent("URLfragment", rel);
        if(rel.contains("#"+rf)){
            components.put("relative_fragment", util.escapeUnicodeChars(rf, util.FRAGMENT_PERCENT_ENCODE));
        }


        return;
    }

    private boolean isSpecialScheme(String scheme){
        String sp=getSpecialComponentContent("URLspecialSchemeNotFile", scheme);
        String f=getSpecialComponentContent("URLschemeFile", scheme);
        String o=getSpecialComponentContent("URLnonSpecialScheme", scheme);
        if(o != null){
            return false;
        }
        if(sp != null || f != null){
            return true;
        }
        return false;
    }

    private String combinePaths(String base, String relative){
        if(base != null){
            int index=base.lastIndexOf("/");
            base=base.substring(0, (index >=0 ? index: 0));
            base=(relative != null ? (base + "/"+relative ): base) ;
        }
        else{
            if(relative!= null && ! relative.startsWith("/")){
                base="/"+relative;
            }
            else{
                base=relative;
            }
            
        }
        return base;
    }

    /***
    * utilizes the prepared components of base and relative URL to create the final component representation
    ***/
    private void combineBaseAndRelativeComponents(){
        // make accessing components for relative and base similar to absolute URLs
        components.put("input", components.get("relative"));
        if(components.get("relative_scheme")!=null){
            components.put("scheme", components.get("relative_scheme"));
        }
        else{
            components.put("scheme", components.get("base_scheme"));
        }
        String relative=components.get("relative").toLowerCase();
        if(! relative.startsWith(components.get("scheme"))){
            //relative does not contain scheme
            String host=components.get("relative_host");
            if(host != null){
                components.put("host", host);
                components.put("port", components.get("relative_port"));
            }
            else{
                if(! relative.startsWith("//") ){
                    //only use base host if relative starts with path (i.e. max one /)
                    components.put("host", components.get("base_host"));
                    components.put("port", components.get("base_port"));
                }

            }
            String path=null;
            if(! relative.startsWith("/")){
                //replace last base segment
                path=combinePaths(components.get("base_path"), components.get("relative_path"));
                String dl="";
                if(isSpecialScheme(components.get("scheme"))){
                     
                    if("file".equals(components.get("scheme"))){
                        dl=components.get("base_driveletter");
                    }
                    if(path != null && ! path.startsWith("/")){
                        path="/"+path;   //paths in special urls start with / in components
                    }
                     //driveletter can only be at the beginning and after a /
                    
                } 
                path=util.normalizePath(path,dl ); //always normalize after combining paths
            }
            else{
                // use relative path
                path=components.get("relative_path");
                String dl="";
                if(isSpecialScheme(components.get("scheme"))){
                     
                    if(components.get("scheme").equals("file")){
                        dl=components.get("relative_driveletter");
                    }
                    if(path != null && ! path.startsWith("/")){
                        path="/"+path;   //paths in special urls start with / in components
                    }
                    
                } 
                path=util.normalizePath(path, dl); //always normalize for relative paths

            }
            components.put("path", path);
            String rpath=components.get("relative_path");
            if(relative.startsWith("?")|| rpath!=null || relative.startsWith("/")){ // relative contains path or host or  query (and fragment)
                components.put("query", components.get("relative_query"));
                components.put("fragment", components.get("relative_fragment"));
            }
            else{ //no path, no query in relative
                if(relative.startsWith("#")){ //relative contains only fragment
                    components.put("query", components.get("base_query"));
                    components.put("fragment", components.get("relative_fragment"));
                }
                else{ // relative contains neither path nor query nor fragment

                    components.put("query", components.get("base_query"));
                    components.put("fragment", components.get("base_fragment"));
                    
                }
                
            }

        }
        else{
            //relative does contain a scheme: use all of relative
            components.put("host", components.get("relative_host"));
            components.put("port", components.get("relative_port"));
            String path=components.get("relative_path");
            String scheme=components.get("scheme");
            if(isSpecialScheme(scheme)){
                if(path != null && ! path.startsWith("/")){
                    path="/"+path;   //paths in special urls start with / in components
                }
                String dl=components.get("relative_driveletter");
                if(!scheme.equals("file")){ //only preserve drive letters in file urls
                    dl="";
                }
                path=util.normalizePath(path, dl);
                
            }
            components.put("path", path); //TODO lower case?
            components.put("query", components.get("relative_query"));
            components.put("fragment", components.get("relative_fragment"));
        }
        return;
    }

    private String prepareQuery(String parent){
        String sq=getSpecialComponentContent("URLSpecialquery", parent);
        String nsq=getSpecialComponentContent("URLquery", parent);
        
        if(parent.contains("?"+sq)){
            return util.escapeUnicodeChars( sq, util.SPECIAL_QUERY_PERCENT_ENCODE);
        }
        if(parent.contains("?"+nsq)){
            return util.escapeUnicodeChars(nsq, util.QUERY_PERCENT_ENCODE);
        }
        return null;
    }

    private String prepareHost(String parent){ 
        String host=null;
        String ophost=getSpecialComponentContent("opaqueHost", parent);
        String d=getSpecialComponentContent("domain", parent); 
        String ipv4=getSpecialComponentContent("ipv4address", parent);
        String ip=getSpecialComponentContent("ipv6address", parent);
        String originalip="["+ip+"]";
        if(ip != null){
            ip="["+util.formatIPv6(ip)+"]";
        }

        

        for(String h: Arrays.asList(ophost, d, ipv4, originalip)){
            if(h != null){
                if(parent.contains("//"+h)){
                    host=h;
                    if(h.equals(originalip)){
                        host=ip;
                    }
                }
            }
        }
        // make sure this is the full host and not a substring of it
        for(String ending: Arrays.asList("/", ":", "?", "#")){
            if(parent.contains("//"+host+ending) || parent.contains("//"+originalip+ending)){
                return host;
            }
        }
        if(parent.endsWith("//"+host) || parent.endsWith("//"+originalip)){
            return host;
        }

        return null;
    }

    private String preparePath(String parent){ //TODO check if these are enough
        String path=null;
        String pa=getSpecialComponentContent("pathAbsoluteURL", parent);
        String panW=getSpecialComponentContent("pathAbsoluteNonWindowsFileURL", parent);
        String prsl=getSpecialComponentContent("pathRelativeSchemelessURL", parent);
        
        for(String p:Arrays.asList(pa, panW, prsl)){
            if(p!=null){
                path=p;
            }
        }

        return path;

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
        String reshost=prepareHost(components.get("input"));
        if(reshost != null){
            components.put("host", util.encodeHost( reshost.toLowerCase()));
        }
        

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
        String driveletter=(file != null ? dict.get("windowsDriveLetter"):"");
        /*if (panW != null){
          pa=null; //pathAbsoluteNonWindowsFileURL contains pathAbsoluteURL
        }*/
        for (String content: Arrays.asList(panW, pa, prsl)){
          if(content !=null){ // only normalize path if a special scheme is used
            String p=(nonspecial !=null ? content : util.normalizePath(content, driveletter));
            components.put("path",util.escapeUnicodeChars(p, util.PATH_PERCENT_ENCODE) );  
          }
        }

        // prepare query
        String query;
        String qs=dict.get("URLSpecialquery");
        String qns=dict.get("URLquery");

        if(qs != null){
          query=util.escapeUnicodeChars(qs, util.SPECIAL_QUERY_PERCENT_ENCODE); 
        }
        else{
          query=util.escapeUnicodeChars(qns, util.QUERY_PERCENT_ENCODE); 
        }
        components.put("query", query);

        String f=util.escapeUnicodeChars(components.get("fragment"), util.FRAGMENT_PERCENT_ENCODE);
        components.put("fragment", f);
        
    }

    public String getComponentContents(String component){
        if(components.containsKey(component)){
            return components.get(component);
        }
        return null;

    }

    public String getSpecialComponentContent(String grammarrule){
        if(dict.containsKey(grammarrule)){
            return dict.get(grammarrule);
        }
        return null;
    }

}