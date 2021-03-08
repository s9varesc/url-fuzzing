

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
    var baseandrel=line.split("<");
    try {
        if(baseandrel[1]!=undefined){
          const url=new URI(baseandrel[1], baseandrel[0]);
        }
        else{
          const url=new URI(line);
        }
        
    }
    catch(err){
	exceptions+="\n{\"url\":\""+line+"\", \"exception\":\""+err.message+"\"}";
    }
  })
  .on('close', function(){
	    fs.writeFile('JavaScriptExceptionsurijs.txt', exceptions, (err) => {  
    if (err) throw err; 
     }) 
  });



