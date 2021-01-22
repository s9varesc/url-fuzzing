# url-fuzzing

## Firefox

[Obtaining and Using a Firefox Coverage Build](firefox/firefox_coverage_build.ipynb)
[URL Fields in Firefox](firefox/FirefoxVerification.ipynb)


## Chromium

[Chromium Coverage Build](chromium/ChromiumBuild.md)
[URL Components in Chromium](chromium/ChromiumVerification.md)

## Languages

[List of Languages and Tools](LanguagesTBFuzzed.ipynb)

## Grammars

[Strictly RFC Based](./grammars/rfc-url.scala)
[Completely Living Standard Based](./grammars/livingstandard-url-base.scala)
[Living Standard Based with Additions](./grammars/living-standard-url.scala)


## Test Generation

[Adding Component Generation to tribble](AddToTribble.md)
[Generating URL Components Implementation](URLComponents.ipynb)


## Experiment Execution

Fuzzing the different URL-parsers can be achieved by manually creating instrumented builds of the respective parsers, manually using tribble with code additions to generate test inputs, manually executing the URL-parsers on these inputs, and manually calling the tools creating a nice representation of the results.
Recreating the exact neccessary execution environments is achievable much more comfortable by using the respective [dockerfiles](https://github.com/s9varesc/url-fuzzing-docker) provided. These should also be used as guide to manually recreate the experiments if desired.

 
