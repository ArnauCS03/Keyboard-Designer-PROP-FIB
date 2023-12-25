JUNIT_JAR=./../../FONTS/libs/junit-4.12.jar
HAMCREST_JAR=./../../FONTS/libs/hamcrest-core-1.3.jar

CLASS_DIR=./../Class

java -cp .:$JUNIT_JAR:$HAMCREST_JAR:$CLASS_DIR org.junit.runner.JUnitCore JUnit.LeerInputTest
