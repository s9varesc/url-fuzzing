package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FirefoxURLComponentsBuilder extends ComponentsBuilder {

    ArrayList<String> InternalComponentNames=new ArrayList<String>();
    HashMap<String, String> translation=new HashMap<>();
    String format = "firefox";

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




      translation.put("port", "URLport");
      translation.put("userPass","userinfo" );
      translation.put("ref", "URLfragment");

    }
    public String getComponentFormat(){
      return format;
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
	    if (tmp.startsWith("[") && tmp.endsWith("]")){ //ipv6: need to remove leading zeros and convert ipv4 pieces
	      tmp=tmp.subSequence(1, tmp.length()-1).toString(); 
	      tmp=formatIPv6(tmp);
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

    private String formatIPv6(String original){
	String[] pieces=original.split(":");
	String result="";
	for (String piece: pieces){
	   if (piece.contains(".")){ //ipv4 piece: i.e. 123.123.234.111, convert to hex(123)hex(123):hex(234)hex(111)
		String[] parts = piece.split(".");
		for (String p: parts){
		    int pnr;
		    try {
		    	pnr=Integer.parseInt(p);
		    } catch (Exception e) {
		        pnr=0;
		    }
		    String tmp=Integer.toHexString(pnr);
		    if (tmp.length()<2){ //need leading zeros
			p="0"+tmp;
		    }
		    else {
			p=tmp;
		    }
		}
		//combine parts 0,1 and 2,3 and get rid of leading zeros
		piece=(parts[0]+parts[1]).replaceFirst("^0+(?!$)", "")+":"+(parts[2]+parts[3]).replaceFirst("^0+(?!$)", "");
	   }
	   if(piece != "" && !piece.contains(":")){
		piece.replaceFirst("^0+(?!$)", ""); //remove leading zeros but keep the string nonempty
	   }
	result += piece +":";
	}
	return result.replaceAll(":$", "");
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
        components.put("host", ophost.toLowerCase());
      }
      else{
        if(d !=null){
          components.put("host", d.toLowerCase());
        }
      }
      


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
          components.put("scheme", content.toLowerCase());
        }
      }


      //build prePath
      String scheme=components.get("scheme");
      String host=components.get("host");
      String userinfo=dict.get("userinfo");
      String p=components.get("port");
      
      String prePath="";
      boolean first=true; 
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
      if(host != null){
        if(first){
          prePath+="//";
          first=false;
        }
        prePath+=host;
      }
      if(p != null && p!= ""){
        prePath+=":"+p;
      }
      if(spec.toLowerCase().startsWith(prePath + "//")){
          prePath+="//";
      }
      if(prePath!="") {
        components.put("prePath", prePath);
      }



      return components;
    }



}
