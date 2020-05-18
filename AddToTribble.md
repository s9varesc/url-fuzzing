# Adding Components Generation to tribble

The following commands add the classes necessary for also generating the components to [tribble](https://github.com/havrikov/tribble/tree/7797acd8801e48cbedb86485032f577cee8ea94c):

'''cp -r path/to/url-fuzzing/tribble-additions/* path/to/tribble/src/main/scala/saarland/cispa/se/tribble/execution 

cd path/to/tribble
./gradlew build 
mv ./build/libs/tribble-0.1.jar tribble.jar'''

Execution with a corresponding grammar:
'''java -jar tribble.jar generate --mode=2-path-30 --suffix=.md --grammar-file=/path/to/url-fuzzing/livingstandard-url.scala --out-dir=/path/to/URLTestFilesRaw'''

The output directory then contains files containing only a generated URL, named "fileXXX_YYY.md" and files containing the componentwise representation (including the full URL), named "components_fileXXX_YYY.md".