PROFILE="" #aws profile to use for ec2 cli
INSTANCE_TYPE=t2.large
ENV=development
EBS_DELETE_ON_TERMINATION=true    # keep true for dev
AMI=ami-c58c1dd3
SECURITY_GROUP=sg-19e03267

maxMinutesToWait=10
EC2_JSON_CONFIG_FILE=aws-opp-docker.json
INIT_SCRIPT="--user-data file://aws-build-opp-docker-on-instance.sh"
