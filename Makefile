JAVAC = javac
JAVA = java
JAVACFLAGS = #-Xlint:deprecation
JAVAFLAGS = -Djava.library.path=/usr/share/lwjgl/native/linux
CLASSPATH = /usr/share/java/slick.jar:/usr/share/lwjgl/lwjgl.jar:/usr/share/lwjgl/lwjgl_util.jar:/usr/share/lwjgl/jar/AppleJavaExtensions.jar:/usr/share/lwjgl/jar/asm-debug-all.jar:/usr/share/lwjgl/jar/jinput.jar:/usr/share/lwjgl/jar/lwjgl-debug.jar:/usr/share/lwjgl/jar/lwjgl_test.jar::/usr/share/lwjgl/jar/lwjgl_util_applet.jar:/usr/share/lwjgl/jar/lwjgl_util.jar:/usr/share/lwjgl/jar/lzma.jar:./

SOURCES = $(shell find . -name "*.java")
OBJS = $(SOURCES:.java=.class)

all :
	$(JAVAC) $(JAVACFLAGS) -cp $(CLASSPATH) $(SOURCES)
	@echo Done.

clean :
	@rm $(shell find . -name "*.class")

launch :
	@echo Launching $(TARGET)
	@$(JAVA) $(JAVAFLAGS) -cp $(CLASSPATH) $(TARGET) $(ARGS)