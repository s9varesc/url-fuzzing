/* -*- indent-tabs-mode: nil; js-indent-level: 2 -*- */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

"use strict";

var gIoService = Cc["@mozilla.org/network/io-service;1"].getService(
  Ci.nsIIOService
);

// Run by: cd objdir;  make -C netwerk/test/ xpcshell-tests
// or: cd objdir; make SOLO_FILE="test_URIs.js" -C netwerk/test/ check-one

// See also test_URIs2.js.

// Relevant RFCs: 1738, 1808, 2396, 3986 (newer than the code)
// http://greenbytes.de/tech/webdav/rfc3986.html#rfc.section.5.4
// http://greenbytes.de/tech/tc/uris/

// TEST DATA
// ---------
var gTests = [
  {
    spec: "about:blank",
    scheme: "about",
    prePath: "about:",
    pathQueryRef: "blank",
    ref: "",
    nsIURL: false,
    nsINestedURI: true,
    immutable: true,
  },
  {
    spec: "about:foobar",
    scheme: "about",
    prePath: "about:",
    pathQueryRef: "foobar",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
    immutable: true,
  },
  {
    spec: "chrome://foobar/somedir/somefile.xml",
    scheme: "chrome",
    prePath: "chrome://foobar",
    pathQueryRef: "/somedir/somefile.xml",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
    immutable: true,
  },
  {
    spec: "data:text/html;charset=utf-8,<html></html>",
    scheme: "data",
    prePath: "data:",
    pathQueryRef: "text/html;charset=utf-8,<html></html>",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "data:text/html;charset=utf-8,<html>\r\n\t</html>",
    scheme: "data",
    prePath: "data:",
    pathQueryRef: "text/html;charset=utf-8,<html></html>",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "data:text/plain,hello%20world",
    scheme: "data",
    prePath: "data:",
    pathQueryRef: "text/plain,hello%20world",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "data:text/plain,hello world",
    scheme: "data",
    prePath: "data:",
    pathQueryRef: "text/plain,hello world",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "file:///dir/afile",
    scheme: "data",
    prePath: "data:",
    pathQueryRef: "text/plain,2",
    ref: "",
    relativeURI: "data:te\nxt/plain,2",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "file://",
    scheme: "file",
    prePath: "file://",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "file:///",
    scheme: "file",
    prePath: "file://",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "file:///myFile.html",
    scheme: "file",
    prePath: "file://",
    pathQueryRef: "/myFile.html",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "file:///dir/afile",
    scheme: "file",
    prePath: "file://",
    pathQueryRef: "/dir/data/text/plain,2",
    ref: "",
    relativeURI: "data/text/plain,2",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "file:///dir/dir2/",
    scheme: "file",
    prePath: "file://",
    pathQueryRef: "/dir/dir2/data/text/plain,2",
    ref: "",
    relativeURI: "data/text/plain,2",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "ftp://ftp.mozilla.org/pub/mozilla.org/README",
    scheme: "ftp",
    prePath: "ftp://ftp.mozilla.org",
    pathQueryRef: "/pub/mozilla.org/README",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "ftp://foo:bar@ftp.mozilla.org:100/pub/mozilla.org/README",
    scheme: "ftp",
    prePath: "ftp://foo:bar@ftp.mozilla.org:100",
    port: 100,
    username: "foo",
    password: "bar",
    pathQueryRef: "/pub/mozilla.org/README",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "ftp://foo:@ftp.mozilla.org:100/pub/mozilla.org/README",
    scheme: "ftp",
    prePath: "ftp://foo@ftp.mozilla.org:100",
    port: 100,
    username: "foo",
    password: "",
    pathQueryRef: "/pub/mozilla.org/README",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  //Bug 706249
  {
    spec: "gopher://mozilla.org/",
    scheme: "gopher",
    prePath: "gopher:",
    pathQueryRef: "//mozilla.org/",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "http://www.example.com/",
    scheme: "http",
    prePath: "http://www.example.com",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://www.exa\nmple.com/",
    scheme: "http",
    prePath: "http://www.example.com",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://10.32.4.239/",
    scheme: "http",
    prePath: "http://10.32.4.239",
    host: "10.32.4.239",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://[::192.9.5.5]/ipng",
    scheme: "http",
    prePath: "http://[::c009:505]",
    host: "::c009:505",
    pathQueryRef: "/ipng",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]:8888/index.html",
    scheme: "http",
    prePath: "http://[fedc:ba98:7654:3210:fedc:ba98:7654:3210]:8888",
    host: "fedc:ba98:7654:3210:fedc:ba98:7654:3210",
    port: 8888,
    pathQueryRef: "/index.html",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://bar:foo@www.mozilla.org:8080/pub/mozilla.org/README.html",
    scheme: "http",
    prePath: "http://bar:foo@www.mozilla.org:8080",
    port: 8080,
    username: "bar",
    password: "foo",
    host: "www.mozilla.org",
    pathQueryRef: "/pub/mozilla.org/README.html",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "jar:resource://!/",
    scheme: "jar",
    prePath: "jar:",
    pathQueryRef: "resource:///!/",
    ref: "",
    nsIURL: true,
    nsINestedURI: true,
  },
  {
    spec: "jar:resource://gre/chrome.toolkit.jar!/",
    scheme: "jar",
    prePath: "jar:",
    pathQueryRef: "resource://gre/chrome.toolkit.jar!/",
    ref: "",
    nsIURL: true,
    nsINestedURI: true,
  },
  {
    spec: "mailto:webmaster@mozilla.com",
    scheme: "mailto",
    prePath: "mailto:",
    pathQueryRef: "webmaster@mozilla.com",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "javascript:new Date()",
    scheme: "javascript",
    prePath: "javascript:",
    pathQueryRef: "new Date()",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "blob:123456",
    scheme: "blob",
    prePath: "blob:",
    pathQueryRef: "123456",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
    immutable: true,
  },
  {
    spec: "place:sort=8&maxResults=10",
    scheme: "place",
    prePath: "place:",
    pathQueryRef: "sort=8&maxResults=10",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "resource://gre/",
    scheme: "resource",
    prePath: "resource://gre",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "resource://gre/components/",
    scheme: "resource",
    prePath: "resource://gre",
    pathQueryRef: "/components/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },

  // Adding more? Consider adding to test_URIs2.js instead, so that neither
  // test runs for *too* long, risking timeouts on slow platforms.
];

var gHashSuffixes = ["#", "#myRef", "#myRef?a=b", "#myRef#", "#myRef#x:yz"];

// TEST HELPER FUNCTIONS
// ---------------------
function do_info(text, stack) {
  if (!stack) {
    stack = Components.stack.caller;
  }

  dump(
    "\n" +
      "TEST-INFO | " +
      stack.filename +
      " | [" +
      stack.name +
      " : " +
      stack.lineNumber +
      "] " +
      text +
      "\n"
  );
}



// Checks that the given property on aURI matches the corresponding property
// in the test bundle (or matches some function of that corresponding property,
// if aTestFunctor is passed in).
function do_check_property(aTest, aURI, aPropertyName, aTestFunctor) {

  if (aTest[aPropertyName]) {
    var comp;
    var expectedVal = aTestFunctor
      ? aTestFunctor(aTest[aPropertyName])
      : aTest[aPropertyName];
    
    try {
      comp=aURI[aPropertyName];
    } catch(e) {
      var url=aTest.relativeURI ? (aTest.spec +"<"+aTest.relativeURI) : aTest.spec;
      dump("\n{\"url\":\""+url+"\", \"error\":{\"component\":\""+aPropertyName+"\", \"expected\":\""+expectedVal +"\", \"actual\":\""+e.name+" " + e.result+"\"}}"); 
      do_throw(e.result);
    }

    if (aURI[aPropertyName] != expectedVal){ //needed to display custom format, adding assertion message spams output
      var url=aTest.relativeURI ? (aTest.spec +"<"+aTest.relativeURI) : aTest.spec;
      dump("\n{\"url\":\""+ url+"\", \"error\":{\"component\":\""+aPropertyName+"\", \"expected\":\""+expectedVal +"\", \"actual\":\""+aURI[aPropertyName]+"\"}}"); 
      do_throw("componens do not match");
    }

    Assert.equal(aURI[aPropertyName], expectedVal);

  }
}

// Test that a given URI parses correctly into its various components.
function do_test_uri_basic(aTest) {
  var URI;

  try {
    URI = gIoService.newURI(aTest.spec);
  } catch (e) {
    var url=aTest.relativeURI ? (aTest.spec +"<"+aTest.relativeURI) : aTest.spec;
    //do_info("Caught error on parse of" + aTest.spec + " Error: "+e.name+" " + e.result);
    dump("\n{\"url\":\""+ url+"\", \"exception\":\""+e.name+" "+e.result+"\"}");
    if (aTest.fail) {
      Assert.equal(e.result, aTest.result);
      return;
    }
    do_throw(e.result);
    
  }

  if (aTest.relativeURI) {
    var relURI;

    try {
      relURI = gIoService.newURI(aTest.relativeURI, null, URI);
    } catch (e) {
      /*do_info(
        "Caught error on Relative parse of " +
          aTest.spec +
          " + " +
          aTest.relativeURI +
          " Error: " +
          e.result
      );*/
      var url=aTest.spec +"<"+aTest.relativeURI;
      //do_info("Caught error on parse of" + aTest.spec + " Error: "+e.name+" " + e.result);
     dump("\n{\"url\":\""+ url+"\", \"exception\":\""+e.name+" "+e.result+"\"}");

      if (aTest.relativeFail) {
        Assert.equal(e.result, aTest.relativeFail);
        return;
      }
      
      do_throw(e.result);
      
    }
    URI=relURI;
  }

  
  // Check the various components   
  try {
      do_check_property(aTest, URI, "scheme");
      do_check_property(aTest, URI, "query");
      do_check_property(aTest, URI, "ref");
      do_check_property(aTest, URI, "port");
      do_check_property(aTest, URI, "username");
      do_check_property(aTest, URI, "password");
      do_check_property(aTest, URI, "host");
      do_check_property(aTest, URI, "specIgnoringRef");
      do_check_property(aTest, URI, "hasRef");
      do_check_property(aTest, URI, "prePath");
      do_check_property(aTest, URI, "pathQueryRef");
  } catch (e) {
    dump("caught exception from components");
    return;
  }
}





// TEST MAIN FUNCTION
// ------------------
//function run_test() {
add_task(function mainTest(){

  gTests.forEach(function(aTest) {
    // Check basic URI functionality
    try {
      do_test_uri_basic(aTest);
    } catch (e) {
      do_info("caught "+e+" in main");
    }
    
  }
  );
});
