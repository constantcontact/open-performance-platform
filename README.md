# OPP (Open Performance Platform)
Open Performance Platform - Trending and performance analysis for both server-side and client-side testing

# Deploying
Deploying the platform with docker is very easy.

`docker-compose up -d`

# Developing
Developing with the platform is easy as well.

## APIs
The APIs are written in Java (Spring boot) and uses gradle as the build tool.  

To build the application:
`./gradlew clean build`

To run the application:
`./gradlew clean bootRun`

To create a new docker image:
`./gradlew build buildDocker`

You will need to create the new docker image before you can see your changes when you run docker-compose.

## UI

### Load Testing UI
The load testing UI is in EXTJS.  This has is benefits as its all code an no actual UI design (yay!).  It was a good fit for building this reporting system without much UI effort.

Getting starting with EXTJS.

You will need to install their Sencha CMD which can be found on their website.  

https://www.sencha.com/products/extjs/cmd-download/

This tested as working on version 5.1.3.x

Next you will need to run the file `opp-ui/init.sh`.  This will extract EXTJS for you.

From there, you can go to this folder `opp-ui/load/dev` to see the app code.

To build the app run the build.sh file in that directory.  If for some reason its not working (very likely with EXTJS as it can be a pain) try the `opp-ui/script/fixExtProject.sh` script I created.  It will essentially rebuild a new project from scratch and copy the important files over.  
