# Account Manager

### Envrionment Requirement

* Jdk 8
* Maven

#### Swagger:

http://localhost:8080/swagger-ui.html

### example request:

#### check balance

```shell
curl -X 'GET' \
  'http://localhost:8080/v1/balance?accountId=12345678' \
  -H 'accept: */*'
```

#### launch a transfer

```shell
curl -X 'POST' \
  'http://localhost:8080/v1/transfer' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "initiatorAccountId": 12345678,
  "counterpartAccountId": 88888888,
  "amount": 100
}'
```

## Discussion

### Data consistency and thread safety

There are multiple ways to guarantee data consistency and thread safety.  
In this project, I use optimistic lock and retry mechanism

* ##### Optimistic locking (used in this project)
  * Pro:
    * More suitable for applications that database reads are more frequent than database write.
    * It offers better scalability
  * Con:
    * Need extra application logic to handle record update and optimistic locking failure correctly.
* ##### Pessimistic locking on row level for databases
  * Pro:
    * Cleaner and easier to understand application logic
  * Con:
    * Worse performance, can introduce slowness on other read only query, but can be remedied with MVCC support
    * Need to handle deadlock, possibly by sequentially locking database row in id order.

### Other alternative architecture

Save transfer record in an immutable and append only transaction log table. Balance is calculated on the fly with past
transfer histories. To improve performance, can run batch job to generate carry forward balance table for balance
snapshot on start of day or start of month to avoid repetitive calculation

Pro:
* It is easier to audit transfer records

Con:
* It is harder to guarantee data consistency with this approach. Possible way:
* table lock, transfer record can only be input one at a time.
* row lock on account

### Checklist for requirements and best practices:

* h2 DB saved on the root folder with h2.mv.db as name
* avoid floating-point number for financial calculation
* Restful API design
  * Use get for data retrial, post for create
  * resource as noun
    * Balance
    * Transfer
  * resource url names are plural
  * swagger ui
    * endpoints are documented
  * meaningful response status code
* API versioning
* support http2
* Response is compressed with gzip
* Database schema versioning
* project structure follows DDD
* validation on incoming requests
  * account must exist
  * transfer amount must > 0
  * after transfer, new balance must >= 0
  * minimal transfer amount must >= 0.01
  * transfer amount must not have more than two decimal place
  * initiator must not be counterpart
* E2E tests
  * happy path
  * edge cases
  * race condition
