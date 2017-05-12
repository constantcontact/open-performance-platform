#!/bin/bash

###### Configs #######
source ./aws-config.sh
maxMinutesToWait=10
EC2_JSON_CONFIG_FILE=aws-opp-docker.json
INIT_SCRIPT="--user-data file://aws-build-opp-docker-on-instance.sh"

SECONDS=0 #start timer

#GRAB JSON and replace env variables --- handle true|false params by removing quotes.  Need them in JSON file to make valid for minification, but need them removed to send to AWS since its replaced with boolean values
JSON=$(jq -c . < $EC2_JSON_CONFIG_FILE)
JSON_PARAMETERIZED=$(echo $(eval echo "'$JSON'") | sed -e 's/"true"/true/' | sed -e 's/"false"/false/')

# start and get instance id
INSTANCE=`aws ec2 run-instances $PROFILE $INIT_SCRIPT --cli-input-json $JSON_PARAMETERIZED`
INSTANCE_ID=`echo $INSTANCE | jq '.Instances[] | .InstanceId' | sed 's/\"//g'` 
echo "Started instance with ID: $INSTANCE_ID"
echo "Checking instance status..."
sleep 10

finished=false
counter=1
sleepTime=20
counterMax=$((maxMinutesToWait*60/sleepTime))
while ! $finished; do
    sleep $sleepTime
    # get instance status - expecting - "initializing" and waiting till it says "ok"
    INSTANCE_STATUS=`aws ec2 $PROFILE describe-instance-status --instance-ids $INSTANCE_ID | jq '.InstanceStatuses[] | .InstanceStatus.Status'`
    
    if [[ "$INSTANCE_STATUS" == "\"ok\"" ]]; then
        echo "Awesome... server is up and running"
        finished=true
    else
        echo "Still initializing with status: $INSTANCE_STATUS"
        counter=$((counter+1))
    fi
   
    # if counter is over 30, try to terminate instance and kill job
    if (( counter > counterMax )); then 
        finished=true
        echo "More than 10 minutes have passed and instance is not yet ready.  Killing instance $INSTANCE_ID"
        aws ec2 $PROFILE terminate-instances --instance-ids $INSTANCE_ID
        failureTime=$((SECONDS/60))
        echo "Failed after $failureTime minutes"
        exit 1
    fi
done

completeTime=$((SECONDS/60))
echo "Container started in $completeTime minutes | Status: $INSTANCE_STATUS"