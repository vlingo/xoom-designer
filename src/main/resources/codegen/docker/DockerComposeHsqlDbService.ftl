hsqldb:
    image: blacklabelops/hsqldb
    container_name: hsqldb
    ports:
      - "9001:9001"
    volumes:
      - /opt/database
    environment:
      # Java-JRE 8 Parameters
      ENV JAVA_VM_PARAMETERS=
      # HSQLDB Development Output
      ENV HSQLDB_TRACE=true
      ENV HSQLDB_SILENT=false
      # HSQLDB Remote Connections
      ENV HSQLDB_REMOTE=true
      # Database name and alias for jdbc url: jdbc:hsqldb:hsql://localhost/test
      ENV HSQLDB_DATABASE_NAME=${appName}-${modelCategory}
      ENV HSQLDB_DATABASE_ALIAS=hsqldb
      # Specify the host if using boot2docker, e.g. 192.168.99.100
      ENV HSQLDB_DATABASE_HOST=localhost
      # Change username and password if necessary
      ENV HSQLDB_USER=sa
      ENV HSQLDB_PASSWORD=
    labels:
      com.blacklabelops.description: "HSQL Development System"
      com.blacklabelops.service: "hsqldb-server"