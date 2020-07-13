# Covid App
This app shows COVID stats. It has both UI and API exposed at the base URL '/covid/app/login.html' and '/covid/api/login' respectively.

# Features
* Signup
* Login
* Covid Dashboard that shows overall covid stats of India and drill down to the state level.
* Logout


# Steps to run:
1. Set the required environment variables mentioned at 'src/main/resources/application-prod.properties'
2. Either build the project using './gradlew clean build' or create an image using Dockerfile present at the root of the project.
3. Depending on Step 2, either run using 'java -jar path/to/jar' or run docker image directly.
