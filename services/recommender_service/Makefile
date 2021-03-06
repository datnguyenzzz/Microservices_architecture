DOCKER_NETWORK = recommender_service_default
hadoop_branch = 2.0.0-hadoop2.7.4-java8
hbase_branch = 1.0.0-hbase1.2.6
#docker exec -it "container" bash
init:
	docker-compose up -d dynamodb kafka redis postgresql dynamodb serviceregistry

buildMvn:
	mvn -pl test-recommender-api -am clean install
	mvn -pl recommender-real-time-service -am clean install
	mvn -pl recommendation-queries-service -am clean install

mimicPressure:
	py mimicBulkRequest.py

testRecommender:
	docker-compose -f docker-compose-test-recommender.yml up -d --scale supervisor=1
	docker-compose -f docker-compose-test-recommender.yml ps

testCassandra:
	docker exec -it cassandra1 bash -c "nodetool status"

testProducer:
	docker exec -it kafka kafka-console-producer.sh --bootstrap-server localhost:9093 --topic fromRecommendationServiceTopic

testConsumer:
	docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9093
	docker exec -it kafka kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic fromRecommendationServiceTopic --from-beginning --property print.key=true --property print.headers=true --property print.timestamp=true

start:
	mvn clean
	mvn install
	docker-compose -f docker-compose-extra-services.yml -f docker-compose-main-services up -d --scale supervisor=1 --scale commandrating=1 --scale queryrating=1
	docker-compose ps
	docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' prometheus

shutdown:
	docker stop $(shell docker ps -a -q)

	docker-compose -f docker-compose-test-recommender.yml down

	docker volume prune  -f
	docker container prune  -f