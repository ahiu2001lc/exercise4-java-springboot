# Tên Dự Án

exercise4: Authentication and Authorization

## 🔹 Công nghệ sử dụng

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Spring Security (JWT)
- Maven
- MariaDB/MySQL
- Docker
- Lombok
- H2 driver

## 🔹 Cài đặt
Build network(only 1 time)
docker network create backend

Build image app
# tại exercise4(3 profiles)
docker build -t demo-app:redis .


docker compose -f compose/docker-compose.redis.yml up -d
docker compose -f compose/docker-compose.redis.yml ps

# Kiểm tra
- Health DB: docker logs mariaDB 
-> database system is ready to accept connections

- Health App: curl http://localhost:8080/actuator/health
-> {"status":"UP"}

- Database:
  docker exec -it mariadb_redis mysql -u root -proot123 -e "USE authorization; SELECT * FROM users;"

# Log backend hoặc mariaDB
- backend: docker logs -f backend
- mariaDB: docker logs -f mariaDB

# Dừng và dọn
docker compose -f compose/docker-compose.redis.yml down

# Xóa cả dữ liệu
docker volume rm compose_db_redis

# Check list
docker images demo-app:reids
-> 395MB

docker ps
-> healthy

curl http://localhost:8080/actuator/health
-> {"status":"UP"}
