metadata {
 definition(name: "MQTT UA Client", namespace: "mqtt_ua", author: "Andrii Podanenko") {
  capability "Initialize"
  capability "Switch"
  capability "Refresh"
  capability "HealthCheck"
  capability "Polling"
 }
 preferences {
  input name: "MQTTBroker", type: "text", title: "MQTT Broker Address", description: "Enter address of the broker, for example, tcp://192.168.1.71:1883", required: true, displayDuringSetup: true
  input name: "username", type: "text", title: "MQTT Username", description: "Keep blank if none", required: false, displayDuringSetup: true
  input name: "password", type: "password", title: "MQTT Password", description: "Keep blank if none", required: false, displayDuringSetup: true
  input name: "publishto", type: "text", title: "Channel to Publish", description: "MQTT Channel used for publishing a message to", required: true, displayDuringSetup: false
  input name: "publishon", type: "text", title: "Text to Publish for switch ON", description: "Text used for publishing a message to a channel", required: true, displayDuringSetup: false
  input name: "publishoff", type: "text", title: "Text to Publish for switch OFF", description: "Text used for publishing a message to a channel", required: true, displayDuringSetup: false
  input name: "statusfrom", type: "text", title: "Channel to get status from", description: "MQTT Channel used for getting status from", required: true, displayDuringSetup: false
  input name: "statuson", type: "text", title: "Status for switch ON", description: "Text used for getting status for ON state", required: true, displayDuringSetup: false
  input name: "statusoff", type: "text", title: "Status for switch OFF", description: "Text used for getting status for OFF state", required: true, displayDuringSetup: false
  input name: "pollRate", type: "number", title: "Polling Rate", description: "Poll switch every X minutes. Set to 0 to disable.", required: true, defaultValue: "0"
 }

}

def updated(){
    initialize()
}

def installed() {
 log.info "installed..."
    initialize()
}

def initialize() {
 state.pollRate = ((pollRate.toInteger())*60)
 if (state.pollRate != 0) runIn (state.pollRate, poll)
 log.info device.getName()
}


def on() {
 log.info "Set ON"
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
  mqtt.publish(settings?.publishto, settings?.publishon, 1, true)
 } catch (e) {
  log.error e.message
 }
}

def off() {
 log.info "Set OFF"
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
  mqtt.publish(settings?.publishto, settings?.publishoff, 1, true)
 } catch (e) {
  log.error e.message
 }

}

def mqttClientStatus(message) {
 log.info message
}

def parse(message) {
 mqtt = interfaces.mqtt
 if (mqtt.parseMessage(message).payload == settings?.statusoff) {
  sendEvent(name: "switch", value: "off", isStateChange: true)
  return
 }
 if (mqtt.parseMessage(message).payload == settings?.statuson) {
  sendEvent(name: "switch", value: "on", isStateChange: true)
  return
 }
 log.error "Unexpected payload data returned from MQTT:" + mqtt.parseMessage(message).payload
}

def refresh() {
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
 } catch (e) {
  log.error e.message
 }
}
def ping() {
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
 mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.statusfrom)
 } catch (e) {
  log.error e.message
 }
 if (state.pollRate != 0) runIn(state.pollRate, poll)
}
