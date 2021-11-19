build:
	docker-compose build
start:
	docker-compose up -d
enterSQL:
	docker exec -it mysql bash
re-setup:
	docker-compose down
debug: re-setup build start enterSQL
quickstart: start enterSQL
