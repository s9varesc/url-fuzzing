/* -*- indent-tabs-mode: nil; js-indent-level: 2 -*- */
"use strict";

var gIoService = Cc["@mozilla.org/network/io-service;1"].getService(
  Ci.nsIIOService
);

// Run by: cd objdir;  make -C netwerk/test/ xpcshell-tests
// or: cd objdir; make SOLO_FILE="test_URIs2.js" -C netwerk/test/ check-one

// This is a clone of test_URIs.js, with a different set of test data in gTests.
// The original test data in test_URIs.js was split between test_URIs and test_URIs2.js
// because test_URIs.js was running for too long on slow platforms, causing
// intermittent timeouts.

// Relevant RFCs: 1738, 1808, 2396, 3986 (newer than the code)
// http://greenbytes.de/tech/webdav/rfc3986.html#rfc.section.5.4
// http://greenbytes.de/tech/tc/uris/

// TEST DATA
// ---------
var gTests = [
  {
    spec: "view-source:about:blank",
    scheme: "view-source",
    prePath: "view-source:",
    pathQueryRef: "about:blank",
    ref: "",
    nsIURL: false,
    nsINestedURI: true,
    immutable: true,
  },
  {
    spec: "view-source:http://www.mozilla.org/",
    scheme: "view-source",
    prePath: "view-source:",
    pathQueryRef: "http://www.mozilla.org/",
    ref: "",
    nsIURL: false,
    nsINestedURI: true,
    immutable: true,
  },
  {
    spec: "x-external:",
    scheme: "x-external",
    prePath: "x-external:",
    pathQueryRef: "",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "x-external:abc",
    scheme: "x-external",
    prePath: "x-external:",
    pathQueryRef: "abc",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "http://www2.example.com/",
    relativeURI: "a/b/c/d",
    scheme: "http",
    prePath: "http://www2.example.com",
    pathQueryRef: "/a/b/c/d",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  // relative URL testcases from http://greenbytes.de/tech/webdav/rfc3986.html#rfc.section.5.4
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g:h",
    scheme: "g",
    prePath: "g:",
    pathQueryRef: "h",
    ref: "",
    nsIURL: false,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "./g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g/",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "/g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "?y",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/d;p?y",
    ref: "", // fix
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g?y",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g?y",
    ref: "", // fix
    specIgnoringRef: "http://a/b/c/g?y",
    hasRef: false,
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "#s",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/d;p?q#s",
    ref: "s", // fix
    specIgnoringRef: "http://a/b/c/d;p?q",
    hasRef: true,
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g#s",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g#s",
    ref: "s",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g?y#s",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g?y#s",
    ref: "s",
    nsIURL: true,
    nsINestedURI: false,
  },
  /*
    Bug xxxxxx - we return a path of b/c/;x
  { spec:    "http://a/b/c/d;p?q",
    relativeURI: ";x",
    scheme:  "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/d;x",
    ref:     "",
    nsIURL:  true, nsINestedURI: false },
  */
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g;x",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g;x",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g;x?y#s",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g;x?y#s",
    ref: "s",
    nsIURL: true,
    nsINestedURI: false,
  },
  /*
    Can't easily specify a relative URI of "" to the test code
  { spec:    "http://a/b/c/d;p?q",
    relativeURI: "",
    scheme:  "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/d",
    ref:     "",
    nsIURL:  true, nsINestedURI: false },
  */
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: ".",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "./",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "..",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../..",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../../",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../../g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },

  // abnormal examples
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../../../g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "../../../../g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },

  // coalesce
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "/./g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "/../g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g.",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g.",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: ".g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/.g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g..",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g..",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "..g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/..g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: ".",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "./../g",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/g",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "./g/.",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g/",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g/./h",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g/h",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g/../h",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/h",
    ref: "", // fix
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g;x=1/./y",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/g;x=1/y",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "http://a/b/c/d;p?q",
    relativeURI: "g;x=1/../y",
    scheme: "http",
    prePath: "http://a",
    pathQueryRef: "/b/c/y",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  // protocol-relative http://tools.ietf.org/html/rfc3986#section-4.2
  {
    spec: "http://www2.example.com/",
    relativeURI: "//www3.example2.com/bar",
    scheme: "http",
    prePath: "http://www3.example2.com",
    pathQueryRef: "/bar",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
  {
    spec: "https://www2.example.com/",
    relativeURI: "//www3.example2.com/bar",
    scheme: "https",
    prePath: "https://www3.example2.com",
    pathQueryRef: "/bar",
    ref: "",
    nsIURL: true,
    nsINestedURI: false,
  },
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
