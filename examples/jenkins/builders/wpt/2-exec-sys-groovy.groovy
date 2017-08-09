import com.roadrunner.service.WptService;


// **********************************************************
// DO NOT TOUCH ANY OF THIS BUILDER CODE
// **********************************************************

// Initializations

jobDir = '/var/lib/jenkins/jobs/'
wptRootPath = '/tmp/performance/WPT/Tests/'
wptTests = [:]
thr1 = Thread.currentThread()
build1 = thr1?.executable
buildVersion = build1.getId()
println "param build.getId() value : ${buildVersion}"

/*
Location Syntax: LOCATION:BROWSER.CONNECTION
Locations: Bedford_W7_02_Chrome-FFX-IE11,  Bedford_W7_02_Chrome-FFX-IE10, Bedford_W7_Chrome-FFX,  Bedford_W7_IE9
Browsers: IE 9, IE 10, IE 11, Firefox, Chrome
Connections: FIOS, Cable, DSL, Dial, 4G, 3G, Native
*/


/*
Parameter	                  Description	           Default
url	                          URL to be tested	
label	                          Label for the test	
location	                  Location to test from	Dulles 5Mbps Cable.  Syntax: LOCATION_NAME:BROWSER.CONNECTION
runs	                          Number of test runs (1-10 on the public instance)	1
fvonly	                  Set to 1 to skip the Repeat View test	0
domelement	          DOM Element to record for sub-measurement	
private	                  Set to 1 to keep the test hidden from the test log	0
connections	          Override the number of concurrent connections IE uses (0 to not override)	0
web10	                  Set to 1 to force the test to stop at Document Complete (onLoad)	0
script                          Scripted test to execute	
block	                          space-delimited list of urls to block (substring match)	
login	                          User name to use for authenticated tests (http authentication)	
password	                  Password to use for authenticated tests (http authentication)	
authType	                  Type of authentication to use: 0 = Basic Auth, 1 = SNS	0
video	                          Set to 1 to capture video	0
f	                          Format. Set to "xml" to request an XML response instead of a redirect ("json" is also provisionally supported)	
r	                          When using the xml interface, will echo back in the response	
notify	                  e-mail address to notify with the test results	
pingback	                  URL to ping when the test is complete (the test ID will be passed as an "id" parameter)	
bwDown	                  Download bandwidth in Kbps (used when specifying a custom connectivity profile)	
bwUp	                          Upload bandwidth in Kbps (used when specifying a custom connectivity profile)	
latency	                  First-hop Round Trip Time in ms (used when specifying a custom connectivity profile)	
plr	                          Packet loss rate - percent of packets to drop (used when specifying a custom connectivity profile)	
k	                          API Key (if assigned) - applies only to runtest.php calls. Contact the site owner for a key if required (pmeenan@webpagetest.org for the public instance)		
tcpdump	                  Set to 1 to enable tcpdump capture	 0
noopt	                  Set to 1 to disable optimization checks (for faster testing)	0
noimages	                  Set to 1 to disable screen shot capturing	0
noheaders	                  Set to 1 to disable saving of the http headers (as well as browser status messages and CPU utilization)	0
pngss	                  Set to 1 to save a full-resolution version of the fully loaded screen shot as a png	
iq	                          Specify a jpeg compression level (30-100) for the screen shots and video capture	
noscript	                  Set to 1 to disable javascript (IE, Chrome, Firefox)	
clearcerts	                  Set to 1 to clear the OS certificate caches (causes IE to do OCSP/CRL checks during SSL negotiation if the certificates are not already cached). Added in 2.11	 0
mobile	                  Set to 1 to have Chrome emulate a mobile browser (screen resolution, UA string, fixed viewport).  Added in 2.11	 0
mv	                          Set to 1 when capturing video to only store the video from the median run.	 0
cmdline	                  Custom command-line options (Chrome only)	
htmlbody	                  Set to 1 to save the content of the first response (base page) instead of all of the text responses (bodies=1)	
*/

//old legacy locations, keep to not break old scripts
locationsChromeFFX = [ "Bedford_W7_01_Chrome-FFX-IE11",  "Bedford_W7_02_Chrome-FFX-IE10", "Bedford_W7_Chrome-FFX"]
locationsChromeFFX2 = [ "Bedford_W7_01_Chrome-FFX-IE11",  "Bedford_W7_02_Chrome-FFX-IE10"]

// new locations
locationsCcUsEast = [ region:"cc-us-east", wptLocationNames: ["Bedford_W7_01_Chrome-FFX-IE11", "Bedford_W7_02_Chrome-FFX-IE10", "Bedford_W7_Chrome-FFX"]]
locationsCcUsWest = [ region: "cc-us-west", wptLocationNames: []]
locationsAwsUsEast = [ region:"aws-us-east", wptLocationNames:["EC2_US_East_Ws12_Chrome-FFX-IE11"]]
locationsAwsUsWest = [ region:"aws-us-west", wptLocationNames:["EC2_US_West_Ws12_Chrome-FFX-IE11"]]
locationsAwsEuDe = [ region:"aws-eu-de", wptLocationNames:["EC2_EU_Frankfurt_Ws12_Chrome-FFX-IE11"]]




DEFAULT_HASH = 
[
     wpturl:'http://wpt.roving.com/runtest.php',
     environment:[''],
     url:'',
     label:'',
     location:'Bedford_W7_01_Chrome-FFX-IE11:Firefox.Cable',
     runs:'6',
     fvonly:'',
     domelement:'',
     private:'',
     connections:'',
     web10:'',
     script:'',
     block:'',
     login:'',
     password:'',
     authType:'',
     video:'1',
     f:'json',
     r:'',
     notify:'',
     pingback:'',
     bwDown:'',
     bwUp:'',
     latency:'',
     plr:'',
     k:'',
     tcpdump:'',
     noopt:'',
     noimages:'',
     noheaders:'',
     pngss:'',
     iq:'',
     noscript:'',
     clearcerts:'',
     mobile:'',
     mv:'',
     cmdline:'',
     htmlbody:'',
  	 saveToGraphite:'FALSE',
  	 slas:'',
     hipchatRoomName:'Perf Jenkins'
]


// Grab and evaluate test files to build jobs with

println ("\n*** START: Read each groovy test file for job building processing ***")

new File(wptRootPath).eachFileRecurse {
   if (it.name.endsWith('.groovy')) {
      print ("Evaluating (read in): " + it.name)
       try {
              evaluate(it)
              println ("...SUCCESS")
          } catch (Exception e)  {
             println ("\nERROR with script: " + it.name + "\n" + e.getMessage())
             println (it.text)
         }
     }
}

println ("*** END: Read each groovy test file for job building processing ***")


//*********************************************
// JOB BUILDER CODE BELOW
//*********************************************

println ("\n*** START: Build WPT JOBS ***")
WptService wptService = new WptService(); // from RoadRunner plugin
input = new File(jobDir + 'Template-WebPageTest-V2/config.xml')
template = input.text
testname = ""
envNames = ["s1", "l1", "f1", "d1", "p2"]

for(test in wptTests.keySet()){

  testname = test.tokenize('/').last()

  for(envir in wptTests.get(test).get('environment')){
	// only set if the environment name doesn't already exist
	//envirName = (envNames.contains(testname.take(2))) ? '' : envir + '-'; // --- old, i don't think we need this anymore
    if(wptService.labelValidation(testname) != ""){
      println "***** ERROR: Test name validation FAILED for " + testname 
      continue;
    } else {
      println "Test name validation PASSED for " + testname  
    }
    //tdir = jobDir + 'UX-WPT-' + envirName + testname   // --- old, i don't think we need this anymore
    tdir = jobDir + 'UX-WPT-' + testname
    //println tdir
    wasCreated = new File(tdir).mkdir()

    tempJob = template
    for(fieldName in wptTests.get(test).keySet()){
      if(fieldName.equals('environment')){
         tempJob  = tempJob.replace("~" + fieldName  +"~", envir.trim())
      }else{
        tempJob  = tempJob.replace("~" + fieldName  +"~",wptTests.get(test).get(fieldName).toString().trim())
      }
    }

    tempJob  = tempJob.replace("BUILT_FROM_TEMPLATE_FALSE","BUILT_FROM_TEMPLATE_TRUE")
    tempJob  = tempJob.replace("~BUILD_VERSION~",buildVersion) 

    //println tempJob

    //BUILD RUN JOB
    new File(tdir + '/config.xml').write(tempJob)
    println "Success " + tdir 

  }

}
println ("*** END: Build WPT JOBS ***\n")