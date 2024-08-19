# inventory management system focusing on re-ordering system
An Inventory management system with mainly one major feature(re-ordering feature) for now

## Getting Started

You'll need to meet the requirements below to have the service running on your machine.

### Prerequisites.

Requirements for the software and other tools to build, test and push

- [Maven](https://maven.apache.org/)
- [Java 21 or above](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

  Requirements for the software to run

- [Keycloak](https://www.keycloak.org/)
- [Java 21 or above](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

### Installing

You're required to have the requirements above fully setup before proceeding to the following steps.

With the above done, you can go ahead building and running the application

    ./mvnw clean install

Running tests

    ./mvnw clean test

If you experience any error at this point, reach out to one of the developers

Build the application

    ./mvnw clean package

After building the application, an executable `target/*.jar` is generated that can be run

    ./*.jar

The application health endpoint is exposed under `/actuator/health`
