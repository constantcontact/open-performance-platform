#!/bin/bash
echo "***** Starting process to rebuild project from scratch since EXTJS gets weird when moving around on computers and directories"
cd ../
echo "***** removing existing tmp directory if it exists"
rm -rf ui2
rm -rf ui-old
echo "***** download extjs 6.2 ****"
curDirectory=`pwd`
cd /tmp
if [ ! -f /tmp/ext-6.2.0-gpl.zip ]; then
    ggID='0ByXdqj-nIL8PVGNNR1Y0U3JvSXc'  
    ggURL='https://drive.google.com/uc?export=download'  
    filename="$(curl -sc /tmp/gcokie "${ggURL}&id=${ggID}" | grep -o '="uc-name.*</span>' | sed 's/.*">//;s/<.a> .*//')"  
    getcode="$(awk '/_warning_/ {print $NF}' /tmp/gcokie)"  
    curl -Lb /tmp/gcokie "${ggURL}&confirm=${getcode}&id=${ggID}" -o "${filename}"  
    cd "$curDirectory"
fi
unzip ext-6.2.0-gpl.zip
echo "***** creating new project"
sencha -sdk /tmp/ext-6.2.0 generate app CCPerf ui2
rm -rf /tmp/ext-6.2.0
# keep around in case you need it again, but uncomment to delete it
# rm -f /tmp/ext-6.2.0-gpl.zip
echo "***** copying key project files to new project"
cp -f ui/app.json ui2/app.json
cp -rf ui/app ui2/
cp -f ui/index.html ui2/
cp -f ui/app.js ui2/
cp -rf ui/sass ui2/
cp -rf ui/resources ui2/
echo "***** building new project"
cd ui2
sencha app build -c production
echo "***** swapping directories to make ui2 dev and dev ui-old"
cd ..
mv ui ui-old
mv ui2 ui
echo "***** everything should be good now"
echo "***** once confirmed, you can delete the ui-old directory"
