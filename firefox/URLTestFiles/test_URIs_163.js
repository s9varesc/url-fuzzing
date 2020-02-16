"use strict";	
 var gIoService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);

var gTests = [{path:"/",
pathQueryRef:"/?U+10FFFB",
hasRef:false,
spec:"/?U+10FFFB"}
]

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

// Checks that the URIs satisfy equals(), in both possible orderings.
// Also checks URI.equalsExceptRef(), because equal URIs should also be equal
// when we ignore the ref.
//
// The third argument is optional. If the client passes a third argument
// (e.g. todo_check_true), we'll use that in lieu of ok.
function do_check_uri_eq(aURI1, aURI2, aCheckTrueFunc = ok) {
  do_info("(uri equals check: '" + aURI1.spec + "' == '" + aURI2.spec + "')");
  aCheckTrueFunc(aURI1.equals(aURI2));
  do_info("(uri equals check: '" + aURI2.spec + "' == '" + aURI1.spec + "')");
  aCheckTrueFunc(aURI2.equals(aURI1));

  // (Only take the extra step of testing 'equalsExceptRef' when we expect the
  // URIs to really be equal.  In 'todo' cases, the URIs may or may not be
  // equal when refs are ignored - there's no way of knowing in general.)
  if (aCheckTrueFunc == ok) {
    do_check_uri_eqExceptRef(aURI1, aURI2, aCheckTrueFunc);
  }
}

// Checks that the URIs satisfy equalsExceptRef(), in both possible orderings.
//
// The third argument is optional. If the client passes a third argument
// (e.g. todo_check_true), we'll use that in lieu of ok.
function do_check_uri_eqExceptRef(aURI1, aURI2, aCheckTrueFunc = ok) {
  do_info(
    "(uri equalsExceptRef check: '" + aURI1.spec + "' == '" + aURI2.spec + "')"
  );
  aCheckTrueFunc(aURI1.equalsExceptRef(aURI2));
  do_info(
    "(uri equalsExceptRef check: '" + aURI2.spec + "' == '" + aURI1.spec + "')"
  );
  aCheckTrueFunc(aURI2.equalsExceptRef(aURI1));
}

// Checks that the given property on aURI matches the corresponding property
// in the test bundle (or matches some function of that corresponding property,
// if aTestFunctor is passed in).
function do_check_property(aTest, aURI, aPropertyName, aTestFunctor) {
  if (aTest[aPropertyName]) {
    var expectedVal = aTestFunctor
      ? aTestFunctor(aTest[aPropertyName])
      : aTest[aPropertyName];

    do_info(
      "testing " +
        aPropertyName +
        " of " +
        (aTestFunctor ? "modified '" : "'") +
        aTest.spec +
        "' is '" +
        expectedVal +
        "'"
    );
    Assert.equal(aURI[aPropertyName], expectedVal);
  }
}

// Test that a given URI parses correctly into its various components.
function do_test_uri_basic(aTest) {
  var URI;

  do_info(
    "Basic tests for " +
      aTest.spec +
      " relative URI: " +
      (aTest.relativeURI === undefined ? "(none)" : aTest.relativeURI)
  );

  try {
    URI = NetUtil.newURI(aTest.spec);
  } catch (e) {
    do_info("Caught error on parse of" + aTest.spec + " Error: " + e.result);
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
      do_info(
        "Caught error on Relative parse of " +
          aTest.spec +
          " + " +
          aTest.relativeURI +
          " Error: " +
          e.result
      );
      if (aTest.relativeFail) {
        Assert.equal(e.result, aTest.relativeFail);
        return;
      }
      do_throw(e.result);
    }
    do_info(
      "relURI.pathQueryRef = " +
        relURI.pathQueryRef +
        ", was " +
        URI.pathQueryRef
    );
    URI = relURI;
    do_info("URI.pathQueryRef now = " + URI.pathQueryRef);
  }

  // Sanity-check
  do_info("testing " + aTest.spec + " equals a clone of itself");
  do_check_uri_eq(URI, URI.mutate().finalize());
  do_check_uri_eqExceptRef(
    URI,
    URI.mutate()
      .setRef("")
      .finalize()
  );
  /*do_info("testing " + aTest.spec + " instanceof nsIURL");
  Assert.equal(URI instanceof Ci.nsIURL, aTest.nsIURL);
  do_info("testing " + aTest.spec + " instanceof nsINestedURI");
  Assert.equal(URI instanceof Ci.nsINestedURI, aTest.nsINestedURI);*/

  do_info(
    "testing that " +
      aTest.spec +
      " throws or returns false " +
      "from equals(null)"
  );
  // XXXdholbert At some point it'd probably be worth making this behavior
  // (throwing vs. returning false) consistent across URI implementations.
  var threw = false;
  var isEqualToNull;
  try {
    isEqualToNull = URI.equals(null);
  } catch (e) {
    threw = true;
  }
  Assert.ok(threw || !isEqualToNull);

  // Check the various components
  do_check_property(aTest, URI, "scheme");
  do_check_property(aTest, URI, "prePath");
  do_check_property(aTest, URI, "pathQueryRef");
  do_check_property(aTest, URI, "query");
  do_check_property(aTest, URI, "ref");
  do_check_property(aTest, URI, "port");
  do_check_property(aTest, URI, "username");
  do_check_property(aTest, URI, "password");
  do_check_property(aTest, URI, "host");
  do_check_property(aTest, URI, "specIgnoringRef");
  if ("hasRef" in aTest) {
    do_info("testing hasref: " + aTest.hasRef + " vs " + URI.hasRef);
    Assert.equal(aTest.hasRef, URI.hasRef);
  }
}

// Test that a given URI parses correctly when we add a given ref to the end
function do_test_uri_with_hash_suffix(aTest, aSuffix) {
  do_info("making sure caller is using suffix that starts with '#'");
  Assert.equal(aSuffix[0], "#");

  var origURI = NetUtil.newURI(aTest.spec);
  var testURI;

  if (aTest.relativeURI) {
    try {
      origURI = gIoService.newURI(aTest.relativeURI, null, origURI);
    } catch (e) {
      do_info(
        "Caught error on Relative parse of " +
          aTest.spec +
          " + " +
          aTest.relativeURI +
          " Error: " +
          e.result
      );
      return;
    }
    try {
      testURI = gIoService.newURI(aSuffix, null, origURI);
    } catch (e) {
      do_info(
        "Caught error adding suffix to " +
          aTest.spec +
          " + " +
          aTest.relativeURI +
          ", suffix " +
          aSuffix +
          " Error: " +
          e.result
      );
      return;
    }
  } else {
    testURI = NetUtil.newURI(aTest.spec + aSuffix);
  }

  do_info(
    "testing " +
      aTest.spec +
      " with '" +
      aSuffix +
      "' appended " +
      "equals a clone of itself"
  );
  do_check_uri_eq(testURI, testURI.mutate().finalize());

  do_info(
    "testing " +
      aTest.spec +
      " doesn't equal self with '" +
      aSuffix +
      "' appended"
  );

  Assert.ok(!origURI.equals(testURI));

  do_info(
    "testing " +
      aTest.spec +
      " is equalExceptRef to self with '" +
      aSuffix +
      "' appended"
  );
  do_check_uri_eqExceptRef(origURI, testURI);

  Assert.equal(testURI.hasRef, true);

  if (!origURI.ref) {
    // These tests fail if origURI has a ref
    do_info(
      "testing setRef('') on " +
        testURI.spec +
        " is equal to no-ref version but not equal to ref version"
    );
    var cloneNoRef = testURI
      .mutate()
      .setRef("")
      .finalize(); // we used to clone here.
    do_info("cloneNoRef: " + cloneNoRef.spec + " hasRef: " + cloneNoRef.hasRef);
    do_info("testURI: " + testURI.spec + " hasRef: " + testURI.hasRef);
    do_check_uri_eq(cloneNoRef, origURI);
    Assert.ok(!cloneNoRef.equals(testURI));

    do_info(
      "testing cloneWithNewRef on " +
        testURI.spec +
        " with an empty ref is equal to no-ref version but not equal to ref version"
    );
    var cloneNewRef = testURI
      .mutate()
      .setRef("")
      .finalize();
    do_check_uri_eq(cloneNewRef, origURI);
    do_check_uri_eq(cloneNewRef, cloneNoRef);
    Assert.ok(!cloneNewRef.equals(testURI));

    do_info(
      "testing cloneWithNewRef on " +
        origURI.spec +
        " with the same new ref is equal to ref version and not equal to no-ref version"
    );
    cloneNewRef = origURI
      .mutate()
      .setRef(aSuffix)
      .finalize();
    do_check_uri_eq(cloneNewRef, testURI);
    Assert.ok(cloneNewRef.equals(testURI));
  }

  do_check_property(aTest, testURI, "scheme");
  do_check_property(aTest, testURI, "prePath");
  if (!origURI.ref) {
    // These don't work if it's a ref already because '+' doesn't give the right result
    do_check_property(aTest, testURI, "pathQueryRef", function(aStr) {
      return aStr + aSuffix;
    });
    do_check_property(aTest, testURI, "ref", function(aStr) {
      return aSuffix.substr(1);
    });
  }
}

// Tests various ways of setting & clearing a ref on a URI.
function do_test_mutate_ref(aTest, aSuffix) {
  do_info("making sure caller is using suffix that starts with '#'");
  Assert.equal(aSuffix[0], "#");

  var refURIWithSuffix = NetUtil.newURI(aTest.spec + aSuffix);
  var refURIWithoutSuffix = NetUtil.newURI(aTest.spec);

  var testURI = NetUtil.newURI(aTest.spec);

  // First: Try setting .ref to our suffix
  do_info(
    "testing that setting .ref on " +
      aTest.spec +
      " to '" +
      aSuffix +
      "' does what we expect"
  );
  testURI = testURI
    .mutate()
    .setRef(aSuffix)
    .finalize();
  do_check_uri_eq(testURI, refURIWithSuffix);
  do_check_uri_eqExceptRef(testURI, refURIWithoutSuffix);

  // Now try setting .ref but leave off the initial hash (expect same result)
  var suffixLackingHash = aSuffix.substr(1);
  if (suffixLackingHash) {
    // (skip this our suffix was *just* a #)
    do_info(
      "testing that setting .ref on " +
        aTest.spec +
        " to '" +
        suffixLackingHash +
        "' does what we expect"
    );
    testURI = testURI
      .mutate()
      .setRef(suffixLackingHash)
      .finalize();
    do_check_uri_eq(testURI, refURIWithSuffix);
    do_check_uri_eqExceptRef(testURI, refURIWithoutSuffix);
  }

  // Now, clear .ref (should get us back the original spec)
  do_info(
    "testing that clearing .ref on " + testURI.spec + " does what we expect"
  );
  testURI = testURI
    .mutate()
    .setRef("")
    .finalize();
  do_check_uri_eq(testURI, refURIWithoutSuffix);
  do_check_uri_eqExceptRef(testURI, refURIWithSuffix);

  if (!aTest.relativeURI) {
    // TODO: These tests don't work as-is for relative URIs.

    // Now try setting .spec directly (including suffix) and then clearing .ref
    var specWithSuffix = aTest.spec + aSuffix;
    do_info(
      "testing that setting spec to " +
        specWithSuffix +
        " and then clearing ref does what we expect"
    );

    testURI = testURI
      .mutate()
      .setSpec(specWithSuffix)
      .setRef("")
      .finalize();
    do_check_uri_eq(testURI, refURIWithoutSuffix);
    do_check_uri_eqExceptRef(testURI, refURIWithSuffix);

    // XXX nsIJARURI throws an exception in SetPath(), so skip it for next part.
    if (!(testURI instanceof Ci.nsIJARURI)) {
      // Now try setting .pathQueryRef directly (including suffix) and then clearing .ref
      // (same as above, but with now with .pathQueryRef instead of .spec)
      testURI = NetUtil.newURI(aTest.spec);

      var pathWithSuffix = aTest.pathQueryRef + aSuffix;
      do_info(
        "testing that setting path to " +
          pathWithSuffix +
          " and then clearing ref does what we expect"
      );
      testURI = testURI
        .mutate()
        .setPathQueryRef(pathWithSuffix)
        .setRef("")
        .finalize();
      do_check_uri_eq(testURI, refURIWithoutSuffix);
      do_check_uri_eqExceptRef(testURI, refURIWithSuffix);

      // Also: make sure that clearing .pathQueryRef also clears .ref
      testURI = testURI
        .mutate()
        .setPathQueryRef(pathWithSuffix)
        .finalize();
      do_info(
        "testing that clearing path from " +
          pathWithSuffix +
          " also clears .ref"
      );
      testURI = testURI
        .mutate()
        .setPathQueryRef("")
        .finalize();
      Assert.equal(testURI.ref, "");
    }
  }
}

// Check that changing nested/about URIs works correctly.
function check_nested_mutations() {
  // nsNestedAboutURI
  let uri1 = gIoService.newURI("about:blank#");
  let uri2 = gIoService.newURI("about:blank");
  let uri3 = uri1
    .mutate()
    .setRef("")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setRef("#")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("about:blank?something");
  uri2 = gIoService.newURI("about:blank");
  uri3 = uri1
    .mutate()
    .setQuery("")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setQuery("something")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("about:blank?query#ref");
  uri2 = gIoService.newURI("about:blank");
  uri3 = uri1
    .mutate()
    .setPathQueryRef("blank")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setPathQueryRef("blank?query#ref")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  // nsSimpleNestedURI
  uri1 = gIoService.newURI("view-source:http://example.com/path#");
  uri2 = gIoService.newURI("view-source:http://example.com/path");
  uri3 = uri1
    .mutate()
    .setRef("")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setRef("#")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("view-source:http://example.com/path?something");
  uri2 = gIoService.newURI("view-source:http://example.com/path");
  uri3 = uri1
    .mutate()
    .setQuery("")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setQuery("something")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("view-source:http://example.com/path?query#ref");
  uri2 = gIoService.newURI("view-source:http://example.com/path");
  uri3 = uri1
    .mutate()
    .setPathQueryRef("path")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setPathQueryRef("path?query#ref")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("view-source:about:blank#");
  uri2 = gIoService.newURI("view-source:about:blank");
  uri3 = uri1
    .mutate()
    .setRef("")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setRef("#")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("view-source:about:blank?something");
  uri2 = gIoService.newURI("view-source:about:blank");
  uri3 = uri1
    .mutate()
    .setQuery("")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setQuery("something")
    .finalize();
  do_check_uri_eq(uri3, uri1);

  uri1 = gIoService.newURI("view-source:about:blank?query#ref");
  uri2 = gIoService.newURI("view-source:about:blank");
  uri3 = uri1
    .mutate()
    .setPathQueryRef("blank")
    .finalize();
  do_check_uri_eq(uri3, uri2);
  uri3 = uri2
    .mutate()
    .setPathQueryRef("blank?query#ref")
    .finalize();
  do_check_uri_eq(uri3, uri1);
}

function check_space_escaping() {
  let uri = gIoService.newURI("data:text/plain,hello%20world#space hash");
  Assert.equal(uri.spec, "data:text/plain,hello%20world#space%20hash");
  uri = gIoService.newURI("data:text/plain,hello%20world#space%20hash");
  Assert.equal(uri.spec, "data:text/plain,hello%20world#space%20hash");
  uri = gIoService.newURI("data:text/plain,hello world#space%20hash");
  Assert.equal(uri.spec, "data:text/plain,hello world#space%20hash");
  uri = gIoService.newURI("data:text/plain,hello world#space hash");
  Assert.equal(uri.spec, "data:text/plain,hello world#space%20hash");
  uri = gIoService.newURI("http://example.com/test path#test path");
  uri = gIoService.newURI("http://example.com/test%20path#test%20path");
}

function check_schemeIsNull() {
  let uri = gIoService.newURI("data:text/plain,aaa");
  Assert.ok(!uri.schemeIs(null));
  uri = gIoService.newURI("http://example.com");
  Assert.ok(!uri.schemeIs(null));
  uri = gIoService.newURI("dummyscheme://example.com");
  Assert.ok(!uri.schemeIs(null));
  uri = gIoService.newURI("jar:resource://gre/chrome.toolkit.jar!/");
  Assert.ok(!uri.schemeIs(null));
  uri = gIoService.newURI("moz-icon://.unknown?size=32");
  Assert.ok(!uri.schemeIs(null));
}

// Check that characters in the query of moz-extension aren't improperly unescaped (Bug 1547882)
function check_mozextension_query() {
  let uri = gIoService.newURI(
    "moz-extension://a7d1572e-3beb-4d93-a920-c408fa09e8ea/_source/holding.html"
  );
  uri = uri
    .mutate()
    .setQuery("u=https%3A%2F%2Fnews.ycombinator.com%2F")
    .finalize();
  Assert.equal(uri.query, "u=https%3A%2F%2Fnews.ycombinator.com%2F");
  uri = gIoService.newURI(
    "moz-extension://a7d1572e-3beb-4d93-a920-c408fa09e8ea/_source/holding.html?u=https%3A%2F%2Fnews.ycombinator.com%2F"
  );
  Assert.equal(
    uri.spec,
    "moz-extension://a7d1572e-3beb-4d93-a920-c408fa09e8ea/_source/holding.html?u=https%3A%2F%2Fnews.ycombinator.com%2F"
  );
  Assert.equal(uri.query, "u=https%3A%2F%2Fnews.ycombinator.com%2F");
}

function check_resolve() {
  let base = gIoService.newURI("http://example.com");
  let uri = gIoService.newURI("tel::+371 27028456", "utf-8", base);
  Assert.equal(uri.spec, "tel::+371 27028456");
}

function test_extra_protocols() {
  // dweb://
  let url = gIoService.newURI("dweb://example.com/test");
  Assert.equal(url.host, "example.com");

  // dat://
  url = gIoService.newURI(
    "dat://41f8a987cfeba80a037e51cc8357d513b62514de36f2f9b3d3eeec7a8fb3b5a5/"
  );
  Assert.equal(
    url.host,
    "41f8a987cfeba80a037e51cc8357d513b62514de36f2f9b3d3eeec7a8fb3b5a5"
  );
  url = gIoService.newURI("dat://example.com/test");
  Assert.equal(url.host, "example.com");

  // ipfs://
  url = gIoService.newURI(
    "ipfs://bafybeiccfclkdtucu6y4yc5cpr6y3yuinr67svmii46v5cfcrkp47ihehy/frontend/license.txt"
  );
  Assert.equal(url.scheme, "ipfs");
  Assert.equal(
    url.host,
    "bafybeiccfclkdtucu6y4yc5cpr6y3yuinr67svmii46v5cfcrkp47ihehy"
  );
  Assert.equal(url.filePath, "/frontend/license.txt");

  // ipns://
  url = gIoService.newURI("ipns://peerdium.gozala.io/index.html");
  Assert.equal(url.scheme, "ipns");
  Assert.equal(url.host, "peerdium.gozala.io");
  Assert.equal(url.filePath, "/index.html");

  // ssb://
  url = gIoService.newURI("ssb://scuttlebutt.nz/index.html");
  Assert.equal(url.scheme, "ssb");
  Assert.equal(url.host, "scuttlebutt.nz");
  Assert.equal(url.filePath, "/index.html");

  // wtp://
  url = gIoService.newURI(
    "wtp://951ead31d09e4049fc1f21f137e233dd0589fcbd/blog/vim-tips/"
  );
  Assert.equal(url.scheme, "wtp");
  Assert.equal(url.host, "951ead31d09e4049fc1f21f137e233dd0589fcbd");
  Assert.equal(url.filePath, "/blog/vim-tips/");
}

// TEST MAIN FUNCTION
// ------------------
function run_test() {
  check_nested_mutations();
  check_space_escaping();
  check_schemeIsNull();
  check_mozextension_query();
  check_resolve();
  test_extra_protocols();

  // UTF-8 check - From bug 622981
  // ASCII
  let base = gIoService.newURI("http://example.org/xenia?");
  let resolved = gIoService.newURI("?x", null, base);
  let expected = gIoService.newURI("http://example.org/xenia?x");
  do_info(
    "Bug 662981: ACSII - comparing " + resolved.spec + " and " + expected.spec
  );
  Assert.ok(resolved.equals(expected));

  // UTF-8 character "è"
  // Bug 622981 was triggered by an empty query string
  base = gIoService.newURI("http://example.org/xènia?");
  resolved = gIoService.newURI("?x", null, base);
  expected = gIoService.newURI("http://example.org/xènia?x");
  do_info(
    "Bug 662981: UTF8 - comparing " + resolved.spec + " and " + expected.spec
  );
  Assert.ok(resolved.equals(expected));

  gTests.forEach(function(aTest) {
    // Check basic URI functionality
    do_test_uri_basic(aTest);

    if (!aTest.fail) {
      // Try adding various #-prefixed strings to the ends of the URIs
      gHashSuffixes.forEach(function(aSuffix) {
        do_test_uri_with_hash_suffix(aTest, aSuffix);
        if (!aTest.immutable) {
          do_test_mutate_ref(aTest, aSuffix);
        }
      });

      // For URIs that we couldn't mutate above due to them being immutable:
      // Now we check that they're actually immutable.
      if (aTest.immutable) {
        Assert.ok(aTest.immutable);
      }
    }
  });
}