import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def jobDir = '/var/lib/jenkins/jobs/'
def tmpHash = null
def loadTests = [:]
def thr1 = Thread.currentThread()
def build1 = thr1?.executable
def buildVersion = build1.getId()
println "param build.getId() value : ${buildVersion}"

def slurper = new JsonSlurper()

def resp = restCall('http://opp-svc.mydomain.com/loadsvc/v1/ci/loadtestjobs?testType=cloudtest', "GET", "")
def jsonRes

try {
   	jsonRes = slurper.parseText(resp)
	 
} catch(Exception ex){
	println "ERROR: " + ex.message
}




//*********************************************
//BUILDER CODE BELOW - DO NOT TOUCH
//*********************************************

def input = new File(jobDir + 'Template-CloudTest-V2/config.xml')
def template = input.text
def jobPrefix = 'Load-CloudTest-'


jsonRes.each() { soastaCompositionPath, testParams -> 
  
  // create a job for each environment
  testParams.environment.each() { envir ->
    // create job name
    def tdir = jobDir + jobPrefix + '-' + testParams.test_name;
    println tdir; // log

    def newDir = new File(tdir)
     def wasCreated = true
    if(newDir.exists()){
        println "directory already exists"
    } else {
       wasCreated = newDir.mkdir();
       println "directory does not exist... creating...  " + (wasCreated) ? "SUCCESS" : "FAILED"
    }
  
    // get template XML
    def tempJobXml = template
   
    // loop through all the parameters and update the XML
    testParams.each() { paramName, paramValue ->
      paramName = paramName.toUpperCase(); // capitalize to match template
      if(paramName.equals('ENVIRONMENT')){
         // set environment variable
         tempJobXml  = tempJobXml.replace("~" + paramName  +"~", envir.trim()) 
}
      else if(paramName.equals('CRON_SCHEDULE'))
{
       tempJobXml  = tempJobXml.replace("1 1 1 1 1",paramValue) 

   }else{
        // set all other variables
        tempJobXml  = tempJobXml.replace("~" + paramName  +"~",paramValue.toString().trim())
      }
    }
    
    // set the full soasta path
    tempJobXml  = tempJobXml.replace("~CT_COMPOSITION_FULL_PATH~",soastaCompositionPath) 
    
    // set some misc job fields
    tempJobXml  = tempJobXml.replace("BUILT_FROM_TEMPLATE_FALSE","BUILT_FROM_TEMPLATE_TRUE")
    tempJobXml  = tempJobXml.replace("~BUILD_VERSION~",buildVersion) 

    // BUILD RUN JOB by overriding XML
    new File(tdir + '/config.xml').write(tempJobXml)
    println "Success " + tdir 

  }
}


def restCall(def strUrl, def method, def queryString){
  def url = new URL(strUrl)
  def conn = url.openConnection()
  conn.setRequestMethod(method)
  conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
  conn.setRequestProperty("Accept","*/*")
  if(method == "POST" || method == "PUT"){
     conn.doOutput = true
     if(queryString.trim() != '') {
       def writer = new OutputStreamWriter(conn.outputStream)
       writer.write(queryString)
       writer.flush()
       writer.close()
    }
  } else {
    conn.doOutput = false
  }

  conn.connect()

  def respText = conn.content.text
  println "====== RESPONSE CODE: " + conn.responseCode
  println "====== RESPONSE TEXT: " + respText

  return respText
}