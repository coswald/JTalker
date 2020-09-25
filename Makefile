#The J variable is the jar compiler
J=jar
#The Javac compiler
JC=javac
#The flags to create a jar file
JFLAGS=cf
#The flags for compiling source
JCFLAGS=-d bin -Xlint:all

#The following variables are used for documentation generation
#see http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/javadoc.html
JD=javadoc
WTITLE="JTalker API"
DTITLE="JTalker By the Contributors of JTalker"
HEADER="<b>JTalker</b>"
BOTTOM='<font size="-1"><a href="https://github.com/coswald/JTalker">Github Home</a><br/> Copyright &copy; 2020 Coved W Oswald.<br/></font>'
SRCDIR="./src"
DOCDIR="./docs"

.DELETE_ON_ERROR:

all: jar clean
	@mkdir -p build
	@mv jtalker.jar build/
	@echo "Moved Jar to build..."

jar: bin
	@cd bin && $(J) $(JFLAGS) ../jtalker.jar * && cd ..
	@echo "Made Jar..."

bin: source
	@mkdir -p bin
	@$(JC) $(JCFLAGS) @sources
	@echo "Compiling successful"

source:
#	@if [ ! -f sources ]; then echo "Generating source files" && if [ "$(OS)" = "Windows_NT" ]; then dir /s/b *.java > sources; else find . -name *.java > sources; fi else echo "Sources file exists"; fi
	@if [ ! -f sources ]; then echo "Generating source files" && find . -name *.java > sources; else echo "Sources file exists"; fi

docs: source
	@mkdir -p docs
	@echo "Making docs..."
	@$(JD) -sourcepath $(SRCDIR) -d $(DOCDIR) -use -splitIndex -windowtitle $(WTITLE) -doctitle $(DTITLE) -header $(HEADER) -bottom $(BOTTOM) -linkoffline https://docs.oracle.com/en/java/javase/15/docs/api/ https://docs.oracle.com/en/java/javase/15/docs/api -html5 -author -version @sources
	@echo "Adding favicon..."
	@./lib/add-favicon ./docs ./img/JTalker.ico && make clean

clean:
	@echo "Cleaning up"
	@rm -rf bin >/dev/null 2>&1
	@rm -f sources 2>/dev/null
