

var whatwg = require('whatwg-url');
var fs = require('fs');
var readline = require('readline');
let exceptions = '';

var rd = readline.createInterface({
    input: fs.createReadStream('../urls/plainURLs'),
    output: '',
    console: false
});
rd
	.on('line', function(line) { 
	     
	    try {
		//line=line.substring(0,line.length-1)
		const url = new whatwg.URL(line)
		//console.log(url)
	    }
	    catch(err){
		//console.log(err);
		exceptions+= "\n{\"url\":\""+line+"\", \"exception\":\""+err.message+"\"}";
		
	    }
	})
	.on('close', function(){
	    fs.writeFile('JavaScriptExceptionswhatwg-url.txt', exceptions, (err) => {  
    if (err) throw err; 
}) 

	});





