import saarland.cispa.se.tribble.dsl._

// Translated from https://tools.ietf.org/html/rfc3986#appendix-A and //https://tools.ietf.org/html/rfc6874

Grammar(
   'uri              := 'scheme ~ ":" ~'hierpart ~ ("?" ~ 'query).? ~ ("#" ~ 'fragment).?,
   'hierpart         := "//" ~ 'authority ~ ('pathbase | 'pathrootless),
   'urireference    := 'uri | 'relativeref,

   //'absoluteURI     := 'scheme ~ ":" ~ 'hierpart ~ ( "?" ~ 'query ).?,

   'relativeref     := 'relativepart ~ ( "?" ~ 'query).? ~ ("#" ~ 'fragment ).?,

   'relativepart    := "//" ~ 'authority ~ ('pathbase | 'pathnoscheme ),

   'scheme          := 'alpha ~ ( 'alpha | 'digit | "+" | "-" | "." ).rep, 

   'authority       := ('userinfo ~ "@").? ~ 'host ~ (":" ~ 'port).?,
   'userinfo        := ( 'unreserved | 'pctencoded | 'subdelims | ":" ).rep, 
   'host            := 'ipliteral | 'ipv4address | 'regname,
   'port            := 'digit.rep, 

   'ipliteral       := "[" ~ ( 'ipv6address | 'ipv6addrz | 'ipvFuture ) ~ "]", 

   'zoneID          := ( 'unreserved | 'pctencoded ).rep(1,1),                   

   'ipv6addrz       := 'ipv6address ~ "%25" ~ 'zoneID,                          

   'ipvFuture       := "v" ~ 'hexdig.rep(1,1) ~ "." ~ ( 'unreserved | 'subdelims | ":" ).rep(1,1),

   'ipv6address     :=                            (( 'h16 ~ ":"  ).rep(6,6) ~ 'ls32)  
                      |                      ( "::" ~ ( 'h16 ~ ":"  ).rep(5,5) ~ 'ls32)
                      | (               'h16 ~ "::" ~ ( 'h16 ~ ":" ).rep(4,4) ~ 'ls32)
                      | (( ( 'h16 ~ ":" ).rep(0,1) ~ 'h16 ).? ~ "::" ~ ( 'h16 ~ ":" ).rep(3,3) ~ 'ls32) 
                      | (( ( 'h16 ~ ":" ).rep(0,2) ~ 'h16 ).? ~ "::" ~ ( 'h16 ~ ":"  ).rep(2,2) ~ 'ls32)
                      | (( ( 'h16 ~ ":" ).rep(0,3) ~ 'h16 ).? ~ "::" ~ 'h16 ~ ":" ~ 'ls32)
                      | (( ( 'h16 ~ ":" ).rep(0,4) ~ 'h16 ).? ~ "::" ~  'ls32)
                      | (( ( 'h16 ~ ":" ).rep(0,5) ~ 'h16 ).? ~ "::" ~  'h16)
                      | (( ( 'h16 ~ ":" ).rep(0,6) ~ 'h16 ).? ~ "::"),

   'h16             := 'hexdig.rep(4,4) ,
   'ls32            := ( 'h16 ~ ":" ~ 'h16 ) | 'ipv4address,
   'ipv4address     := 'decoctet ~ "." ~ 'decoctet ~ "." ~ 'decoctet ~ "." ~ 'decoctet,


   'decoctet        := 'digit                   
                      | ("[1-9]".regex ~ 'digit)         
                      | ("1" ~ 'digit.rep(2,2))           
                      | ("2" ~ ("0"|"1"|"2"|"3"|"4") ~ 'digit)     
                      | ("25" ~ ("0"|"1"|"2"|"3"|"4"|"5")) ,         

   'regname         := ( 'unreserved | 'pctencoded | 'subdelims ).rep,

   


   //'path            := 'pathabempty | 'pathabsolute | 'pathnoscheme | 'pathrootless | 'pathempty,      
   'pathbase        := 'pathabempty | 'pathabsolute | 'pathempty,
   'pathabempty     := ( "/" ~ 'segment ).rep, 
   'pathabsolute    := "/" ~ ( 'segmentnz ~ ( "/" ~ 'segment ).rep ).?,
   'pathnoscheme    := 'segmentnznc ~ ( "/" ~  'segment ).rep,
   'pathrootless    := 'segmentnz ~ ( "/" ~ 'segment ).rep,
   'pathempty       := "", 

   'segment         := 'pchar.rep, 
   'segmentnz       := 'pchar.rep(1,1),
   'segmentnznc     := ( 'unreserved | 'pctencoded | 'subdelims | "@" ).rep(1,1),
                 
   'pchar           := 'unreserved | 'pctencoded | 'subdelims | ":" | "@",

   'query           := ( 'pchar | "/" | "?" ).rep,

   'fragment        := ( 'pchar | "/" | "?" ).rep,

   'pctencoded      := "%" ~ 'hexdig ~ 'hexdig,

   'unreserved      := 'alpha | 'digit | "-" | "." | "_" | "~",
   //'reserved        := 'gendelims | 'subdelims, //TODO
   //'gendelims       := ":" | "/" | "?" | "#" | "[" | "]" | "@",
   'subdelims       := "!" | "$" | "&" | "'" | "(" | ")" | "*" | "+" | "," | ";" | "=",

	 'digit            := "[0-9]".regex,
	 'alpha            := "[a-zA-Z]".regex,
	 'hexdig           := 'digit |"[a-f]".regex

)