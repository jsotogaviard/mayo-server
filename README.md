To test
 - Run the jettyserver
 
 - register a user
 curl -d mainEmail=jsotogaviard@gmail.com -d name=j1 -d password=s1 -d emails=[] -d phones=[] http://localhost:9090/rest/mayo/registerUser
 curl -d mainEmail=jsotogaviard@gmail.com -d name=j1 -d password=s1 -d emails=[] -d phones=[] http://ec2-54-214-124-166.us-west-2.compute.amazonaws.com:9090/rest/mayo/registerUser
  - login a user (Email must be verified first)
 curl -d mainEmail=jsotogaviard@gmail.com -d password=s1 http://localhost:9090/rest/mayo/login
 http://ec2-54-214-124-166.us-west-2.compute.amazonaws.com:9090/rest/mayo/login
  - add a connection for jonathan
 curl --cookie "MAYO_AUTH_TOKEN=45b52033-e076-45fe-aeef-f5c164f9c950" -d name=jo2 -d emails=[\"jsotogaviard2@gmail.com\"] -d phones=[] http://localhost:9090/rest/mayo/userConnection
 
 - register a user
 curl -d mainEmail=jsotogaviard2@gmail.com -d name=j2 -d password=s2 -d emails=[] -d phones=[] http://localhost:9090/rest/mayo/registerUser
  - login a user (Email must be verified first)
 curl -d mainEmail=jsotogaviard2@gmail.com -d password=s2 http://localhost:9090/rest/mayo/login
  - add a connection for jonathan
 curl --cookie "MAYO_AUTH_TOKEN=d49ba0ce-d3f7-4c37-acff-080c8a920e69" -d name=j -d emails=[\"jsotogaviard@gmail.com\"] -d phones=[] http://localhost:9090/rest/mayo/userConnection
 
 - An email should be sent to both
 