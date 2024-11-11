# CoreWillSoft (CWS) Software Engineer Test Task Project. Backend.

## Project structure
This project contains implementation of Backend Components provides REST API to get paginated list of  _GitHub_  users  
and scheduled Batch process to synchronize intern data with user data from  _GitHub_ .  

**Database** component  
contains database migration scripts controlled by  _Flyway_  

**Service** component  
provides  _REST API_  allows request to get paginated list of  _GitHub_  users

**Sync** component  
provides scheduled batch process to synchronize internal database with data from  _GitHub_
   
## Configuration  
Projects contain  _application.yml_  files with configuration properties.  
This files have place holders instead of property values. Place holders are resolved by  _maven_  resource filtering that considers  _maven_  profiles are defined in  _POM_ .  
Property values are stored in  _maven_  settings file. Usually _~/.m2/settings.xml_ or  _$MAVEN_HOME/settings.xml_  _Maven_  setting file has to be secured since it contains confidant data like user password, access token etc.  
Follow values have to be defined in  _maven_ settings file.  

### Database Component  

_cws.datasource.url_  

_cws.datasource.username_  

_cws.datasource.password_  

_cws.datasource.schema_  

_cws.jpa.ddl.auto_  

_cws.database.root.password_

### Service Component  

_cws.service.port.exposed_  

_cws.log.base.path_  


###Sync Component  

_cws.github.authorization.token_  Authorization token can be getting from GitHub using GitHub account

_cws.github.sync.scheduled.rate_  Time interval in sec. is used by scheduler to run sync job. Default value 60

_cws.github.user.page.size_   Size of page is used by paginated request to get GitHub users. Default value 100

_cws.github.user.count.max_   Max. number of GitHub users are requested to synchronize with internal content. Default value 1000

_cws.sync.port.exposed_  

###Security configuration (Backend and Sync)
_cws.security.jwt.secret_     Plain text 64 character secret key is used by creating of authorization token (JWT)

_cws.security.jwt.expiration.ms_  

_cws.security.session.timeout_  Security session timeout in sec.  

_cws.security.refresh.token.expiration.ms_  Refresh token expiration time in ms.  



## Build  
_Maven_  configuration contains plugins to compile source code, to build executable .jar file and to create docker image configured according to profile provided by build command.  

__mvn -P<profile> clean package__

## Deployment  
Project contains  

 _docker-compose.yml_  
 
configuration file to build and run  _docker containers_  from  _docker images_  are created during build process. 
Configuration file expected some environment variables have to be set:  

MYSQL_DATABASE  

MYSQL_USER  

MYSQL_PASSWORD  

MYSQL_ROOT_PASSWORD  

CWS_DATABASE_PORT

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

CWS_DATABASE_PORT=3306; export CWS_DATABASE_PORT

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


## Using  
_service_  Component provides endpoint to authenticate user and get paginated list of Github users.  

__Security endpoints:__

__POST__ _<host>[:port]/cws-service/api/auth/signup_ 


__POST__ _<host>[:port]/cws-service/api/auth/signin_  


__POST__ _<host>[:port]/cws-service/api/auth/refreshtoken_


__GitHub user list endpoint:__  


__GET__  _<host>[:port]/cws-service/api/users_  

To see detailed documentation see _<host>[:port]/cws-backend/swagger-ui/index.html_  

_sync_  Component runs scheduled job process to synchronize users with actual GitHub users.  
Component provides endpoints to activate/deactivate scheduled job:  

__PUT__  _<host>[:port]/cws-sync/job/scheduler/activate_  

__PUT__  _<host>[:port]/cws-sync/job/scheduler/deactivate_ 

