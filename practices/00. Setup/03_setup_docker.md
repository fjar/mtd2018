# Setup Docker

```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"

sudo apt update
sudo apt install -y docker-ce

sudo gpasswd -a "$(whoami)" docker
# reconnect if connected via ssh
```

The following changes are needed to make our docker installation work seamlessly with Apache OpenWhisk:

```
cat <<EOF | sudo tee /etc/docker/daemon.json
{
  "hosts": ["fd://", "tcp://0.0.0.0:4243"]
}
EOF

#sudo -E bash -c 'echo '\''export DOCKER_HOST="tcp://0.0.0.0:4243"'\'' >> /etc/bash.bashrc'
#source /etc/bash.bashrc

sudo sed -i 's/^\(ExecStart=\/usr\/bin\/dockerd\) .*/\1/' /lib/systemd/system/docker.service

sudo systemctl daemon-reload
sudo systemctl restart docker
```
