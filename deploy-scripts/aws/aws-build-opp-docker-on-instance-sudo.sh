#!/bin/bash

# setup EBS mount for docker volumes
DEVICE=/dev/xvdb
sudo mkfs -t ext4 $DEVICE
sudo mkdir /docker-volumes
sudo mount $DEVICE /docker-volumes
uuid=`sudo blkid -o value -s UUID $DEVICE`
sudo su -c "echo \"UUID=$uuid       /docker-volumes   ext4    defaults,nofail        0\" >> /etc/fstab"
sudo mount -a

# update and install git and docker
sudo yum update -y
sudo yum install git docker epel-release python-pip -y

# move docker volumes to EBS mount
sudo rm -rf /var/lib/docker/volumes
sudo ln -s /docker-volumes /var/lib/docker/volumes

# configure docker
sudo usermod -aG docker $(whoami)
sudo chkconfig docker on
sudo service docker start
sudo chown ec2-user:docker /var/run/docker.*

# install docker compose
sudo pip install docker-compose
sudo yum upgrade python*

# fire up OPP
git clone https://github.com/constantcontact/open-performance-platform.git
cd open-performance-platform/
docker-compose up -d