<?php
require "./vendor/league/uri-interfaces/src/Contracts/UriInterface.php";
require "./vendor/league/uri/src/UriString.php";
require "./vendor/league/uri/src/Uri.php";
require "./vendor/league/uri/src/UriResolver.php";
require "./vendor/league/uri-interfaces/src/Contracts/UriException.php";








foreach (glob("./vendor/league/uri-interfaces/src/Exceptions/*.php") as $filename)
{
    require $filename;
}




use PHPUnit\Framework\TestCase;
use League\Uri\Contracts\UriInterface;
use League\Uri\Uri;
use League\Uri\UriResolver;
use League\Uri\Exceptions\SyntaxError;

class PHPMainTest extends TestCase {
public function test_urls(){
$file = fopen("../urls/plainURLs","r");
$exceptions="";

while(! feof($file))
  {
  $url= fgets($file);
  //parse_url($url);
  $url=substr($url, 0, -1);
  $base="";
  $rel="";
  try{
  	list($base, $rel)=explode("<", $url);
  } catch(Exception $e){
  	$base="";
  	$rel="";
  }
  
try {
	if(!empty($rel)){
	    
	    $r=Uri::createFromBaseUri($rel, $base);
	    
	}else{
	    Uri::createFromString($url);
	}
    
} catch (Exception $e) {
   $exceptions.="\n{\"url\":\"".$url."\", \"exception\":\"".$e->getMessage()."\"}";
}
  }

fclose($file);
file_put_contents('PHPExceptions.txt', $exceptions); 
$this->assertTrue(True);
}
}
?>
