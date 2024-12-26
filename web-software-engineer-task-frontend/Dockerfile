# Use an official Node runtime as a parent image
FROM node:23

WORKDIR /app

COPY ./package*.json ./

RUN npm install --force

COPY . .

RUN npm run build

EXPOSE 3000

CMD [ "npm", "run", "start" ]