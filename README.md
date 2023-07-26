<div align="center" id="top"> 
  <img src="./.github/app.gif" alt="Training Management System" />


  &#xa0;

</div>

<h1 align="center">Training Management System (TMS)</h1>

<p align="center">
  <img alt="Github top language" src="https://img.shields.io/github/languages/top/jewelcse/training-management-system?color=56BEB8">

  <img alt="Github language count" src="https://img.shields.io/github/languages/count/jewelcse/training-management-system?color=56BEB8">

  <img alt="Repository size" src="https://img.shields.io/github/repo-size/jewelcse/training-management-system?color=56BEB8">

  <img alt="License" src="https://img.shields.io/github/license/jewelcse/training-management-system?color=56BEB8">

  <img alt="Github issues" src="https://img.shields.io/github/issues/jewelcse/training-management-system?color=56BEB8" /> 

  <img alt="Github forks" src="https://img.shields.io/github/forks/jewelcse/training-management-system?color=56BEB8" /> 

  <img alt="Github stars" src="https://img.shields.io/github/stars/jewelcse/training-management-system?color=56BEB8" /> 
</p>

<!-- Status -->

<h4 align="center"> 
	🚧  Training Management System 🚀 Readme not completed yet..I will update it soon.  🚧
</h4> 

<hr>

<p align="center">
  <a href="#dart-about">About</a> &#xa0; | &#xa0; 
  <a href="#sparkles-features">Features</a> &#xa0; | &#xa0;
  <a href="#rocket-technologies">Technologies</a> &#xa0; | &#xa0;
  <a href="#white_check_mark-requirements">Requirements</a> &#xa0; | &#xa0;
  <a href="#checkered_flag-starting">Starting</a> &#xa0; | &#xa0;
  <a href="#memo-license">License</a> &#xa0; | &#xa0;
  <a href="https://github.com/{{YOUR_GITHUB_USERNAME}}" target="_blank">Author</a>
</p>

<br>

## :dart: About ##


This project is a training management system (TMS) that allows users to create and manage training courses. 
The TMS is built on the Spring Boot web framework and uses a MySQL database. 


## :sparkles: Features ##

The TMS has a number of features, including: \
:heavy_check_mark: Trainee Registration: Admin will register for the seleted trainee candidate.\
:heavy_check_mark: Trainer Registration: Admin will register for the seleted trainer.\
:heavy_check_mark: Batch creation: Admin can create training batches, including the batch start and end dates.\
:heavy_check_mark: Batch creation: Admin can create training batches, including the batch start and end dates.\
:heavy_check_mark: Course creation: Admin can create new courses, including adding course content, setting deadlines,.\
:heavy_check_mark: Course management: Admin can view and edit existing courses, as well as assign courses to students.\
:heavy_check_mark: Course scheduling: Admin can create and manages the course scheduling with calendars view.\
:heavy_check_mark: Assignment Creation: Admin will create assignment and his students will get the assignment.\
:heavy_check_mark: Classroom Feature: Trainer can post message, notice with file. Filtering system will be available.There will a notice boad that will be managed
by the traienrs.\
:heavy_check_mark: Assignment Submission: Trainee can submit their assign task withinn deadline.\


Here are some additional details about the project:



## :rocket: Technologies ##

The following tools were used in this project:

- [Java 11](#)
- [Maven](#)
- [Spring Boot](#)
- [Spring Security](#)
- [JWT Token](#)
- [Spring JPA](#)
- [MySQL](#)
- [Unit Testing](#)

## :white_check_mark: Requirements ##

Before starting :checkered_flag:, you need to have [A code Editor like Intellij IDEA] installed.

## :checkered_flag: Starting ##


## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 17+
* Gradle 3+
* MySql


To build and run the project, follow these steps:

* Login to mySQL driver with user root and and provide the respective user's specific password
* Clone the repository: `https://github.com/BJITAcademy2021/YSD_B02_J2EE_FinalProject_Habibullah.git`
* Open the settings.gradle file and ensure that all the service modules are included in the build.
* Open the application.properties files for each service and configure the necessary settings
* Gradle will automatically handle the dependencies and run configuration for each service module. It will use Spring Boot's embedded server to start the services.

### Access the Application
You can access the application at http://localhost:9080


## Functions 

### Funtion I will update it soon

| Mapping | Function          | URL                                           | Permission  | Request Body Input                                          |
|---------|-------------------|-----------------------------------------------|-------------|-------------------------------------------------------------|
| POST    | User Registration | http://localhost:9080/admin/register    | ADMIN     | userName, email, password, roles 
| POST    | User Login        | http://localhost:9080/login       | ALL         | email, password                                             |


## Reference 

For Reference you can read all this documents.

* [Official Gradle documentation](https://docs.gradle.org)
* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/gradle-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

&#xa0;

<a href="#top">Back to top</a>
