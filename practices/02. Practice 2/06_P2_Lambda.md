# Lambda

## Procesar un fichero subido a S3

### IAM Role
Necesitamos crear un role que usaremos para dar a nuestra función lambda los permisos de leer y escribir en buckets de S3
```
# En AWS Console
IAM > Roles > Create Role
AWS Service > Lambda > Next: Permissions

Buscar AWSLambda y seleccionar AWSLambdaExecute. Next: Review
Role name: s3-lambda-execution-role
Create Role

En el nuevo role: Add Inline Policy
Service: S3
Actions: Read.GetObject y Write.PutObject
Resources: all Resources

Review

Name: s3_mtd_policy

Create Policy
```
### Desarrollo y Pruebas de la función
En la consola AWS creamos una función lambda:
```
Nombre: s3_lambda
Runtime: python 3.6
Existing role: s3-lambda-execution-role
```
Pegar el código s3_lambda.py en el editor online. Salvar.

Subimos un fichero a S3 para hacer pruebas a mano
```
aws s3 cp s3_website/index.html s3://mtd2018-files
```
Ahora podemos crear un evento y probar la función manualmente.

### Permisos

El siguiente paso es asociar un evento a la función. Para ello necesitamos primero dar permisos a S3 para que pueda ejecutar nuestra función
```
aws lambda add-permission --function-name s3_lambda \
--statement-id stmt-00001 --action "lambda:InvokeFunction" \
--principal s3.amazonaws.com --source-arn arn:aws:s3:::mtd2018-files

# Check
aws lambda get-policy --function-name s3_lambda
```
### Defiinición del Evento en S3
También necesitamos habilitar en S3, para el bucket mtd2018-files, el evento ObjectCreate (All) y enviarlo a nuestra función lambda.

### Listo!

Finalmente, podemos crear un fichero, subirlo al bucket mtd2018-files y veremos cómo se ejecuta automáticamente la función.

## Microservice lambda
En este caso, crearemos la función lambda a partir de un blueprint:
microservice-http-endpoint
```
# Basic Information
Lambda name: mtd_lambda
Role name: mtd_lambda_role

# API Gateway
API: Create new API
API name: mtd_api
Deployment stage: prod
Security: open
```
Pegar el código mtd_lambda.py en el editor online. Salvar.

Ya podemos invocar nuestra función invocando el API:
```
https://{restapi_id}.execute-api.{region}.amazonaws.com/prod/mtd_lambda
```
