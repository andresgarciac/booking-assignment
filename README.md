
- start app

docker build -t booking-service .
docker-compose up

- connect to mysql
docker exec -it {container_id} /bin/bash
mysql -usa -p