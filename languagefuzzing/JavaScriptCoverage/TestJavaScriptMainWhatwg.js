

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
		var baseandrel=line.split("<");
		try {
		    if(baseandrel[1]!=undefined){
		      const url=new whatwg.URL(baseandrel[1], baseandrel[0]);
		    }
		    else{
		      const url = new whatwg.URL(line)
		    }
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





