#!/bin/bash

# update system
yum update -y
yum install -y epel-release
 
# install docker
yum install -y yum-utils device-mapper-persistent-data lvm2 python-pip git
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# makes sure centos-extras is repo is setup. by default ctct removes this
yum install docker-ce
yum makecache fast
systemctl enable docker
systemctl start docker
 
# install docker-compose
pip install docker-compose
 
# configure for ElasticSearch support (min value of 262144)
sysctl -w vm.max_map_count=362144
 
####### Only use below if you want to be able to build the docker opp-service image on the server
 
# install java jdk and unzip for gradle install
yum install -y unzip java-1.8.0-openjdk-devel
 
# install gradle to /opt/gradle
gradle_version=2.9
mkdir /opt/gradle
wget -N http://services.gradle.org/distributions/gradle-${gradle_version}-all.zip
unzip -oq ./gradle-${gradle_version}-all.zip -d /opt/gradle
ln -sfnv gradle-${gradle_version} /opt/gradle/latest
printf "export GRADLE_HOME=/opt/gradle/latest\nexport PATH=\$PATH:\$GRADLE_HOME/bin" > /etc/profile.d/gradle.sh
. /etc/profile.d/gradle.sh
hash -r ; sync
# check installation
gradle -v
