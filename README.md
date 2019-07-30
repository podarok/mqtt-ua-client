# mqtt-ua-client
Hubitat drivers for working with Tasmota like devices

### Welcome

This is a MQTT Driver for Tasmota kind of devices.

### Supported devices
Supported: 
- Switches https://github.com/podarok/mqtt-ua-client/tree/master/MQTT_UA_Client
- IR Blaster https://github.com/podarok/mqtt-ua-client/tree/master/MQTT_UA_IR_Blaster

### TODO
 - H801 5 channel dimmer support

### Quick Start
 - To get Switch device - import driver https://raw.githubusercontent.com/podarok/mqtt-ua-client/master/MQTT_UA_Client/mqttua.groovy into Custom Driver Hubitat section
  - To get IR Blaster device - import driver https://raw.githubusercontent.com/podarok/mqtt-ua-client/master/MQTT_UA_IR_Blaster/MQTT_UA_IR_Blaster_Client.groovy into Custom Driver Hubitat section
 - Add Device by chosing respective one as a Driver
 - Fill in MQTT Broker, channel, ON/OFF messages, stat channel(for switch feedback)
 - Add device to Dashboard and chose Switch
Enjoy

Report bugs if any https://github.com/podarok/mqtt-ua-client/issues/new

### Example setup

Switch Driver's device
![image](https://user-images.githubusercontent.com/563412/61614844-5d67b080-ac6d-11e9-927a-ba377c473622.png)
Rule for polling
![image](https://user-images.githubusercontent.com/563412/61615135-f1397c80-ac6d-11e9-8385-e0de6513ffb7.png)
IR Blaster config
![image](https://user-images.githubusercontent.com/563412/62138796-eb3c3f00-b2f0-11e9-9214-cd511feda99f.png)

