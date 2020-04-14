<?php
require "./vendor/league/uri/src/UriString.php";
require "./vendor/league/uri-interfaces/src/Contracts/UriException.php";



foreach (glob("./vendor/league/uri-interfaces/src/Exceptions/*.php") as $filename)
{
    require $filename;
}




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
  $url= substr($url, 0, -1);
  //parse_url($url);
  
try {
    UriString::parse($url);
} catch (Exception $e) {
   $exceptions.="\n{ url:\"".$url."\",\n exception:\"".$e->getMessage()."\"},";
}
  }

fclose($file);
file_put_contents('PHPExceptions.txt', "[".substr($exceptions, 0, -1)."]"); 
$this->assertTrue(True);
}
}
?>
