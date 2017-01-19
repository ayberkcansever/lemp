# LEMP (Light Extensible Messaging Protocol) Server
LEMP is a new insfrastructure for instant messaging. It is based on JSON. There are 3 main datum types in LEMP:

1. Request
2. Information
3. Message

#### 1. REQUEST
1. Authentication Request/Response

  ```{ rq:{id:”id”,a:{i:”identity”,t:”token”}} } ```
  
  ```{ rp:{id:”id”, r:0|1} }```
2. User Followings Requests
  1. Register User's Followings Request
  
    ```{ rq:{id:”id”, f:[{u:“f1”,n:”nick1”},{u:“f2”,n:”nick2”},{u:“f2”,n:”nick2”}]} } ```
    
  2. Add New Following Request
  
     ```{ rq:{id:”id”, af:[{u:“f1”,n:”nick1”},{u:“f2”,n:”nick2”},{u:“f2”,n:”nick2”}]} }  ```
  3. Remove Following Request
  
    ```{ rq:{id:”id”, rf:[“f1”,”f2”]} }  ```
  4. Update Following Request
  
    ```{ rq:{id:”id”, uf:[{u:“f1”,n:”nick1”},{u:“f2”,n:”nick2”},{u:“f2”,n:”nick2”}]} }  ```
3. State (Last Offline) Request/Response

  ```{ rq:{id:”id”, s:{u:”username”}} } ```
  
  ```{ rp:{id:”id”, s:{u:”username”, v:”0|unixtime”}} }  ```
4. Personal Requests
  1. Personal Information Request/Response
  
    ```{ rq:{id:”id”, i:{u:”username”}} }  ```
    
    ```{ rp:{id:”id”, i:{u:”username”, c:”creationdate”, r:”registerdate”}} }   ```
  2. Picture Set Request
  
    ```{ rq:{id:”id”, p:{t:”s”, u:”username”, v:”base4value”}} }  ```
  3. Picture Get Request/Response
  
    ```{ rq:{id:”id”, p:{t:”g”, u:”username”}} }  ```
    
    ```{ rp:{id:”id”, p:{u:”username”, v:”base64value”}} }   ```
  4. Status Set Request
  
    ```{ rq:{id:”id”, st:{t:”s”, u:”username”, s:”status”}} }  ```
  5. Status Get Request/Response
  
    ```{ rq:{id:”id”, st:{t:”g”, u:”username”}} }  ```
    
    ```{ rp:{id:”id”, st:{u:”username”, s:”status”}} }   ```
5. Privacy Requests
  1. Block Request
  
    ```{ rq:{id:”id”, b:{u:”username”}} }   ```
  2. Unblock Request
  
    ```{ rq:{id:”id”, ub:{u:”username”}} }   ```
6. Group Requests
  1. Create Group Request
    
    ```{ rq:{id:”id”, g:{t:”c”, i:”id”,n:”groupname”, p:”picture”, m:[“member1”, “member2”, “member3”]}}}  ```
  2. Add Users to Group Request
  
    ```{ rq:{id:”id”, g:{t:”a”, i:”id”,m:[“member1”, “member2”, “member3”]}}}  ```
  3. Banish Users from Group Request
  
    ```{ rq:{id:”id”, g:{t:”b”,i:”id”,m:[“member1”, “member2”, “member3”]}}}   ```
  4. Leave from Group Request
  
    ```{ rq:{id:”id”, g:{t:”l”, i:”id”}}}  ```
  5. Terminate Group Request
  
    ```{ rq:{id:”id”, g:{t:”t”, i:”id”}}}   ```
  6. Group Information Request/Response
  
    ```{ rq:{id:”id”, g:{t:”i”, i:”id”}}}    ```
    
    ```{ rp:{id:”id”, g:{i:”id”,c:”creationdate”,mt:”mutetill”,m:[“member1”, “member2”,”member3”]}} }   ```
  7. Admins Add Request
  
    ```{ rq:{id:”id”, g:{t:”aad”, i:”id”, ad:[“member1”,“member2”]}}}   ```
  8. Admins Get Request/Response
  
    ```{ rq:{id:”id”, g:{t:”ad”, i:”id”}}}  ```
    
    ```{ rp:{id:”id”, g:{i:”id”,ad:[“member1”, “member2”,”member3”]}} }   ```
  9. Picture Set Request
  
    ```{ rq:{id:”id”, p:{t:”s”, i:”id”, v:”base64value”}} }  ```

  10. Picture Get Request/Response
  
    ```{ rq:{id:”id”, p:{t:”g”, i:”id”}} }   ```
    
    ```{ rp:{id:”id”, p:{i:”id”, v:”base64value”}} }   ```
  11. Name Set Request
  
    ```{ rq:{id:”id”, n:{t:”s”, i:”id”, n:”name”}} }  ```
  12. Name Get Request/Response
  
    ```{ rq:{id:”id”, n:{t:”g”, i:”id”}} }   ```
    
    ```{ rp:{id:”id”, n:{i:”id”, n:”groupname”}} }   ```
  13. Mute Request
  
    ```{ rq:{id:”id”, mt:{i:”id”,t:”mutetill”}} }   ```
  14. Unmute Request
  
    ```{ rq:{id:”id”, umt:{i:”id”}} }   ```
7. Broadcast Group Requests
  1. Create Broadcast Group Request
  
    ```{ rq:{id:”id”, br:{t:”c”, i:”id”,n:”broadcastname”, p:”picture”, m:[“member1”, “member2”, “member3”]}}}   ```
  2. Add Users to Broadcast Group Request
  
    ```{ rq:{id:”id”, br:{t:”a”, i:”id”,m:[“member1”, “member2”, “member3”]}}}   ```
  3. Banish Users from Broadcast Group Request
  
    ```{ rq:{id:”id”, br:{t:”b”,i:”id”,m:[“member1”, “member2”, “member3”]}}}  ```
  4. Terminate Broadcast Group Request
  
    ```{ rq:{id:”id”, br:{t:”t”, i:”id”}}}  ```
  5. Broadcast Group Information Request/Response
  
    ```{ rq:{id:”id”, br:{t:”i”, i:”id”}}}   ```
    
    ```{ rp:{id:”id”, br:{i:”id”,c:”creationdate”,n:”name”,m:[“member1”, “member2”,”member3”]}} }   ```
  6. Picture Set Request
  
    ```{ rq:{id:”id”, p:{t:”s”, b:”id”, v:”base64value”}} }   ```
  7. Picture Get Request/Response
  
    ```{ rq:{id:”id”, p:{t:”g”, b:”id”}} }   ```
    
    ```{ rp:{id:”id”, p:{b:”id”, v:”base64value”}} }  ```
  8. Name Set Request
  
    ```{ rq:{id:”id”, n:{t:”s”, b:”id”, n:”name”}} }   ```
  9. Name Get Request/Response
  
    ```{ rq:{id:”id”, n:{t:”s”, b:”id”}} }   ```
    
    ```{ rp:{id:”id”, n:{b:”id”, n:”broadcastname”}} }   ```
8. Server Reqeusts
  1. Server Time Request/Response
  
    ```{srq:{id:”id”,t:”t”}}   ```
    
    ```{ srp:{id:”id”, t:”t”, tm:”serverunixtime”} }   ```
  2. Knock-Knock Request
    
    ```{srq:{id:”id”,t:”k”}}  ```
    
    ```{ srp:{id:”id”, t:”k”} }   ```
9. Response/Error
  1. Successful Response
  
     ```{ rp:{id:”id”} }  ```
  2. Erroneous Response
  
     ```{ rp:{id:”id”, e:{c:”code”,d:”description”}} }   ```

#### 2. INFORMATION

1. Login/Logout info broadcast to followers

```{ i:{id:”id”, li:”username”} }    ```


#### 3. MESSAGE
