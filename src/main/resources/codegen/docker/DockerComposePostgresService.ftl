postgres:
    image: postgres:latest
    container_name: postgres
    ports:
      - '5432:5432'
    volumes:
      - 'postgres'
    environment:
      POSTGRES_DB: ${appName}<#if modelCategory?has_content>-${modelCategory}</#if>
      POSTGRES_USER: postgres
      ALLOW_EMPTY_PASSWORD: "yes"
      POSTGRES_HOST_AUTH_METHOD: "trust"