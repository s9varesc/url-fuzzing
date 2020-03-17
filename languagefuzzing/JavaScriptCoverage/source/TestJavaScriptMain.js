

var URL = require('url').URL;
var fs = require('fs'),
    readline = require('readline');

var rd = readline.createInterface({
    input: fs.createReadStream('/home/vera/url-fuzzing/languagefuzzing/exampleURLs/plainURLs'),
    output: process.stdout,
    console: false
});

rd.on('line', function(line) {
    try {
        const url=new URL(line);
    }
    catch(err){}
});



