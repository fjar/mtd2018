import boto3
import sys
import pprint

sg = sys.argv[1]
key = sys.argv[2]

ec2 = boto3.resource('ec2')

instance = ec2.create_instances(ImageId='ami-1853ac65', InstanceType='t2.micro', MinCount=1, MaxCount=1, SecurityGroups=[sg], KeyName=key)

pprint.pprint(instance)

