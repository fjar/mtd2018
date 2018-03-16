# Setup Apache OpenWhisk

The following commands are executed once. This part is already setup in the virtual machine:

```
sudo apt install -y python
wget https://bootstrap.pypa.io/get-pip.py
sudo python get-pip.py
sudo python3 get-pip.py
rm get-pip.py
sudo apt install -y python3-dev
sudo apt install -y gcc

git clone https://github.com/apache/incubator-openwhisk.git openwhisk

cd openwhisk/tools/ubuntu-setup
./misc.sh
sudo pip install argcomplete
sudo pip install couchdb
sudo pip install docker-py
sudo pip2 install argcomplete
sudo pip2 install couchdb
sudo pip2 install docker-py
./java8.sh
./ansible.sh

cd ../..
./gradlew distDocker
```

The following commands must be executed one (not included in virtual machine):

```
cd ansible
ansible-playbook setup.yml
ansible-playbook couchdb.yml
ansible-playbook initdb.yml
ansible-playbook wipe.yml
ansible-playbook apigateway.yml # redis, apigateway
ansible-playbook openwhisk.yml

cd .. # Up to openwhisk dir
# Get edge.host from whisk.properties and set apihost:
./bin/wsk property set --apihost 172.17.0.1
./bin/wsk property set --auth `cat ansible/files/auth.guest`

./bin/wsk sdk install bashauto
source wsk_cli_bash_completion.sh

# To avoid allways having to pass --insecure parameter for our local environment
echo "alias wsk='wsk --insecure'" >> ~/.bashrc

export PATH="$PATH:$HOME/openwhisk/bin/"
```

The following commands must be executed every time the virtual machine is restarted:

```
sudo chmod -R ga+w /tmp/wskconf
sudo chmod -R ga+w /tmp/wsklogs
docker stop invoker0
docker container rm invoker0
ansible-playbook openwhisk.yml
```
