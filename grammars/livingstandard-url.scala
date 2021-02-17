import saarland.cispa.se.tribble.dsl._

//following 4.3 URL writing at https://url.spec.whatwg.org/
//including parts from https://tools.ietf.org/html/rfc3986#appendix-A and //https://tools.ietf.org/html/rfc6874
//(whenever the living standard documentation was not sufficient to formulate a grammar)

Grammar(
  'url := 'absoluteURLwithFragment,
  'absoluteURLwithFragment :='absoluteURL ~ ("#" ~ 'URLfragment).?,
  'absoluteURL := (('URLspecialSchemeNotFile ~ ":" ~ 'schemeRelativeSpecialURL ~ ("?" ~ 'URLSpecialquery).?)  
    | ('URLnonSpecialScheme ~ ":" ~ ('schemeRelativeURL | 'pathAbsoluteURL | 'pathRelativeSchemelessURL ) ~ ("?" ~ 'URLquery ).?)//'relativeURL ) // relativeURL includes driveletter
    | ('URLschemeFile ~ ":" ~ 'schemeRelativeFileURL ~ ("?" ~ 'URLSpecialquery).?)) , 

  'URLspecialSchemeNotFile := "ftp" | "http" | "https" | "ws" | "wss", 
  'URLnonSpecialScheme := 'alpha ~ ('alphanum | "+" | "-" | ".").rep,
  'URLschemeFile := "file",

 
  'schemeRelativeSpecialURL := "//" ~ 'domain ~ ((":" ~ 'URLport).? ~ 'pathAbsoluteURL).?, 

  
  'schemeRelativeURL := "//" ~ 'opaqueHostAndPort ~ 'pathAbsoluteURL.?, 
  'opaqueHostAndPort := ('opaqueHost ~ (":" ~ 'URLport).?).?, 
  'opaqueHost := 'opaqueHostCodePoint.rep(1) | ("[" ~ 'ipv6address ~ "]"), 
  'schemeRelativeFileURL := "//" ~ ((('domain | 'ipAddress).? ~ 'pathAbsoluteNonWindowsFileURL.?) | 'pathAbsoluteURL ),
  'ipAddress:= 'ipv4address | ("[" ~ 'ipv6address ~ "]"),
  'pathAbsoluteURL := ("/"~'windowsDriveLetter).? ~"/" ~ 'pathRelativeURL,
  'pathAbsoluteNonWindowsFileURL := 'URLpathSegment ~ ("/" ~ 'pathRelativeURL).?,
  'windowsDriveLetter := 'alpha ~ (":" | "|"),
  'pathRelativeURL := 'URLpathSegment ~ ("/" ~ 'pathRelativeURL).?,  
  'pathRelativeSchemelessURL := ('pathRelativeURL ~ ":").?,
  //pathRelativeURL can't start with URLscheme
  'URLpathSegment := ('pathCodePoint.rep) | 'singleDotPathSegment | 'doubleDotPathSegment, 
  // URLunit can't be /,?, singleDotPathSegment, doubleDotPathSegment
  'singleDotPathSegment := "." | "%2e",
  'doubleDotPathSegment := ".." | ".%2e" | "%2e." | "%2e%2e", 
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
  
  //'host := ('userinfo ~ "@").? ~ 'domain,  //TODO userinfo is deprecated
  'domain := 'internationalHost | 'hostAllowed.rep(1) | 'ipAddress, 
  'internationalHost := (("xn--").? ~ 'hostAllowed.rep(1)).rep(1),
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
  'alphanum := "[a-zA-Z0-9]".regex,
  'alpha := "[a-zA-Z]".regex,
  'hexdig := ("[a-f]".regex) | 'digit,
  

  //'userinfoCodePoint := 'userinfoAllowed | 'userinfoPercentEncoded,
  'pathCodePoint := 'pathAllowed | 'pathPercentEncoded,
  'queryCodePoint := 'specialQueryAllowed | "'" | 'queryPercentEncoded,
  'specialQueryCodePoint := 'specialQueryAllowed | 'queryPercentEncoded | "%27",
  'fragmentCodePoint := 'fragmentAllowed | 'fragmentPercentEncoded,
  //'c0CodePoint := 'c0Allowed | 'c0PercentEncoded,
  'opaqueHostCodePoint := 'hostAllowed | 'opaqueHostPercentEncoded,

  'hostAllowed := 'unreserved | "!" | "\"" | "$" | "&"  |"'" | "(" | ")" | "*" | "+" | "," |  "{" | "}" |"`"  |  ";" | "=" | "|",
  'opaqueHostPercentEncoded := "%" ~ (("0" ~ ("[1-8]".regex | "b" | "c" | "e" | "f") )| ("1" ~ 'hexdig)), //TODO c0percent encoding above %7f

  'unicodeUtil := "%" ~ ("8"|"9"|"a"|"b") ~ 'hexdig,

  'c0PercentEncoded:= "%" ~ ((("0"|"1") ~ 'hexdig)  //c0 control
                            | ("7f")
                            | ("c" ~ "[2-9a-f]".regex ~ 'unicodeUtil) //U+0080-U+03FF
                            | ("d" ~ 'hexdig ~ 'unicodeUtil) //U+0400-U+07ff
                            | ("e" ~ (("0"~ "%"~("a"|"b") ~ 'hexdig ~ 'unicodeUtil)
                                        | (("[1-9a-c]".regex | "e") ~ 'unicodeUtil.rep(2,2))
                                        | ("d" ~ "%" ~ ("8"|"9") ~ 'hexdig ~ 'unicodeUtil) //excludes surrogates
                                        | ("f" ~ "%" ~ ((("8"|"9"|"a") ~ 'hexdig ~ 'unicodeUtil)
                                                          | ("b" ~ ((("[0-6a-f]".regex | "8" | "9") ~ 'unicodeUtil)
                                                                      | ("7" ~ "%" ~ ("[0-8]".regex | "b") ~ 'hexdig) //excludes noncharachters
                                                                    )
                                                            )
                                                        )
                                          )
                                      )
                              )
                            | ("f0" ~ "%90" ~("%" ~ ((("8"|"9"|"a") ~ 'hexdig ~ 'unicodeUtil)
                                                    | ("b" ~ "[0-9a-e]".regex ~ 'unicodeUtil)
                                                    | ("bf" ~ "%" ~ ((('digit | "a") ~ 'hexdig)
                                                                      | ("b" ~ "[0-9a-c]".regex) //stop at U+10FFFD
                                                                    )
                                                      )
                                                  )
                                             )
                              )
                          ),
  
  'fragmentPercentEncoded := 'c0PercentEncoded | ( "%" ~ ("20" | "22" | "3c" | "3e" | "60")),
  'queryPercentEncoded := 'c0PercentEncoded | ("%" ~ ("20" | "22" | "23" | "3c" | "3e" )),
  'pathPercentEncoded := 'queryPercentEncoded | ("%" ~ ("3f" | "60" | "7b" | "7d")),
  //'userinfoPercentEncoded := 'pathPercentEncoded | ("%" ~ ("2f" | "3a" | "3b" | "3d" | "40" | "5b"| "5c" | "5d" | "5e" | "7c")),

  'userinfoAllowed := 'unreserved | "!" | "$" | "&" | "%" | "'" | "(" | ")" | "*" | "+" | "," ,
  'pathAllowed := 'userinfoAllowed | "/" | ":" | ";" | "=" | "@" | "[" | "]" |  "^" | "|",
  'specialQueryAllowed := 'unreserved | "!" | "$" | "&" | "%"  | "(" | ")" | "*" | "+" | "," | "?" | "{" | "}" |"`" | "/" | ":" | ";" | "=" | "@" | "[" | "]" | "\\" | "^" | "|", 
  'fragmentAllowed := 'pathAllowed | "?" | "{" | "}" | "#",
  //'c0Allowed := 'fragmentAllowed | " " | "\"" | "<" | ">" | "`",
)

