# Music Gateway
![Music Notes Image](music_image.png)

## Overview
This is an HTTP gateway to some cool music metadata databases. In addition to routing, it offers some content enrichment. The solution is based on Spring Cloud framework and is packaged as a docker image.

## Main Features / Design Decisions
- configurable routing to several backing services with minimal coding thanks to Spring Cloud Zuul proxy.
- Content enrichment filters (interceptors).
- Autogenerated correlation IDs for easier transaction tracing and troubleshooting.
- Microservice deployable as a docker container with an inbuilt health check end point for runtime monitoring and management.
- Insightful management endpoints thanks to Spring Boot Actuator framework:
    - http://localhost/actuator/info
    - http://localhost/actuator/health
    - http://localhost/actuator/metrics
    - http://localhost/actuator/routes
    - http://localhost/actuator/filters
- Helpful home page with sample requests
    - http://localhost

## Synopsis
To build (package) the docker image
```bash
$ cd music-gateway
$ ./mvnw clean package docker:build
```
To run the Docker container (Ctrl+C to remove container)
```bash
$ docker run -it --rm -p 80:80 --name music-gateway music-gateway
```

To check container status
```bash
$ docker ps
CONTAINER ID        IMAGE                              COMMAND                  CREATED             STATUS                    PORTS                                                  NAMES
70f2e5e04193        music-gateway                      "java -jar /app/musi…"   49 seconds ago      Up 49 seconds (healthy)   0.0.0.0:80->80/tcp                                     music-gateway
```

## Browse to the home page
![Home Page Image](home_page.png)
