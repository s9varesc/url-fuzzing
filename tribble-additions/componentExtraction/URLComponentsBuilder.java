package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

/***
* abstract class for creating URL component representations
*/
public abstract class URLComponentsBuilder implements ComponentsBuilder{
  UniversalURLComponentsBuilder universalcomponentrep;

  public URLComponentsBuilder(UniversalURLComponentsBuilder univcomp){
    this.universalcomponentrep=univcomp;
  }
  
}
