# mqtt-ua-client
Hubitat driver for working with Tasmota like devices

### Welcome

This is a MQTT Driver for Tasmota kind of devices.

### Supported devices
Supported switches as for now.

### TODO
 - H801 5 channel dimmer support
 - Tasmota IR Bridge universal remote (in progress...)

### Quick Start
 - Import driver https://raw.githubusercontent.com/podarok/mqtt-ua-client/master/MQTT_UA_Client/mqttua.groovy into Custom Driver Hubitat section
 - Add Device by chosing MQTT UA Client as a Driver
 - Fill in MQTT Broker, channel, ON/OFF messages, stat channel(for feedback)
 - Add device to Dashboard and chose Switch
Enjoy

Report bugs if any https://github.com/podarok/mqtt-ua-client/issues/new

### Example setup

Driver's device
![image](https://user-images.githubusercontent.com/563412/61614844-5d67b080-ac6d-11e9-927a-ba377c473622.png)
Rule for polling
![image](https://user-images.githubusercontent.com/563412/61615135-f1397c80-ac6d-11e9-8385-e0de6513ffb7.png)
