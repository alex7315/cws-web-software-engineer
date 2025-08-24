# Software Engineer Test Task Project. Backend.

## Project structure
This project contains implementation of Backend Components provides REST API to get paginated list of  _GitHub_  users  
and scheduled Batch process to synchronize intern data with user data from  _GitHub_ .  

**Database** component  
contains database migration scripts controlled by  _Flyway_  

**Security** component  
security library is used in  __Service__  and  __Sync__  components

**Service** component  
provides  _REST API_  allows request to get paginated list of  _GitHub_  users

**Sync** component  
provides scheduled batch process to synchronize internal database with data from  _GitHub_
   
## Configuration  
Projects contain  _application.yml_  files with configuration properties.  
This files have place holders instead of property values. Place holders are resolved by  _maven_  resource filtering that considers  _maven_  profiles are defined in  _POM_ .  
Property values are stored in  _maven_  settings file. Usually  _~/.m2/settings.xml_  or  _$MAVEN_HOME/settings.xml_  

_Maven_  setting file has to be secured since it contains confidant data like user password, access token etc.  
Follow values have to be defined in  _maven_  settings file.  

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

_cws.user.service.sort.default_  



### Sync Component  

_cws.github.api.authorization.token_  Authorization token can be getting from GitHub using GitHub account

_cws.github.api.base.url_ GitHub API base URL (e.g. https://api.github.com)

_cws.github.sync.scheduled.rate_  Time interval in sec. is used by scheduler to run sync job. Default value 60

_cws.github.user.page.size_   Size of page is used by paginated request to get GitHub users. Default value 100

_cws.github.user.count.max_   Max. number of GitHub users are requested to synchronize with internal content. Default value 1000

_cws.sync.port.exposed_  

### Security configuration (Backend and Sync)
_cws.security.jwt.secret_     Plain text 64 character secret key is used by creating of authorization token (JWT)

_cws.security.jwt.expiration.ms_  

_cws.security.session.timeout_  Security session timeout in sec.  

_cws.security.refresh.token.expiration.ms_  Refresh token expiration time in ms.  

_cws.security.ssl.enabled_   Enabling SSL connection (true/false)  

_cws.security.ssl.keystore.type_  SSL Keystore type (PKCS12/JKS)  

_cws.security.ssl.keystore.location_   Path to SSL Keystore  

_cws.security.ssl.keystore.password_  

_cws.security.ssl.keystore.alias_  


###Configuration profiles  

There are profiles defined in POM files of component projects:  
__development__  
used to build component .jar during development time, to execute unit tests and to measure current project test coverage  

__docker__  
used to build docker images of components and store them in local docker repo  

Example of  _settings.xml_  


	<settings>
		<profiles>
			<profile>
				<id>docker</id>
          	<properties>
					<build.profile.id>docker</build.profile.id>
					<cws.datasource.url>jdbc:mysql://mysqldb:3306/cws_github?useUnicode=true&amp;characterEncoding=UTF-8&amp;allowPublicKeyRetrieval=true&amp;useSSL=false&amp;useJDBCCompliantTimezoneeShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC</cws.datasource.url>
					<cws.datasource.username>database_username</cws.datasource.username>
					<cws.datasource.password>password</cws.datasource.password>
					<cws.datasource.schema>cws_github</cws.datasource.schema>
					<cws.jpa.ddl.auto>none</cws.jpa.ddl.auto>
					<cws.service.port.exposed>443</cws.service.port.exposed>
					<cws.database.root.password>database root password</cws.database.root.password>
					<cws.github.api.authorization.token>github token</cws.github.api.authorization.token>
					<cws.github.api.base.url>https://api.github.com</cws.github.api.base.url>

					<!-- sync with GitHub each 1 minute -->
                	<cws.github.sync.scheduled.rate>60</cws.github.sync.scheduled.rate>
                	<cws.github.user.page.size>100</cws.github.user.page.size>
               	<cws.github.user.count.max>1000</cws.github.user.count.max>
                	<cws.sync.port.exposed>8080</cws.sync.port.exposed>
                	<cws.log.base.path>/cws/logs</cws.log.base.path>
					<cws.security.jwt.secret>jwt secret</cws.security.jwt.secret>
					
					<!-- token expiration time 10 min -->
					<cws.security.jwt.expiration.ms>600000</cws.security.jwt.expiration.ms>
					
					<!-- token expiration time 60 min -->
					<cws.security.refresh.token.expiration.ms>3600000</cws.security.refresh.token.expiration.ms>

					<!-- user session idle timeout 5min. -->
					<cws.security.session.timeout>300</cws.security.session.timeout>
					
					<cws.security.ssl.enabled>true</cws.security.ssl.enabled>
				<cws.security.ssl.keystore.type>PKCS12</cws.security.ssl.keystore.type>
				<cws.security.ssl.keystore.location>/cws/ssl</cws.security.ssl.keystore.location>
				<cws.security.ssl.keystore.password><keystore password></cws.security.ssl.keystore.password>
				<cws.security.ssl.keystore.alias>cws</cws.security.ssl.keystore.alias>
				<cws.security.ssl.keystore.base.path><path to keystore></cws.security.ssl.keystore.base.path>
				</properties>
		 
			</profile>
		
			<profile>
				<id>development</id>
	          	<properties>
						<build.profile.id>development</build.profile.id>
					
					
					...
					
					</properties>
			</profile>
		</profiles>  
   </settings>  




## Build  
_Maven_  configuration contains plugins to compile source code, to build executable .jar file and to create docker image configured according to profile provided by build command.  

__mvn -Pdocker clean package__  

### Code quality using Sonar Qube Cloud 

__mvn -Pdevelopment verify sonar:sonar__  

  [Code analysis overview](https://sonarcloud.io/project/overview?id=software-engineer-task-token)  
  

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

CWS_SERVICE_PORT_EXPOSED - port exposed by docker image of service application

CWS_SERVICE_PORT - port exposed by container 

CWS_SYNC_PORT_EXPOSED - port exposed by docker image of sync application

CWS_SYNC_PORT - port exposed by container

One possibility to set environment variables and start  _docker compose_  is to write simple  _.sh_  command file.  

e.g.  

```
\#!/bin/bash

MYSQL_DATABASE=<data base name>; export MYSQL_DATABASE  

MYSQL_USER=<data base user>; export MYSQL_USER  

MYSQL_PASSWORD=<data base user password>; export MYSQL_PASSWORD  

MYSQL_ROOT_PASSWORD=<data base root password>; export MYSQL_ROOT_PASSWORD  

CWS_DATABASE_PORT=3306; export CWS_DATABASE_PORT

CWS_LOG_BASEDIR=/home/cws/logs; export CWS_LOG_BASEDIR  

CWS_SERVICE_PORT_EXPOSED = 443; export CWS_SERVICE_PORT_EXPOSED

CWS_SERVICE_PORT=8443; export CWS_BACKEND_PORT  

CWS_SYNC_PORT_EXPOSED = 443; export CWS_SYNC_PORT_EXPOSED

CWS_SYNC_PORT=9443; export CWS_SYNC_PORT  

CWS_MYSQL_CONTAINER_NAME="mysql80"; export CWS_MYSQL_CONTAINER_NAME

CWS_DATABASE_IMAGE_NAME="cws/web-software-engineer-task-database:0.0.2"; export CWS_DATABASE_IMAGE_NAME

CWS_DATABASE_CONTAINER_NAME="database"; export CWS_DATABASE_CONTAINER_NAME

CWS_SERVICE_IMAGE_NAME="cws/web-software-engineer-task-service:0.0.2"; export CWS_SERVICE_IMAGE_NAME

CWS_SERVICE_CONTAINER_NAME="service"; export CWS_SERVICE_CONTAINER_NAME

CWS_SYNC_IMAGE_NAME="cws/web-software-engineer-task-sync:0.0.2"; export CWS_SYNC_IMAGE_NAME

CWS_SYNC_CONTAINER_NAME="sync"; export CWS_SYNC_CONTAINER_NAME


CWS_WORKDIR=~/git/cws-web-software-engineer/web-software-engineer-task  

containers=($CWS_MYSQL_CONTAINER_NAME $CWS_DATABASE_CONTAINER_NAME $CWS_SERVICE_CONTAINER_NAME $CWS_SYNC_CONTAINER_NAME)  
for i in ${containers[@]}  
do  
        docker rm $i  
done  


pushd $CWS_WORKDIR  

docker-compose up  

popd  
```

## Using  
__SERVICE__  Component provides endpoint to authenticate user and get paginated list of Github users.  

__Api documentation URL:__  

__Swagger__  _<host>[:port]/cws-service/swagger-ui/index.html_  


__Security endpoints:__

__POST__   _host[:port]/cws-service/api/auth/signin_  


__POST__   _host[:port]/cws-service/api/auth/refreshtoken_


__GitHub user list endpoint:__  


__GET__   _host[:port]/cws-service/api/users_  

To see detailed documentation _<host>[:port]/cws-service/swagger-ui/index.html_  

__SYNC__  Component runs scheduled job process to synchronize users with actual GitHub users.  
Component provides endpoints to activate/deactivate scheduled job:  

__Api documentation URL:__  

__Swagger__   _host[:port]/cws-sync/swagger-ui/index.html_  


__PUT__   _host[:port]/cws-sync/job/scheduler/activate_  

__PUT__   _host[:port]/cws-sync/job/scheduler/deactivate_ 

