package saarland.cispa.se.tribble.execution.componentExtraction;

public class URLComponentsUtil {

	/***
    * formats the original IPv6 address(given without brackets) by converting the IPv4 part to hex
    * @return the given IPv6 address without IPv4 formatting
    */
    public String formatIPv6(String original){
		if(original.startsWith("::") && original.endsWith("::")){
		   return original;
		}
		String[] pieces=original.split(":");
		String result="";
		for (String piece: pieces){
		    if (piece.contains(".")){ //ipv4 piece: i.e. 123.123.234.111, convert to hex(123)hex(123):hex(234)hex(111)
				String[] parts = piece.split("\\.");
				int index=0;
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
				    parts[index]=p;
				    index++;
				}
				//combine parts 0,1 and 2,3 and get rid of leading zeros
				piece=(parts[0]+parts[1]).replaceFirst("^0+(?!$)", "")+":"+(parts[2]+parts[3]).replaceFirst("^0+(?!$)", "");
		    }
		    if(piece != "" && !piece.contains(":")){
				piece=piece.replaceFirst("^0+(?!$)", ""); //remove leading zeros but keep the string nonempty
		    }
		    result += piece +":"; 
		}
		if(original.endsWith("::")){
		   
		   	if(!result.endsWith("::")){
	           	//complete :: at the end
	 	      	return result+":";
	        }
		   	return result;
		}
	    if(result != "" && result !="::"){
		   	//remove additional : at the end
		   	return result.subSequence(0, result.length()-1).toString();
		}
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

	
}