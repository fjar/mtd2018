# Docker Practice

## Basics
```
docker run hello-world

docker ps
docker ps --all

docker container logs <name|id>
docker logs <name|id>

docker inspect <name|id>
docker inspect -f '{{.State.Pid}}' <name|id>
```

## Containers and Volumes
We are going to create a container running MongoDB database. We will also check when data written inside the container is persisted or not.

The official image for the MongoDB database defines two volumes. If we do not specify otherwise, Docker will create these two volumes when we run the container, but they will be difficult to identify by name. So we are going to create our own volumes and attach them to the MongoDB container:

```
docker volume create mtdmongo_db
docker volume create mtdmongo_configdb

docker volume ls
docker volume inspect mtdmongo_db
```

Create a container running MongoDB, using the volumes just created:
```
docker run --name mtdmongo --mount type=volume,source=mtdmongo_db,target=/data/db --mount type=volume,source=mtdmongo_configdb,target=/data/configdb -d mongo:3.6
```
We will use the mongo shell to insert some data in the database. Connect to the mongo shell in the new container:
```
docker exec -it mtdmongo mongo
```
Once in the mongo shell, insert some data:
```
show databases
use MTD
db.USERS.insert({'name':'some_name'})
db.USERS.find()

exit
```
Write some file in the container, outside the location of the volumes. We will use this file as a flag to check under which conditions it is persisted or not:
```
docker exec mtdmongo touch am_i_persistent
docker exec mtdmongo ls -l am_i_persistent
```
Let's stop the container and start it again (or restart it):
```
docker stop mtdmongo
docker start mtdmongo
```
Connect again to the mongo shell and check that data is still available:
```
docker exec -it mtdmongo mongo
```
```
show databases
use MTD
show collections
db.USERS.find()

exit
```
Check the existence of the flag file:
```
docker exec mtdmongo ls -l am_i_persistent
```
Get rid of the previous container:
```
docker stop mtdmongo
docker rm mtdmongo
```
We have deleted the container but the volumes are still available (if we had executed ```docker rm --volumes mtdmongo``` then the volumes would have been deleted).

Now, we run a new container, using our volumes:
```
docker run --name mtdmongo --mount type=volume,source=mtdmongo_db,target=/data/db --mount type=volume,source=mtdmongo_configdb,target=/data/configdb -d mongo:3.6
```
Let's check again if the data and flag files exist:
```
docker exec -it mtdmongo mongo
```
```
show databases
use MTD
db.USERS.insert({'name':'some_name'})
db.USERS.find()

exit
```
```
docker exec mtdmongo ls -l am_i_persistent
```

Remove the ontainer
```
docker stop mtdmongo
docker rm --volumes mtdmongo
```

## Docker Swarm

Init the swarm
```
docker swarm init --advertise-addr 10.0.100.1

docker info
docker node ls
```

Add the workers:
```
lxc exec dw00 -- <command reported by swarm init>
lxc exec dw01 -- <command reported by swarm init>
docker node ls
```

If you forget which is the command to add a worker to the swarm, execute:
```
docker swarm join-token worker
```
or to add a manager:
```
docker swarm join-token manager
```

Create a service
```
docker service create --replicas 1 --name test_service alpine ping www.google.com
```

Check the new service
```
docker service ls
docker service inspect test_service
docker service inspect --pretty test_service
docker service ps test_service
```
In the node where the container for test_service is running:
```
docker ps
docker logs -f test_service.1.<id>
```

Let's scale the service
```
docker service scale test_service=4
docker service ps test_service
```

Scale down:
```
docker service scale test_service=2
docker service ps test_service
```
Remove the service
```
docker service rm test_service
docker service ls
```

## Docker Compose
Install Compose
```
sudo curl -L https://github.com/docker/compose/releases/download/1.19.0/docker-compose-Linux-x86_64 -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```
```
mkdir myapp
cd myapp
```
Create app.py file
```
cat > app.py <<EOF
import time

import redis
from flask import Flask


app = Flask(__name__)
cache = redis.Redis(host='redis', port=6379)


def get_hit_count():
    retries = 5
    while True:
        try:
            return cache.incr('hits')
        except redis.exceptions.ConnectionError as exc:
            if retries == 0:
                raise exc
            retries -= 1
            time.sleep(0.5)


@app.route('/')
def hello():
    count = get_hit_count()
    return 'Hello World! I have been seen {} times.\n'.format(count)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
EOF
```
Create requirements.txt file
```
cat > requirements.txt <<EOF
flask
redis
EOF
```

Create the Dockerfile
```
cat > Dockerfile <<EOF
FROM python:3.4-alpine
ADD . /code
WORKDIR /code
RUN pip install -r requirements.txt
CMD ["python", "app.py"]
EOF
```

Create the compose file
```
cat > docker-compose.yml <<EOF
version: '3'
services:
  web:
    build: .
    ports:
     - "5000:5000"
  redis:
    image: "redis:alpine"
EOF
```

Build and run the application
```
docker-compose up
```
Use the app: http://ip:5000

Stop the application
```
docker-compose down
```

Currently, to modify the code of the app we need to rebuild the image. We can add the myapp directory as a volume to the docker container in the compose file
```
cat > docker-compose.yml <<EOF
version: '3'
services:
  web:
    build: .
    ports:
     - "5000:5000"
    volumes:
     - .:/code
  redis:
    image: "redis:alpine"
EOF
```
Rebuild and launch the application
```
docker-compose up --build
```
Play a little bit with the app, modify app.py and check what happens.

Stop the application again
```
docker-compose down
```
Now we will add a MongoDB to our application.
Update the app.py file
```
cat > app.py <<EOF
import time
import datetime

import redis
from flask import Flask
from flask_pymongo import PyMongo


app = Flask(__name__)
app.config['MONGO_DBNAME'] = 'reqs'
app.config['MONGO_URI'] = 'mongodb://mongo:27017/reqs'
mongo = PyMongo(app)
cache = redis.Redis(host='redis', port=6379)


def get_hit_count():
    retries = 5
    while True:
        try:
            return cache.incr('hits')
        except redis.exceptions.ConnectionError as exc:
            if retries == 0:
                raise exc
            retries -= 1
            time.sleep(0.5)


@app.route('/')
def hello():
    count = get_hit_count()
    ts = str(datetime.datetime.now())
    mongo.db.hits.insert({'hits': count, 'ts': ts})
    return 'Hello New World! I have been seen {} times.\n'.format(count)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
EOF
```

Upddate requirements
```
cat > requirements.txt <<EOF
flask
redis
flask_pymongo
EOF
```

Update the compose file
```
cat > docker-compose.yml <<EOF
version: '3'
services:
  web:
    build: .
    ports:
     - "5000:5000"
    volumes:
     - .:/code
  redis:
    image: "redis:alpine"
  mongo:
    image: "mongo:3.6"
    ports:
     - "27017:27017"
EOF
```

Rebuild and launch the application
```
docker-compose up --build
```

After playing with the app, connect to the mongo container
```
docker exec -it mtdmongo mongo
```
and check if there are hits recorded
```
show databases
use reqs
db.hits.find()

exit
```
Stop the application
```
docker-compose down
```
