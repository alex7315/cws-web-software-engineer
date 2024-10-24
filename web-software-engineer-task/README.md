# CoreWillSoft (CWS) Software Engineer Test Task Project

##Project structure
This project contains implementation of Backend Components provides REST API to get paginated list of  _GitHub_  users  
and scheduled Batch process to synchronize intern data with user data from  _GitHub_ .  

**Database** component  
contains database migration scripts controlled by  _Flyway_  

**Backend** component  
provides  _REST API_  allows request to get paginated list of  _GitHub_  users

**Sync** component  
provides scheduled batch process to synchronize internal database with data from  _GitHub_
   
##Configuration  
Projects contains  _application.yml_  files with configuration properties.  
This files have place holders instead of property values. Place holders are resolved by  _maven_  resource filtering that consider  _maven_  profiles are defined in  _POM_ .  
Property values are stored in  _maven_  settings file. Usually _~/.m2/settings.xml_ or  _$MAVEN_HOME/settings.xml_  _Maven_  setting file has to be secured since it contains confidant data like user password, access token etc.  
Follow values have to be defined in  _maven_ settings file.  

###Database Component  

_cws.datasource.url_  

_cws.datasource.username_  

_cws.datasource.password_  

_cws.datasource.schema_  

_cws.jpa.ddl.auto_  

_cws.database.root.password_

###Backend Component  

_cws.backend.port.exposed_  

_cws.log.base.path_  

###Sync Component  

_cws.github.authorization.token_  

_cws.github.sync.scheduled.rate_  

_cws.github.user.page.size_  

_cws.github.user.count.max_  

_cws.sync.port.exposed_  



##Build  
_Maven_  configuration contains plugins to compile source code, to build executable .jar file and to create docker image configured according to profile provided by build command.  

__mvn -P<profile> clean package__

##Deployment  
Project contains  

 _docker-compose.yml_  
 
configuration file to build and run  _docker containers_  from  _docker images_  are created during build process. 
Configuration file expected some environment variables have to be set:  

MYSQL_DATABASE  

MYSQL_USER  

MYSQL_PASSWORD  

MYSQL_ROOT_PASSWORD  

CWS_LOG_BASEDIR  

CWS_BACKEND_PORT  

CWS_SYNC_PORT  

One possibility to set environment variables and start  _docker compose_  is to write simple  _.sh_  command file.  

e.g.  
\#!/bin/bash

MYSQL_DATABASE=<data base name>; export MYSQL_DATABASE  

MYSQL_USER=<data base user>; export MYSQL_USER  

MYSQL_PASSWORD=<data base user password>; export MYSQL_PASSWORD  

MYSQL_ROOT_PASSWORD=<data base root password>; export MYSQL_ROOT_PASSWORD  

CWS_LOG_BASEDIR=/home/cws/logs; export CWS_LOG_BASEDIR  

CWS_BACKEND_PORT=8080; export CWS_BACKEND_PORT  

CWS_SYNC_PORT=8090; export CWS_SYNC_PORT  


CWS_WORKDIR=~/git/cws-web-software-engineer/web-software-engineer-task  

containers=("web-software-engineer-task_sync_service_1" "web-software-engineer-task_backend_service_1" "web-software-engineer-task_database_service_1" "web-software-engineer-task_mysqldb_1")  
for i in ${containers[@]}  
do  
        docker rm $i  
done  


pushd $CWS_WORKDIR  

docker-compose up  

popd  


##Using  
_backend_  Component provides endpoint to get paginated list of Github users.  

__GET__  _<host>[:port]/cws-backend/api/users_  

To see detailed documentation see _<host>[:port]/cws-backend/swagger-ui/index.html_  

_sync_  Component runs scheduled job process to synchronize users with actual GitHub users.  
Component provides endpoints to activate/deactivate scheduled job:  

__PUT__  _<host>[:port]/cws-sync/job/scheduler/activate_  

__PUT__  _<host>[:port]/cws-sync/job/scheduler/deactivate_ 

