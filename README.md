# LEMP (Light Extensible Messaging Protocol) Server
LEMP is a new insfrastructure for instant messaging. It is based on JSON. There are 3 main datum types in LEMP:

1. Request
2. Information
3. Message

#### 1. REQUEST
======

Every request, except logout requests has a response (Success or Error), covered in section 1.9

A response may have a content or may be empty (only success or error)

##### 1.1. Authentication Request/Response

  ###### 1.1.1 Login Request
    
  ```{ rq:{id:"id",a:{i:"identity",t:"token"}} } ```
  
  ```{ rp:{id:"id", r:0|1} }```
  
   ###### 1.1.2 Logout Request
  
  ```{ rq:{id:"id", lo:{r:"reason"}} } ```
 
##### 1.2. User Followings Requests
  ###### 1.2.1. Add Followings Request
  
  ```{ rq:{id:"id", af:[{u:"f1",n:"nick1"},{u:"f2",n:"nick2"},{u:"f2",n:"nick2"}]} }  ```
  ###### 1.2.2. Remove Following Request
  
  ```{ rq:{id:"id", rf:["f1","f2"]} }  ```
  ###### 1.2.3. Update Following Request
  
  ```{ rq:{id:"id", uf:[{u:"f1",n:"nick1"},{u:"f2",n:"nick2"},{u:"f2",n:"nick2"}]} }  ```
##### 1.3. State (Last Offline) Request/Response

  ```{ rq:{id:"id", s:{u:"username"}} } ```
  
  ```{ rp:{id:"id", s:{u:"username", v:"0|unixtime"}} }  ```
##### 1.4. Personal Requests
  ###### 1.4.1. Personal Information Request/Response
  
  ```{ rq:{id:"id", i:{u:"username"}} }  ```
   
  ```{ rp:{id:"id", i:{u:"username", c:"creationdate", r:"registerdate"}} }   ```
  ###### 1.4.2. Picture Set Request
  
  ```{ rq:{id:"id", p:{t:"s", u:"username", v:"base4value"}} }  ```
  ###### 1.4.3. Picture Get Request/Response
  
  ```{ rq:{id:"id", p:{t:"g", u:"username"}} }  ```
   
  ```{ rp:{id:"id", p:{u:"username", v:"base64value"}} }   ```
  ###### 1.4.4. Status Set Request
  
  ```{ rq:{id:"id", st:{t:"s", u:"username", s:"status"}} }  ```
  ###### 1.4.5. Status Get Request/Response
  
  ```{ rq:{id:"id", st:{t:"g", u:"username"}} }  ```
    
  ```{ rp:{id:"id", st:{u:"username", s:"status"}} }   ```
##### 1.5. Privacy Requests
  ###### 1.5.1. Block Request
  
  ```{ rq:{id:"id", b:{u:"username"}} }   ```
  ###### 1.5.2. Unblock Request
  
  ```{ rq:{id:"id", ub:{u:"username"}} }   ```
##### 1.6. Group Requests
  ###### 1.6.1. Create Group Request
    
  ```{ rq:{id:"id", g:{t:"c", i:"id",n:"groupname", p:"picture", m:["member1", "member2", "member3"]}}}  ```
  ###### 1.6.2. Add Users to Group Request
  
  ```{ rq:{id:"id", g:{t:"a", i:"id",m:["member1", "member2", "member3"]}}}  ```
  ###### 1.6.3. Banish Users from Group Request
  
  ```{ rq:{id:"id", g:{t:"b",i:"id",m:["member1", "member2", "member3"]}}}   ```
  ###### 1.6.4. Leave from Group Request
  
  ```{ rq:{id:"id", g:{t:"l", i:"id"}}}  ```
  ###### 1.6.5. Terminate Group Request
  
  ```{ rq:{id:"id", g:{t:"t", i:"id"}}}   ```
  ###### 1.6.6. Group Information Request/Response
  
  ```{ rq:{id:"id", g:{t:"i", i:"id"}}}    ```
    
  ```{ rp:{id:"id", g:{i:"id",c:"creationdate",mt:"mutetill",m:["member1", "member2","member3"]}} }   ```
  ###### 1.6.7. Admins Add Request
  
  ```{ rq:{id:"id", g:{t:"aad", i:"id", ad:["member1","member2"]}}}   ```
  ###### 1.6.8. Admins Get Request/Response
  
  ```{ rq:{id:"id", g:{t:"ad", i:"id"}}}  ```
    
  ```{ rp:{id:"id", g:{i:"id",ad:["member1", "member2","member3"]}} }   ```
  ###### 1.6.9. Picture Set Request
  
  ```{ rq:{id:"id", p:{t:"s", i:"id", v:"base64value"}} }  ```

  ###### 1.6.10. Picture Get Request/Response
  
  ```{ rq:{id:"id", p:{t:"g", i:"id"}} }   ```
    
  ```{ rp:{id:"id", p:{i:"id", v:"base64value"}} }   ```
  ###### 1.6.11. Name Set Request
  
  ```{ rq:{id:"id", n:{t:"s", i:"id", n:"name"}} }  ```
  ###### 1.6.12. Name Get Request/Response
  
  ```{ rq:{id:"id", n:{t:"g", i:"id"}} }   ```
    
  ```{ rp:{id:"id", n:{i:"id", n:"groupname"}} }   ```
  ###### 1.6.13. Mute Request
  
  ```{ rq:{id:"id", mt:{i:"id",t:"mutetill"}} }   ```
  ###### 1.6.14. Unmute Request
  
  ```{ rq:{id:"id", umt:{i:"id"}} }   ```
    
##### 1.7. Broadcast Group Requests
  ###### 1.7.1. Create Broadcast Group Request
  
  ```{ rq:{id:"id", br:{t:"c", i:"id",n:"broadcastname", p:"picture", m:["member1", "member2", "member3"]}}}   ```
  ###### 1.7.2. Add Users to Broadcast Group Request
  
  ```{ rq:{id:"id", br:{t:"a", i:"id",m:["member1", "member2", "member3"]}}}   ```
  ###### 1.7.3. Banish Users from Broadcast Group Request
  
  ```{ rq:{id:"id", br:{t:"b",i:"id",m:["member1", "member2", "member3"]}}}  ```
  ###### 1.7.4. Terminate Broadcast Group Request
  
  ```{ rq:{id:"id", br:{t:"t", i:"id"}}}  ```
  ###### 1.7.5. Broadcast Group Information Request/Response
  
  ```{ rq:{id:"id", br:{t:"i", i:"id"}}}   ```
    
  ```{ rp:{id:"id", br:{i:"id",c:"creationdate",n:"name",m:["member1", "member2","member3"]}} }   ```
  ###### 1.7.6. Picture Set Request
  
  ```{ rq:{id:"id", p:{t:"s", b:"id", v:"base64value"}} }   ```
  ###### 1.7.7. Picture Get Request/Response
  
  ```{ rq:{id:"id", p:{t:"g", b:"id"}} }   ```
    
  ```{ rp:{id:"id", p:{b:"id", v:"base64value"}} }  ```
  ###### 1.7.8. Name Set Request
  
  ```{ rq:{id:"id", n:{t:"s", b:"id", n:"name"}} }   ```
  ###### 1.7.9. Name Get Request/Response
  
  ```{ rq:{id:"id", n:{t:"s", b:"id"}} }   ```
    
  ```{ rp:{id:"id", n:{b:"id", n:"broadcastname"}} }   ```
##### 1.8. Server Requests
  ###### 1.8.1. Server Time Request/Response
  
  ```{srq:{id:"id",t:"t"}}   ```
    
  ```{ srp:{id:"id", t:"t", tm:"serverunixtime"} }   ```
  ###### 1.8.2. Knock-Knock Request
    
  ```{srq:{id:"id",t:"k"}}  ```
    
  ```{ srp:{id:"id", t:"k"} }   ```
##### 1.9. Response/Error
  ###### 1.9.1. Successful Response
  
  ```{ rp:{id:"id"} }  ```
  ###### 1.9.2. Erroneous Response
  
  ```{ rp:{id:"id", e:{c:"code",d:"description"}} }   ```

#### 2. INFORMATION

##### 2.1. Login/Logout info broadcast to followers

  ###### 2.1.1.  Login Broadcast
  
  ```{ i:{id:"id", li:"username"} }    ```
  
  ###### 2.1.2.  Logout Broadcast
  
  ```{ i:{id:"id", lo:"username"} }    ```
  
##### 2.2.  Group Information Broadcast

  ###### 2.2.1.  Group created info

  ```{ i:{id:"id", g:{t:"c",i:"id",n:"name"} } }    ```
   
  ###### 2.2.2.  Group member added info

  ```{ i:{id:"id", g:{t:"a",i:"id",m:["member1", "member2", "member3"]} } }    ```
    
  ###### 2.2.3.  Group user banished info

  ```{ i:{id:"id", g:{t:"b",i:"id",m:"member1"} } }    ```
  
  ###### 2.2.4.  Group terminated info

  ```{ i:{id:"id", g:{t:"t"} } }     ```
    
  ###### 2.2.5.  Group picture changed info

  ```{ i:{id:"id", g:{t:"p"} } }      ```
    
  ###### 2.2.6.  Group name changed info

  ```{ i:{id:"id", g:{t:"n"} } }      ```
  
    
#### 3. MESSAGE

  ```{ m:{id:"id", s:"sender", r:"receiver", t:"t|p|v|a|l", c:"content", st:sent_time} }    ```
  
  - Message Types
      * t: text
      * p: photo
      * v: video
      * a: audio
      * l: location
      
   ###### 3.1.  Server Receipt Message
   
   Server receipt message is sent for all messages delivered to the server.
   
  ```{ m:{id:"id", sr:{id:"sent_messageId"} } }    ```
    
   ###### 3.2.  Delivered Message
   
  ```{ m:{id:"id", st:sent_time, dm:{id:"received_messageId"} } }    ```
    
   ###### 3.3.  Noticed Message
   
  ```{ m:{id:"id", st:sent_time, nm:{id:"noticed_messageId"} } }    ```
