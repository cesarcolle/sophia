#!/usr/bin/env bash


echo "add xyz service"
curl -H "Content-Type: application/json" -X POST -d '{"name":"xyz", "address" : "123.com", "port":123}' http://localhost:8080/addService

echo "add abc service"
curl -H "Content-Type: application/json" -X POST -d '{"name":"abc", "address" : "456.com", "port":456}' http://localhost:8080/addService


#echo "list all services"
#curl -X GET http://localhost:8080/list
#
#echo "list all services"
#curl -X GET http://localhost:8080/list
#
#sleep 5
#echo "find service xyz"
#curl -H "Content-Type: application/json" -X GET http://localhost:8080/getService?nameService=xyz


