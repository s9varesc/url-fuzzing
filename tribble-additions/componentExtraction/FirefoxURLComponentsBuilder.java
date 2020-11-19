package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
* concrete implementation of a ComponentsBuilder for the livingstandard-url grammar and the Firefox URL format
*/
public class FirefoxURLComponentsBuilder extends ComponentsBuilder {

    ArrayList<String> InternalComponentNames=new ArrayList<String>();
    HashMap<String, String> translation=new HashMap<>();
    String format = "firefox";
    URLComponentsUtil util=new URLComponentsUtil();

    public FirefoxURLComponentsBuilder(){
        this.InternalComponentNames.add("spec");
        this.InternalComponentNames.add("specIgnoringRef");
        this.InternalComponentNames.add("scheme");
//this.InternalComponentNames.add("hostPort");
        this.InternalComponentNames.add("host");
        this.InternalComponentNames.add("port");
        this.InternalComponentNames.add("userPass");
        this.InternalComponentNames.add("username");
        this.InternalComponentNames.add("password");
        this.InternalComponentNames.add("pathQueryRef");
        this.InternalComponentNames.add("prePath");
        this.InternalComponentNames.add("hasRef");
        this.InternalComponentNames.add("ref");


//components which need no further processing
        translation.put("port", "URLport");
        translation.put("userPass","userinfo" );
        translation.put("ref", "URLfragment");
        translation.put("spec", "absoluteURLwithFragment");

    }

    /***
    * @return returns the name of the specified format that this ComponentBuilder will produce 
    */
    public String getComponentFormat(){
        return format;
    }

    @Override
    /***
    *
    * @return components and their contents in the Firefox component format
    */
    public String buildRepresentation() { //TODO escape quotation marks in content
        Map<String, String> components=buildMapping();
        String result="{";
        for(String key:components.keySet()){
            if(key != "hasRef") {
                String content = components.get(key);
                String tmp=content.replaceAll("\\\\", "\\\\\\\\");
                tmp=tmp.replaceAll("\\\"", "\\\\\\\"");
                if (key=="host"){
                    if (tmp.startsWith("[") && tmp.endsWith("]")){ //ipv6: need to remove leading zeros and convert ipv4 pieces
                        tmp=tmp.subSequence(1, tmp.length()-1).toString(); 
                        tmp=util.formatIPv6(tmp);
                    }
                }
                result += key + ":\"" + tmp + "\",\n";
            }
            else{
                result += key + ":" + components.get(key) + ",\n";
            }
        }
        result=result.subSequence(0, result.length()-2).toString();
        result+="}\n";
        return result;
    }


    /***
    * uses the dictionary created by DictExtractor to create a mapping of component name (in ff formatting) and component content
    * @return a mapping of component name to component content
    */
    private Map<String, String> buildMapping(){
        Map<String, String> components=new HashMap<>();
        for(String name:translation.keySet()){
            String content=dict.get(translation.get(name));
            if (content!=null){
                components.put(name, content);
            }
        }
        //build spec
        /*String url=dict.get("relativeURLwithFragment");
        String baseurl=dict.get("absoluteURL");
        if(url !=null && baseurl != null ){
        components.put("spec", baseurl+url);
        }
        else{
        components.put("spec", dict.get("absoluteURLwithFragment"));
        }*/
        String spec=components.get("spec");

        //build host
        String ophost=dict.get("opaqueHost");
        String d=dict.get("domain");
        String ip=dict.get("ipAddress");
        String reshost="";
        if (d!=null){
        //ip is contained in domain
            ip=null;
            reshost=d.toLowerCase();
        }
        if(ophost !=null && ophost!=""){
            reshost= ophost.toLowerCase();
        } 
        if(ip != null){
            reshost=ip.toLowerCase();
        }


        String tmp=reshost;
        if (tmp.startsWith("[") && tmp.endsWith("]")){ //ipv6: remove leading zeros and convert ipv4 pieces
            tmp=tmp.subSequence(1, tmp.length()-1).toString(); 
            tmp="["+util.formatIPv6(tmp)+"]";
        }
        reshost=tmp;   
        components.put("host", reshost);

        //build scheme
        String specialnf=dict.get("URLspecialSchemeNotFile");
        String nonspecial=dict.get("URLnonSpecialScheme");
        String file=dict.get("URLschemeFile");

        for (String content: Arrays.asList(specialnf, nonspecial, file)){
            if(content !=null){
                components.put("scheme", content.toLowerCase());
            }
        }


        //build prePath  TODO 
        String scheme=components.get("scheme");
        String host=components.get("host"); 
        String userinfo=dict.get("userinfo");
        String p=components.get("port");
        String d2=dict.get("domain"); //used in schemeRelativeFileURL together with ip

        String prePath="";

        if(nonspecial !=null){
            //custom scheme
            prePath+=components.get("scheme")+":";
        }
        else{
            if(file!=null){
                //file scheme
                prePath+=components.get("scheme")+"://";
            }
            else{
                //special scheme
                prePath+=components.get("scheme")+"://";
                if(userinfo!=null){
                    prePath+=userinfo+"@";
                }
                prePath+=host;
                if(p!=null){
                    prePath+=":"+p;
                }
            }
        }
        components.put("prePath", prePath);

        /*boolean first=true; 
        if(scheme != null){
            prePath+=scheme;
            if (spec.toLowerCase().startsWith(scheme+":")){
                prePath+=":";
            }
        }
        if(userinfo != null && userinfo != ""){
            if(first){
                prePath+="//";
                first=false;
            }
            prePath+=userinfo+"@";
        }
        if((host != null && host != "" )){
            if(first){
                prePath+="//";
                first=false;
            }
            if( scheme != "file"){ //prePath for file URLs is just "file://"
                prePath+=host;
                if(p != null && p!= ""){
                    prePath+=":"+p;
                }
            }
        }

        if(prePath.contains("file:")){ //TODO is there a better way? 
        prePath="file://";
        }*/
        /*if(prePath!="" /*&& spec.toLowerCase().startsWith(prePath.toLowerCase())) { 
        components.put("prePath", prePath);
        } */

        //build pathQueryRef 

        String ref=components.get("ref");
        String pqr="";

        //build path

        String pa=dict.get("pathAbsoluteURL");
        String sr=dict.get("schemeRelativeURL");
        String prsl=dict.get("pathRelativeSchemelessURL"); 
        String srf=dict.get("schemeRelativeFileURL"); 
        if( srf != null || sr != null ){
        pa=null; //schemeRelativeFileURL contains pathAbsolute productions
        }
        String path="";

        for (String content: Arrays.asList(sr, pa, prsl, srf)){
            if(content !=null){
                path=content;
            }
        }

        if(nonspecial!=null){ //nonspecial scheme, url treated as pathurl
            //TODO check in specification again

            String srel=dict.get("schemeRelativeURL"); 
            String pabs=dict.get("pathAbsoluteURL");
            String prel=dict.get("pathRelativeSchemelessURL");
            if(srel!=null){
                pabs=null; //contained in schemeRelativeURL
            }
            String pc=""; //includes leading slashes
            for (String content: Arrays.asList(srel, pabs, prel)){
                if(content !=null){
                    pc=content;
                }
            }

            components.put("host",""); //TODO find a better solution, only want to add present components
            components.put("port", "-1");
            pqr+=pc;
        }
        else{
            pqr+=path;
        }


        //get query
        String query="";
        String qs=dict.get("URLSpecialquery");
        String qns=dict.get("URLquery");

        if(qs != null && qs != ""){
            query=qs; 
        }
        if(qns != null && qns != ""){
            query=qns; 
        }
        if(query != ""){
            pqr+="?"+query; 
        }
        if(ref != null) {
            //build hasRef
            components.put("hasRef", "true");
            pqr+="#"+ref;
        } else {
            components.put("hasRef", "false");
            components.put("ref", "");
        }
        if(!pqr.startsWith("/") && !prePath.endsWith(":")){//don't add if pqr is empty
            String addslash="/"+pqr; 
            pqr=addslash;
        }

        components.put("pathQueryRef", pqr);

        return components;
    }

    private String normalize(String pqr){ //TODO 

        //pqr=pqr.replaceAll("%2e",".");
        Path npqr=Paths.get(pqr);
        String res=npqr.normalize().toString();
        // add slashes that were removed during normalization
        if(pqr.startsWith("//") && !res.startsWith("//")){
            if (res.startsWith("/")){
                String nres="/"+res;//TODO doesn't work
                res=nres;
            }
            else {
                String nres="//"+res;//TODO doesn't work
                res=nres;
            }

        }
        if(pqr.endsWith("/") && !res.endsWith("/")){
            res+="/";
        }
        return res;

    }



}
