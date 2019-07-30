metadata {
 definition(name: "MQTT UA IR blaster Client", namespace: "mqtt_ua_ir", author: "Andrii Podanenko") {
  capability "Initialize"
  capability "Switch"
 }
 preferences {
  input name: "MQTTBroker", type: "text", title: "MQTT Broker Address", description: "Enter address of the broker, for example, tcp://192.168.1.71:1883", required: true, displayDuringSetup: true
  input name: "username", type: "text", title: "MQTT Username", description: "Keep blank if none", required: false, displayDuringSetup: true
  input name: "password", type: "password", title: "MQTT Password", description: "Keep blank if none", required: false, displayDuringSetup: true
  input name: "publishto", type: "text", title: "Channel to Publish", description: "MQTT Channel used for publishing a message to", required: true, displayDuringSetup: false
  input name: "publishon", type: "text", title: "Text to Publish for switch ON", description: "Text used for publishing a message to a channel", required: true, displayDuringSetup: false
  input name: "publishoff", type: "text", title: "Text to Publish for switch OFF", description: "Text used for publishing a message to a channel", required: true, displayDuringSetup: false
 }

}


def installed() {
 log.info "installed..."
}

def initialize() {
 sendEvent(name: "checkInterval", value: 300)
 log.info device.getName()
}


def on() {
 log.info "Set ON"
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.publish(settings?.publishto, settings?.publishon, 1, false)
     sendEvent(name: "switch", value: "on", isStateChange: true)
     mqtt.disconnect()
 } catch (e) {
  log.error e.message
 }
}

def off() {
 log.info "Set OFF"
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.publish(settings?.publishto, settings?.publishoff, 1, false)
     sendEvent(name: "switch", value: "off", isStateChange: true)
     mqtt.disconnect()
 } catch (e) {
  log.error e.message
 }

}

def mqttClientStatus(message) {
 log.info message
}

def parse(message) {
    return
 mqtt = interfaces.mqtt
 if (mqtt.parseMessage(message).payload == settings?.statuson) {
  sendEvent(name: "switch", value: "on", isStateChange: true)
  return
 }
 else {
  sendEvent(name: "switch", value: "off", isStateChange: true)
  return
 }
 log.error "Unexpected payload data returned from MQTT:" + mqtt.parseMessage(message).payload
}

def refresh() {
    return
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
 } catch (e) {
  log.error e.message
 }
}
def ping() {
    return
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
 } catch (e) {
  log.error e.message
 }
}

/**
 * Use Rule Machine to run polling every 5 minutes for all your devices.
 */
def poll() {
    return
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
 } catch (e) {
  log.error e.message
 }
}
