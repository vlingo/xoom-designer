version: '3.5'

services:

  rabbitmq:
    image: rabbitmq:3.8.3-management
    container_name: rabbitmq
    environment:
      - RABBITMQ_USERNAME=guest
      - RABBITMQ_PASSWORD=guest
      - RABBITMQ_VHOST=/
    ports:
      - 5672:5672
      - 15672:15672
    volumes:
      - 'rabbitmq'
<#list dockerComposeServices as service>

  ${service}
</#list>