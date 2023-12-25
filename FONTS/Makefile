SRC_DIR := .
OUT_DIR := ../EXE/Class
LIBS = "$(OUT_DIR):libs/junit-4.12.jar:libs/hamcrest-core-1.3.jar:."
LIBS_MOCK = "$(OUT_DIR):libs/junit-4.12.jar:libs/hamcrest-core-1.3.jar:/libs/byte-buddy-1.12.16.jar:/libs/byte-buddy-agent-1.12.16.jar:libs/mockito-core-4.9.0.jar:libs/objenesis-3.3.jar:."
TEST_DIR := JUnit
JAVAC := javac
JAVA := java
JFLAGS := --release 11

# Encontrar todos los .java de la carpeta FONTS
FILES :=  $(wildcard $(SRC_DIR)/Domini/*.java) \
          $(wildcard $(SRC_DIR)/Presentacio/*.java) \
          $(wildcard $(SRC_DIR)/Persistencia/*.java) \
          $(wildcard $(SRC_DIR)/Excepcions/*.java)



# si se hace make  solo, hace esto, compilar todas las clases
all: compile


# compilar todas las clases y moverlo a EXE/Class (ejecutar el main desde alli, hay un script)
compile: $(FILES)
	$(JAVAC) $(JFLAGS) -d $(OUT_DIR) $^

run:
	$(JAVA) -cp $(OUT_DIR) Presentacio.Main


# Tests JUnit

#Compilar todos los tests a la vez, iran en EXE/JUnit   (primero hay que compilar todos los .java, pero ya se asegura de hacerlo este comando)
compileTests: compile compileCotaGLTest compileHungarianTest compileQAPTest compileLeerInputTest compileProcesarTextoTest


# Compilar test de la clase A_cotaGilmoreLawler, en el classpath se idica donde esta el A_cotaGilmoreLawler.class ya compilado, y las dos librerias de JUnit
compileCotaGLTest:
	$(JAVAC) -cp $(LIBS) -d $(OUT_DIR) $(TEST_DIR)/CotaGLTest.java

# Compilar Hungarian con los test de la clase A_hungarian, en el classpath se idica donde esta el A_hungarian.class ya compilado, y las dos librerias de JUnit
compileHungarianTest:
	$(JAVAC) -cp $(LIBS) -d $(OUT_DIR) $(TEST_DIR)/HungarianTest.java

# Compilar QAP con los tests de la clase A_QAP, en el classpath se indica donde esta A_QAP.class y las dos librerias de JUnit y las 4 para el mock
compileQAPTest:
	$(JAVAC) -cp $(LIBS_MOCK) -d $(OUT_DIR) $(TEST_DIR)/QAPTest.java

# Compilar test de la clase P_LeerInput en el classpath se indica donde esta P_LeerInput.class y las dos librerias de JUnit
compileLeerInputTest:
	$(JAVAC) -cp $(LIBS) -d $(OUT_DIR) $(TEST_DIR)/LeerInputTest.java

# Compilar test de la clase ProcesarTexto en el classpath se indica donde esta ProcesarTexto.class y las dos librerias de JUnit
compileProcesarTextoTest:
	$(JAVAC) -cp $(LIBS) -d $(OUT_DIR) $(TEST_DIR)/ProcesarTextoTest.java



# Elimina todos los .class de las clases y de los tests en EXE/Class
clean:
	find $(OUT_DIR) -name "*.class" -exec rm -rf {} +