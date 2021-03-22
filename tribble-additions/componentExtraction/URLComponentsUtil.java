package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class URLComponentsUtil {
	
	public String[] FRAGMENT_PERCENT_ENCODE={" ", "\"", ">", "<", "`"};
	public String[] QUERY_PERCENT_ENCODE={" ", "\"", ">", "<", "#"};
	public String[] SPECIAL_QUERY_PERCENT_ENCODE=QUERY_PERCENT_ENCODE+{"\'"};
	public String[] PATH_PERCENT_ENCODE=QUERY_PERCENT_ENCODE+{"?", "`", "{", "}"};
	public String[] USERINFO_PERCENT_ENCODE=PATH_PERCENT_ENCODE+{"/", ":", "=", "@", "[", "\\", "]", "^", "|"};
	

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

	/***
	* removes dot and double-dot segments where applicable and fixes drive letter representation
	*
	* @param originalPath the path as extracted from the tree representation
	* @param driveletter a string containing the drive letter contained in the path if any
	* @return the normalized path
	*/
	public String normalizePath(String originalPath, String driveletter){
		if (originalPath!=null){
			String result="";
			String tmp=originalPath;
			// convert backslashes to forward slashes: \\=/
			tmp=tmp.replaceAll("\\\\", "/"); 

			//apply single-dot changes
			tmp=tmp.replaceAll("/%2e/", "/./");
			tmp=tmp.replaceAll("/%2E/", "/./");
			while(tmp.contains("/./")){
				tmp=tmp.replaceAll("/./", "/");
			}
			

			if(tmp.endsWith("/.")){
				tmp=tmp.substring(0, tmp.length()-1);
			}
			if(tmp.endsWith("/%2e") || tmp.endsWith("/%2E")){
				tmp=tmp.substring(0, tmp.length()-3);
			}

			//apply double-dot changes
			List<String> segments=splitPath(tmp); 
			List<String> newsegments=new ArrayList<String>(); 
			for(String s: segments){ //initialize newsegments
				newsegments.add("");
			}
			newsegments.add("");
			 
			String[] ddots=new String[] {"/..", "/%2e%2e", "/%2E%2E", "/.%2e", "/.%2E", "/%2e.", "/%2E"};
			int prev=0;
			String drive=null;
			if(driveletter != null && ! driveletter.equals("")){
				drive="/"+driveletter;
			}
			
			for(String current:segments){
				if(Arrays.asList(ddots).contains(current)){ 
					// current segment is double-dot -> remove previous segment if not drive letter
					if( !(newsegments.get(prev).equals(drive))){
						newsegments.set(prev,"");
						prev=(prev>0) ? prev-1 : 0;

					}
					
				}
				else{
					// current segment is not double-dot -> add current segment
					if(!current.equals("/.") && !current.equals("/%2e") && !current.equals("/%2E")){
						//there should be no single dot segments left, but some are not removed in the previous checks
						newsegments.set(prev+1, current); 
						prev++;
					}
					
				}
			}

			// put remaining segments back together
			if(Arrays.asList(new String []{"..", "%2e%2e", "%2E%2E", ".%2e", ".%2E", "%2e.", "%2E."}).contains(newsegments.get(0))){
				newsegments.set(0, "");
			}
			for(String seg:newsegments){
				if(seg.equals(drive)){
					seg=seg.replaceFirst("\\|", ":");
				}
				result+=seg;
			}
			
			// check if the original path ended in a double-dot-segment: new path ending should then be /
			Boolean endsindots=false;
			for( String dots : Arrays.asList(ddots)){
				if(originalPath.endsWith(dots)){
					endsindots=true;
				}
			}
			// fix path ending for now empty paths and paths ending in double-dot-segments
			if((originalPath!="" && result=="")|| endsindots){ 
				result+="/";
			}
			


			return result;
		}
		return originalPath;
	}

	/***
	* splits the given input into segments using / (forward slash) as separator
	* while keeping all separators (concatenating all segments equals the input)
	* @return a list of strings containing the segments
	*/
	private List<String> splitPath(String input){
		String slash="/";
		ArrayList<String>result=new ArrayList<String>();
		

		int index=0;
		while(index<input.length() && index >= 0){
			int next=input.indexOf(slash, index+1); //exclude leading / from search
			result.add(input.substring(index, (next!=-1 ? next : input.length()))); //exclude trailing / from segment
			index=next;
		}
		return result;
	}



	/***
	* replaces all unicode chars (anything above \u007f) by their percent encoding
	* @return  the string with percent encoded unicode chars
	* @param input the string whose content should be encoded
	* @param additionallyEncode a list of characters that should be encoded as well (their values may be lower than u+007f)
	*/
	public String escapeUnicodeChars(String input, String[] additionallyEncode){
		String res="";
				for(int codePoint:input.codePoints().toArray()){
					if(codePoint<=127&&codePoint>=32){
						// no encoding required unless specified otherwise
						String c=(char) codePoint;
						boolean needsEncoding=false;
						for(String encode:additionallyEncode){
							if(encode.contains(c)){
								needsEncoding=true;
							}
						}
						if(needsEncoding){
							res+=percentEncode(codePoint);
						}
						else{
							res+=(char) codePoint;
						}
						 
					}
					else{
						// encoding required
						res+=percentEncode(codePoint);
					}
				}
				return res;

	}

	/***
	* utility method that encodes a given codePoint in percent encoding
	* @return a string containing the percent encoded representaion of the give codePoint
	* @param codePoint the integer representing the unicode symbol to be encoded
	*/
	private String percentEncode(int codePoint){
		String res="";
		int[] codeunits={codePoint};
		byte[] bytes=(new String(codeunits, 0, 1)).getBytes(StandardCharsets.UTF_8);
		//convert each byte into hex
		for(int i=0; i<bytes.length; i++){
			res+="%"+Integer.toHexString(bytes[i]&0xff);
		}
		return res;
	}
}