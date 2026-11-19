# 02 Event publishing after commit
* **Context**: dual-write problem (DB + Kafka can’t share a transaction)
* **Decision**: AFTER_COMMIT listener for the MVP
* **Consequence**: events can be lost if Kafka fails after commit
* **Future**: transactional outbox (outbox_events table + poller/relay)