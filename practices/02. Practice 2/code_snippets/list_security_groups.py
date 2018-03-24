import boto3
import pprint

ec2 = boto3.client('ec2')
groups = ec2.describe_security_groups()
pprint.pprint(groups )

