import os
import datetime

def lambda_handler(event, context):
    region = os.environ['AWS_REGION']   
    return {
        'statusCode': '200',
        'body': "Hello World from {} @ {}".format(region, datetime.datetime.today()),
        'headers': {
            'Content-Type': 'text/plain',
        },
    }

