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

#### Run MySQL instance locally

- Use below commands to create MySQL instance , required database and table.
  
  
        #Run docker container
        docker run -d --name mysql -e MYSQL_ROOT_HOST=%  -e  MYSQL_ROOT_PASSWORD=root -v mysql-db:/var/lib/mysql -p 3306:3306 -d mysql/mysql-server	
    
        #Connect to  the container
        docker exec -it mysql mysql -u root -p
        
        #Create required database 
        create database isom_Service;
        use isom_Service;
        
        #create table
        CREATE TABLE  id_table (fo_id varchar(100));
        
        #insert sample record
        INSERT INTO id_table (fo_id) VALUES ('1c238d20-a0b8-11ea-aafa-4de03f00b166');
        
        #Get records from the table
        select * from id_table; 
        ``