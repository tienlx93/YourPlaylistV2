#!/bin/bash
location=$1
tomcat=$2
cd ${location}
sudo cp -rf yourplaylist-1.0/* ${tomcat}/ROOT
sudo chown -R tomcat:tomcat ${tomcat}/ROOT
sudo service tomcat8 restart