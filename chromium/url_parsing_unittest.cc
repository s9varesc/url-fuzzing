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

void ExpectInvalidComponent(const Component& component) {
  EXPECT_EQ(0, component.begin);
  EXPECT_EQ(-1, component.len);
}

//Test inputs
//TODO
static URLParseCase parse_cases[]={
	{"http://user:pass@foo:21/bar;par?b#c", "http", "user", "pass",    "foo",       21, "/bar;par","b",          "c"},
    	{"http:foo.com",                        "http", NULL,  NULL,      "foo.com",    -1, NULL,      NULL,        NULL}

};

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
			if( IsStandard(parse_cases[i].input,scheme)){
				if (parse_cases[i].input[0,scheme.end()]==kFileScheme){
					ParseFileURL(parse_cases[i].input,len, &parsed);
				}
				else {
					if (parse_cases[i].input[0,scheme.end()]==kFileSystemScheme){
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
