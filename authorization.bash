cp -R --force /opt/app_jar/Authorization.jar  /opt/app_jar/oldversion/Authorization.jar

pkill -f Authorization.jar;

cp -R ./authorization-application/target/Authorization.jar  /opt/app_jar/Authorization.jar

chmod 7777 /opt/app_jar/Authorization.jar

java -Xms1024m -Xmx1024m -Dspring.profiles.active=stage -jar /opt/app_jar/Authorization.jar&









