import saarland.cispa.se.tribble.dsl._

//following 4.3 URL writing at https://url.spec.whatwg.org/
//including parts from https://tools.ietf.org/html/rfc3986#appendix-A and //https://tools.ietf.org/html/rfc6874
//(whenever the living standard documentation was not sufficient to formulate a grammar)

Grammar(
  'url := "" ~ 'absoluteURLwithFragment,
  'absoluteURLwithFragment :=('absoluteURL ~ ("#" ~ 'URLfragment).?).?,
  'absoluteURL := (('URLspecialSchemeNotFile ~ ":" ~ 'schemeRelativeSpecialURL ~ ("?" ~ 'URLSpecialquery).?)  //TODO include other relative alternatives?
    | ('URLnonSpecialScheme ~ ":" ~ ('schemeRelativeURL | 'pathAbsoluteURL | 'pathRelativeSchemelessURL ) ~ ("?" ~ 'URLquery ).?)//'relativeURL ) // relativeURL includes driveletter
    | ('URLschemeFile ~ ":" ~ 'schemeRelativeFileURL ~ ("?" ~ 'URLSpecialquery).?)) , 

  'URLspecialSchemeNotFile := "ftp" | "http" | "https" | "ws" | "wss", 
  'URLnonSpecialScheme := 'alpha ~ ('alphanum | "+" | "-" | ".").rep,
  'URLschemeFile := "file",

  //'relativeURL := ('specialSchemeNotFile | 'fileScheme | 'otherScheme) ~ ("?" ~ 'URLquery).?, // use URLSpecialquery 
  //'specialSchemeNotFile := 'schemeRelativeSpecialURL | 'pathAbsoluteURL | 'pathRelativeSchemelessURL,
  //'fileScheme := 'schemeRelativeFileURL | 'pathAbsoluteURL
   // | 'pathAbsoluteNonWindowsFileURL | 'pathRelativeSchemelessURL,
  //'otherScheme := 'schemeRelativeURL | 'pathAbsoluteURL | 'pathRelativeSchemelessURL,
  'schemeRelativeSpecialURL := "//" ~ 'host ~ ((":" ~ 'URLport).? ~ 'pathAbsoluteURL).?, 

  
  'schemeRelativeURL := "//" ~ 'opaqueHostAndPort ~ 'pathAbsoluteURL.?, 
  'opaqueHostAndPort := ('opaqueHost ~ (":" ~ 'URLport).?).?, 
  'opaqueHost := 'opaqueHostCodePoint.rep(1) | ("[" ~ 'ipv6address ~ "]"), 
  'schemeRelativeFileURL := "//" ~ ((('domain | 'ipv4address | "[" ~ 'ipv6address ~ "]").? ~ 'pathAbsoluteNonWindowsFileURL.?) | 'pathAbsoluteURL ),
  'pathAbsoluteURL := "/" ~ 'pathRelativeURL,
  'pathAbsoluteNonWindowsFileURL := 'pathAbsoluteURL ~ 'windowsDriveLetter ~ "/", 
  'windowsDriveLetter := 'alpha ~ (":" | "|"),
  'pathRelativeURL := 'URLpathSegment ~ ("/" ~ 'pathRelativeURL).?,
  'pathRelativeSchemelessURL := ('pathRelativeURL ~ ":").?,
  //pathRelativeURL can't start with URLscheme
  'URLpathSegment := ('pathCodePoint.rep) | 'singleDotPathSegment | 'doubleDotPathSegment,
  // URLunit can't be /,?, singleDotPathSegment, doubleDotPathSegment
  'singleDotPathSegment := "." | "%2e",
  'doubleDotPathSegment := ".." | ".%2e" | "%2e." | "%2e%2e", //also add %2E ?
  'URLquery := 'queryCodePoint.rep(1),
  'URLSpecialquery := 'specialQueryCodePoint.rep(1),
  'URLfragment := 'fragmentCodePoint.rep,
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
  
  'host := ('userinfo ~ "@").? ~ 'domain,
  'domain := 'hostAllowed.rep(1) | 'ipv4address | ("[" ~ 'ipv6address ~ "]"), //TODO explixitly include xn-- variations?
  'userinfo := 'userinfoCodePoint ~ 'userinfoCodePoint.rep ~ (":" ~ 'userinfoCodePoint ~ 'userinfoCodePoint.rep).?, 
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
  'alphanum := "[a-zA-Z0-9]".regex,
  'alpha := "[a-zA-Z]".regex,
  'hexdig := ("[a-f]".regex) | 'digit,
  

  'userinfoCodePoint := 'userinfoAllowed | 'userinfoPercentEncoded,
  'pathCodePoint := 'pathAllowed | 'pathPercentEncoded,
  'queryCodePoint := 'specialQueryAllowed | "'" | 'queryPercentEncoded,
  'specialQueryCodePoint := 'specialQueryAllowed | 'queryPercentEncoded | "%27",
  'fragmentCodePoint := 'fragmentAllowed | 'fragmentPercentEncoded,
  //'c0CodePoint := 'c0Allowed | 'c0PercentEncoded,
  'opaqueHostCodePoint := 'hostAllowed | "%"| 'opaqueHostPercentEncoded,

  'hostAllowed := 'unreserved | "!" | "\"" |"$" | "&"  |"'" | "(" | ")" | "*" | "+" | "," |  "{" | "}" |"`"  |  ";" | "=" |  "|",
  'opaqueHostPercentEncoded := "%" ~ (("0" ~ ("[1-8]".regex | "b" | "c" | "e" | "f") )| ("1" ~ 'hexdig)), //TODO c0percent encoding above %7f

  'c0PercentEncoded:= "%" ~ ((("0"|"1") ~ 'hexdig) | (("7" | "8" | "9" | "[a-f]".regex) ~ 'hexdig)), //TODO add code points above %ff
  //unicode: code points in u+00A0 to u+10FFFD, excluding surrogates(u+D800-u+DFFF) and noncharachters(u+FDD0-u+FDEF)
  'fragmentPercentEncoded := 'c0PercentEncoded | ( "%" ~ ("20" | "22" | "3c" | "3e" | "60")),
  'queryPercentEncoded := 'c0PercentEncoded | ("%" ~ ("20" | "22" | "23" | "3c" | "3e" )),
  'pathPercentEncoded := 'queryPercentEncoded | ("%" ~ ("3f" | "60" | "7b" | "7d")),
  'userinfoPercentEncoded := 'pathPercentEncoded | ("%" ~ ("2f" | "3a" | "3b" | "3d" | "40" | "5b"| "5c" | "5d" | "5e" | "7c")),

  'userinfoAllowed := 'unreserved | "!" | "$" | "&" | "%" | "'" | "(" | ")" | "*" | "+" | "," ,
  'pathAllowed := 'userinfoAllowed | "/" | ":" | ";" | "=" | "@" | "[" | "]" |  "^" | "|",
  'specialQueryAllowed := 'unreserved | "!" | "$" | "&" | "%"  | "(" | ")" | "*" | "+" | "," | "?" | "{" | "}" |"`" | "/" | ":" | ";" | "=" | "@" | "[" | "]" | "\\" | "^" | "|", 
  'fragmentAllowed := 'pathAllowed | "?" | "{" | "}" | "#",
  //'c0Allowed := 'fragmentAllowed | " " | "\"" | "<" | ">" | "`",
)

