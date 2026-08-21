up:
	docker compose -f docker/docker-compose.yml up -d
down:
	docker compose -f docker/docker-compose.yml down
logs:
	docker compose -f docker/docker-compose.yml logs -f
ps:
	docker compose -f docker/docker-compose.yml ps
downvol:
	docker compose -f docker/docker-compose.yml down -v
