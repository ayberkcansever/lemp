# LEMP (Light Extensible Messaging Protocol) Server
LEMP is a new insfrastructure for instant messaging. Its packet format is based on JSON. It can be thought as a lighter alternative to XMPP (Extensible Messaging and Presence Protocol). It does not have any pretensions to be alternative of SIP, MQTT, AMQP vs. It defines its packet formats for its purposes. It is not a transport protocol indeed.

Presence of a client is determined by its authentication. No packet formats exist for controlling presence as in XMPP. If a client is authenticated to server then it is considered as "online". Store-Forward mechanism exists for messages of offline members. The messages sent to offline members are stored and they are forwarded to the client when it authenticates.

This project is a Java implementation of LEMP server. The server is implemeted to have websocket interface for packet flow. It uses Cassandra as the persistence layer and Apache Ignite as the cache layer.

There are 3 main datum types in LEMP. 

1. Request
2. Information
3. Message

#### 1. REQUEST 
------

Every request, except logout requests must have a response (Success or Error covered in section 1.9)

A response may have a content or may be empty (only success or error)

##### 1.1. Authentication Request/Response

  ###### 1.1.1 Login Request 
    
An authentication request is sent to the server after the persistence network connection is setup.
    
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id",a:{i:"identity",t:"token"}} } ```  |
| Response      | ```{ rp:{id:"id", response:0/1} } ```               |
  
  ###### 1.1.2 Logout Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", lo:{r:"reason"}} } ``` |


##### 1.2. User Followings Requests
  ###### 1.2.1. Add Followings Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", af:[{u:"f1",n:"nick1"},{u:"f2",n:"nick2"},{u:"f2",n:"nick2"}]} }  ``` |
  
  
  ###### 1.2.2. Remove Following Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", rf:["f1","f2"]} }  ``` |
  
  
  ###### 1.2.3. Update Following Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       |   ```{ rq:{id:"id", uf:[{u:"f1",n:"nick1"},{u:"f2",n:"nick2"},{u:"f2",n:"nick2"}]} }  ``` |

##### 1.3. State (Last Offline) Request/Response

| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", s:{u:"username"}} } ``` |
| Response      | ```{ rp:{id:"id", s:{u:"username", v:"0/unixtime"}} }  ``` |
    
  
##### 1.4. Personal Requests
  ###### 1.4.1. Personal Information Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", i:{u:"username"}} }  ``` |
| Response      | ```{ rp:{id:"id", i:{u:"username", c:"creationdate", r:"registerdate"}} }   ``` |
  
  ###### 1.4.2. Picture Set Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", p:{t:"s", u:"username", v:"base4value"}} }  ``` |
    
  ###### 1.4.3. Picture Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", p:{t:"g", u:"username"}} }  ``` |
| Response      | ```{ rp:{id:"id", p:{u:"username", v:"base64value"}} }   ``` |
  
  ###### 1.4.4. Status Set Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", st:{t:"s", u:"username", s:"status"}} }  ``` |

  ###### 1.4.5. Status Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", st:{t:"g", u:"username"}} }  ``` |
| Response      | ```{ rp:{id:"id", st:{u:"username", s:"status"}} }   ``` |
  
##### 1.5. Privacy Requests
  ###### 1.5.1. Block Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", b:{u:"username"}} }   ``` |
  
  ###### 1.5.2. Unblock Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", ub:{u:"username"}} }   ``` |
  
##### 1.6. Group Requests
  ###### 1.6.1. Create Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       |  ```{ rq:{id:"id", g:{t:"c", i:"id",n:"groupname", p:"picture", m:["member1", "member2", "member3"]}}}  ``` |
     
  ###### 1.6.2. Add Users to Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       |  ```{ rq:{id:"id", g:{t:"a", i:"id",m:["member1", "member2", "member3"]}}}  ``` |
  
  ###### 1.6.3. Banish Users from Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       |  ```{ rq:{id:"id", g:{t:"b",i:"id",m:["member1", "member2", "member3"]}}}   ``` |
  
  ###### 1.6.4. Leave from Group Request

| Type          | Packet      | 
| ------------- |-------------
| Request       |  ```{ rq:{id:"id", g:{t:"l", i:"id"}}}  ``` |
  
  ###### 1.6.5. Terminate Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       |  ```{ rq:{id:"id", g:{t:"t", i:"id"}}}   ``` |
  
  ###### 1.6.6. Group Information Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", g:{t:"i", i:"id"}}}    ``` |
| Response      | ```{ rp:{id:"id", g:{i:"id",c:"creationdate",mt:"mutetill",m:["member1", "member2","member3"]}} }   ``` |
  
  ###### 1.6.7. Admins Add Request

| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", g:{t:"aad", i:"id", ad:["member1","member2"]}}}   ``` |
  
  ###### 1.6.8. Admins Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", g:{t:"ad", i:"id"}}}  ``` |
| Response      | ```{ rp:{id:"id", g:{i:"id",ad:["member1", "member2","member3"]}} }   ``` |
  
  ###### 1.6.9. Picture Set Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", p:{t:"s", i:"id", v:"base64value"}} }  ``` |
  
  ###### 1.6.10. Picture Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", p:{t:"g", i:"id"}} }   ``` |
| Response      | ```{ rp:{id:"id", p:{i:"id", v:"base64value"}} }   ``` |  
  
  ###### 1.6.11. Name Set Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", n:{t:"s", i:"id", n:"name"}} }  ``` |  
  
  ###### 1.6.12. Name Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", n:{t:"g", i:"id"}} }   ``` |
| Response      | ```{ rp:{id:"id", n:{i:"id", n:"groupname"}} }   ``` |  
  
  ###### 1.6.13. Mute Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", mt:{i:"id",t:"mutetill"}} }   ``` |  
  
  ###### 1.6.14. Unmute Request

| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", umt:{i:"id"}} }   ``` |  


##### 1.7. Broadcast Group Requests
  ###### 1.7.1. Create Broadcast Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", br:{t:"c", i:"id",n:"broadcastname", p:"picture", m:["member1", "member2", "member3"]}}}   ``` |    
  
  ###### 1.7.2. Add Users to Broadcast Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", br:{t:"a", i:"id",m:["member1", "member2", "member3"]}}}   ``` |    
  
  ###### 1.7.3. Banish Users from Broadcast Group Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", br:{t:"b",i:"id",m:["member1", "member2", "member3"]}}}  ``` |      
  
  ###### 1.7.4. Terminate Broadcast Group Request

| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", br:{t:"t", i:"id"}}}  ``` |      
  
  ###### 1.7.5. Broadcast Group Information Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", br:{t:"i", i:"id"}}}   ``` |
| Response      | ```{ rp:{id:"id", br:{i:"id",c:"creationdate",n:"name",m:["member1", "member2","member3"]}} }   ``` |   
  
  ###### 1.7.6. Picture Set Request

| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", p:{t:"s", b:"id", v:"base64value"}} }   ``` |
  
  ###### 1.7.7. Picture Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", p:{t:"g", b:"id"}} }   ``` |
| Response      | ```{ rp:{id:"id", p:{b:"id", v:"base64value"}} }  ``` |   
  
  ###### 1.7.8. Name Set Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", n:{t:"s", b:"id", n:"name"}} }   ``` |
  
  ###### 1.7.9. Name Get Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", n:{t:"s", b:"id"}} }   ``` |
| Response      | ```{ rp:{id:"id", n:{b:"id", n:"broadcastname"}} }   ``` |   


##### 1.8. Server Requests
  ###### 1.8.1. Server Time Request/Response
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{srq:{id:"id",t:"t"}}   ``` |
| Response      | ```{ srp:{id:"id", t:"t", tm:"serverunixtime"}, o:-2 }   ``` |     
  
  ###### 1.8.2. Knock-Knock Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{srq:{id:"id",t:"k"}}  ``` |
| Response      | ```{ srp:{id:"id", t:"k"} }   ``` |   
  
##### 1.9. Response/Error

Id of the response must be same as the id of the request.

  ###### 1.9.1. Successful Response
  
| Type          | Packet      | 
| ------------- |-------------
| Response      | ```{ rp:{id:"id"} }  ``` |  
  
  
  ###### 1.9.2. Erroneous Response
  
| Type          | Packet      | 
| ------------- |-------------
| Response      | ```{ rp:{id:"id", e:{c:"code",d:"description"}} }   ``` |   

##### 1.10. Admin Requests

  ###### 1.10.1. Create New User Request
  
| Type          | Packet      | 
| ------------- |-------------
| Request       | ```{ rq:{id:"id", cr:{i:"identity", t:"token", ty:"0/1"}}}  ``` |  
  
  - User Types (ty)
      * 0: user
      * 1: admin
  
#### 2. INFORMATION
------

##### 2.1. Login/Logout info broadcast to followers

  ###### 2.1.1.  Login Information Broadcast
  
| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", li:"username"} }    ``` |      
  
  ###### 2.1.2.  Logout Information Broadcast
  
| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", lo:"username"} }    ``` |     
  
  
##### 2.2.  Group Information Broadcast

  ###### 2.2.1.  Group created info

| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", g:{t:"c",i:"id",n:"name"} } }    ``` |     
   
  ###### 2.2.2.  Group member added info

| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", g:{t:"a",i:"id",m:["member1", "member2", "member3"]} } }    ``` |     

  ###### 2.2.3.  Group user banished info

| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", g:{t:"b",i:"id",m:"member1"} } }    ``` |     
  
  ###### 2.2.4.  Group terminated info

| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", g:{t:"t"} } }     ``` |     
    
  ###### 2.2.5.  Group picture changed info

| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", g:{t:"p"} } }      ``` |   
    
  ###### 2.2.6.  Group name changed info

| Type          | Packet      | 
| ------------- |-------------
| Info          | ```{ i:{id:"id", g:{t:"n"} } }      ``` |   
  
    
#### 3. MESSAGE
------

| Type          | Packet      | 
| ------------- |-------------
| Message       | ```{ m:{id:"id", s:"sender", r:"receiver", t:"t/p/v/a/l", c:"content", st:sent_time, p:"0/1", sc:"0/1", pc:"0/1"} }    ``` | 
  
  - Message Types (t)
      * t: text
      * p: photo
      * v: video
      * a: audio
      * l: location
  
  - Persistency (p)
      * 0: do not persist if the receiver is offline
      * 1: persist the message if the receiver is offline (default)
      
  - Server Receipt (sc)
      * 0: the server does not sent message received message
      * 1: the server sents message received message (default)
      
  - Peer Receipt (pc)
      * 0: the peer does not sent message received message
      * 1: the peer sents message received message (default)
      
   ###### 3.1.  Server Receipt Message
   
   Server receipt message is sent for all messages delivered to the server.
   
| Type          | Packet      | 
| ------------- |-------------
| Message       | ```{ m:{id:"id", sr:{id:"sent_messageId"} } }    ``` |    
    
   ###### 3.2.  Delivered Message

| Type          | Packet      | 
| ------------- |-------------
| Message       | ```{ m:{id:"id", st:sent_time, dm:{id:"received_messageId"} } }    ``` |    
    
   ###### 3.3.  Noticed Message
   
| Type          | Packet      | 
| ------------- |-------------
| Message       | ```{ m:{id:"id", st:sent_time, nm:{id:"noticed_messageId"} } }    ``` |    
  
