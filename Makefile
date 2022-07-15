MAVEN := mvn
SRC:= lexer-chilero
JARFILE := lexer-chilero/target/lexer-chilero-1.0-SNAPSHOT-jar-with-dependencies.jar

compile: Makefile
	${MAVEN} -f ${SRC}/pom.xml clean verify

lexer:
	@rm -f lexer
	echo '#!/bin/bash' >> lexer
	echo 'java -jar ${JARFILE} $$*' >> lexer
	chmod 755 lexer

dofactorial: clean compile lexer
	-./lexer cooltests/factloop.cl cooltests/atoi.cl
	-./mycoolc cooltests/factloop.cl cooltests/atoi.cl
	-./bin/spim cooltests/factloop.s < cooltests/factloop.test

dostack: clean compile lexer
	-./lexer cooltests/stack.cl cooltests/atoi.cl
	-./mycoolc cooltests/stack.cl cooltests/atoi.cl
	-./bin/spim cooltests/stack.s < cooltests/stack.test

clean:
	-${MAVEN} -f ${SRC}/pom.xml clean
	-rm -f cooltests/*.s lexer
	-rm -f input.test
