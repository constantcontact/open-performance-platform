FROM nginx
# copy config and remove old config
COPY ./nginx/conf/ /etc/nginx
RUN rm -f /etc/nginx/conf.d/default.conf
# nginx dropped this in newer versions for some dumb reason
RUN apt-get install openssl -y
# make dir and copy web files over
RUN mkdir -p /var/www/opp
COPY ./ui/build/opp-ui/ /var/www/opp/
# log volume
VOLUME /var/log/nginx/log

EXPOSE 80
EXPOSE 443
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
