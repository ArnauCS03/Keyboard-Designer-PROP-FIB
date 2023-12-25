JUNIT_JAR=./../../FONTS/libs/junit-4.12.jar
HAMCREST_JAR=./../../FONTS/libs/hamcrest-core-1.3.jar
MOCKITO_JAR=./../../FONTS/libs/mockito-core-4.9.0.jar
BUDDYAGENT_JAR=./../../FONTS/libs/byte-buddy-agent-1.12.16.jar
OBJENESIS_JAR=./../../FONTS/libs/objenesis-3.3.jar
BUDDY_JAR=./../../FONTS/libs/byte-buddy-1.12.16.jar

CLASS_DIR=./../CLASS

javac -cp .:$JUNIT_JAR:$HAMCREST_JAR:$MOCKITO_JAR:$OBJENESIS_JAR:$BUDDYAGENT_JAR:$BUDDY_JAR:$CLASS_DIR org.junit.runner.JUnitCore QAPTest.java
