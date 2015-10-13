#!/bin/sh

echo "Creating test data...";

echo "Creating 30 apprentice types...";
for i in 1 2 3 4 5 6 7 8 9; do
	curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/apprenticeTypes -d "{\"title\": \"at$i\"}";
done;

echo "Creating 30 supervisors...";
for i in 1 2 3 4 5 6 7 8 9; do
	curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/persons -d "{\"ldap\": \"sv$i\", \"firstName\": \"supfin$i\", \"lastName\": \"vsor$i\"}";
	curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/supervisors -d "{\"ldap\": \"sv$i\", \"person\": \"persons/sv$i\", \"dept\": \"CC $i\"}";
done;

echo "Creating 30 apprentices with each 1 idea and project...";
for i in 1 2 3 4 5 6 7 8 9; do
    curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/persons -d "{\"ldap\": \"ldap$i\", \"firstName\": \"fin$i\", \"lastName\": \"lan$i\"}";
	curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/apprentices -d "{\"ldap\": \"ldap$i\", \"person\": \"persons/ldap$i\", \"beginApprenticeship\": \"2015-10-0$i\", \"supervisor\": \"supervisors/sv$i\", \"apprenticeType\": \"apprenticeTypes/at$i\"}";
    curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/ideas -d "{\"title\": \"title $i\", \"author\": \"persons/ldap$i\", \"description\": \"44*$i\"}";
    curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/projects -d "{\"description\": \"lorem ipsum $i\", \"title\": \"proj title $i\", \"status\": \"DEVELOPMENT\", \"manager\": \"persons/ldap$i\", \"beginning\": \"2016-12-0$i\", \"end\": \"2017-05-0$i\"}";
done;

echo "Creating 30 trainings...";
for i in 1 2 3 4 5 6 7 8 9; do
	curl -s -H "Content-Type:application/json" -X POST http://localhost:8080/trainings -d "{\"name\": \"training name $i\", \"beginning\": \"2013-01-0$i\", \"end\": \"2013-03-0$i\"}";
done;

echo "Done!";
