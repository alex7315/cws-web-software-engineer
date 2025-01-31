# Software Engineer Test Task Project. Frontend.  
## Common
This project implements required user interface ( _frontend_ ) to login to the service ( _backend_ ) and to show list of GitHub users provided by  _backend_  via REST API.  
Implementation uses React and Node.js  

## Configuration 
__REACT_APP_API_AUTH_URL__  - url to authentication/authorization endpoints of _backend REST API_
__REACT_APP_API_USERS_URL__  - url to resource endpoints of _backend REST API_
__REACT_APP_USER_IDLE_TIMEOUT_S__  -  _user idle timeout_  in seconds. After  _user idle timeout_   is expired without any user activity, user will be logged out and user session will be closed.  

Configuration properties has to be stored in  __.env__  file that is used by deployment

## Deployment
Project contains  

_Dockerfile_  to define build process of  _docker image_  

 _docker-compose.yml_  configuration file to build and run  _docker containers_  from  _docker image_  . 
Configuration file expected environment variable have to be set:  

__CWS_FRONTEND_PORT__   - port is exposed to access  _frontend_  application  

One possibility to set environment variables and start  _docker compose_  is to write simple  _.sh_  command file.  

e.g.  
\#!/bin/bash  

IMAGE_NAME=web-software-engineer-task-frontend:0.0.1  
CONTAINER_NAME=web-software-engineer-task-frontend  
CWS_WORKDIR=~/cws/cws-web-software-engineer/web-software-engineer-task-frontend  

CWS_FRONTEND_PORT=55403; export CWS_FRONTEND_PORT  

docker stop $CONTAINER_NAME  

docker rm $CONTAINER_NAME  

docker rmi $IMAGE_NAME  

pushd $CWS_WORKDIR  
docker-compose up  
$IMAGE_NAME  
popd  

## original react project description started here
This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can't go back!**

If you aren't satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you're on your own.

You don't have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn't feel obligated to use this feature. However we understand that this tool wouldn't be useful if you couldn't customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
