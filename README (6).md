
# Student Information Management System

## Requiremnts

Since this app is written as a Spring Boot Java application, we need to follow some requirements to write/run the app:

### Docker: 
In this project, we will deploy Docker to make sure the app can run swiftly in other device. Base from your device, you should download Docker based on it.

### Java Development Kit (JDK):

We must have JDK (Java Development Kit) installed on your system as the JDK includes the Java compiler (javac) and the Java runtime environment (java) which provide necessary tools to create/run Java apps.

### Text Editor or Integrated Development Environment (IDE):

We should have a text editor or an Integrated Development Environment (IDE) installed to write and edit the Java code. Common choices include Visual Studio Code, IntelliJ IDEA, Eclipse, or Notepad++.

### Having Command Prompt (CMD) or Terminal:

In some cases, when we don't have access to text editor apps or an IDE, we will need access to a command-line interface such as Command Prompt (on Windows) or Terminal (on macOS or Linux) as a replacement.

### Knowledge of Basic Command-Line Operations:

You should be familiar with basic command-line operations such as navigating directories (cd command), compiling Java files (javac command), and running Java applications (java command).

### Spring Boot dependencies:

Spring Boot framework gives us a lot of tools to create a Java Web App and based from your preferences, you can choose dependencies that you can work best with but in this project, we will use Web Sping, MySQL Driver, Thymeleaf, Spring Data JPA dependencies to write the application. You can generate a new Spring Boot project using Spring Initializr. Go to Google and search start.spring.io, and select Maven or Gradle, add those dependencies (Web, MySQL Driver, Thymeleaf, Spring Data JPA), and click on "Generate" to download the project zip file. Once downloaded successfully, extract it and open it by your preference IDE tool or Text Editor app.

### Read the Spring Boot Documentation:

Since we are using Spring Boot Framework to create this app, it is essential to read the documentation given by the Framework's developers to understand the stucture of it and its unique syntaxes. It is advised by the writer to create some small projects to further understand some features of the Framework before applying it to this project.


## Planning

Let's plan the structure of the application by identifying the classes and their responsibilities. The app will have four main components: Entity, Service, Controller, and Repositories, each represented by corresponding packages with a Java class or interface. 
The Student class in the Entity package maps individual student objects to database tables. The StudentService in the Service package handles business logic and acts as a bridge between controllers and repositories. The StudentRepository in the Repositories package provides data access interfaces. The StudentController in the Controller package manages HTTP requests, executes business logic, and returns responses.

Next, we will create three HTML files (index.html, addStudent.html, editStudent.html) in src/main/resources/templates to design the app's interface. After that, we will set up the database, Thymeleaf configuration, in the application.properties and Dockerfile file.

After that, we will create images and containers of MySQL server and the app in Docker then connect them with a network. Then after we run those containers, we can gain access to the web via the port link in the Docker app.

## Developing

### Create MySQL image and container
Fistly, enter this into the command prompt:

```
C:\USER\(your user name)>docker pull mysql
```
After successfully download it, it should be displayed in the Docker app in image section. After that, you can create a MySQL container by inputing this code in the CP:

```
C:\USER\(your user name)>docker run -p 3307:3306 --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=studentDB -d mysql
```
Then in the container section of the Docker app, you can see the container named "mysqlcontainer"

### Set up database

Using your command prompt and enter these code 

Find and Open MySQL Command Line client (It should be in the MySQL folder in the second row when you click the Start button) as a user who can create new users.

``` 
mysql> create user 'root'@'%' identified by 'ThePassword'; (Creates the user)
mysql> grant all on db_studentDB.* to 'root'@'%'; (Gives all privileges to the new user on the newly created database)
``` 

### Student 

In the Student class, we will define attributes such as id, name, and email along with methods to add and display student information. These methods encapsulate the behavior related to student objects.

``` 
package org.example.sms4.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Student {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String email;

    // Default constructor
    public Student(){

    }

    // Parameterized constructor
    public Student(String name, String email){
        this.name = name;
        this.email = email;
    }

    // Getter method for id
    public long getId(){
        return id;
    }

    // Setter method for id
    public void setId(long id){
        this.id = id;
    }

    // Getter method for name
    public String getName(){
        return name;
    }

    // Setter method for name
    public void setName(String name){
        this.name = name;
    }

    // Getter method for email
    public String getEmail(){
        return email;
    }

    // Setter method for email
    public void setEmail(String email){
        this.email = email;
    }
}

``` 

### StudentRepository

The StudentRepository interface serves as a contract for accessing and managing Student entities within the application. It extends the Spring Data CrudRepository interface, inheriting basic CRUD (Create, Read, Update, Delete) operations for the Student entity. The primary responsibility of the existsByEmail method is to provide a convenient way to determine whether a student with a particular email exists in the database. This method encapsulates the logic required to execute a query to check for the existence of a student with the specified email.

``` 
package org.example.sms4.repository;

import org.example.sms4.entity.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Annotation indicating that this interface serves as a repository
@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    // Declaration of a query method to check if a student with a specific email exists
    // Spring Data JPA will automatically implement this method based on the method name
    boolean existsByEmail(String email);
}

``` 

### StudentService
The code provided is a service class named StudentService that manages student-related operations. It is annotated with @Service in the Spring framework, indicating it is a service component. The @Transactional annotation ensures transactional context for each method. StudentService injects an instance of StudentRepository using @Autowired for database operations. It provides methods for CRUD operations on student entities: listAll(), save(), get(), and delete(). Two validation methods, validateNewInformation() and validateEditInformation(), check for empty fields and duplicate emails. These methods return error messages for validation errors.

``` 
package org.example.sms4.service;

import jakarta.transaction.Transactional;
import org.example.sms4.entity.Student;
import org.example.sms4.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


import java.util.List;

@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository repo;

    // Method to list all students
    public List<Student> listAll() {
        return (List<Student>) repo.findAll();
    }

    // Method to save a student
    public void save(Student student) {
        repo.save(student);
    }

    // Method to get a student by id
    public Student get(long id) {
        return repo.findById(id).get();
    }

    // Method to delete a student by id
    public void delete(long id) {
        repo.deleteById(id);
    }

    // Method to validate new student information
    public String validateNewInformation(Student student){
        String err = null;
        // Checking if student name is empty
        if(student.getName() == ""){
            err = "Student must have a name";
            System.out.println("1");
        }
        // Checking if student email is empty
        if(student.getEmail() == ""){
            err = "Student must have an email address";
            System.out.println("2");
        }
        // Checking if student email already exists in the database
        String email = student.getEmail();
        boolean emailDuplicate = repo.existsByEmail(email);
        if (emailDuplicate == true){
            err = "This email address has been used";
            System.out.println("3");
        }
        return err;
    }

    // Method to validate edited student information
    public String validateEditInformation(Student student){
        String err = null;
        // Checking if student name is empty
        if(student.getName() == ""){
            err = "Student must have a name";
            System.out.println("1");
        }
        // Checking if student email is empty
        if(student.getEmail() == ""){
            err = "Student must have an email address";
            System.out.println("2");
        }
        return err;
    }
}
``` 
### StudentController
The StudentController class in the MVC architecture handles HTTP requests for student management. It depends on StudentService for student operations. The @Controller annotation marks it as a Spring MVC controller. The mappings are:
- @RequestMapping("/") maps the root URL to viewHomePage()
- @RequestMapping("/addStudent") maps "/addStudent" to showNewStudentPage()
- @RequestMapping(value = "/save", method = RequestMethod.POST) maps POST requests to saveStudent()
- @RequestMapping(value = "/saveEdit", method = RequestMethod.POST) maps POST requests to saveEditStudent()
- @RequestMapping("/edit/{id}") maps "/edit/{id}" to showEditStudentPage()
- @RequestMapping("/delete/{id}") maps "/delete/{id}" to deleteStudent()

```
package org.example.sms4.controller;


import org.example.sms4.entity.Student;
import org.example.sms4.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import java.util.List;
@Controller
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    // Request mapping for the home page
    @RequestMapping("/")
    public String viewHomePage(Model model) {
        // Retrieves list of all students
        List<Student> listStudents = service.listAll();
        model.addAttribute("listStudents", listStudents);

        return "index"; // Return the name of the template to render (assuming index.html is in src/main/resources/templates)
    }

    // Request mapping to show the page for adding a new student
    @RequestMapping("/addStudent")
    public String showNewStudentPage(Model model) {
        // Create a new student object and add it to the model
        Student student = new Student();
        model.addAttribute("student", student);

        return "addStudent"; // Return the name of the template to render
    }

    // Request mapping to save a new student
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute("student") Student student, Model model) {
        // Validate new student information
        String errorMessage = service.validateNewInformation(student);
        if (errorMessage != null) {
            // If validation fails, add error message to the model and return to the add student page
            model.addAttribute("errorMessage", errorMessage);
            return "addStudent";
        } else {
            // If validation passes, save the student and redirect to the home page
            service.save(student);
            return "redirect:/";
        }
    }

    // Request mapping to save edited student information
    @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
    public String saveEditStudent(@ModelAttribute("student") Student student, RedirectAttributes model) {
        // Validate edited student information
        String errorMessage = service.validateEditInformation(student);
        if (errorMessage != null) {
            // If validation fails, add error message as a flash attribute and redirect to the edit student page
            model.addFlashAttribute("errorMessage", errorMessage);
            long id = student.getId();
            return "redirect:/edit/" + id;
        } else {
            // If validation passes, save the edited student and redirect to the home page
            service.save(student);
            return "redirect:/";
        }
    }

    // Request mapping to show the edit student page
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("editStudent"); // Corrected template name
        // Retrieve the student with the given id
        Student student = service.get(id);
        mav.addObject("student", student);

        return mav;
    }

    // Request mapping to delete a student
    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable(name = "id") long id) {
        // Delete the student with the given id
        service.delete(id);
        return "redirect:/"; // Redirect to the home page after deletion
    }
}
 ``` 
### application.properties

This file contains the configuration of the app's database, Thymeleaf configuration, and Server Configuration

``` 
spring.application.name=SMS4

# Database Configuration
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB_NAME:studentDB}
spring.datasource.username= ${MYSQL_USER:root}
spring.datasource.password= ${MYSQL_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# Thymeleaf Configuration
spring.thymeleaf.prefix=classpath:/templates/  # Prefix for Thymeleaf templates
spring.thymeleaf.suffix=.html  # Suffix for Thymeleaf templates
spring.thymeleaf.cache=false  # Disable template caching for development

# Server Configuration
#server.port = 8080

``` 

### StudentmanagementApplication
This file contain the sole function to run the app.
```
package org.example.sms4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Sms4Application {

    public static void main(String[] args) {
        SpringApplication.run(Sms4Application.class, args);
    }

}
``` 
### index.html

``` 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Index</title>
</head>
<body>
<div align="center">
    <h1>List of Students</h1>
    <a href="addStudent">Add new student information</a>
    <br/><br/>
    <table border="1" cellpadding="10">
        <thead>
        <tr>
            <th>Student ID</th>
            <th>Name</th>
            <th>Student Email</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="student: ${listStudents}">
            <td th:text="${student.id}">Student ID</td>
            <td th:text="${student.name}">Name</td>
            <td th:text="${student.email}">Student Email</td>
            <td>
                <a th:href="@{'/edit/' + ${student.id}}">Edit</a>
                &nbsp;&nbsp;&nbsp;
                <a th:href="@{'/delete/' + ${student.id}}">Delete</a>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>
``` 

### addStudent.html

``` 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Add New Student Information</title>

</head>
<body>
<div align="center">
    <h1>Add new student</h1>
    <br />
    <form action="#" th:action="@{/save}" th:object="${student}"
          method="post">


        <table border="0" cellpadding="10">
            <tr>
                <td>Student Name:</td>
                <td><input type="text" th:field="*{name}" /></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="text" th:field="*{email}" /></td>
            </tr>
            <tr>
                <td colspan="2"><button type="submit">Save</button> </td>
            </tr>
        </table>
    </form>
    <p th:if="${errorMessage}" th:text="${errorMessage}" style="color: red;"></p>
</div>
</body>
</html>
``` 

### editStudent.html

``` 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Edit Student Information</title>
</head>
<body>
<div align="center">
    <h1>Edit Student</h1>
    <br />
    <form action="#" th:action="@{/saveEdit}" th:object="${student}"
          method="post">

        <table border="0" cellpadding="10">
            <tr>
                <td>Student ID:</td>
                <td>
                    <input type="text" th:field="*{id}" readonly="readonly" />
                </td>
            </tr>
            <tr>
                <td>Student Name:</td>
                <td>
                    <input type="text" th:field="*{name}" />
                </td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="text" th:field="*{email}" /></td>
            </tr>
            <tr>
                <td colspan="2"><button type="submit">Save</button> </td>
            </tr>
        </table>
    </form>
    <p th:if="${errorMessage}" th:text="${errorMessage}" style="color: red;"></p>
</div>
</body>
</html>

``` 

### Dockerfile

```
FROM openjdk:17
ADD target/SMS4-0.0.1-SNAPSHOT.jar SMS4-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/SMS4-0.0.1-SNAPSHOT.jar"]

```

### Create sms4 (the app name) image and container 

#### Maven build the project to create the target folder. 

Since the app is also a maven project, so you just need to navigate to Project Directory and put this code:

```
mvn clean install
```
This command will compile the source code, run any tests, and package the project into a JAR or WAR file. The compiled output will be placed in the target folder within your project directory. Then after running the mvn clean install command, navigate to the target folder within your Maven project directory. You'll find the compiled artifacts there.

#### Create sms4 image and container

Firstly, navigate to Project Directory and put this code:

```
 docker build -t sms4 .

```
This command will build an image called "sms4" in the Docker app

Secondly, put this code in the command prompt:

```
docker create network create managesystem 
```
This command will create a network named "managesystem". We will use this to connect the app container and mysql container.

Then finally, run this code to create the container 

```
docker run -p 8090:8080 --name sms4container --net sms4network -e MYSQL_HOST=mysqlcontainer -e MYSQL_PORT=3306 -e MYSQL_DB_NAME=studentDB -e
 MYSQL_USER=root -e MYSQL_PASSWORD=root sms4

```

## How to run the application

This is a quick summary of how to use the application:

### Launch the Application:

In order to run the application, we only need to open Docker app and run the sms4 and mysql container. Then click the port in the Port column in the container section of the Docker app. (Or enter this URL "http://localhost:8090/")

### Exiting the Application:

You can shut down the app by closing the browser tap. 

 

