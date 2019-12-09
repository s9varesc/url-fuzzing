"use strict";
 var gIoService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);

var gTests = [{path:"",
pathQueryRef:"??/?//?%fd?",
host:"[va.~]",
hostPort:"[va.~]",
hasRef:false,
spec:"//[va.~]??/?//?%fd?",
prePath:"//[va.~]"}
,
{path:"@/_%07//~%db+@",
scheme:"T",
pathQueryRef:"@/_%07//~%db+@",
host:"[::89da:cc9e:8.252.104.141%254]",
hostPort:"[::89da:cc9e:8.252.104.141%254]",
hasRef:false,
spec:"T://[::89da:cc9e:8.252.104.141%254]@/_%07//~%db+@",
prePath:"T://[::89da:cc9e:8.252.104.141%254]"}
,
{path:"-/-:-%bf%d6$(@/@@'@%bb:(%ce@/&:",
scheme:"V",
pathQueryRef:"-/-:-%bf%d6$(@/@@'@%bb:(%ce@/&:",
host:"[b8be::5427:ddbb:bacc:72ed:117.207.95.253%25%99]",
hostPort:"[b8be::5427:ddbb:bacc:72ed:117.207.95.253%25%99]",
hasRef:false,
spec:"V://[b8be::5427:ddbb:bacc:72ed:117.207.95.253%25%99]-/-:-%bf%d6$(@/@@'@%bb:(%ce@/&:",
prePath:"V://[b8be::5427:ddbb:bacc:72ed:117.207.95.253%25%99]"}
,
{path:"/:)%3c=!!:%50/%dd%bc@+/@.:@%18/%ff:%79*/%08-/::3~+::/:)_=L+'@/%0d%dd@%bd@$@@%fb/:/%6a",
scheme:"Z",
pathQueryRef:"/:)%3c=!!:%50/%dd%bc@+/@.:@%18/%ff:%79*/%08-/::3~+::/:)_=L+'@/%0d%dd@%bd@$@@%fb/:/%6a?!",
host:"116.27.226.112",
hostPort:"116.27.226.112",
hasRef:false,
spec:"Z://116.27.226.112/:)%3c=!!:%50/%dd%bc@+/@.:@%18/%ff:%79*/%08-/::3~+::/:)_=L+'@/%0d%dd@%bd@$@@%fb/:/%6a?!",
prePath:"Z://116.27.226.112"}
,
{path:"%8d/=:''~%f7+/%b6_1+%ad_%d6%ef/@:%98&%c7/@@:%ffT%b5/::6%ab%f4%ef:.",
scheme:"T",
pathQueryRef:"%8d/=:''~%f7+/%b6_1+%ad_%d6%ef/@:%98&%c7/@@:%ffT%b5/::6%ab%f4%ef:.",
host:"[c58f::1.151.254.6]",
hostPort:"[c58f::1.151.254.6]",
hasRef:false,
spec:"T://[c58f::1.151.254.6]%8d/=:''~%f7+/%b6_1+%ad_%d6%ef/@:%98&%c7/@@:%ffT%b5/::6%ab%f4%ef:.",
prePath:"T://[c58f::1.151.254.6]"}
,
{path:"/,/",
scheme:"W",
pathQueryRef:"/,/",
host:"2.5.236.4",
hostPort:"2.5.236.4",
hasRef:false,
spec:"W://2.5.236.4/,/",
prePath:"W://2.5.236.4"}
,
{path:"~/,+::9%2d)//::@_)::",
scheme:"P",
pathQueryRef:"~/,+::9%2d)//::@_)::",
host:"[va.3]",
hostPort:"[va.3]",
hasRef:false,
spec:"P://[va.3]~/,+::9%2d)//::@_)::",
prePath:"P://[va.3]"}
,
{path:"/%c0@/%13@'%d5-._@=",
scheme:"T",
pathQueryRef:"/%c0@/%13@'%d5-._@=",
host:"[297f::%25%ce]",
hostPort:"[297f::%25%ce]",
hasRef:false,
spec:"T://[297f::%25%ce]/%c0@/%13@'%d5-._@=",
prePath:"T://[297f::%25%ce]"}
,
{path:"_/@@%d4-/;_%b3R/::~&V_C@_//&",
scheme:"Z",
pathQueryRef:"_/@@%d4-/;_%b3R/::~&V_C@_//&",
host:"135.250.9.39",
hostPort:"135.250.9.39",
hasRef:false,
spec:"Z://135.250.9.39_/@@%d4-/;_%b3R/::~&V_C@_//&",
prePath:"Z://135.250.9.39"}
,
{path:"/@$:_%b6@/%13::/=.:'@$%d5@%8f@//(%54%07:%9f@@%76/:%eb!_'_-(I@/:8@=':-$+:",
scheme:"X",
pathQueryRef:"/@$:_%b6@/%13::/=.:'@$%d5@%8f@//(%54%07:%9f@@%76/:%eb!_'_-(I@/:8@=':-$+:",
host:"42.152.214.9",
hostPort:"42.152.214.9",
hasRef:false,
spec:"X://42.152.214.9/@$:_%b6@/%13::/=.:'@$%d5@%8f@//(%54%07:%9f@@%76/:%eb!_'_-(I@/:8@=':-$+:",
prePath:"X://42.152.214.9"}
,
{path:"/",
scheme:"L",
pathQueryRef:"/",
host:"[::cb1a:bbab:576d:c73d:8393]",
hostPort:"[::cb1a:bbab:576d:c73d:8393]",
hasRef:false,
spec:"L://[::cb1a:bbab:576d:c73d:8393]/",
prePath:"L://[::cb1a:bbab:576d:c73d:8393]"}
,
{path:"/%bd//~",
scheme:"S",
pathQueryRef:"/%bd//~",
host:"[11a3::6c00:fd8c:0d02]",
hostPort:"[11a3::6c00:fd8c:0d02]",
hasRef:false,
spec:"S://[11a3::6c00:fd8c:0d02]/%bd//~",
prePath:"S://[11a3::6c00:fd8c:0d02]"}
,
{path:"/%bf~::!-:@3/;/:::/@/::::@T://%93~:~.-@%06~@/-@!~_%e2",
scheme:"T",
pathQueryRef:"/%bf~::!-:@3/;/:::/@/::::@T://%93~:~.-@%06~@/-@!~_%e2",
host:"[::ad5a:97d1:deeb:2e6a]",
hostPort:"[::ad5a:97d1:deeb:2e6a]",
hasRef:false,
spec:"T://[::ad5a:97d1:deeb:2e6a]/%bf~::!-:@3/;/:::/@/::::@T://%93~:~.-@%06~@/-@!~_%e2",
prePath:"T://[::ad5a:97d1:deeb:2e6a]"}
,
{path:"./~",
pathQueryRef:"./~",
host:"%f8+*%bf7_-$6.",
hostPort:"%f8+*%bf7_-$6.",
hasRef:false,
spec:"//%f8+*%bf7_-$6../~",
prePath:"//%f8+*%bf7_-$6."}
,
{path:"/%6d/@::E@~/%ce*::&'-@.,/=%47:-5",
scheme:"X",
pathQueryRef:"/%6d/@::E@~/%ce*::&'-@.,/=%47:-5",
host:"[::35a2:00a5:8f42:8edc:f111:be9a:4adc]",
hostPort:"[::35a2:00a5:8f42:8edc:f111:be9a:4adc]",
hasRef:false,
spec:"X://[::35a2:00a5:8f42:8edc:f111:be9a:4adc]/%6d/@::E@~/%ce*::&'-@.,/=%47:-5",
prePath:"X://[::35a2:00a5:8f42:8edc:f111:be9a:4adc]"}
,
{path:"/)~_%01%05-@@/::-@:@/*,%86@G/;+:@:)K@@/%d4_/-G:=.@*",
scheme:"U",
pathQueryRef:"/)~_%01%05-@@/::-@:@/*,%86@G/;+:@:)K@@/%d4_/-G:=.@*",
host:"254.254.164.250",
hostPort:"254.254.164.250",
hasRef:false,
spec:"U://254.254.164.250/)~_%01%05-@@/::-@:@/*,%86@G/;+:@:)K@@/%d4_/-G:=.@*",
prePath:"U://254.254.164.250"}
,
{path:"/",
scheme:"X",
pathQueryRef:"/",
userPass:"'",
host:"251.201.252.255",
hostPort:"251.201.252.255",
hasRef:false,
spec:"X://'@251.201.252.255/",
prePath:"X://'@251.201.252.255"}
,
{path:"'",
pathQueryRef:"'",
host:"[vb.*]",
hostPort:"[vb.*]",
hasRef:false,
spec:"//[vb.*]'",
prePath:"//[vb.*]"}
,
{path:"/",
scheme:"V",
pathQueryRef:"/",
host:"[v4.,]",
hostPort:"[v4.,]",
hasRef:false,
spec:"V://[v4.,]/",
prePath:"V://[v4.,]"}
,
{path:"%bc/@/%9f:%87/:@.RM%2b%68*=$/.!6%94/A&!%a4!~:~/,+$._(@:%fe@",
scheme:"F",
pathQueryRef:"%bc/@/%9f:%87/:@.RM%2b%68*=$/.!6%94/A&!%a4!~:~/,+$._(@:%fe@",
host:"251.253.242.255",
hostPort:"251.253.242.255",
hasRef:false,
spec:"F://251.253.242.255%bc/@/%9f:%87/:@.RM%2b%68*=$/.!6%94/A&!%a4!~:~/,+$._(@:%fe@",
prePath:"F://251.253.242.255"}
,
{path:"/@%0e@:%19:*",
scheme:"X",
pathQueryRef:"/@%0e@:%19:*",
host:"[f6c5::ad76:f7fb:b4ff:bedb]",
hostPort:"[f6c5::ad76:f7fb:b4ff:bedb]",
hasRef:false,
spec:"X://[f6c5::ad76:f7fb:b4ff:bedb]/@%0e@:%19:*",
prePath:"X://[f6c5::ad76:f7fb:b4ff:bedb]"}
,
{path:"@",
scheme:"L",
pathQueryRef:"@",
host:"[::bf2c:d3f4:3b3d:145.252.128.212%25J]",
hostPort:"[::bf2c:d3f4:3b3d:145.252.128.212%25J]",
hasRef:false,
spec:"L://[::bf2c:d3f4:3b3d:145.252.128.212%25J]@",
prePath:"L://[::bf2c:d3f4:3b3d:145.252.128.212%25J]"}
,
{path:"/@",
scheme:"B",
pathQueryRef:"/@",
userPass:")",
host:"6.252.161.200",
hostPort:"6.252.161.200",
hasRef:false,
spec:"B://)@6.252.161.200/@",
prePath:"B://)@6.252.161.200"}
,
{path:"/@/9%d8%36!'%c2@/%de;/@:G)+/@@%a4@:%7d_@%be/@-.%15&//&*/._/~:_",
scheme:"J",
pathQueryRef:"/@/9%d8%36!'%c2@/%de;/@:G)+/@@%a4@:%7d_@%be/@-.%15&//&*/._/~:_",
host:"241.246.35.234",
hostPort:"241.246.35.234",
hasRef:false,
spec:"J://241.246.35.234/@/9%d8%36!'%c2@/%de;/@:G)+/@@%a4@:%7d_@%be/@-.%15&//&*/._/~:_",
prePath:"J://241.246.35.234"}
,
{path:"%04/:@_+:@$~./--/~~4:$@@./@@@/:~,(.%13%dd.!%e8",
scheme:"A",
pathQueryRef:"%04/:@_+:@$~./--/~~4:$@@./@@@/:~,(.%13%dd.!%e8",
host:"!",
hostPort:"!",
hasRef:false,
spec:"A://!%04/:@_+:@$~./--/~~4:$@@./@@@/:~,(.%13%dd.!%e8",
prePath:"A://!"}
,
{path:"_/@@@/@&@%bb:%4d::.:/",
scheme:"P",
pathQueryRef:"_/@@@/@&@%bb:%4d::.:/",
host:"[::5dfe:8f1d:eba5:e302:5d91:76.251.250.181%25D]",
hostPort:"[::5dfe:8f1d:eba5:e302:5d91:76.251.250.181%25D]",
hasRef:false,
spec:"P://[::5dfe:8f1d:eba5:e302:5d91:76.251.250.181%25D]_/@@@/@&@%bb:%4d::.:/",
prePath:"P://[::5dfe:8f1d:eba5:e302:5d91:76.251.250.181%25D]"}
,
{path:":",
scheme:"G",
pathQueryRef:":",
host:"254.1.231.254",
hostPort:"254.1.231.254",
hasRef:false,
spec:"G://254.1.231.254:",
prePath:"G://254.1.231.254"}
,
{path:"/'@&_@@%06/-@%a5$_/:%f5%7a%86:/*/%ed++:/:-*-@/:'%f5@!%8a/)=$:_/~M:$/::%63%2b%a7:0.2",
scheme:"E",
pathQueryRef:"/'@&_@@%06/-@%a5$_/:%f5%7a%86:/*/%ed++:/:-*-@/:'%f5@!%8a/)=$:_/~M:$/::%63%2b%a7:0.2",
host:"[cd88:1dcd:ef7e:7fb9:c824:4177:198.1.5.254%25M]",
hostPort:"[cd88:1dcd:ef7e:7fb9:c824:4177:198.1.5.254%25M]",
hasRef:false,
spec:"E://[cd88:1dcd:ef7e:7fb9:c824:4177:198.1.5.254%25M]/'@&_@@%06/-@%a5$_/:%f5%7a%86:/*/%ed++:/:-*-@/:'%f5@!%8a/)=$:_/~M:$/::%63%2b%a7:0.2",
prePath:"E://[cd88:1dcd:ef7e:7fb9:c824:4177:198.1.5.254%25M]"}
,
{path:"%af/",
scheme:"Q",
pathQueryRef:"%af/?:",
host:"62.36.119.6",
hostPort:"62.36.119.6",
hasRef:false,
spec:"Q://62.36.119.6%af/?:",
prePath:"Q://62.36.119.6"}
,
{path:":",
scheme:"I",
pathQueryRef:":",
host:"[c6df:b3e1::9fca:b660:11be:1137:6dd0]",
hostPort:"[c6df:b3e1::9fca:b660:11be:1137:6dd0]",
hasRef:false,
spec:"I://[c6df:b3e1::9fca:b660:11be:1137:6dd0]:",
prePath:"I://[c6df:b3e1::9fca:b660:11be:1137:6dd0]"}
,
{path:"*/*%13:@@/7%21/C@@/@_@@@%ac%a25:",
scheme:"B",
pathQueryRef:"*/*%13:@@/7%21/C@@/@_@@@%ac%a25:",
host:"214.59.48.251",
hostPort:"214.59.48.251",
hasRef:false,
spec:"B://214.59.48.251*/*%13:@@/7%21/C@@/@_@@@%ac%a25:",
prePath:"B://214.59.48.251"}
,
{path:"/",
scheme:"N",
pathQueryRef:"/",
host:"[3d66:b244::229.225.52.7]",
hostPort:"[3d66:b244::229.225.52.7]",
hasRef:false,
spec:"N://[3d66:b244::229.225.52.7]/",
prePath:"N://[3d66:b244::229.225.52.7]"}
,
{path:"://-'@+/%2b(:@*)%44/-&:@:/((_&%15:N@:%21/-F=%d0,:/$_%38%81!1_(",
scheme:"O",
pathQueryRef:"://-'@+/%2b(:@*)%44/-&:@:/((_&%15:N@:%21/-F=%d0,:/$_%38%81!1_(",
userPass:",",
host:"[::b722:aaac]",
hostPort:"[::b722:aaac]",
hasRef:false,
spec:"O://,@[::b722:aaac]://-'@+/%2b(:@*)%44/-&:@:/((_&%15:N@:%21/-F=%d0,:/$_%38%81!1_(",
prePath:"O://,@[::b722:aaac]"}
,
{path:"*",
pathQueryRef:"*",
host:"[301d::e71e:e252:0d0c:5ea0]",
hostPort:"[301d::e71e:e252:0d0c:5ea0]",
hasRef:false,
spec:"//[301d::e71e:e252:0d0c:5ea0]*",
prePath:"//[301d::e71e:e252:0d0c:5ea0]"}
,
{path:"/",
scheme:"D",
pathQueryRef:"/",
host:"[3b0f:8dc2::]",
hostPort:"[3b0f:8dc2::]",
hasRef:false,
spec:"D://[3b0f:8dc2::]/",
prePath:"D://[3b0f:8dc2::]"}
,
{path:"",
scheme:"R",
pathQueryRef:"",
host:"[v8.=]",
hostPort:"[v8.=]",
hasRef:false,
spec:"R://[v8.=]",
prePath:"R://[v8.=]"}
,
{path:"/B%80:_:@-:@:",
scheme:"T",
pathQueryRef:"/B%80:_:@-:@:",
host:"4.1.57.252",
hostPort:"4.1.57.252",
hasRef:false,
spec:"T://4.1.57.252/B%80:_:@-:@:",
prePath:"T://4.1.57.252"}
,
{path:"@/!*,(/%f1:%cb%62:@@%f8%c2/%30!.$:@%dd%02/H;,@::@%d7:%8b",
scheme:"U",
pathQueryRef:"@/!*,(/%f1:%cb%62:@@%f8%c2/%30!.$:@%dd%02/H;,@::@%d7:%8b",
host:"[::8036:251.120.164.8]",
hostPort:"[::8036:251.120.164.8]",
hasRef:false,
spec:"U://[::8036:251.120.164.8]@/!*,(/%f1:%cb%62:@@%f8%c2/%30!.$:@%dd%02/H;,@::@%d7:%8b",
prePath:"U://[::8036:251.120.164.8]"}
,
{path:"/",
scheme:"I",
pathQueryRef:"/",
host:"[::558c%25~]",
hostPort:"[::558c%25~]",
hasRef:false,
spec:"I://[::558c%25~]/",
prePath:"I://[::558c%25~]"}
,
{path:":/!@@='@':%e8_/%d2+.@/:$/5@'%4c@L/&%f9:$%b2@:/%09%cd",
scheme:"Y",
pathQueryRef:":/!@@='@':%e8_/%d2+.@/:$/5@'%4c@L/&%f9:$%b2@:/%09%cd",
host:"[v1.-]",
hostPort:"[v1.-]",
hasRef:false,
spec:"Y://[v1.-]:/!@@='@':%e8_/%d2+.@/:$/5@'%4c@L/&%f9:$%b2@:/%09%cd",
prePath:"Y://[v1.-]"}
,
{path:";/)%f2_%f6/%bd%4c@::=",
scheme:"B",
pathQueryRef:";/)%f2_%f6/%bd%4c@::=",
host:"[560f::1d76]",
hostPort:"[560f::1d76]",
hasRef:false,
spec:"B://[560f::1d76];/)%f2_%f6/%bd%4c@::=",
prePath:"B://[560f::1d76]"}
,
{path:"%d8/::':.%f0:,/@N::@:%d1%12!%6a/::@.:__",
pathQueryRef:"%d8/::':.%f0:,/@N::@:%d1%12!%6a/::@.:__",
userPass:"",
host:"%19~_R",
hostPort:"%19~_R",
hasRef:false,
spec:"//@%19~_R%d8/::':.%f0:,/@N::@:%d1%12!%6a/::@.:__",
prePath:"//@%19~_R"}
,
{path:"/:&,@%b5:Q./@@+;%68/'~%7a_.@8/_*-%ec%9b%ed/%2a%cc_-:=::@3/%1e~D(%0e::~%6c/-",
scheme:"O",
pathQueryRef:"/:&,@%b5:Q./@@+;%68/'~%7a_.@8/_*-%ec%9b%ed/%2a%cc_-:=::@3/%1e~D(%0e::~%6c/-??",
host:"252.4.84.110",
hostPort:"252.4.84.110",
hasRef:false,
spec:"O://252.4.84.110/:&,@%b5:Q./@@+;%68/'~%7a_.@8/_*-%ec%9b%ed/%2a%cc_-:=::@3/%1e~D(%0e::~%6c/-??",
prePath:"O://252.4.84.110"}
,
{path:"$/,/J@@/%a62:@@/%fc=@@(@.%d6@/",
scheme:"U",
pathQueryRef:"$/,/J@@/%a62:@@/%fc=@@(@.%d6@/",
host:"[3d97:8ecd::fca5:d5ab:af5e%25%ae]",
hostPort:"[3d97:8ecd::fca5:d5ab:af5e%25%ae]",
hasRef:false,
spec:"U://[3d97:8ecd::fca5:d5ab:af5e%25%ae]$/,/J@@/%a62:@@/%fc=@@(@.%d6@/",
prePath:"U://[3d97:8ecd::fca5:d5ab:af5e%25%ae]"}
,
{path:"%d3/::,//@~':GE@-/+$)%84_:%744/%cb.+:+:6",
scheme:"F",
pathQueryRef:"%d3/::,//@~':GE@-/+$)%84_:%744/%cb.+:+:6",
host:"236.0.7.28",
hostPort:"236.0.7.28",
hasRef:false,
spec:"F://236.0.7.28%d3/::,//@~':GE@-/+$)%84_:%744/%cb.+:+:6",
prePath:"F://236.0.7.28"}
,
{path:"",
scheme:"K",
pathQueryRef:"",
host:"[071b:7e81::c29e:fe61:72f5]",
hostPort:"[071b:7e81::c29e:fe61:72f5]",
hasRef:false,
spec:"K://[071b:7e81::c29e:fe61:72f5]",
prePath:"K://[071b:7e81::c29e:fe61:72f5]"}
,
{path:"",
scheme:"E",
pathQueryRef:"",
host:"74.7.255.245",
hostPort:"74.7.255.245",
hasRef:false,
spec:"E://74.7.255.245",
prePath:"E://74.7.255.245"}
,
{path:":/):.@:@(5&/-Z@@-",
scheme:"I",
pathQueryRef:":/):.@:@(5&/-Z@@-",
host:"[v9._]",
hostPort:"[v9._]",
hasRef:false,
spec:"I://[v9._]:/):.@:@(5&/-Z@@-",
prePath:"I://[v9._]"}
,
{path:"%c8/%a6=:2/&%aa@%e2/(@O!//:%f7",
scheme:"X",
pathQueryRef:"%c8/%a6=:2/&%aa@%e2/(@O!//:%f7",
userPass:"(",
host:";%f0&%ad%f6",
hostPort:";%f0&%ad%f6",
hasRef:false,
spec:"X://(@;%f0&%ad%f6%c8/%a6=:2/&%aa@%e2/(@O!//:%f7",
prePath:"X://(@;%f0&%ad%f6"}
,
{path:"",
scheme:"N",
pathQueryRef:"",
host:"[vd.']",
hostPort:"[vd.']",
hasRef:false,
spec:"N://[vd.']",
prePath:"N://[vd.']"}
,
{path:"@/:%fe+:%bc-/&-:.:!;%ee;/%b7I%98/_:@3:I!*@%2c/%c8,-:@*%82/:",
scheme:"Y",
pathQueryRef:"@/:%fe+:%bc-/&-:.:!;%ee;/%b7I%98/_:@3:I!*@%2c/%c8,-:@*%82/:",
host:"[cfac::34df:4e83:fcf4:b0cf:d9b2:d3e6]",
hostPort:"[cfac::34df:4e83:fcf4:b0cf:d9b2:d3e6]",
hasRef:false,
spec:"Y://[cfac::34df:4e83:fcf4:b0cf:d9b2:d3e6]@/:%fe+:%bc-/&-:.:!;%ee;/%b7I%98/_:@3:I!*@%2c/%c8,-:@*%82/:",
prePath:"Y://[cfac::34df:4e83:fcf4:b0cf:d9b2:d3e6]"}
,
{path:"%a6/@:/,0%47//@:.:::%52./:'/.@/:::%e7@:@@%ec/@:@:@8/-:%1b;:@:/=@U%b1",
scheme:"V",
pathQueryRef:"%a6/@:/,0%47//@:.:::%52./:'/.@/:::%e7@:@@%ec/@:@:@8/-:%1b;:@:/=@U%b1",
host:"[3fcc:0fee::dfbb:ae00:0.0.80.250%25%ce]",
hostPort:"[3fcc:0fee::dfbb:ae00:0.0.80.250%25%ce]",
hasRef:false,
spec:"V://[3fcc:0fee::dfbb:ae00:0.0.80.250%25%ce]%a6/@:/,0%47//@:.:::%52./:'/.@/:::%e7@:@@%ec/@:@:@8/-:%1b;:@:/=@U%b1",
prePath:"V://[3fcc:0fee::dfbb:ae00:0.0.80.250%25%ce]"}
,
{path:"8/:&-&@@%fe~'/%e0@::@@%c1/;@%fb@/@+%5e@@%e3/~/:%86%05-:/;.:",
scheme:"H",
pathQueryRef:"8/:&-&@@%fe~'/%e0@::@@%c1/;@%fb@/@+%5e@@%e3/~/:%86%05-:/;.:",
host:"[1ae6:6b6a::09ab:5cea:232.75.3.196]",
hostPort:"[1ae6:6b6a::09ab:5cea:232.75.3.196]",
hasRef:false,
spec:"H://[1ae6:6b6a::09ab:5cea:232.75.3.196]8/:&-&@@%fe~'/%e0@::@@%c1/;@%fb@/@+%5e@@%e3/~/:%86%05-:/;.:",
prePath:"H://[1ae6:6b6a::09ab:5cea:232.75.3.196]"}
,
{path:"/;@//!$:~@@/:&@%82@@%07/@:%95@@@_@~/%9e:W@&@(,/=@~-)~",
scheme:"Q",
pathQueryRef:"/;@//!$:~@@/:&@%82@@%07/@:%95@@@_@~/%9e:W@&@(,/=@~-)~",
userPass:"_",
host:"163.5.250.169",
hostPort:"163.5.250.169",
hasRef:false,
spec:"Q://_@163.5.250.169/;@//!$:~@@/:&@%82@@%07/@:%95@@@_@~/%9e:W@&@(,/=@~-)~",
prePath:"Q://_@163.5.250.169"}
,
{path:"7",
pathQueryRef:"7",
host:"[::c08b:836f]",
hostPort:"[::c08b:836f]",
hasRef:false,
spec:"//[::c08b:836f]7",
prePath:"//[::c08b:836f]"}
,
{path:"%9e/%3c.S@:/*:-.,$.%0c//'%cc@@(6:_@/@*::&/@@~*&/;@&/%82:/@@%c7'@",
scheme:"F",
pathQueryRef:"%9e/%3c.S@:/*:-.,$.%0c//'%cc@@(6:_@/@*::&/@@~*&/;@&/%82:/@@%c7'@",
host:"[2db6:f30a::fe0a:cfcd:f09a:241.151.1.253%25.]",
hostPort:"[2db6:f30a::fe0a:cfcd:f09a:241.151.1.253%25.]",
hasRef:false,
spec:"F://[2db6:f30a::fe0a:cfcd:f09a:241.151.1.253%25.]%9e/%3c.S@:/*:-.,$.%0c//'%cc@@(6:_@/@*::&/@@~*&/;@&/%82:/@@%c7'@",
prePath:"F://[2db6:f30a::fe0a:cfcd:f09a:241.151.1.253%25.]"}
,
{path:"/=:-/):'~:@3/_:B:::/%aa(:-$(%9b-$%c6",
scheme:"E",
pathQueryRef:"/=:-/):'~:@3/_:B:::/%aa(:-$(%9b-$%c6",
host:"249.215.63.221",
hostPort:"249.215.63.221",
hasRef:false,
spec:"E://249.215.63.221/=:-/):'~:@3/_:B:::/%aa(:-$(%9b-$%c6",
prePath:"E://249.215.63.221"}
,
{path:"/",
scheme:"M",
pathQueryRef:"/",
host:"[v0..]",
hostPort:"[v0..]",
hasRef:false,
spec:"M://[v0..]/",
prePath:"M://[v0..]"}
,
{path:"/",
scheme:"O-",
pathQueryRef:"/",
host:"45.225.19.238",
hostPort:"45.225.19.238",
hasRef:false,
spec:"O-://45.225.19.238/",
prePath:"O-://45.225.19.238"}
,
{path:"%e7/~+=9@:%ff@%4e:/@_:_%a1'@:%be./%d5@-;/-/':W_/:@:@:)%9c%f6/:6@L:%9f$::X/./@%ad%cf,@$/::@:",
scheme:"P",
pathQueryRef:"%e7/~+=9@:%ff@%4e:/@_:_%a1'@:%be./%d5@-;/-/':W_/:@:@:)%9c%f6/:6@L:%9f$::X/./@%ad%cf,@$/::@:",
userPass:"+",
host:"=%91%ab!%43!",
hostPort:"=%91%ab!%43!",
hasRef:false,
spec:"P://+@=%91%ab!%43!%e7/~+=9@:%ff@%4e:/@_:_%a1'@:%be./%d5@-;/-/':W_/:@:@:)%9c%f6/:6@L:%9f$::X/./@%ad%cf,@$/::@:",
prePath:"P://+@=%91%ab!%43!"}
,
{path:"~/(3~%7e.:/_'M-~%25/@%e8@:-:%ea@:/:/%b9=~@!@@D:/:@%f3/66,_(/@3/",
ref:"",
scheme:"L",
pathQueryRef:"~/(3~%7e.:/_'M-~%25/@%e8@:-:%ea@:/:/%b9=~@!@@D:/:@%f3/66,_(/@3/#",
host:"152.46.140.31",
hostPort:"152.46.140.31",
hasRef:true,
spec:"L://152.46.140.31~/(3~%7e.:/_'M-~%25/@%e8@:-:%ea@:/:/%b9=~@!@@D:/:@%f3/66,_(/@3/#",
prePath:"L://152.46.140.31"}
,
{path:"/",
pathQueryRef:"/",
host:"5.62.88.2",
hostPort:"5.62.88.2",
hasRef:false,
spec:"//5.62.88.2/",
prePath:"//5.62.88.2"}
,
{path:"",
scheme:"W",
pathQueryRef:"",
userPass:"%a3",
host:"[v5.']",
hostPort:"[v5.']",
hasRef:false,
spec:"W://%a3@[v5.']",
prePath:"W://%a3@[v5.']"}
,
{path:"%0d/&:-@::@=@:/:*:'%60:@=/.@::@%08F//@!+&%b6%16::%6d$/'%f1%db)",
scheme:"C",
pathQueryRef:"%0d/&:-@::@=@:/:*:'%60:@=/.@::@%08F//@!+&%b6%16::%6d$/'%f1%db)",
host:"5.202.253.250",
hostPort:"5.202.253.250",
hasRef:false,
spec:"C://5.202.253.250%0d/&:-@::@=@:/:*:'%60:@=/.@::@%08F//@!+&%b6%16::%6d$/'%f1%db)",
prePath:"C://5.202.253.250"}
,
{path:"%98/,;!DN_@:*/(@+:)/:=-%58-:@%0b%9a/%d0:+=;:/_'*,%6a@:-/)&)%f6%f1/%dd%f4//%e6(:(:/%4c%5d%dc",
ref:"/",
scheme:"U",
pathQueryRef:"%98/,;!DN_@:*/(@+:)/:=-%58-:@%0b%9a/%d0:+=;:/_'*,%6a@:-/)&)%f6%f1/%dd%f4//%e6(:(:/%4c%5d%dc#/",
host:"[vb.']",
hostPort:"[vb.']",
hasRef:true,
spec:"U://[vb.']%98/,;!DN_@:*/(@+:)/:=-%58-:@%0b%9a/%d0:+=;:/_'*,%6a@:-/)&)%f6%f1/%dd%f4//%e6(:(:/%4c%5d%dc#/",
prePath:"U://[vb.']"}
,
{path:"%cf",
scheme:"M+",
pathQueryRef:"%cf",
host:"151.22.238.96",
hostPort:"151.22.238.96",
hasRef:false,
spec:"M+://151.22.238.96%cf",
prePath:"M+://151.22.238.96"}
,
{path:"+",
pathQueryRef:"+",
host:"[vf.)]",
hostPort:"[vf.)]",
hasRef:false,
spec:"//[vf.)]+",
prePath:"//[vf.)]"}
,
{path:"@/%cf%ae/:1%2f+@@",
scheme:"P",
pathQueryRef:"@/%cf%ae/:1%2f+@@",
host:"255.60.209.53",
hostPort:"255.60.209.53",
hasRef:false,
spec:"P://255.60.209.53@/%cf%ae/:1%2f+@@",
prePath:"P://255.60.209.53"}
,
{path:"",
scheme:"C",
pathQueryRef:"",
host:"[vd.(]",
hostPort:"[vd.(]",
hasRef:false,
spec:"C://[vd.(]",
prePath:"C://[vd.(]"}
,
{path:"Y//';-$.S~./@::I./@J'-/+::@%bc%cd%06/%cc-,&(%ff%db%d8/-:@%daR%80/@%ea/_:",
scheme:"O",
pathQueryRef:"Y//';-$.S~./@::I./@J'-/+::@%bc%cd%06/%cc-,&(%ff%db%d8/-:@%daR%80/@%ea/_:",
userPass:"$",
host:"",
hostPort:"",
hasRef:false,
spec:"O://$@Y//';-$.S~./@::I./@J'-/+::@%bc%cd%06/%cc-,&(%ff%db%d8/-:@%daR%80/@%ea/_:",
prePath:"O://$@"}
,
{path:",",
pathQueryRef:",",
host:"_%2e%10-%4c%5d%ad",
hostPort:"_%2e%10-%4c%5d%ad",
hasRef:false,
spec:"//_%2e%10-%4c%5d%ad,",
prePath:"//_%2e%10-%4c%5d%ad"}
,
{path:":/:",
scheme:"J",
pathQueryRef:":/:",
userPass:"X",
host:"%f9%99%e9%77",
hostPort:"%f9%99%e9%77",
hasRef:false,
spec:"J://X@%f9%99%e9%77:/:",
prePath:"J://X@%f9%99%e9%77"}
,
{path:":/@@@.%31F%00&/~%0f%f2/:@::@%55:_/-&%56/:%c3%2d:$%65%de:%d7/%7e+%5d)::@://:=':%a6-%1e,:",
scheme:"V",
pathQueryRef:":/@@@.%31F%00&/~%0f%f2/:@::@%55:_/-&%56/:%c3%2d:$%65%de:%d7/%7e+%5d)::@://:=':%a6-%1e,:",
host:"[v2.!]",
hostPort:"[v2.!]",
hasRef:false,
spec:"V://[v2.!]:/@@@.%31F%00&/~%0f%f2/:@::@%55:_/-&%56/:%c3%2d:$%65%de:%d7/%7e+%5d)::@://:=':%a6-%1e,:",
prePath:"V://[v2.!]"}
,
{path:"%be/%c8-%91/%4f:$%70@:':%3a/@-%7c%c8%c8%fa/%b3%f0-:/::%ae%cc%fc/%7d(;=@':!+@/@J@__/%15&%da@/!@%cf7%a09_6@%cc/9%72-_)%fb",
scheme:"R",
pathQueryRef:"%be/%c8-%91/%4f:$%70@:':%3a/@-%7c%c8%c8%fa/%b3%f0-:/::%ae%cc%fc/%7d(;=@':!+@/@J@__/%15&%da@/!@%cf7%a09_6@%cc/9%72-_)%fb",
host:"'",
hostPort:"'",
hasRef:false,
spec:"R://'%be/%c8-%91/%4f:$%70@:':%3a/@-%7c%c8%c8%fa/%b3%f0-:/::%ae%cc%fc/%7d(;=@':!+@/@J@__/%15&%da@/!@%cf7%a09_6@%cc/9%72-_)%fb",
prePath:"R://'"}
,
{path:"/",
scheme:"B",
pathQueryRef:"/",
host:"[dade:27be::1aab:4110:244.69.39.104%25_]",
hostPort:"[dade:27be::1aab:4110:244.69.39.104%25_]",
hasRef:false,
spec:"B://[dade:27be::1aab:4110:244.69.39.104%25_]/",
prePath:"B://[dade:27be::1aab:4110:244.69.39.104%25_]"}
,
{path:"/-/'%2b%6f:*%8d@:~",
ref:"?",
pathQueryRef:"/-/'%2b%6f:*%8d@:~#?",
host:"[1aff:a4af:ae27:d6a1:dc28:5ea8:0a96:4c63%25%dc]",
hostPort:"[1aff:a4af:ae27:d6a1:dc28:5ea8:0a96:4c63%25%dc]",
hasRef:true,
spec:"//[1aff:a4af:ae27:d6a1:dc28:5ea8:0a96:4c63%25%dc]/-/'%2b%6f:*%8d@:~#?",
prePath:"//[1aff:a4af:ae27:d6a1:dc28:5ea8:0a96:4c63%25%dc]"}
,
{path:"(/.@=~/-:)@_B@%15://7~%bc/~:(%f0@:K@;-",
scheme:"E",
pathQueryRef:"(/.@=~/-:)@_B@%15://7~%bc/~:(%f0@:K@;-",
port:"",
host:"[v2.:]",
hostPort:"[v2.:]:",
hasRef:false,
spec:"E://[v2.:]:(/.@=~/-:)@_B@%15://7~%bc/~:(%f0@:K@;-",
prePath:"E://[v2.:]:"}
,
{path:"~",
pathQueryRef:"~",
host:"!D.&%fd%7b",
hostPort:"!D.&%fd%7b",
hasRef:false,
spec:"//!D.&%fd%7b~",
prePath:"//!D.&%fd%7b"}
,
{path:"",
scheme:"H",
pathQueryRef:"",
userPass:";",
host:"250.9.142.244",
hostPort:"250.9.142.244",
hasRef:false,
spec:"H://;@250.9.142.244",
prePath:"H://;@250.9.142.244"}
,
{path:"/@/%44!%dc(%ed/L@@~/@@:-%fa-@(/:/~W%f9-@:-:@/%f2%92::)//%ff/(@::@@%c4W",
scheme:"G",
pathQueryRef:"/@/%44!%dc(%ed/L@@~/@@:-%fa-@(/:/~W%f9-@:-:@/%f2%92::)//%ff/(@::@@%c4W",
host:"[decc:85eb::a3af%257]",
hostPort:"[decc:85eb::a3af%257]",
hasRef:false,
spec:"G://[decc:85eb::a3af%257]/@/%44!%dc(%ed/L@@~/@@:-%fa-@(/:/~W%f9-@:-:@/%f2%92::)//%ff/(@::@@%c4W",
prePath:"G://[decc:85eb::a3af%257]"}
,
{path:"/:&%cf:+%e3/-.:B+/:.,=-:@/$):$@./++~_$&@%61_/*:2;,$::@_/%9b%b8%0f=:B*=-)/*:/%4a@':/:T@",
ref:"-",
scheme:"R",
pathQueryRef:"/:&%cf:+%e3/-.:B+/:.,=-:@/$):$@./++~_$&@%61_/*:2;,$::@_/%9b%b8%0f=:B*=-)/*:/%4a@':/:T@#-",
host:"[::59c0:a2be:128.119.254.141%25~]",
hostPort:"[::59c0:a2be:128.119.254.141%25~]",
hasRef:true,
spec:"R://[::59c0:a2be:128.119.254.141%25~]/:&%cf:+%e3/-.:B+/:.,=-:@/$):$@./++~_$&@%61_/*:2;,$::@_/%9b%b8%0f=:B*=-)/*:/%4a@':/:T@#-",
prePath:"R://[::59c0:a2be:128.119.254.141%25~]"}
,
{path:"",
scheme:"R0",
pathQueryRef:"",
host:"37.140.234.1",
hostPort:"37.140.234.1",
hasRef:false,
spec:"R0://37.140.234.1",
prePath:"R0://37.140.234.1"}
,
{path:":/%50:/'@%14:$&/G",
scheme:"F",
pathQueryRef:":/%50:/'@%14:$&/G",
host:"[9eb0:db7a:8263:f072:eecd:41c6:dd21:09d2]",
hostPort:"[9eb0:db7a:8263:f072:eecd:41c6:dd21:09d2]",
hasRef:false,
spec:"F://[9eb0:db7a:8263:f072:eecd:41c6:dd21:09d2]:/%50:/'@%14:$&/G",
prePath:"F://[9eb0:db7a:8263:f072:eecd:41c6:dd21:09d2]"}
,
{path:"_",
pathQueryRef:"_",
host:"[395e::19fc:43ef:dc1e:bec1:169.26.255.231%25%c4]",
hostPort:"[395e::19fc:43ef:dc1e:bec1:169.26.255.231%25%c4]",
hasRef:false,
spec:"//[395e::19fc:43ef:dc1e:bec1:169.26.255.231%25%c4]_",
prePath:"//[395e::19fc:43ef:dc1e:bec1:169.26.255.231%25%c4]"}
,
{path:"/@@.$~:@%c0.2/~~:_C6/@%eeL%ea/H_:L/:_-%75_;~,@)/$+@%ad@;@/./::_(@",
scheme:"P",
pathQueryRef:"/@@.$~:@%c0.2/~~:_C6/@%eeL%ea/H_:L/:_-%75_;~,@)/$+@%ad@;@/./::_(@",
host:"[v2.$]",
hostPort:"[v2.$]",
hasRef:false,
spec:"P://[v2.$]/@@.$~:@%c0.2/~~:_C6/@%eeL%ea/H_:L/:_-%75_;~,@)/$+@%ad@;@/./::_(@",
prePath:"P://[v2.$]"}
,
{path:"&/:%dd-/@%a0(:_:=&/@~@+:%84",
scheme:"S",
pathQueryRef:"&/:%dd-/@%a0(:_:=&/@~@+:%84",
port:"9",
host:"[d7c1:24ff:d841:2bc1:bbba:d89c:136.146.1.250]",
hostPort:"[d7c1:24ff:d841:2bc1:bbba:d89c:136.146.1.250]:9",
hasRef:false,
spec:"S://[d7c1:24ff:d841:2bc1:bbba:d89c:136.146.1.250]:9&/:%dd-/@%a0(:_:=&/@~@+:%84",
prePath:"S://[d7c1:24ff:d841:2bc1:bbba:d89c:136.146.1.250]:9"}
,
{path:":/%4e::F*%fc$:@@/+%4f@%80%68/%5d%4d@%cc%ed%7d+.,/:/E/.:_@",
ref:":",
scheme:"P",
pathQueryRef:":/%4e::F*%fc$:@@/+%4f@%80%68/%5d%4d@%cc%ed%7d+.,/:/E/.:_@#:",
host:"[v7.:]",
hostPort:"[v7.:]",
hasRef:true,
spec:"P://[v7.:]:/%4e::F*%fc$:@@/+%4f@%80%68/%5d%4d@%cc%ed%7d+.,/:/E/.:_@#:",
prePath:"P://[v7.:]"}
,
{path:"$",
pathQueryRef:"$",
host:"4.5.98.142",
hostPort:"4.5.98.142",
hasRef:false,
spec:"//4.5.98.142$",
prePath:"//4.5.98.142"}
,
{path:"%cc/'(::%42&@_:./-@%fb~:.-:/:%dc%2f:&@@6@/-:~$:(/~@':@",
scheme:"D",
pathQueryRef:"%cc/'(::%42&@_:./-@%fb~:.-:/:%dc%2f:&@@6@/-:~$:(/~@':@",
host:"147.239.212.244",
hostPort:"147.239.212.244",
hasRef:false,
spec:"D://147.239.212.244%cc/'(::%42&@_:./-@%fb~:.-:/:%dc%2f:&@@6@/-:~$:(/~@':@",
prePath:"D://147.239.212.244"}
,
{path:"+/,/%5e/_/@~%3f_C@%4c/::%1b@K=@-@:/6_/",
scheme:"C",
pathQueryRef:"+/,/%5e/_/@~%3f_C@%4c/::%1b@K=@-@:/6_/",
host:"[abc8:9be1::cebb]",
hostPort:"[abc8:9be1::cebb]",
hasRef:false,
spec:"C://[abc8:9be1::cebb]+/,/%5e/_/@~%3f_C@%4c/::%1b@K=@-@:/6_/",
prePath:"C://[abc8:9be1::cebb]"}
,
{path:"",
ref:"%a1",
scheme:"D",
pathQueryRef:"#%a1",
host:"[::251.187.3.242%25%dc]",
hostPort:"[::251.187.3.242%25%dc]",
hasRef:true,
spec:"D://[::251.187.3.242%25%dc]#%a1",
prePath:"D://[::251.187.3.242%25%dc]"}
,
{path:"+/@%0b.@@@:/@)'%ad$/:@.@:+%6f%fd%1d%f8/@/@.!@$%35:/@$/",
scheme:"Z",
pathQueryRef:"+/@%0b.@@@:/@)'%ad$/:@.@:+%6f%fd%1d%f8/@/@.!@$%35:/@$/",
host:"252.254.251.37",
hostPort:"252.254.251.37",
hasRef:false,
spec:"Z://252.254.251.37+/@%0b.@@@:/@)'%ad$/:@.@:+%6f%fd%1d%f8/@/@.!@$%35:/@$/",
prePath:"Z://252.254.251.37"}
,
{path:"",
ref:";",
scheme:"G",
pathQueryRef:"#;",
host:"[3bf9:a27a::]",
hostPort:"[3bf9:a27a::]",
hasRef:true,
spec:"G://[3bf9:a27a::]#;",
prePath:"G://[3bf9:a27a::]"}
,
{path:"%96/&%5e_:@@/$@/;/:@%e8~@,%d6:%fa/:%349%db:@/:@@%fd'%cb/-=%55:!.::://:-:@:",
scheme:"K",
pathQueryRef:"%96/&%5e_:@@/$@/;/:@%e8~@,%d6:%fa/:%349%db:@/:@@%fd'%cb/-=%55:!.::://:-:@:",
userPass:":",
host:"36.220.150.255",
hostPort:"36.220.150.255",
hasRef:false,
spec:"K://:@36.220.150.255%96/&%5e_:@@/$@/;/:@%e8~@,%d6:%fa/:%349%db:@/:@@%fd'%cb/-=%55:!.::://:-:@:",
prePath:"K://:@36.220.150.255"}
,
{path:"/",
scheme:"M",
pathQueryRef:"/",
userPass:"~",
host:"%a4_%51%86%9f_,",
hostPort:"%a4_%51%86%9f_,",
hasRef:false,
spec:"M://~@%a4_%51%86%9f_,/",
prePath:"M://~@%a4_%51%86%9f_,"}
,
{path:"/@::@%97%dd:D:/(%4a@/:::'/:%9d%d68",
scheme:"E",
pathQueryRef:"/@::@%97%dd:D:/(%4a@/:::'/:%9d%d68?.",
host:"[::ce55:5dcc:199.3.207.254]",
hostPort:"[::ce55:5dcc:199.3.207.254]",
hasRef:false,
spec:"E://[::ce55:5dcc:199.3.207.254]/@::@%97%dd:D:/(%4a@/:::'/:%9d%d68?.",
prePath:"E://[::ce55:5dcc:199.3.207.254]"}
,
{path:"/8/%b8:(-~+_&:/@@/$%63:4(%66$:/@F::%20!/*_6!~%5d@K@@/%8f_F%26",
scheme:"T",
pathQueryRef:"/8/%b8:(-~+_&:/@@/$%63:4(%66$:/@F::%20!/*_6!~%5d@K@@/%8f_F%26",
host:"[1e48:6a05::252.96.9.228%25.]",
hostPort:"[1e48:6a05::252.96.9.228%25.]",
hasRef:false,
spec:"T://[1e48:6a05::252.96.9.228%25.]/8/%b8:(-~+_&:/@@/$%63:4(%66$:/@F::%20!/*_6!~%5d@K@@/%8f_F%26",
prePath:"T://[1e48:6a05::252.96.9.228%25.]"}
,
{path:"%b8/@&1@_/::/@:~:::@%ba/.;-*%d2",
ref:"@",
scheme:"C",
pathQueryRef:"%b8/@&1@_/::/@:~:::@%ba/.;-*%d2#@",
host:"254.210.39.197",
hostPort:"254.210.39.197",
hasRef:true,
spec:"C://254.210.39.197%b8/@&1@_/::/@:~:::@%ba/.;-*%d2#@",
prePath:"C://254.210.39.197"}
,
{path:"",
scheme:"V",
pathQueryRef:"",
host:"203.7.4.129",
hostPort:"203.7.4.129",
hasRef:false,
spec:"V://203.7.4.129",
prePath:"V://203.7.4.129"}
,
{path:"E",
pathQueryRef:"E",
host:"&-%c8",
hostPort:"&-%c8",
hasRef:false,
spec:"//&-%c8E",
prePath:"//&-%c8"}
,
{path:";",
pathQueryRef:";",
host:"1.250.250.254",
hostPort:"1.250.250.254",
hasRef:false,
spec:"//1.250.250.254;",
prePath:"//1.250.250.254"}
,
{path:"/~/&-%6d:@/@:@@/%ae:::)(::/.:-@;(B%88/:.@/!@@::-/;@*:0",
pathQueryRef:"/~/&-%6d:@/@:@@/%ae:::)(::/.:-@;(B%88/:.@/!@@::-/;@*:0",
port:"",
host:"2.32.203.92",
hostPort:"2.32.203.92:",
hasRef:false,
spec:"//2.32.203.92:/~/&-%6d:@/@:@@/%ae:::)(::/.:-@;(B%88/:.@/!@@::-/;@*:0",
prePath:"//2.32.203.92:"}
,
{path:"+/@S%3c::%de%23//%ec*_*%60%ef%8d%e7&/:@~~~_:/%df+/-@:!:-,$);/%8d9%202/:_%3e$%43@%a6%9d/@%f4@%81/:_:3==&@)",
ref:")",
pathQueryRef:"+/@S%3c::%de%23//%ec*_*%60%ef%8d%e7&/:@~~~_:/%df+/-@:!:-,$);/%8d9%202/:_%3e$%43@%a6%9d/@%f4@%81/:_:3==&@)#)",
host:"59.230.72.9",
hostPort:"59.230.72.9",
hasRef:true,
spec:"//59.230.72.9+/@S%3c::%de%23//%ec*_*%60%ef%8d%e7&/:@~~~_:/%df+/-@:!:-,$);/%8d9%202/:_%3e$%43@%a6%9d/@%f4@%81/:_:3==&@)#)",
prePath:"//59.230.72.9"}
,
{path:"",
scheme:"N",
pathQueryRef:"",
host:"[v4.&]",
hostPort:"[v4.&]",
hasRef:false,
spec:"N://[v4.&]",
prePath:"N://[v4.&]"}
,
{path:"(",
pathQueryRef:"(",
host:"=!~%be%0b%915",
hostPort:"=!~%be%0b%915",
hasRef:false,
spec:"//=!~%be%0b%915(",
prePath:"//=!~%be%0b%915"}
,
{path:"!/3%dd-:%42/=//.%cf/:;%f1%c8%2b",
scheme:"B",
pathQueryRef:"!/3%dd-:%42/=//.%cf/:;%f1%c8%2b",
host:")",
hostPort:")",
hasRef:false,
spec:"B://)!/3%dd-:%42/=//.%cf/:;%f1%c8%2b",
prePath:"B://)"}
,
{path:"",
scheme:"S.",
pathQueryRef:"",
host:"250.49.253.184",
hostPort:"250.49.253.184",
hasRef:false,
spec:"S.://250.49.253.184",
prePath:"S.://250.49.253.184"}
,
{path:"/@%91%6d@@%da:_:%c8/~'%a622:*/%c6(:':::/M%40@O@%ce::%9b:",
ref:"?",
scheme:"F",
pathQueryRef:"/@%91%6d@@%da:_:%c8/~'%a622:*/%c6(:':::/M%40@O@%ce::%9b:#?",
host:"5.90.39.192",
hostPort:"5.90.39.192",
hasRef:true,
spec:"F://5.90.39.192/@%91%6d@@%da:_:%c8/~'%a622:*/%c6(:':::/M%40@O@%ce::%9b:#?",
prePath:"F://5.90.39.192"}
,
{path:"P/:%96%bf:'@%4e/@'/C/@%7b%a8%00/@~@:5/1$",
scheme:"Z",
pathQueryRef:"P/:%96%bf:'@%4e/@'/C/@%7b%a8%00/@~@:5/1$?@",
host:"7.152.154.8",
hostPort:"7.152.154.8",
hasRef:false,
spec:"Z://7.152.154.8P/:%96%bf:'@%4e/@'/C/@%7b%a8%00/@~@:5/1$?@",
prePath:"Z://7.152.154.8"}
,
{path:"",
ref:"/",
pathQueryRef:"#/",
host:"[vd.:]",
hostPort:"[vd.:]",
hasRef:true,
spec:"//[vd.:]#/",
prePath:"//[vd.:]"}
,
{path:"",
scheme:"W",
pathQueryRef:"",
host:"[::3afb:1cfb:c172%25-]",
hostPort:"[::3afb:1cfb:c172%25-]",
hasRef:false,
spec:"W://[::3afb:1cfb:c172%25-]",
prePath:"W://[::3afb:1cfb:c172%25-]"}
,
{path:"-",
pathQueryRef:"-",
host:"[4ca2::b567:2315:770c:253.206.253.82%25-]",
hostPort:"[4ca2::b567:2315:770c:253.206.253.82%25-]",
hasRef:false,
spec:"//[4ca2::b567:2315:770c:253.206.253.82%25-]-",
prePath:"//[4ca2::b567:2315:770c:253.206.253.82%25-]"}
,
{path:"G/(:=,%ba!%ea~@/@-:/$&_/-,:%f7;!%cc:/@%c5::@~.!/$%44:(~.@:/(+@*$:/%38:)%4e:&'",
scheme:"V",
pathQueryRef:"G/(:=,%ba!%ea~@/@-:/$&_/-,:%f7;!%cc:/@%c5::@~.!/$%44:(~.@:/(+@*$:/%38:)%4e:&'",
host:"[vc.G]",
hostPort:"[vc.G]",
hasRef:false,
spec:"V://[vc.G]G/(:=,%ba!%ea~@/@-:/$&_/-,:%f7;!%cc:/@%c5::@~.!/$%44:(~.@:/(+@*$:/%38:)%4e:&'",
prePath:"V://[vc.G]"}
,
{path:"",
scheme:"L",
pathQueryRef:"",
host:"[1fb3:4ca5::%25K]",
hostPort:"[1fb3:4ca5::%25K]",
hasRef:false,
spec:"L://[1fb3:4ca5::%25K]",
prePath:"L://[1fb3:4ca5::%25K]"}
,
{path:"!",
pathQueryRef:"!",
host:"[::fbb0:a89f:16b6:606a:4c6a:beaf:f4fb]",
hostPort:"[::fbb0:a89f:16b6:606a:4c6a:beaf:f4fb]",
hasRef:false,
spec:"//[::fbb0:a89f:16b6:606a:4c6a:beaf:f4fb]!",
prePath:"//[::fbb0:a89f:16b6:606a:4c6a:beaf:f4fb]"}
,
{path:")",
pathQueryRef:")",
host:"[b9e4:020d:a726::ec2d:780e:57.227.2.218]",
hostPort:"[b9e4:020d:a726::ec2d:780e:57.227.2.218]",
hasRef:false,
spec:"//[b9e4:020d:a726::ec2d:780e:57.227.2.218])",
prePath:"//[b9e4:020d:a726::ec2d:780e:57.227.2.218]"}
,
{path:"",
scheme:"H",
pathQueryRef:"",
host:"212.255.251.209",
hostPort:"212.255.251.209",
hasRef:false,
spec:"H://212.255.251.209",
prePath:"H://212.255.251.209"}
,
{path:"@",
pathQueryRef:"@",
host:"255.6.171.160",
hostPort:"255.6.171.160",
hasRef:false,
spec:"//255.6.171.160@",
prePath:"//255.6.171.160"}
,
{path:"&/:@@;$/@:+*//%3e.7.*-@%89:",
scheme:"XU",
pathQueryRef:"&/:@@;$/@:+*//%3e.7.*-@%89:",
host:"0.252.69.83",
hostPort:"0.252.69.83",
hasRef:false,
spec:"XU://0.252.69.83&/:@@;$/@:+*//%3e.7.*-@%89:",
prePath:"XU://0.252.69.83"}
,
{path:".",
scheme:"H",
pathQueryRef:".",
userPass:"&",
host:"4.251.17.254",
hostPort:"4.251.17.254",
hasRef:false,
spec:"H://&@4.251.17.254.",
prePath:"H://&@4.251.17.254"}
,
{path:")",
scheme:"I",
pathQueryRef:")",
host:"[v7.;]",
hostPort:"[v7.;]",
hasRef:false,
spec:"I://[v7.;])",
prePath:"I://[v7.;]"}
,
{path:"/",
scheme:"Q",
pathQueryRef:"/",
userPass:"!",
host:"()5",
hostPort:"()5",
hasRef:false,
spec:"Q://!@()5/",
prePath:"Q://!@()5"}
,
{path:":/:::_=:%1e,.",
scheme:"Z",
pathQueryRef:":/:::_=:%1e,.",
userPass:".",
host:"%af+%a8~~-+..%24",
hostPort:"%af+%a8~~-+..%24",
hasRef:false,
spec:"Z://.@%af+%a8~~-+..%24:/:::_=:%1e,.",
prePath:"Z://.@%af+%a8~~-+..%24"}
,
{path:"L//+/:%ce/L&@)@.%a2%4f*/$@-/%c2~@/+@%10)%aa%3b*/_::@=:/_:!%c2",
scheme:"S",
pathQueryRef:"L//+/:%ce/L&@)@.%a2%4f*/$@-/%c2~@/+@%10)%aa%3b*/_::@=:/_:!%c2",
userPass:"-",
host:"~%df*%f3*%0a",
hostPort:"~%df*%f3*%0a",
hasRef:false,
spec:"S://-@~%df*%f3*%0aL//+/:%ce/L&@)@.%a2%4f*/$@-/%c2~@/+@%10)%aa%3b*/_::@=:/_:!%c2",
prePath:"S://-@~%df*%f3*%0a"}
,
{path:"%1b//:@@@%ee2:.@///&:%20:/%8c:/39%5c-%fa::W%48/'/%9f:@P%079.-(",
scheme:"A",
pathQueryRef:"%1b//:@@@%ee2:.@///&:%20:/%8c:/39%5c-%fa::W%48/'/%9f:@P%079.-(?/",
host:"5.147.90.7",
hostPort:"5.147.90.7",
hasRef:false,
spec:"A://5.147.90.7%1b//:@@@%ee2:.@///&:%20:/%8c:/39%5c-%fa::W%48/'/%9f:@P%079.-(?/",
prePath:"A://5.147.90.7"}
,
{path:"/:/@/;_:&=%67%c7%ba/:%a2%2e://@/2@%0cQ::",
scheme:"E",
pathQueryRef:"/:/@/;_:&=%67%c7%ba/:%a2%2e://@/2@%0cQ::",
userPass:"*",
host:"122.255.22.251",
hostPort:"122.255.22.251",
hasRef:false,
spec:"E://*@122.255.22.251/:/@/;_:&=%67%c7%ba/:%a2%2e://@/2@%0cQ::",
prePath:"E://*@122.255.22.251"}
,
{path:"&",
pathQueryRef:"&",
host:"[edbc:15ab:1e39:3b77:209f:a3e6:78fe:dccc%252]",
hostPort:"[edbc:15ab:1e39:3b77:209f:a3e6:78fe:dccc%252]",
hasRef:false,
spec:"//[edbc:15ab:1e39:3b77:209f:a3e6:78fe:dccc%252]&",
prePath:"//[edbc:15ab:1e39:3b77:209f:a3e6:78fe:dccc%252]"}
,
{path:"/:/:/+I%cd)%ba-~://&@/@=:%e0@0:/:/@/=@%e4%dc:%36./:-:/%aa)!%8b:@*.*",
scheme:"O",
pathQueryRef:"/:/:/+I%cd)%ba-~://&@/@=:%e0@0:/:/@/=@%e4%dc:%36./:-:/%aa)!%8b:@*.*",
userPass:"2",
host:"125.104.183.150",
hostPort:"125.104.183.150",
hasRef:false,
spec:"O://2@125.104.183.150/:/:/+I%cd)%ba-~://&@/@=:%e0@0:/:/@/=@%e4%dc:%36./:-:/%aa)!%8b:@*.*",
prePath:"O://2@125.104.183.150"}
,
{path:"=",
pathQueryRef:"=",
host:"[1cf6::7afa:be8b:0ecd:0347:250.125.48.178]",
hostPort:"[1cf6::7afa:be8b:0ecd:0347:250.125.48.178]",
hasRef:false,
spec:"//[1cf6::7afa:be8b:0ecd:0347:250.125.48.178]=",
prePath:"//[1cf6::7afa:be8b:0ecd:0347:250.125.48.178]"}
,
{path:"~",
scheme:"R",
pathQueryRef:"~",
host:"[v7.+]",
hostPort:"[v7.+]",
hasRef:false,
spec:"R://[v7.+]~",
prePath:"R://[v7.+]"}
,
{path:":/:%fc//~_~%51/_%bf$%bc",
scheme:"X",
pathQueryRef:":/:%fc//~_~%51/_%bf$%bc",
userPass:"=",
host:"200.207.9.148",
hostPort:"200.207.9.148",
hasRef:false,
spec:"X://=@200.207.9.148:/:%fc//~_~%51/_%bf$%bc",
prePath:"X://=@200.207.9.148"}
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
  do_info("testing " + aTest.spec + " instanceof nsIURL");
  Assert.equal(URI instanceof Ci.nsIURL, aTest.nsIURL);
  do_info("testing " + aTest.spec + " instanceof nsINestedURI");
  Assert.equal(URI instanceof Ci.nsINestedURI, aTest.nsINestedURI);

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
