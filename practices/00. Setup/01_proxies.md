
# Proxies Setup

## Set proxy for docker
Set proxies for docker. This must be executed on the host and each of the docker worker nodes. Set your values for `http_proxy` and `https_proxy`.
```
HTTP_PROXY=<http_proxy>
HTTPS_PROXY=<https_proxy>
NO_PROXY="localhost,127.0.0.1,10.0.100.0/24"

sudo su -
mkdir -p /etc/systemd/system/docker.service.d
cat <<EOF | tee /etc/systemd/system/docker.service.d/10-proxy.conf
[Service]    
Environment="HTTP_PROXY=${HTTP_PROXY}" "HTTPS_PROXY=${HTTPS_PROXY}" "NO_PROXY=${NO_PROXY}"
EOF
systemctl daemon-reload
systemctl restart docker

systemctl show  --property=Environment docker
```
## Unset proxy for docker
```
sudo su -
rm -rf /etc/systemd/system/docker.service.d/
systemctl daemon-reload
systemctl restart docker

systemctl show  --property=Environment docker
```
## Set LXC proxy
```
lxc config set core.proxy_http $HTTP_PROXY
lxc config set core.proxy_https $HTTPS_PROXY
```

## Unset LXC proxy
```
lxc config unset core.proxy_http
lxc config unset core.proxy_https
```
