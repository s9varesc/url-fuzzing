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

while(! feof($file))
  {
  $url= fgets($file);
  //parse_url($url);
  
try {
    UriString::parse($url);
} catch (Exception $e) {
   
}
  }

fclose($file);
$this->assertTrue(True);
}
}
?>
