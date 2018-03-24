import boto3
import pprint

ec2 = boto3.client('ec2')

instances = ec2.describe_instances()
pprint.pprint(instances)

