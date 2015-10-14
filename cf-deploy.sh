#!/bin/bash

mvn clean install
cf push

echo "Success!"
