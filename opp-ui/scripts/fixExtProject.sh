#!/bin/bash
echo "***** Starting process to rebuild project from scratch since EXTJS gets weird when moving around on computers and directories"
cd ../load
echo "***** removing existing tmp directory if it exists"
rm -rf dev2
rm -rf dev-old
echo "***** creating new project"
unzip ../dependencies/extjs-5.1.1.zip -d /tmp
sencha -sdk /tmp/extjs-5.1.1 generate app CCPerf dev2
rm -rf /tmp/extjs-5.1.1.zip
echo "***** copying key project files to new project"
cp -f dev/app.json dev2/app.json
cp -rf dev/app dev2/
cp dev/build.sh dev2/
cp -f dev/index.html dev2/
cp -f dev/app.js dev2/
echo "***** building new project"
chmod +x dev2/build.sh
cd dev2
./build.sh
echo "***** swapping directories to make dev2 dev and dev dev-old"
cd ..
mv dev dev-old
mv dev2 dev
echo "***** everything should be good now"
