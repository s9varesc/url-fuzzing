# URL Components as used in Chromium
These structures are used in the existin URL parser tests [url_parse_unittest.cc](https://source.chromium.org/chromium/chromium/src/+/master:url/url_parse_unittest.cc?originalUrl=https:%2F%2Fcs.chromium.org%2Fchromium%2Fsrc%2Furl%2Furl_parse_unittest.cc).

## Basic URL representation
'''struct URLParseCase {
  const char* input;

  const char* scheme;
  const char* username;
  const char* password;
  const char* host;
  int port;
  const char* path;
  const char* query;
  const char* ref;
};'''

## Special Representation for Filesystem URLs
'''struct FileSystemURLParseCase {
  const char* input;

  const char* inner_scheme;
  const char* inner_username;
  const char* inner_password;
  const char* inner_host;
  int inner_port;
  const char* inner_path;
  const char* path;
  const char* query;
  const char* ref;
};'''

## Special Representation for Path URLs
'''struct PathURLParseCase {
  const char* input;

  const char* scheme;
  const char* path;
};'''
