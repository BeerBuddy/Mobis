# Server starten
1. Im Server Verzeichnis "gradlew run" ausführen -> Server startet mit In-Memory-DB
2. URLs aufrufen 
    GET http://localhost:9000/person/all
    GET http://localhost:9000/person/get/{id}
    GET http://localhost:9000/person/remove/{id}
    POST http://localhost:9000/person/save
        Beispiel JSON Objekt:
            {
                "username":"test",
                "email":"test@test.de"
            }
         Anfragen kann man gut mit dem Tool Postman testen https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop
      