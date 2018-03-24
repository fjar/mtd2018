import boto3
import os
import sys
import uuid

s3_client = boto3.client('s3')

def lambda_handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key']
        download_path = '/tmp/{}{}'.format(uuid.uuid4(), key)
        upload_path = '/tmp/stats-{}'.format(key)
        print("bucket:{} key:{}".format(bucket,key))
        s3_client.download_file(bucket, key, download_path)

        num_lines = sum(1 for line in open(download_path))
        with open(upload_path, 'w+') as f:
            f.write('{} has {} lines\n'.format(key, num_lines))

        s3_client.upload_file(upload_path, '{}-stats'.format(bucket), 'stats-{}'.format(key))

