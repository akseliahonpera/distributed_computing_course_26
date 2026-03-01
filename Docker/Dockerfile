FROM eclipse-temurin:21-jdk-alpine
LABEL hermes.fi=asd
COPY server/server_fat.jar server.jar
COPY server/certs/ds26-truststore.p12 certs/ds26-truststore.p12 
COPY server/certs/oulu.p12 certs/oulu.p12 
COPY server/configs/node0_conf.txt node0_conf.txt
COPY server/configs/node1_conf.txt node1_conf.txt
COPY server/configs/node2_conf.txt node2_conf.txt
COPY server/users.txt users.txt
EXPOSE 8001
EXPOSE 8002
EXPOSE 8003

ENV JAVA_OPTS="-Djavax.net.ssl.trustStore=certs/truststore.p12 -Djavax.net.ssl.trustStorePassword=Gambiinakiuas522 -Djavax.net.ssl.trustStoreType=PKCS12"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /server.jar"]
