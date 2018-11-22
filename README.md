# GreatGaming
# Getting started
- Download maven https://maven.apache.org/download.cgi and install it https://maven.apache.org/install.html
- Clone the repository from git in some directory $SOME_DIRECTORY

# Packaging
```
cd $SOME_DIRECTORY\greatgaming-server
mvn package
```

# Running
```
java -cp target\greatgaming-server-1.0-SNAPSHOT-jar-with-dependencies.jar com.greatgaming.server.Main
```