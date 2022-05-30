###########################################
# MY Docker Image for OpenShift Deployment  #
###########################################

FROM centos:7
USER root
RUN mkdir /hh
RUN yum install -y epel-release && \
  yum install -y nginx && \
  yum install -y nfs-utils nfs-utils-lib
COPY . /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
COPY script.sh service.sh /tmp/
RUN chmod 755 /tmp/script.sh /tmp/service.sh
CMD /usr/sbin/nginx -g 'daemon off;'
# CMD /tmp/service.sh
# CMD ["sh","-c","/usr/sbin/nginx -g 'daemon off;' && /bin/bash /tmp/script.sh & "]


# FROM httpd:latest
# RUN rm -rf /var/www/html/*
# COPY . /var/www/html/

## Docker file for Static Application Deployment
#
# FROM centos:latest
# MAINTAINER Anand Reddy < anand@gmail.com >
# ENV workdir /var/www/html
# USER root
# RUN yum -y update && yum -y install httpd && rm -rf $workdir/*
# WORKDIR $workdir
# COPY . $workdir
# EXPOSE 80
# ENTRYPOINT ["/usr/sbin/httpd", "-D", "FOREGROUND"]

