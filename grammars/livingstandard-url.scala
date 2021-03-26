import saarland.cispa.se.tribble.dsl._

//following 4.3 URL writing at https://url.spec.whatwg.org/
//including parts from https://tools.ietf.org/html/rfc3986#appendix-A and //https://tools.ietf.org/html/rfc6874
//(whenever the living standard documentation was not sufficient to formulate a grammar)

Grammar(
  'url := 'baseAndRelativeURL | 'absoluteURLwithFragment,
  'absoluteURLwithFragment :='absoluteURL ~ ("#" ~ 'URLfragment).?,
  'absoluteURL := 'specialAbsoluteURL | 'fileAbsoluteURL | 'otherAbsoluteURL,

  'URLspecialSchemeNotFile := "ftp" | "http" | "https" | "ws" | "wss", 
  'URLnonSpecialScheme := 'alpha ~ ('alphanum | "+" | "-" | ".").rep,
  'URLschemeFile := "file",

  'baseAndRelativeURL := ('specialBaseURL ~  "<" ~ 'specialRelativeURL )
                          | ('fileBaseURL ~  "<" ~ 'fileRelativeURL )  //TODO might need to differentiate between empty/nonempty host
                          | ('otherBaseURL ~ "<" ~ 'otherRelativeURL ), 



  'specialBaseURL := 'specialAbsoluteURL ~ ("#" ~ 'URLfragment).?,
  'fileBaseURL := 'fileAbsoluteURL ~ ("#" ~ 'URLfragment).?,
  'otherBaseURL := 'otherAbsoluteURL ~ ("#" ~ 'URLfragment).?,

  'specialAbsoluteURL := 'URLspecialSchemeNotFile ~ ":" ~ 'schemeRelativeSpecialURL ~ ("?" ~ 'URLSpecialquery).?,  
  'fileAbsoluteURL := 'URLschemeFile ~ ":" ~ 'schemeRelativeFileURL ~ ("?" ~ 'URLSpecialquery).? , 
  'otherAbsoluteURL := 'URLnonSpecialScheme ~ ":" ~ ('schemeRelativeURL | 'pathAbsoluteURL ) ~ ("?" ~ 'URLquery ).?,

  'specialRelativeURL := ('schemeRelativeSpecialURL | 'pathAbsoluteURL | 'pathRelativeSchemelessURL) ~ ("?" ~ 'URLSpecialquery).? ~ ("#" ~ 'URLfragment).?,
  'fileRelativeURL := ('schemeRelativeFileURL | 'pathAbsoluteURL | 'pathAbsoluteNonWindowsFileURL | 'pathRelativeSchemelessURL) ~ ("?" ~ 'URLSpecialquery).? ~ ("#" ~ 'URLfragment).?,
  'otherRelativeURL := ('schemeRelativeURL | 'pathAbsoluteURL | 'pathRelativeSchemelessURL) ~ ("?" ~ 'URLquery).? ~ ("#" ~ 'URLfragment).?,

  'schemeRelativeSpecialURL := "//" ~ ('domain | 'ipAddress) ~ ((":" ~ 'URLport.?).? ~ 'pathAbsoluteURL).?, 
  'schemeRelativeURL := "//" ~ 'opaqueHostAndPort ~ 'pathAbsoluteURL.?, 
  'schemeRelativeFileURL := "//" ~ ((('domain | 'ipAddress) ~ 'pathAbsoluteNonWindowsFileURL.?) | 'pathAbsoluteURL ),
  
  'opaqueHostAndPort := 'opaqueHost ~ (":" ~ 'URLport).?, 
  'opaqueHost := ((('basicHost | 'opaqueHostPercentEncoded) ~ 'opaqueHostCodePoint.rep) | 'hostunicode) | ("[" ~ 'ipv6address ~ "]"), 
  'ipAddress:= 'ipv4address | ("[" ~ 'ipv6address ~ "]"),
  
  'pathAbsoluteURL := ("/"~'windowsDriveLetter.?).? ~ (("/" ~ 'pathRelativeURLstart.?) | "/"),
  'pathAbsoluteNonWindowsFileURL := "/" ~ 'URLpathSegment ~ ("/" ~ 'pathRelativeURL).?,
  'pathRelativeURL := 'URLpathSegment ~ ("/" ~ 'pathRelativeURL).? , //not allowed to start with /
  'pathRelativeURLstart := (('firstPathCodePoint ~ 'pathCodePoint.rep) | 'singleDotPathSegment | 'doubleDotPathSegment) ~ ("/" ~ 'pathRelativeURL).?,  
  'pathRelativeSchemelessURL := 'pathRelativeURLstart, // not allowed to start with scheme:


  'windowsDriveLetter := 'alpha ~ (":" | "|"),
  'URLpathSegment := ('pathCodePoint.rep) | 'singleDotPathSegment | 'doubleDotPathSegment,
  'singleDotPathSegment := "." | "%2e",
  'doubleDotPathSegment := ".." | ".%2e" | "%2e." | "%2e%2e", 
  
  
  'URLquery := 'queryCodePoint.rep(1),
  'URLSpecialquery := 'specialQueryCodePoint.rep(1),
  'URLfragment := 'fragmentCodePoint.rep(1),
   // 0<=port<=65535
  'URLport := ('digit.rep(1,4))		
		| (( "1" | "2" | "3" | "4"| "5") ~ 'digit.rep(4,4))
		| ("6" ~ ("0" | "1" | "2" | "3" | "4") ~ 'digit.rep(3,3))
		| ("65" ~ ("0" | "1" | "2" | "3" | "4") ~ 'digit.rep(2,2))
		| ("655" ~ ("0" | "1" | "2" ) ~ 'digit)
		| ("6553" ~ ( "0"|"1" | "2" | "3" | "4"| "5")),
  //'URLunit := 'URLcodePoint | 'percentEncodedByte,
  //'URLcodePoint := 'unreserved, //| 'unicode |'reserved ,
  //'reserved := ":" | "/" | "?" | "#" | "[" | "]" | "@" | 'subdelims,
  //'subdelims := "!" | "$" | "&" | "'" | "(" | ")" | "*" | "+" | "," | ";" | "=",
  'unreserved := 'alphanum | "-" | "." | "_" | "~",
  
  //'host := ('userinfo ~ "@").? ~ 'domain,  //userinfo is deprecated
  'domain := 'internationalHost |  'basicHost,
  'basicHost := ('alpha ~ 'hostAllowed.rep) | (('hostnonAlphaNum) ~ 'hostAllowed.rep) | (('digit.rep(1) ~ ".".?).rep ~ ('alpha | 'hostnonAlphaNum) ~ 'hostAllowed.rep),
  'internationalHost := ('hostAllowed | 'hostunicode ).rep(1) , //TODO check length allowed in idn
   
  //'userinfo := 'userinfoCodePoint ~ 'userinfoCodePoint.rep ~ (":" ~ 'userinfoCodePoint ~ 'userinfoCodePoint.rep).?, 
  'ipv4address := 'decoctet ~ "." ~ 'decoctet ~ "." ~ 'decoctet ~ "." ~ 'decoctet,
  'ipv6address := (('h16 ~ ":").rep(6, 6) ~ 'ls32)
    | ((('h16 ~ ":").rep(0, 1) ~ 'h16).? ~ "::" ~ ('h16 ~ ":").rep(2, 2) ~ 'ls32)
    | ((('h16 ~ ":").rep(0, 2) ~ 'h16).? ~ "::" ~ 'h16 ~ ":" ~ 'ls32)
    | ((('h16 ~ ":").rep(0, 3) ~ 'h16).? ~ "::" ~ 'ls32)
    | ((('h16 ~ ":").rep(0, 4) ~ 'h16).? ~ "::" ~ 'h16)
    | ((('h16 ~ ":").rep(0, 5) ~ 'h16).? ~ "::"),
  'decoctet := 'digit | ("[1-9]".regex ~ 'digit)
    | ("1" ~ 'digit.rep(2, 2))
    | ("2" ~ ("0" | "1" | "2" | "3" | "4") ~ 'digit)
    | ("25" ~ ("0" | "1" | "2" | "3" | "4" | "5")),
  'h16 := 'hexdig ~ 'hexdig ~ 'hexdig ~ 'hexdig,
  'ls32 := ('h16 ~ ":" ~ 'h16) | 'ipv4address,
  'digit := "[0-9]".regex,
  'alphanum := 'alpha | 'digit,
  'alpha := "[a-zA-Z]".regex,
  'hexdig := ("[a-f]".regex) | 'digit,
  

  'hostAllowed := 'alphanum | 'hostnonAlphaNum,// | ".", TODO allow dot in host but not .host or host. or h..ost
  'hostnonAlphaNum := "!" | "\"" | "$" | "&"  |"'" | "(" | ")" | "*" | "+" | "," |  "{" | "}" |"`"  |  ";" | "=" | "|" |  "-"  | "_" | "~",

  
  'opaqueHostCodePoint := 'hostAllowed | 'opaqueHostPercentEncoded ,
  //'inthostAllowed := 'unreserved | "!" | "$" | "&"  | "(" | ")" | "*" | "+" | "," |  "{" | "}" |  ";" | "=" | "|",
  'opaqueHostPercentEncoded := "%00" | "%09" | "%20" | "%23" | "%25" | "%2f" | "%3a" | "%3c" | "%3e" | "%3f" | "%40" | "%5b" | "%5c" | "%5d" | "%5e" | "%7c" ,
  

  'unicode := "[\u00a0-\ud7ff\ue000-\ufdcf\ufdf0-\ufffd]".regex , //this also contains rtl chars
             // | "[\u10000-\u1fffd]".regex, //TODO also use unicode above ffff

  'hostunicode := "[\u0100-\u0148\u0148-\u017f]".regex,,
    //"[\u024f-\u02af\u1050-\u1090\u10d0-\u10fa\u1200-\u1248\u1780-\u17b3\u1820-\u1877\ua000-\ua4fd\ua980-\ua9c0]".regex, 
  // this is a subset of code points allowed in hosts and far from exhaustive


  'queryCodePoint := 'specialQueryAllowed | "'" | 'queryPercentEncoded | 'unicode,
  'queryPercentEncoded := "%20" | "%22" | "%23" | "%3c" | "%3e",
  'specialQueryAllowed := 'pathAllowed | "?" | "{" | "}" | "`" ,
  'specialQueryCodePoint := 'specialQueryAllowed |'queryPercentEncoded | 'unicode, //check if ' should be percent encoded
  
  //'userinfoCodePoint := 'userinfoAllowed | 'userinfoPercentEncoded | 'unicode,
  'userinfoAllowed := 'unreserved | "!" | "$" | "&" | "%" | "'" | "(" | ")" | "*" | "+" | "," ,
  //'userinfoPercentEncoded := "%2f" | "%3a" | "%3b" | "%3d" | "%40" | "%5b" | "%5c" | "%5d" | "%5e" | "%7c",

  'pathCodePoint := 'pathAllowed | 'pathPercentEncoded | 'unicode,
  'pathAllowed := 'userinfoAllowed | "/" | ":" | ";" | "=" | "@" | "[" | "]" |  "^" | "|",
  'pathPercentEncoded := 'queryPercentEncoded | "%3f" | "%60" | "%7b" | "%7d",

  'firstPathCodePoint := 'userinfoAllowed |  ";" | "=" | "@" | "[" | "]" |  "^" | "|" | 'pathPercentEncoded | 'unicode, //check if / and : should be percent encoded 

  'fragmentCodePoint := 'fragmentAllowed | 'fragmentPercentEncoded | 'unicode,
  'fragmentAllowed := 'pathAllowed | "?" | "{" | "}" | "#",
  'fragmentPercentEncoded := "%20" | "%22" | "%3c" | "%3e" | "%60",
 
)

