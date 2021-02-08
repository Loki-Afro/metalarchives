#!/bin/sh
mvn clean install -DskipTests
mvn release:clean -Darguments=-DskipTests
mvn release:prepare -Darguments=-DskipTests
mvn release:perform -Darguments=-DskipTests


