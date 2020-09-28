

var URI = require('urijs');
var fs = require('fs');
var readline = require('readline');
var exceptions='';

var rd = readline.createInterface({
    input: fs.createReadStream('../urls/plainURLs'),
    output: '',
    console: false
});

rd
  .on('line', function(line) {
    try {
        const url=new URI(line);
    }
    catch(err){
	exceptions+="\n{ url:\""+line+"\",\n exception:\""+err.message+"\"},";
    }
  })
  .on('close', function(){
	    fs.writeFile('JavaScriptExceptionsurijs.txt', exceptions.substring(0,exceptions.length-1), (err) => {  
    if (err) throw err; 
     }) 
  });


