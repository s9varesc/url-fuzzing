#include "url/third_party/mozilla/url_parse.h"

#include <stddef.h>

#include "base/stl_util.h"
#include "testing/gtest/include/gtest/gtest.h"
#include "url/third_party/mozilla/url_parse.h"
#include "url/url_util.h"


namespace url {
namespace {

// describes the structure of inputs
// following url_parse_unittest.cc:47-58
// single struct to ease generation
struct URLParseCase {
  //TODO might need to include inner_... fields (see url_parse_unittest.cc:81-86)
  const char* input;

  const char* scheme;
  const char* username;
  const char* password;
  const char* host;
  int port;
  const char* path;
  const char* query;
  const char* ref;
};

// verification methods (url_parse_unittest.cc:92-117)
bool ComponentMatches(const char* input,
                      const char* reference,
                      const Component& component) {
  // If the component is nonexistent (length == -1), it should begin at 0.
  EXPECT_TRUE(component.len >= 0 || component.len == -1);

  // Begin should be valid.
  EXPECT_LE(0, component.begin);

  // A NULL reference means the component should be nonexistent.
  if (!reference)
    return component.len == -1;
  if (component.len < 0)
    return false;  // Reference is not NULL but we don't have anything

  if (strlen(reference) != static_cast<size_t>(component.len))
    return false;  // Lengths don't match

  // Now check the actual characters.
  return strncmp(reference, &input[component.begin], component.len) == 0;
}

/*void ExpectInvalidComponent(const Component& component) {
  EXPECT_EQ(0, component.begin);
  EXPECT_EQ(-1, component.len);
}*/

//Test inputs
static URLParseCase parse_cases[]={{"/%2e?U+01BE",NULL,NULL,NULL,NULL,-1,"/%2e","U+01BE",NULL},
{"ws://(@254.103.6.252","ws","(",NULL,"254.103.6.252",-1,NULL,NULL,NULL},
{"//[cbad:55fb::d89f]",NULL,NULL,NULL,"[cbad:55fb::d89f]",-1,NULL,NULL,NULL},
{"//[::aa8f:f9da:9fb3:255.253.8.32]",NULL,NULL,NULL,"[::aa8f:f9da:9fb3:255.253.8.32]",-1,NULL,NULL,NULL},
{"U+FEFC:",NULL,NULL,NULL,NULL,-1,"U+FEFC:",NULL,NULL},
{"/%2e?U+FDB7",NULL,NULL,NULL,NULL,-1,"/%2e","U+FDB7",NULL},
{"http://254.228.89.250","http",NULL,NULL,"254.228.89.250",-1,NULL,NULL,NULL},
{"//%36%13",NULL,NULL,NULL,"%36%13",-1,NULL,NULL,NULL},
{"?U+10FFC1",NULL,NULL,NULL,NULL,-1,NULL,"U+10FFC1",NULL},
{"wss://,@_=__)==","wss",",",NULL,"_=__)==",-1,NULL,NULL,NULL},
{"../_%24:M:?(",NULL,NULL,NULL,NULL,-1,"../_%24:M:","(",NULL},
{"\r",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"file://7.168.130.66%2e%2eR:/","file",NULL,NULL,"7.168.130.66",-1,"%2e%2eR:/",NULL,NULL},
{"\r",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"//[::8a89:be4a:825f:bfcf:e3df:252.236.224.227]",NULL,NULL,NULL,"[::8a89:be4a:825f:bfcf:e3df:252.236.224.227]",-1,NULL,NULL,NULL},
{"//[bbce:adbc::81.254.62.254]",NULL,NULL,NULL,"[bbce:adbc::81.254.62.254]",-1,NULL,NULL,NULL},
{"//@254.148.105.214",NULL,"",NULL,"254.148.105.214",-1,NULL,NULL,NULL},
{"https://[1fb3::b5da:99.31.57.219]:/.","https",NULL,NULL,"[1fb3::b5da:99.31.57.219]",,"/.",NULL,NULL},
{"http://170.96.27.173","http",NULL,NULL,"170.96.27.173",-1,NULL,NULL,NULL},
{"ws://[ddce:40da::1dd8]","ws",NULL,NULL,"[ddce:40da::1dd8]",-1,NULL,NULL,NULL},
{"http://[facf:638c::]","http",NULL,NULL,"[facf:638c::]",-1,NULL,NULL,NULL},
{"//[eeee::ae5b:f00e]",NULL,NULL,NULL,"[eeee::ae5b:f00e]",-1,NULL,NULL,NULL},
{"\r",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"file://","file",NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"\t\t\n\tfile://211.254.54.203./../R|?U+FD0F%fa%2b%70U+00AB~%94-U+CCDD%63","file",NULL,NULL,"211.254.54.203",-1,"./../R|","U+FD0F%fa%2b%70U+00AB~%94-U+CCDD%63",NULL},
{"//[bf5d::dd0a:ad8c:07c3]",NULL,NULL,NULL,"[bf5d::dd0a:ad8c:07c3]",-1,NULL,NULL,NULL},
{"//%bc",NULL,NULL,NULL,"%bc",-1,NULL,NULL,NULL},
{"ftp://'","ftp",NULL,NULL,"'",-1,NULL,NULL,NULL},
{"?U+D6DB",NULL,NULL,NULL,NULL,-1,"","U+D6DB",NULL},
{"\r",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"file:///U+FDBF","file",NULL,NULL,NULL,-1,"/U+FDBF",NULL,NULL},
{"/%7c%e3#~%b8%f7%f7%8b",NULL,NULL,NULL,NULL,-1,"/%7c%e3#~%b8%f7%f7%8b",NULL,NULL},
{"http://[::9936:d077:5cad:6411:9a44:d974:df2b]","http",NULL,NULL,"[::9936:d077:5cad:6411:9a44:d974:df2b]",-1,NULL,NULL,NULL},
{"R-:.%2e:#%a3","r-",NULL,NULL,NULL,-1,".%2e:",NULL,"%a3"},
{".J|",NULL,NULL,NULL,NULL,-1,".J|",NULL,NULL},
{"ftp://[::e87b:bfca:32d8:faf7]","ftp",NULL,NULL,"[::e87b:bfca:32d8:faf7]",-1,NULL,NULL,NULL},
{"E+://%da*:*@85.250.7.38:199/..","e+","%da*","*","85.250.7.38",199,"/..",NULL,NULL},
{"?P",NULL,NULL,NULL,NULL,-1,NULL,"P",NULL},
{"C:","c",NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"RG://","rg",NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"//[3fe1::89d1:b4f8:be5d:600f:2.9.9.9]",NULL,NULL,NULL,"[3fe1::89d1:b4f8:be5d:600f:2.9.9.9]",-1,NULL,NULL,NULL},
{"//?U+FDFA",NULL,NULL,NULL,NULL,-1,NULL,"U+FDFA",NULL},
{"/./%54/%59/%2e/?U+FA68",NULL,NULL,NULL,NULL,-1,"/./%54/%59/%2e/","U+FA68",NULL},
{"//[bfad:46b4::]",NULL,NULL,NULL,"[bfad:46b4::]",-1,NULL,NULL,NULL},
{"/../%2e/.%2e/?&",NULL,NULL,NULL,NULL,-1,"/../%2e/.%2e/","&",NULL},
{"//%74:",NULL,NULL,NULL,"%74",,NULL,NULL,NULL},
{"\t",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"https://)@,P%cd%d0e~&","https",")",NULL,",p%cd%d0e~&",-1,NULL,NULL,NULL},
{" ?U+48AD",NULL,NULL,NULL,NULL,-1,NULL,"U+48AD",NULL},
{"http://241.6.191.19","http",NULL,NULL,"241.6.191.19",-1,NULL,NULL,NULL},
{"\n?_",NULL,NULL,NULL,NULL,-1,NULL,"_",NULL},
{"//[42bf::81d3:1bfb:9fcf:cadc:253.98.7.250]",NULL,NULL,NULL,"[42bf::81d3:1bfb:9fcf:cadc:253.98.7.250]",-1,NULL,NULL,NULL},
{"//[cfbf:e413::adcd:252.223.250.231]",NULL,NULL,NULL,"[cfbf:e413::adcd:252.223.250.231]",-1,NULL,NULL,NULL},
{"file://~(]%25G|\\","file",NULL,NULL,"~",-1,"(]%25G|\\",NULL,NULL},
{" ?+",NULL,NULL,NULL,NULL,-1,NULL,"+",NULL},
{"%2e.Y|\\",NULL,NULL,NULL,NULL,-1,"%2e.Y|\\",NULL,NULL},
{"wss://*","wss",NULL,NULL,"*",-1,NULL,NULL,NULL},
{"http://77.121.243.201","http",NULL,NULL,"77.121.243.201",-1,NULL,NULL,NULL},
{"\r?!",NULL,NULL,NULL,NULL,-1,NULL,"!",NULL},
{"ws://[0627::1ceb:2eb6:252.255.236.74]","ws",NULL,NULL,"[0627::1ceb:2eb6:252.255.236.74]",-1,NULL,NULL,NULL},
{".%2e/.%2e:",NULL,NULL,NULL,NULL,-1,".%2e/.%2e:",NULL,NULL},
{"ws://[::69fe:d152:71.2.251.250]:9","ws",NULL,NULL,"[::69fe:d152:71.2.251.250]",9,NULL,NULL,NULL},
{" ",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"//?U+FB0D",NULL,NULL,NULL,NULL,-1,NULL,"U+FB0D",NULL},
{"%2e.:?U+10E2AB",NULL,NULL,NULL,NULL,-1,"%2e.:","U+10E2AB",NULL},
{"//[8bc6:b542:d4ff:98cb:d5bd:aad2:f3ec:a3a0]",NULL,NULL,NULL,"[8bc6:b542:d4ff:98cb:d5bd:aad2:f3ec:a3a0]",-1,NULL,NULL,NULL},
{"//[bbde:b8da::4ce4:cf5e:6.36.9.241]",NULL,NULL,NULL,"[bbde:b8da::4ce4:cf5e:6.36.9.241]",-1,NULL,NULL,NULL},
{"wss://(","wss",NULL,NULL,"(",-1,NULL,NULL,NULL},
{"wss://'@25.232.119.255","wss","'",NULL,"25.232.119.255",-1,NULL,NULL,NULL},
{"ftp://*@135.33.250.11","ftp","*",NULL,"135.33.250.11",-1,NULL,NULL,NULL},
{"//?U+00A0",NULL,NULL,NULL,NULL,-1,NULL,"U+00A0",NULL},
{"https://[::983c:1.252.252.252]","https",NULL,NULL,"[::983c:1.252.252.252]",-1,NULL,NULL,NULL},
{"https://-","https",NULL,NULL,"-",-1,NULL,NULL,NULL},
{"wss://+","wss",NULL,NULL,"+",-1,NULL,NULL,NULL},
{"https://[aced:f5cf::cac0:6cad:c59c:5.253.201.255]","https",NULL,NULL,"[aced:f5cf::cac0:6cad:c59c:5.253.201.255]",-1,NULL,NULL,NULL},
{"http://&@%28_%01*$%caZ","http","&",NULL,"%28_%01*$%caz",-1,NULL,NULL,NULL},
{"/%ac~%ba@U+FDCE:%bf%b1%39/%2e/%2e/?U+10F77A",NULL,NULL,NULL,NULL,-1,"/%ac~%ba@U+FDCE:%bf%b1%39/%2e/%2e/","U+10F77A",NULL},
{"https://2.247.192.5","https",NULL,NULL,"2.247.192.5",-1,NULL,NULL,NULL},
{"wss://255.3.0.252","wss",NULL,NULL,"255.3.0.252",-1,NULL,NULL,NULL},
{"./.%2e/:?U+F9B0",NULL,NULL,NULL,NULL,-1,"./.%2e/:","U+F9B0",NULL},
{"//:;:-:%edA%0a%71_@189.250.254.154:/%2e?,",NULL,"",";:-:%edA%0a%71_","189.250.254.154",,"/%2e",",",NULL},
{"ftp://[bdda:87f5::201.217.252.253]","ftp",NULL,NULL,"[bdda:87f5::201.217.252.253]",-1,NULL,NULL,NULL},
{"%2e.:?;",NULL,NULL,NULL,NULL,-1,"%2e.:",";",NULL},
{"http://[de5d::]","http",NULL,NULL,"[de5d::]",-1,NULL,NULL,NULL},
{"?U+10FF2A",NULL,NULL,NULL,NULL,-1,"","U+10FF2A",NULL},
{"wss://199.250.251.81","wss",NULL,NULL,"199.250.251.81",-1,NULL,NULL,NULL},
{"ws://%0f@[::ccda:814d:a44a:231.221.8.224]","ws","%0f",NULL,"[::ccda:814d:a44a:231.221.8.224]",-1,NULL,NULL,NULL},
{"\n",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"ws://;","ws",NULL,NULL,";",-1,NULL,NULL,NULL},
{"?U+FDAA",NULL,NULL,NULL,NULL,-1,NULL,"U+FDAA",NULL},
{"",NULL,NULL,NULL,NULL,-1,"",NULL,NULL},
{"//:?U+FE42",NULL,NULL,NULL,":",-1,NULL,"U+FE42",NULL},
{"ftp://[::48.253.254.200]","ftp",NULL,NULL,"[::48.253.254.200]",-1,NULL,NULL,NULL},
{"\n",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"//",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"wss://[deff:ff97::3b7c:2f5f:233.9.233.230]","wss",NULL,NULL,"[deff:ff97::3b7c:2f5f:233.9.233.230]",-1,NULL,NULL,NULL},
{"\t",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"ftp://!","ftp",NULL,NULL,"!",-1,NULL,NULL,NULL},
{"//[dcaa:dc20::9afa:fd6f:cdda:e9a3:b0c8]",NULL,NULL,NULL,"[dcaa:dc20::9afa:fd6f:cdda:e9a3:b0c8]",-1,NULL,NULL,NULL},
{"G:\n?","g",NULL,NULL,NULL,-1,NULL,"",NULL},
{"/%76-Y:#%8c%2a%4d?U+0C8E",NULL,NULL,NULL,NULL,-1,"/%76-Y:#%8c%2a%4d","U+0C8E",NULL},
{"http://$@125.152.4.55","http","$",NULL,"125.152.4.55",-1,NULL,NULL,NULL},
{"#U+A3FD",NULL,NULL,NULL,NULL,-1,"",NULL,"U+A3FD"},
{"//[e7be::ec2a]",NULL,NULL,NULL,"[e7be::ec2a]",-1,NULL,NULL,NULL},
{"%a9/.%2eO|/?'",NULL,NULL,NULL,NULL,-1,"%a9/.%2eO|/","'",NULL},
{"wss://.@213.220.147.238","wss",".",NULL,"213.220.147.238",-1,NULL,NULL,NULL},
{"http://","http",NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"wss://=","wss",NULL,NULL,"=",-1,NULL,NULL,NULL},
{"http://@_","http","",NULL,"_",-1,NULL,NULL,NULL},
{"\n?=",NULL,NULL,NULL,NULL,-1,NULL,"=",NULL},
{"https://!@&%f4%d9)%73","https","!",NULL,"&%f4%d9)%73",-1,NULL,NULL,NULL},
{"///%2e%2e/?U+FDF6",NULL,NULL,NULL,NULL,-1,"/%2e%2e/","U+FDF6",NULL},
{"ws://155.251.85.194","ws",NULL,NULL,"155.251.85.194",-1,NULL,NULL,NULL},
{"https://[::a002:5850:231.25.3.1]","https",NULL,NULL,"[::a002:5850:231.25.3.1]",-1,NULL,NULL,NULL},
{"//?U+BE4C",NULL,NULL,NULL,NULL,-1,NULL,"U+BE4C",NULL},
{"",NULL,NULL,NULL,NULL,-1,"",NULL,NULL},
{"wss://[fcdc:24b6::c0d0:225.9.214.222]","wss",NULL,NULL,"[fcdc:24b6::c0d0:225.9.214.222]",-1,NULL,NULL,NULL},
{"F.: ","f.",NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"/%7c*U+D4F5U+D5CD/.%2e?$",NULL,NULL,NULL,NULL,-1,"/%7c*U+D4F5U+D5CD/.%2e","$",NULL},
{"//[04ad::ac3e:a2d0:7a3a:647f:114a:a210]?U+FCB0",NULL,NULL,NULL,"[04ad::ac3e:a2d0:7a3a:647f:114a:a210]",-1,NULL,"U+FCB0",NULL},
{" ",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"http://251.4.153.84","http",NULL,NULL,"251.4.153.84",-1,NULL,NULL,NULL},
{"\t?U+108BB2",NULL,NULL,NULL,NULL,-1,NULL,"U+108BB2",NULL},
{"http://250.203.255.254","http",NULL,NULL,"250.203.255.254",-1,NULL,NULL,NULL},
{"wss://+@.","wss","+",NULL,".",-1,NULL,NULL,NULL},
{"\t",NULL,NULL,NULL,NULL,-1,NULL,NULL,NULL},
{"//[::eaba:f3ba:e558:323c]",NULL,NULL,NULL,"[::eaba:f3ba:e558:323c]",-1,NULL,NULL,NULL},
{"https://132.190.253.254","https",NULL,NULL,"132.190.253.254",-1,NULL,NULL,NULL},
{"/./%e6%a0%58-//?U+10FFFA",NULL,NULL,NULL,NULL,-1,"/./%e6%a0%58-//","U+10FFFA",NULL},
{"//?U+E7B4",NULL,NULL,NULL,NULL,-1,NULL,"U+E7B4",NULL},
{"https://[cc00:aa8f:b12d:f9ce:9fb6:577a:d2f7:ea48]","https",NULL,NULL,"[cc00:aa8f:b12d:f9ce:9fb6:577a:d2f7:ea48]",-1,NULL,NULL,NULL},
{"wss://=@20.65.200.57","wss","=",NULL,"20.65.200.57",-1,NULL,NULL,NULL},
{"/%2e/.?U+10FEFC",NULL,NULL,NULL,NULL,-1,"/%2e/.","U+10FEFC",NULL},
{"//186.223.254.255#-%00@%b9T:",NULL,NULL,NULL,"186.223.254.255",-1,"#-%00@%b9T:",NULL,NULL},
{"http://~@154.12.243.158","http","~",NULL,"154.12.243.158",-1,NULL,NULL,NULL},
{"/%2e%2e/#~#?/?U+10FFF7",NULL,NULL,NULL,NULL,-1,"/%2e%2e/#~#?/","U+10FFF7",NULL},
{" ?)",NULL,NULL,NULL,NULL,-1,NULL,")",NULL},
{"http://2.179.209.251","http",NULL,NULL,"2.179.209.251",-1,NULL,NULL,NULL},
{"http://$","http",NULL,NULL,"$",-1,NULL,NULL,NULL},
{"ftp://%cd","ftp",NULL,NULL,"%cd",-1,NULL,NULL,NULL},
{"//.[%28?%68--%d9:9",NULL,NULL,NULL,".[%28?%68--%d9",9,NULL,NULL,NULL},
{"/%2e./%af?U+FFB3",NULL,NULL,NULL,NULL,-1,"/%2e./%af","U+FFB3",NULL},
{"https://[10c6::894c:12dd:4980]","https",NULL,NULL,"[10c6::894c:12dd:4980]",-1,NULL,NULL,NULL},
{"https://[59a1:6a9f:01ae:1f07:8ab8:d0d2:3.252.251.9]","https",NULL,NULL,"[59a1:6a9f:01ae:1f07:8ab8:d0d2:3.252.251.9]",-1,NULL,NULL,NULL},
{"ws://[::1abd:8fb6:df30:cfb8:a187]","ws",NULL,NULL,"[::1abd:8fb6:df30:cfb8:a187]",-1,NULL,NULL,NULL},
{"file://@%cd%a5*%8d%a1%2c","file","",NULL,"%cd%a5*%8d%a1%2c",-1,NULL,NULL,NULL},
{"ws://[debf::029e:36a2:68e4:cedf:6b1f:cc4b]","ws",NULL,NULL,"[debf::029e:36a2:68e4:cedf:6b1f:cc4b]",-1,NULL,NULL,NULL}};
//test execution
TEST(URLParser, Parsing){
	for (size_t i = 0; i < base::size(parse_cases); i++) {
		Parsed parsed;
		Component scheme;
		int len =static_cast<int>(strlen(parse_cases[i].input));

		if(!ExtractScheme(parse_cases[i].input, len, &scheme)){
			//no scheme found
			//TODO decide what to do
			return;
		} 
		else{
			std::string schemeString(parse_cases[i].input + scheme.begin, parse_cases[i].input+scheme.end());
			if( IsStandard(parse_cases[i].input,scheme)){
				if (schemeString==kFileScheme){
					ParseFileURL(parse_cases[i].input,len, &parsed);
				}
				else {
					if (schemeString==kFileSystemScheme){
						ParseFileSystemURL(parse_cases[i].input, len, &parsed);
					}
					else{
						//standard schemes, see url_util.cc
						ParseStandardURL(parse_cases[i].input, len, &parsed);
					}
				}
			}
			else {
				//non-standard scheme
				ParsePathURL(parse_cases[i].input, len,false, &parsed);
			}
		}

		//verify components
		//TODO might have to use specialized methods to correctly include inner_...
		const char* url = parse_cases[i].input;
		int port = ParsePort(url, parsed.port);

	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].scheme, parsed.scheme));
	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].username, parsed.username));
	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].password, parsed.password));
	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].host, parsed.host));
	    EXPECT_EQ(parse_cases[i].port, port);
	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].path, parsed.path));
	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].query, parsed.query));
	    EXPECT_TRUE(ComponentMatches(url, parse_cases[i].ref, parsed.ref));

	}

}

}
}
