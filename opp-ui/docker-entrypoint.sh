#!/bin/bash

# ===== generate DH for extra security ======
# set default size for DH
# if [ -z ${SSL_DH_SIZE+x} ]
# then
#   >&2 echo ">> no \$SSL_DH_SIZE specified using default" 
#   DH_SIZE="2048"
# fi
# DH="/etc/nginx/certs/dh.pem"
# # generate DH if its anot already present
# if [ ! -e "$DH" ]
# then
#   echo "---> generating $DH with size: $SSL_DH_SIZE"
#   openssl dhparam -out "$DH" $SSL_DH_SIZE
# fi


# configure NGINX files
envsubst '$$NGINX_HOST_UI $$NGINX_HOST_SVC $$NGINX_PORT' < /etc/nginx/conf.d/opp.template > /etc/nginx/conf.d/opp.conf 
chown -R nginx:nginx /var/www/opp

echo "SVC: $NGINX_HOST_SVC"
echo "UI: $NGINX_HOST_UI"

# allow for a path for the certs to be passed in via ENV variable
echo "checking for an external cert path"
if [ -z "$SSL_CERT_PATH" ]; then
	echo "no cert path set"
else
	echo "found cert path"
	mkdir -p /etc/nginx/certs
	cp -r $SSL_CERT_PATH/* /etc/nginx/certs
fi


# if not run through docker-compose this directory won't exist
if [ ! -d "/var/certs" ]; then
	echo "Creating certs directory"
	mkdir -p /var/certs
fi

##### build or get certs for UI and SVC #####
CERTS=(ui svc)
for app in ${CERTS[@]}; do
	if [ -z "$SSL_CERT_PATH" ] || [ ! -e "/etc/nginx/certs/opp-${app}-crt.pem" ] || [ ! -e "/etc/nginx/certs/opp-${app}-key.pem"]
	then
		mkdir -p /etc/nginx/certs
		# look for cached certs
		if [ -e "/var/certs/opp-${app}-crt.pem" ] || [ -e "/var/certs/opp-${app}-key.pem" ]
		then
			echo "---> copying cached ${app} certs"
			cp /var/certs/opp-${app}-crt.pem /etc/nginx/certs
			cp /var/certs/opp-${app}-key.pem /etc/nginx/certs
		else
			# get dynamic value of selected domain
			appU=$(echo $app | awk '{print toupper($0)}')
			varName="NGINX_HOST_${appU}"
			hostVal=$(eval "echo \$$varName")

			echo "---> generating self signed cert for opp ${app}"
			openssl req -x509 -newkey rsa:4086 \
			-subj "/${SSL_CERT_INFO}/CN=${hostVal}" \
			-keyout "/etc/nginx/certs/opp-${app}-key.pem" \
			-out "/etc/nginx/certs/opp-${app}-crt.pem" \
			-days 3650 -nodes -sha256
			echo "---> caching certs to docker cert volume"
			cp /etc/nginx/certs/opp-${app}-crt.pem /var/certs/
			cp /etc/nginx/certs/opp-${app}-key.pem /var/certs/
		fi
	fi
done


# exec CMD
echo "---> exec docker CMD"
echo "$@"
exec "$@"