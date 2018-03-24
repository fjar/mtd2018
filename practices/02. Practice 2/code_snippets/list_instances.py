import boto3
import pprint

ec2 = boto3.resource('ec2')

for i in  ec2.instances.all():
    if i.tags:
        tags = ','.join(['{}={}'.format(t['Key'], t['Value']) for t in i.tags])
    else:
        tags = 'None'
    print('id: {}, image: {}, ipv4: {}, tags: {}'.format(i.id, i.image_id, i.private_ip_address, tags))

