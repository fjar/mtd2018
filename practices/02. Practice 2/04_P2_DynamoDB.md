# DynamoDB

Creamos una tabla en DynamoDB
```
aws dynamodb create-table \
    --table-name app2.db \
    --attribute-definitions \
        AttributeName=type,AttributeType=S \
        AttributeName=count,AttributeType=N \
    --key-schema AttributeName=type,KeyType=HASH AttributeName=count,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1

aws dynamodb list-tables
aws dynamodb describe-table --table-name app2.db --output table

aws dynamodb put-item \
    --table-name app2.db \
    --item '{"type":{"S":"hits"}, "count": {"N":"0"}}'
```
Con Python
```
python3 code_snippets/dynamodb_put_item.py k1 4
```
Probar a añadir algún item más. El segundo parámetro debe ser un número pues corresponde con el atributo `count` que hemos definido como N.

Cuando hayamos terminado, podemos borrar la tabla:
```
aws dynamodb delete-table --table-name app2.db
```
El borrado es asíncrono: podemos ver como la tabla pasa por un estado DELETING.
