# Creación de usuarios
En la consola de AWS
Creación del grupo
```
IAM > Groups > Create New Group

Dar un nombre (mtd_gXX) > Next

Asignar la política AdministratorAccess > Next Step > Create Group
```
Creación del usuario
```
IAM > Users > Add User

Dar un nombre (mtd_uXX). Habilitar Programmatic Access (y Console si se desea) > Next: Permissions

Asignar el usuario al grupo creado previamente > Next: Review > Create User
```
Se puede descargar un fichero csv que contien el Access ID, la Secret Key y la url para conectarse a la consola de la cuenta en AWS.

# Setup
Preparación del servidor donde vamos a realizar la práctica

## Install AWS CLI and boto3 (AWS Python SDK)
Ubuntu 16.04
```
sudo apt update
sudo apt -y upgrade

sudo apt install -y python3-pip
pip3 install --upgrade pip

pip install boto3
pip install awscli --upgrade --user

complete -C '/home/ubuntu/.local/bin/aws_completer' aws
```
Check
```
aws --version
```

## Configure credentials and default region
Replace <ACCESS_KEY> and <SECRET_KEY> by your own  values
```
mkdir .aws
cat > ~/.aws/credentials <<EOF
[default]
aws_access_key_id = <ACCESS_KEY>
aws_secret_access_key = <SECRET_KEY>
EOF
chmod 400 ~/.aws/credentials
```
Replace <default_region>, e.g. eu-west-1
```
cat > ~/.aws/config <<EOF
[default]
region=<default_region>
output = json
EOF
```
o, de forma alternativa:
```
aws configure
``

Check
```
aws ec2 describe-regions --output table
aws iam list-groups
aws iam list-users

```
