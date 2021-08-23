# Task Manager App
Task Manager Backend REST API Application

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Installing Prerequisites

A step by step series of examples that tell you how to get a development environment running

* [JDK11]
* [MySQL8+]
* [Maven]

### Verify Installation

```bash
java --version
```
You should see output similar to this:
```
openjdk 11.0.8 2020-07-14 LTS
OpenJDK Runtime Environment Corretto-11.0.8.10.1 (build 11.0.8+10-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.8.10.1 (build 11.0.8+10-LTS, mixed mode)
```
```bash
mysql --version
```
You should see output similar to this:
```
Ver 8.0.21 for Win64 on x86_64 (MySQL Community Server - GPL)
```

### Configuration Prerequisites

List of things that you need to configure in your development machine to build the project successfully.

MySQL:
* Change mysql user (default: root), password (default: 1234) configuration [application-dev.properties](app/src/main/resources)
* Create an empty database `task_manager_db`
* Db engine should be INNODB with UTF-8 (UTF8-General) Encoding



### Building Task Manager App

This application uses [maven](https://maven.apache.org/) to automate the build process.

Install App:
```bash
cd ~/workspace/
git clone https://github.com/Nedhro/task-manager.git

cd ./app
mvn clean install
```
Get yourself a cup of :coffee: as this will take a while as maven will download all the project dependencies.

### Run App Server:
spring-boot:run 

Now open your browser and go to *http://localhost:8080*

login as admin using *admin:admin* 
or
login as user using *user:user*

### Coding style Guidelines

For Backend we use [Google Style Guide](https://github.com/google/styleguide). This makes sure whatever IDE we use we always have identical coding standard.

* For IntelliJ [IDE](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
* For Eclipse [IDE](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)

## Deployment

Three profiles are configured in maven for deployment in stage and production environment.

Stage Server:
```
mvn clean install -Pstage
```
Production Server:
```
mvn clean install -Pproduction
```
Check *app/target/* folder to get the desired war file.

## Built With

* [Spring](https://spring.io/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Hibernate](http://hibernate.org/) - Object-relational mapping Tool
* [Lombok](https://projectlombok.org/) - Java Automation Tool

## Authors

* **Mohammad Amdadul Islam** - *Owner* - [Nedhro](https://gitlab.com/Nedhro)
