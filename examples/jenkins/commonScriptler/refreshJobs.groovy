/***** 
* This is a scriptler script.  This will add and update jobs from disk.
* Set the following parameter in the script:  _JobPrefix which will only
* update jobs that start with that prefix
******/

import hudson.model.*
import javax.xml.transform.Source;

def jenkins = hudson.model.Hudson.instance

def _PROJECT = null
try {
  _PROJECT = PROJECT
}catch(Exception e){

}
println(_PROJECT)

//Anything found in this hash will not be updated!
def EXCLUDE_JOB = [
  "EXCLUDE_ME":true
]
def JobDir = new File("/var/lib/jenkins/jobs/")
def _addOnly = "F"


JobDir.eachDir {
  try{
    if (EXCLUDE_JOB.get(it.name) == null) {


      if( (it.name.startsWith(_JobPrefix) && (_PROJECT==null || _PROJECT.equalsIgnoreCase('ALL'))) || (it.name.startsWith(_JobPrefix) && it.name.contains(_PROJECT))   ){

        //if(it.name.startsWith(_JobPrefix)){
        def input = new File(it.canonicalPath + '/config.xml')
        if(input.exists()){
          InputStream is = new ByteArrayInputStream(input.text.getBytes("UTF-8"));

          def itemFound = jenkins.getItem(it.name);
          project = (AbstractProject) itemFound;

          if(project==null){
            println "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
            println "Creating New Job - ${it.canonicalPath}";
            println "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";

            jenkins.createProjectFromXML(it.name,is);

          }else if(_addOnly.equals("F")){
            println "Updating Job - ${it.canonicalPath}";
            project.updateByXml((Source)new javax.xml.transform.stream.StreamSource(is));
          }
        }
      }
    } else {
      println "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
      println "Skipping Job - ${it.canonicalPath} due to exclude hash.";
      println "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
    }
  }catch(Exception e){
    println e
  }
}