package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FirefoxURLComponentsBuilderLS extends ComponentsBuilder {

    ArrayList<String> InternalComponentNames=new ArrayList<String>();
    HashMap<String, String> translation=new HashMap<>();

    public FirefoxURLComponentsBuilderLS(){
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




      translation.put("port", "URLport");
      translation.put("userPass","userinfo" );
      translation.put("ref", "URLfragment");

    }

    @Override
    public String buildRepresentation() {
      Map<String, String> components=buildMapping();
      String result="{";
      for(String key:components.keySet()){
        if(key != "hasRef") {
	  String content = components.get(key);
	  String tmp=content.replaceAll("\\\\", "\\\\\\\\");
	  if (key=="host"){
	    if (tmp.startsWith("[") && tmp.endsWith("]")){
	      tmp=tmp.subSequence(1, tmp.length()-2).toString();
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

    private Map<String, String> buildMapping(){
      Map<String, String> components=new HashMap<>();
      for(String name:translation.keySet()){
        String content=dict.get(translation.get(name));
        if (content!=null){
          components.put(name, content);
        }
      }
      //build spec
      String url=dict.get("relativeURLwithFragment");
      if(url !=null){
        components.put("spec", url);
      }
      else{
        components.put("spec", dict.get("absoluteURLwithFragment"));
      }
      String spec=components.get("spec");
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
        components.put("host", ophost);
      }
      else{
        if(d !=null){
          components.put("host", d);
        }
      }
      //build hostport
      /*String hosts=components.get("host");
      String port=components.get("port");
      if(hosts !=null){
        if(port !=null){
          components.put("hostPort", hosts+":"+port); //TODO only add ":" if it is there originally -> search for hosts+":"+port in string
        }
        else{
          components.put("hostPort", hosts);
        }

      }*/


      //build pathQueryRef
      String path=components.get("path");
      String query=dict.get("URLquery");
      String ref=components.get("ref");
      String pqr="";
      if(path !=null) {
        pqr = path;
      }
      if (query != null) {
        pqr += "?" + query; 
      }
      if (ref != null) {
        pqr += "#" + ref;
          //build hasRef
        components.put("hasRef", "true");
      } else {
        components.put("hasRef", "false");
	components.put("ref", "");
      }
      components.put("pathQueryRef", pqr);

      //build scheme
      String specialnf=dict.get("URLspecialSchemeNotFile");
      String nonspecial=dict.get("URLnonSpecialScheme");
      String file=dict.get("URLschemeFile");

      for (String content: Arrays.asList(specialnf, nonspecial, file)){
        if(content !=null){
          components.put("scheme", content);
        }
      }


      //build prePath
      String scheme=components.get("scheme");
      String host=components.get("host");
      String userinfo=dict.get("userinfo");
      String p=components.get("port");
      String slashes=dict.get("slashes");
      String prePath="";
      boolean first=true; 
      if(scheme != null){
        prePath+=scheme ;
	if (spec.startsWith(scheme+":")){
	  prePath+=":";
	}
      }
      if(userinfo != null && userinfo != ""){
        if(first){
          prePath+=slashes;
          first=false;
        }
        prePath+=userinfo+"@";
      }
      if(host != null){
        if(first){
          prePath+=slashes;
          first=false;
        }
        prePath+=host;
      }
      if(p != null){
        prePath+=":"+p;
      }
      if(prePath!="") {
        components.put("prePath", prePath);
      }



      return components;
    }



}
