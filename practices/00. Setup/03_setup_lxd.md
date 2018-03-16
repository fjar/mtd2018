# Setup LXD
```
sudo update
sudo upgrade -y

sudo apt remove -y --purge lxd lxd-client
sudo snap install lxd
sudo apt install -y zfsutils-linux

sudo lxd init
```
The following answers are used:
```
Do you want to configure a new storage pool (yes/no) [default=yes]?
Name of the new storage pool [default=default]:
Name of the storage backend to use (dir, btrfs, ceph, lvm, zfs) [default=zfs]:
Create a new ZFS pool (yes/no) [default=yes]?
Would you like to use an existing block device (yes/no) [default=no]? yes
Path to the existing block device: /dev/sdb
Would you like LXD to be available over the network (yes/no) [default=no]?
Would you like stale cached images to be updated automatically (yes/no) [default=yes]?
Would you like to create a new network bridge (yes/no) [default=yes]?
What should the new bridge be called [default=lxdbr0]?
What IPv4 address should be used (CIDR subnet notation, “auto” or “none”) [default=auto]? 10.0.100.1/24
Would you like LXD to NAT IPv4 traffic on your bridge? [default=yes]?
What IPv6 address should be used (CIDR subnet notation, “auto” or “none”) [default=auto]? none
```

Later we will run docker workers inside LXD containers, so we prepera those containers:

```
lxc init ubuntu:16.04 dw00 -c security.nesting=true
lxc ls
lxc config show dw00
lxc network attach lxdbr0 dw00 eth0
lxc config device set dw00 eth0 ipv4.address 10.0.100.10
lxc config show dw00
lxc start dw00
lxc exec dw00 -- apt update
lxc exec dw00 -- apt -y upgrade
```

Init some container so the image for Alpine Linux is downloaded:

```
lxc init images:alpine/3.6 test_alpine
```

Install Docker in the dw00 container. Launch a shell in the container:

```
lxc exec dw00 -- /bin/bash
```

Inside the container:

```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
apt update
apt install -y docker-ce
touch /.dockerenv
systemctl restart docker
exit
```

Check Docker is working in the container:

```
lxc exec dw00 -- docker run hello-world
```
