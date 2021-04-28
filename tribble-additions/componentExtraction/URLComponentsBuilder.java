package de.cispa.se.tribble.componentExtraction;

import java.util.*;

/***
* abstract class for creating URL component representations
*/
public abstract class URLComponentsBuilder extends ComponentsBuilder{
  UniversalURLComponentsBuilder univcomp;

  public URLComponentsBuilder(UniversalURLComponentsBuilder univcomp){
    this.univcomp=univcomp;
  }
  
}
