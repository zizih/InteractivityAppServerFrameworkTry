#first time to use python in a app
#auto: hezi

import socket
import sys
s = socket.socket()

#host = raw_input('input server ip: ')
#port = input('input server port: ')
#host = socket.gethostname()
host = 'localhost'
port = 9000

s.connect((host,port))
print 'connected to ',host,port


# function to get option id
def option(str):
    lines = str.split('\n')
    for line in lines:
        if line.startswith('Step: '):
            return line[5:]


while True:
    result = s.recv(1024)
    lines = result.split('\n')
    for line in lines:
        if line.startswith('['):
            print '   ',line
        else:
            print line

    data = raw_input('>')
    s.send(option(result) + ':' + data)
    if data.find('exit') != -1:
        sys.exit(0)
    #s.send(data)

s.close
