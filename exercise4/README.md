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
# tại exercise4
docker build -t demo-app:local .
docker build -t demo-app:dev .

Khởi chạy prod-like:
cp .env.prod .env

Khởi chạy dev-like:
cp .env.dev .env

docker compose -f compose/docker-compose.prod.yml up -d
docker compose -f compose/docker-compose.prod.yml ps

docker compose -f compose/docker-compose.dev.yml up -d
docker compose -f compose/docker-compose.dev.yml ps

# Kiểm tra
- Health DB: docker logs mariaDB 
-> database system is ready to accept connections

- Health App: curl http://localhost:8080/actuator/health
-> {"status":"UP"}

- Database: 
  docker exec -it mariadb_prod mysql -u root -proot123 -e "USE authorization; SELECT * FROM users;"
  
  docker exec -it mariadb_dev mysql -u root -proot123 -e "USE authorization; SELECT * FROM users;"

# Log backend hoặc mariaDB
- backend: docker logs -f backend
- mariaDB: docker logs -f mariaDB

# Dừng và dọn
docker compose -f compose/docker-compose.prod.yml down
docker compose -f compose/docker-compose.dev.yml down

# Xóa cả dữ liệu
docker volume rm compose_db_data
docker volume rm compose_db_dev

# Check list
docker images demo-app:local
-> 375MB

docker ps
-> healthy

curl http://localhost:8080/actuator/health
-> {"status":"UP"}

