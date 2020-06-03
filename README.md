#### Pre requisites 
- Make sure the isom-service instance is running.
- Make sure the MySQL instance is running.

#### Run MySQL instance locally

- Use below commands to create MySQL instance , required database and table.
  
        #Run docker container
        docker run -d --name mysql -e MYSQL_ROOT_HOST=%  -e  MYSQL_ROOT_PASSWORD=root -v mysql-db:/var/lib/mysql -p 3306:3306 -d mysql/mysql-server	
    
        #Connect to  the container
        docker exec -it mysql mysql -u root -p
        
        #Create required database 
        create database isom_Service;
        use isom_Service;
        
        #Create table
        CREATE TABLE  id_table (sr_id INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, fo_id varchar(100));
        
        #Insert sample record
        INSERT INTO id_table (fo_id) VALUES ('1c238d20-a0b8-11ea-aafa-4de03f00b166');
        
        #Get records from the table
        select * from id_table; 
       
#### Run for create FO objects 
- Update the input params values in src/test/resources/application.properties , scenario names in scenario_name.csv and run below command.

        mvn clean compile gatling:test -Dgatling.simulationClass=com.isom.service.simulations.ISOMCreateLoadTest -Dusers=5 -Dduration=10
 
#### Run for get FO objects
- Update the input params values in src/test/resources/application.properties and run below command.

        mvn clean compile gatling:test -Dgatling.simulationClass=com.isom.service.simulations.ISOMGetLoadTest -Dusers=5 -Dduration=10
 
#### Verify test results
- Checkout the gatling reports for the last run test at below location.
    
        <project.basedir>/isom-test-reports