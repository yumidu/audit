
# uCode.ai

A brief description to run your API and WEB


1) There are Dockerfile and Docker-compose in the root of the project folder. You can build the whole project executing  "docker-compose up" 


## Docker  

Build the project using docker

```bash
  docker-compose up
```
### FYI: We are using  Java version 17  for Spring boot which has some issues in docker . User may have to build the REST API app manually  in VSCODE or other  IDE

## Manually Run the  API and Web  (excluding Java Spring Boot)

Go to the each project project directory

```bash
  cd my-project 
```

Install dependencies

```bash
  npm install
```

    mysql+ PostgreSQL Configure the  db.js file in src\db.js 
    mongodb Configure the file common\config\env.config.js 
## REST API
Start the server ( for back-end)

```bash
  npm run start
```
To create a default user for testing the API endpoint and web UI, follow these steps:

Open the generated REST client files for API endpoint testing located in {API_FOLDER}\resclient\users.http.
Find the specific endpoint you want to execute.
Execute the endpoint using REST client or Postman.
If you are not familiar with REST client, you can use Postman to create the very first user.
```bash
###

POST {{HOST}}/users/reg HTTP/1.1
content-type: application/json

{
"email":"test@ucode.ai",
"password":"123456",
"firstName":"uCode",
"lastName":"Test",
"userType":"0",
"mobile":"60176408250"
}

```
For running automated  test  (for back-end)

```bash
  npm test
```

## WEB APP
Start the server . The API end-point URL has   defined in   src\config\index.js|ts

```bash
  npm run dev
```





## Usage/Examples



## Feedback
uCode.ai, our cutting-edge platform, is currently in its early stages, and while it shows great promise, we acknowledge that minor issues may arise during its usage. However, we want to assure you that we are fully committed to providing you with the best possible experience and continually improving the platform.
If you have any feedback , query or support  please reach out to us at support@ucode.ai

