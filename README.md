# ask01_425

`@author: cdemet08 - Christoforos Demetriou`

`@author: FrLeand - Frederikos `

````

The project is simulate one web server with request from N users, 
with random or specific request and users in the server.

The users run simultaneously and they send request in server. 
The server answer with one message and payload with random byte in size of 300Kb-2Mb.

We calculate the RTT in the client side and in the server side the throughput,cpu utilization
and memory.

We run the program for 1,2 and 4 cpu and we see the difference.

````


````

run:
    Server:          java Server portnumber
            example: java Server 9090
    Client:          java Client IP Portnumber
            example: java Client 127.0.0.1 9090
            
````

