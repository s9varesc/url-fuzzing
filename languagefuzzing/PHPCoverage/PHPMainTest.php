<?php
require "./vendor/league/uri/src/UriString.php";
require "./vendor/league/uri-interfaces/src/Contracts/UriException.php";
require "./vendor/league/uri-interfaces/src/Exceptions/SyntaxError.php";


use PHPUnit\Framework\TestCase;
use League\Uri\UriString;
use League\Uri\Exceptions\SyntaxError;

class PHPMainTest extends TestCase {
public function test_urls(){
$file = fopen("../urls/plainURLs","r");
$exceptions="";

while(! feof($file))
  {
  $url= fgets($file);
  //parse_url($url);
  
try {
    UriString::parse($url);
} catch (Exception $e) {
   $exceptions.="\n{ url:\"".$url."\",\n exception:\"".$e->getemssage()."\"},";
}
  }

fclose($file);
file_put_contents('PHPExceptions.txt', "[".substr($exceptions, 0, -1)."]"); 
$this->assertTrue(True);
}
}
?>
