#### Run for create FO objects
- Update the input params values in src/test/resources/application.properties and run below commands as per the requirements.

    ``
    mvn clean compile gatling:test -Dgatling.simulationClass=com.isom.service.test.ISOMCreateLoadTest
    ``

#### Run for GET FO objects
- Update the input params values in src/test/resources/application.properties and run below commands as per the requirements.

    ``
    mvn clean compile gatling:test -Dgatling.simulationClass=com.isom.service.test.ISOMGetLoadTest
    ``
