Gatling Java Project
============================================

Documentation
[refer Gatling website](https://gatling.io/docs/current/extensions/maven_plugin/) for usage. 

This project is written in Java,

It includes:

* [Maven Wrapper](https://maven.apache.org/wrapper/)
* Minimal `pom.xml` file
* Latest version of `io.gatling:gatling-maven-plugin`
* Sample [Simulation](https://gatling.io/docs/gatling/reference/current/general/concepts/#simulation) class,
  demonstrating sufficient Gatling functionality

## How to run 
1. Right-click on the 'Engine' class and select
```jshelllanguage
 Run Engine.Run()
```
2. Choose a simulation number and ENTER:
```java
 [0] socialcarepassport.EmployerListSimulation
 [1] socialcarepassport.LoginSimulation
 [2] socialcarepassport.GetBookedCoursesSimulation
```
3. Select run description (optional)
```java
Choose any description(example: Login Test) and ENTER
```
4. Gatling reports

```java
Find gatling reports under: '/target/gatling' or check console for the reports link
```

### Useful terms

* **Scenario**: The summary of steps that the virtual users perform to simulate typical user behavior, e.g., log in to an socialcarepassport, View training records etc.
* **Simulation**: The definition of the load tests about how many virtual users will execute the Scenario in a given timeframe.
* **Session**: A state container assigned for each virtual user.
* **Recorder**: The Gatling UI to record a Scenario and output a Simulation.
* **Feeders**: A way to inject data for our virtual users from an external source like CSV files, JSON files, a JDBC data source, etc.