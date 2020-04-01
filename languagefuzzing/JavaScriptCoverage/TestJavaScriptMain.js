

var URI = require('urijs');
var fs = require('fs');
var readline = require('readline');

var rd = readline.createInterface({
    input: fs.createReadStream('/home/vera/url-fuzzing/languagefuzzing/exampleURLs/plainURLs'),
    output: '',
    console: false
});

rd.on('line', function(line) {
    try {
        const url=new URI(line);
    }
    catch(err){}
});



