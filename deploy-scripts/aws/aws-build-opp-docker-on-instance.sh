#!/bin/bash

# by default, this should be run as root
# setup EBS mount for docker volumes
DEVICE=/dev/xvdb
mkfs -t ext4 $DEVICE
mkdir /docker-volumes
mount $DEVICE /docker-volumes
uuid=`blkid -o value -s UUID $DEVICE`
echo "UUID=$uuid       /docker-volumes   ext4    defaults,nofail        0" >> /etc/fstab
mount -a

# update and install git and docker
yum update -y
yum install git docker epel-release python-pip -y

# move docker volumes to EBS mount
rm -rf /var/lib/docker/volumes
ln -s /docker-volumes /var/lib/docker/volumes

# configure docker
#su - ec2-user
usermod -aG docker $(whoami)
chkconfig docker on
service docker start
#sudo chown ec2-user:docker /var/run/docker.*

# install docker compose
pip install docker-compose
yum upgrade python*

# increase for ES
echo "vm.max_map_count=262144" >> /etc/sysctl.conf
sysctl -w vm.max_map_count=262144

# fire up OPP
cd ~
git clone https://github.com/constantcontact/open-performance-platform.git
cd open-performance-platform/
git checkout dev
/usr/local/bin/docker-compose up -d