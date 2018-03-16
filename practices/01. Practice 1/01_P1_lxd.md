# LXC, LXD



Lista de contenedores
```
lxc ls
```

Lista de imágenes
```
lxc image list
```

Lista de repositorios de contenidos
```
lxc storage list
```

Creación de un contenedor
```
lxc launch images:alpine/3.6 testlxd
```

Ejecución de comandos en el contenedor, desde el host
```
lxc exec testlxd -- ls /
lxc exec testlxd -- touch mtd
```

Ejecución de comandos en el contenedor, dentro del contenedor
```
lxc exec testlxd -- /bin/sh
ls
pwd
cat /proc/meminfo
df -h .
exit
```

Configuración del contenedor
```
lxc config show testlxd
lxc stop testlxd
```

Profiles
```
lxc profile list
lxc profile show default
```
Creamos un nuevo perfil
```
lxc profile create mtd-profile
cat <<EOF | lxc profile edit mtd-profile
config:
  limits.memory: 512MB
description: MTD LXD profile
devices:
  eth0:
    nictype: bridged
    parent: lxdbr0
    type: nic
  root:
    path: /
    pool: default
    type: disk
name: mtd-profile
EOF
```
Aplicamos el nuevo profile a nuestro contenedor
```
lxc profile assign testlxd mtd-profile
lxc start testlxd
lxc exec testlxd -- cat /proc/meminfo
```

## Snapshots
```
lxc snapshot testlxd snap00
lxc info testlxd
lxc exec testlxd -- touch other_file
lxc exec testlxd -- ls -l
lxc restore testlxd snap00
lxc exec testlxd -- ls -l
```

## dw01

The practice with Docker will require several Docker workers. dw00 is already setup in the virtual machine. We will now clone the dw00 container to have a couple of workers.

```
lxc copy dw00 dw01 --container-only
lxc config device set dw01 eth0 ipv4.address 10.0.100.11
```
