#!/bin/sh
mvn clean install
mvn release:clean
mvn release:prepare
mvn release:perform


