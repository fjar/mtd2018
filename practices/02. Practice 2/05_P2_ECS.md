# ECS

## Repositorio
En primer lugar vamos a crear un repositorio de Docker alojado en AWS
```
aws ecr create-repository --repository-name mtd/app1
```
Necesitamos el comando para logarnos en el registry remoto
```
aws ecr get-login --no-include-email --region us-east-1
```
Ejecutar el comando que AWS devuelve en el comando anterior. Será algo como esto:
```
docker login -u AWS -p eyJw....== https://573884811710.dkr.ecr.us-east-1.amazonaws.com
```

## App1 - HelloWorld
Go to the app1 directory and build the
```
cd app1
docker build -t mtd/app1 .
```
Corremos un contenedor localmente para ver el comportamiento esperado:
```
docker run -p 80:80 --name test_app1 -d mtd/app1
curl http://localhost/
docker stop test_app1
docker rm -v test_app1
```
Etiquetamos la imagen para subirla al repo remoto
```
docker tag mtd/app1:latest 573884811710.dkr.ecr.us-east-1.amazonaws.com/mtd/app1:latest
```
Push
```
docker push 573884811710.dkr.ecr.us-east-1.amazonaws.com/mtd/app1:latest
```

### Despliegue de App1
Vamos a desplegar App1 en AWS usando el servicio de contenedores ECS.
Usaremos el modo Fargate, que no require que gestionemos la infraestructura (instancias de EC2 donde corren los contenedores...)
```
# AWS Console
https://console.aws.amazon.com/ecs/home?region=us-east-1#/firstRun
```
```
# Container
Name: mtd-Container

# Task Definition
Name: mtd-task

# Service
Name: mtd-service
Num tasks: 2
