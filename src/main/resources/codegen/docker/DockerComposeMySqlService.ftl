mysql:
    image: mysql:5.7
    container_name: mysql
    volumes:
      - 'mysql'
    environment:
      MYSQL_ALLOW_EMPTY_PASSWORD: "true"
      MYSQL_DATABASE: ${appName}<#if modelCategory?has_content>-${modelCategory}-model</#if>