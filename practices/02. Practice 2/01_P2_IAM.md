# IAM

## Gestión de  Usuarios y Grupos
Podríamos crear los grupos y usuario programáticamente, si tenemos los permisos adecuados
```
aws iam create-group --group-name mtd_g0x
aws iam create-user --user-name mtd_u0x

aws iam get-group --group-name mtd_g0x
aws iam get-user --user-name mtd_0x
```
Nuestro usuario no pertenece al grupo que hemos creado. Lo añadimos
```
aws iam list-groups-for-user --user-name mtd_u0x

aws iam add-user-to-group --user-name mtd_u0x --group-name mtd_g0x

aws iam list-groups-for-user --user-name mtd_u0x
aws iam get-group --group-name mtd_g0x
```

Ni el grupo ni el usuario tienen asignada ninguna política. Vamos a considerar que es un grupo de administadores.
```
aws iam list-user-policies --user-name mtd_u0x

aws iam list-group-policies --group-name mtd_g0x
aws iam list-attached-group-policies --group-name mtd_g0x

aws iam attach-group-policy --group-name mtd_g0x --policy-arn 'arn:aws:iam::aws:policy/AdministratorAccess'

aws iam list-group-policies --group-name mtd_g0x
aws iam list-attached-group-policies --group-name mtd_g0x

```
Finalmente borramos estos usuarios
```
aws iam remove-user-from-group --user-name mtd_u0x --group-name mtd_g0x
aws iam delete-user --user-name mtd_u0x

aws iam detach-group-policy --group-name mtd_g0x --policy-arn 'arn:aws:iam::aws:policy/AdministratorAccess'
aws iam delete-group --group-name mtd_g0x
```
