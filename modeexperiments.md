### Tribble mode Experiments 
#### without relative URLs

Mode | nr URLs | Firefox | Chromium | whatwg-url |
--- | --- | --- | --- | --- |
2-path-30 | 213 | parsing errors: 9<br> component errors: 72 <br> coverage: 67,5% | parsing errors: 10<br> component errors: 75<br> coverage: 37,03% | parsing errors: 13 <br> coverage: 58,08% |
2-path-50 | 223 | parsing errors: 15<br> component errors: 81<br> coverage: 67,8% | parsing errors: 12<br> component errors: 72<br> coverage: 37,22% | parsing errors: 11<br> coverage: 57,93% |
2-path-70 | 223 | parsing errors: 12<br> component errors: 78<br> coverage: 67,8% | parsing errors: 14 <br> component errors: 69 <br> coverage: 37,02% | parsing errors: 14<br> coverage: 57,78% |
3-path-30 | 335 | parsing errors: 16 <br> component errors: 127 <br> coverage: 67,8% | parsing errors: 14 <br> component errors: 104<br> coverage: 37,22% | parsing errors: 14 <br>  coverage: 57,63% |
3-path-50 | 338 | parsing errors: 18 <br> component errors: 115 <br> coverage: 67,8% | parsing errors: 10 <br> component errors: 106<br> coverage:  37,03% |parsing errors: 12<br> coverage: 58,08% |
3-path-70 | 321 | parsing errors: 9<br> component errors: 119 <br> coverage: 67,8% | parsing errors: 10<br> component errors: 86 <br> coverage: 37,03% |parsing errors: 17 <br> coverage: 58,38% |
4-path-30 | 580 |  parsing errors: 12<br> component errors: 209 <br> coverage: 67,8% | parsing errors: 33 <br> component errors: 130 <br> coverage: 37,03% |parsing errors: 19 <br> coverage: 58,38% |
4-path-50 | 562 |  parsing errors: 17<br> component errors: 181<br> coverage:  67,8% | parsing errors: 18 <br> component errors: 121 <br> coverage: 37,03% |parsing errors: 23 <br> coverage:  58,38%|
4-path-70 | 541 | parsing errors: 19<br> component errors: 179 <br> coverage: 67,8% | parsing errors: 19 <br> component errors: 120 <br> coverage: 37,03% |parsing errors: 16 <br> coverage:  58,38|
5-path-30 | 765 | parsing errors: 17 <br> component errors: 354 <br> coverage: 67,8% | parsing errors: 24 <br> component errors: 250 <br> coverage: 37,22% |parsing errors: 8<br> coverage:  57,93% |  
5-path-50 | 757 | parsing errors: 12 <br> component errors: 374 <br> coverage: 67,8% | parsing errors: 36 <br> component errors: 252 <br> coverage: 37,03% |parsing errors: 23 <br>coverage: 58,38%|


#### with relative URLs

due to problems with generating a coverage report for chromium  in one build, there is a isolated run for chromium with a different build for each mode

Mode | nr URLs | Firefox | Chromium | whatwg-url |
--- | --- | --- | --- | --- |
2-path-50 | 192 | parsing errors: 17 <br> component errors: 89<br> coverage:  69,1% | parsing errors: 34<br> component errors: 82 <br> coverage: - |parsing errors: 2 <br> coverage: 65,42% |
2-path-50 | 237 | - |parsing errors: 43 <br> component errors:  70 <br> coverage: 49,25%| -|
3-path-50 | 371 | parsing errors:26 <br> component errors: 170 <br> coverage: 69,1% | parsing errors: 75 <br> component errors: 132 <br> coverage: - | parsing errors: 4 <br>  coverage: 65,72%
3-path-50 | 428 | - |parsing errors: 92 <br> component errors: 141 <br> coverage: 49,25% | -|
4-path-50 | 739	| parsing errors: 52 <br> component errors: 420<br> coverage: 69,7% |parsing errors: 182<br> component errors:  232<br> coverage: - | parsing errors: 13 <br> coverage: 66,02% |
4-path-50 | 928 | - |parsing errors: 183 <br> component errors: 237 <br> coverage: 49,25%| -|




























parsing errors: <br> component errors: <br> coverage: 