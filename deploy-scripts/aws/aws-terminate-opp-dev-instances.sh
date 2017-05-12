#!/bin/bash

# terminate running instances
RUNNING_INSTANCES=(`aws ec2 --profile ctctperformance describe-tags --filters "Name=value,Values=development" | jq '.Tags[].ResourceId' | sed -n -e 'H;${x;s/\n/ /g;s/^,//;p;}' | sed 's/\"//g' | sed 's/^[ \t]*//'`)
echo "terminating... ${RUNNING_INSTANCES[@]}"
aws ec2 --profile ctctperformance terminate-instances --instance-ids "${RUNNING_INSTANCES[@]}" | jq '.TerminatingInstances[] | .InstanceId + "|| Previous Status: " + .PreviousState.Name + "| Current Status: " + .CurrentState.Name'
