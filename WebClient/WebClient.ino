/*
  WiFi Web Server
 
 A simple web server that shows the value of the analog input pins.
 using a WiFi shield.
 
 This example is written for a network using WPA encryption. For 
 WEP or WPA, change the Wifi.begin() call accordingly.
 
 Circuit:
 * WiFi shield attached
 * Analog inputs attached to pins A0 through A5 (optional)
 
 created 13 July 2010
 by dlf (Metodo2 srl)
 modified 31 May 2012
 by Tom Igoe

 */

#include <SPI.h>
#include <WiFi101.h>


char ssid[] = "jam";      // your network SSID (name) 
char pass[] = "12345678";   // your network password
int keyIndex = 0;                 // your network key Index number (needed only for WEP)

String server = "169.254.144.245:8080";

int status = WL_IDLE_STATUS;

WiFiClient client;

String data;

void setup() {
  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while (true);       // don't continue
  }

  
  // attempt to connect to Wifi network:
  while ( status != WL_CONNECTED) { 
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:    
    status = WiFi.begin(ssid,pass);

    // wait 10 seconds for connection:
    delay(10000);
  } 
  //server.begin();
  // you're connected now, so print out the status:
  printWifiStatus();
}


void loop() {

  data ="";
  
  if(client.connect("169.254.153.223", 8080)) {
    client.println("POST /StadiumWebSite/api/v1/communication/refreshments HTTP/1.1");
    client.println("Host: 169.254.153.223:8080");
    client.println("Content-Type: application/json");
    client.println("Cache-Control: no-cache");
    client.print("Content-Length: "),
    data.concat("{ ");
    for (int analogChannel = 0; analogChannel < 2; analogChannel++) {
            data.concat("\"attendance");
            data.concat(analogChannel+1);
            data.concat("\" : ");
            data.concat(analogRead(analogChannel));
            if (analogChannel == 0) {
              data.concat(", "); 
            }  
    }
    
    data.concat("}");
    client.println(data.length());
    client.println();
    client.print(data);
  }

  while (client.available()) {
    char c = client.read();
    Serial.print(c);
  }
          
  if (client.connected()) {
    client.stop();
  }

  delay(5000);
}


void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}
