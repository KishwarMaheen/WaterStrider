import sys, socket, select
import os, glob
import RPi.GPIO as GPIO
import time

# temperature sensor activation through 1-wire
os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')

base_dir = '/sys/bus/w1/devices/'
device_folder = glob.glob(base_dir + '28*')[0]
device_file = device_folder + '/w1_slave'

#GPIO setup
GPIO.setmode(GPIO.BCM)

#servo motor
sm=18

#water pump
water = 27

#Motor AB
ma=16
mb=13

#Motor CD
mc=19
md=26

GPIO.setup(ma,GPIO.OUT)
GPIO.setup(mb,GPIO.OUT)

GPIO.setup(mc,GPIO.OUT)
GPIO.setup(md,GPIO.OUT)

GPIO.setup(water, GPIO.OUT)
GPIO.output(water, GPIO.HIGH)

GPIO.setup(sm,GPIO.OUT)

pwm=GPIO.PWM(sm, 100)

pwm.start(5)

#calling  the server
HOST = '192.168.0.102'
SOCKET_LIST = []
RECV_BUFFER = 4096 
PORT = 1994

def chat_server():

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT))
    server_socket.listen(10)
 
    # add server socket object to the list of readable connections
    SOCKET_LIST.append(server_socket)
 
    print ("Chat server started on port " + str(PORT))
 
    while 1:

        # get the list sockets which are ready to be read through select
        # 4th arg, time_out  = 0 : poll and never block
        ready_to_read,ready_to_write,in_error = select.select(SOCKET_LIST,[],[],0)
      
        for sock in ready_to_read:
            # a new connection request recieved
            if sock == server_socket: 
                sockfd, addr = server_socket.accept()
                SOCKET_LIST.append(sockfd)
                print ("Client (%s, %s) connected" % addr)
                 
                broadcast(server_socket, sockfd, "[%s:%s] entered ourchatting room\n" % addr)
             
            # a message from a client, not a new connection
            else:
                # process data recieved from client, 
                try:
                    # receiving data from the socket.
                    data = sock.recv(RECV_BUFFER)
                    if data:
                        # there is something in the socket
                        if data=='Up':
                            fwd()
                            time.sleep(1)
                            stop()
                        elif data=='Down':
                            back()
                            time.sleep(1)
                            stop()
                        elif data=='Left':
                            left()
                            time.sleep(1)
                            stop()
                        elif data=='Right':
                            right()
                            time.sleep(1)
                            stop()
                        elif data=='Draw':
                            draw()
                        elif data=='Update':
                            update()
                        else:
                            servo_rotate(int(data)*10)
                    else:
                        # remove the socket that's broken    
                        if sock in SOCKET_LIST:
                            SOCKET_LIST.remove(sock)

                        # at this stage, no data means probably theconnection has been broken
                        broadcast(server_socket, sock, "Client (%s, %s)is offline\n" % addr) 

                # exception 
                except:
                    broadcast(server_socket, sock, "Client (%s, %s) isoffline\n" % addr)
                    continue

    server_socket.close()
    
# broadcast chat messages to all connected clients
def broadcast (server_socket, sock, message):
    for socket in SOCKET_LIST:
        # send the message only to peer
        if socket != server_socket and socket != sock :
            try :
                socket.send(message)
            except :
                # broken socket connection
                socket.close()
                # broken socket, remove it
                if socket in SOCKET_LIST:
                    SOCKET_LIST.remove(socket)

#method for forward movement
def fwd():
    GPIO.output(ma, True)
    GPIO.output(mb, False)
    GPIO.output(mc, True)
    GPIO.output(md, False)
    print ('going forward')

#method for reverse movement
def back():
    GPIO.output(ma, False)
    GPIO.output(mb, True)
    GPIO.output(mc, False)
    GPIO.output(md, True)
    print('going reverse')

#method for left movement
def left():
    GPIO.output(ma, True)
    GPIO.output(mb, False)
    GPIO.output(mc, False)
    GPIO.output(md, True)
    print('going left')

#method for right movement
def right():
    GPIO.output(ma, False)
    GPIO.output(mb, True)
    GPIO.output(mc, True)
    GPIO.output(md, False)
    print('going right')

#method for stopping movement
def stop():
    GPIO.output(ma, False)
    GPIO.output(mb, False)
    GPIO.output(mc, False)
    GPIO.output(md, False)

#method for servo movement
def servo_rotate(x):
    print(x)
    duty= float(x) / 10.0 + 2.5
    pwm.ChangeDutyCycle(duty)

#method for drawing water
def draw():
    GPIO.output(water, GPIO.LOW)
    time.sleep(3)
    GPIO.output(water, GPIO.HIGH)
    print("Draw")

#reading raw DS18B20 sensor data
def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

#converting raw data to deg C and deg F
def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string) / 1000.0
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        return temp_c, temp_f

#method for temperature update
def update():
    print(read_temp())# <<<EI STRING TA RE PASS KORBI
    print("Update")
 
if __name__ == "__main__":
    sys.exit(chat_server())
