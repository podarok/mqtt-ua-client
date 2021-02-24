
metadata {
 definition(name: "MQTT UA AC Controller", namespace: "mqtt_ua", author: "Andrii Podanenko") {
     capability "Initialize"
     capability "Switch"
     capability "Refresh"
     capability "HealthCheck"
     capability "Polling"
     capability "Thermostat" // @see https://docs.hubitat.com/index.php?title=Driver_Capability_List#Thermostat
     capability "ThermostatMode"// @see https://docs.hubitat.com/index.php?title=Driver_Capability_List#ThermostatMode
     capability "ThermostatFanMode" // @see https://docs.hubitat.com/index.php?title=Driver_Capability_List#ThermostatMode
     capability "ThermostatHeatingSetpoint" // @see https://docs.hubitat.com/index.php?title=Driver_Capability_List#ThermostatHeatingSetpoint
     
  
     command "jsonConfig", [[name:"Device Config", type: "JSON_OBJECT", description: "Paste config from MQTT"] ]

     
     attribute "Config", "JSON_OBJECT"
     attribute "thermostatMode", "ENUM"
     attribute "supportedThermostatModes", "ENUM"
     attribute "thermostatFanMode", "ENUM"
     attribute "supportedThermostatFanModes", "ENUM"
     attribute "heatingSetpoint", "NUMBER"
     attribute "coolingSetpoint", "NUMBER"
     attribute "temperature", "NUMBER"
     
     attribute "mode_cmd_t", "STRING"
     attribute "mode_stat_t", "STRING"
     attribute "curr_temp_t", "STRING"
     attribute "temp_cmd_t", "STRING"
     attribute "temp_stat_t", "STRING"
     attribute "fan_mode_command_topic", "STRING"
     attribute "fan_mode_state_topic", "STRING"
     attribute "pl_on", "STRING"
     attribute "pl_off", "STRING"
     attribute "swing_mode_state_topic", "STRING"
     attribute "swing_mode_command_topic", "STRING"
     attribute "power_state_topic", "STRING"
     attribute "power_command_topic", "STRING"
     
 }
 preferences {
     input name: "MQTTBroker", type: "text", title: "MQTT Broker Address", description: "Enter address of the broker, for example, tcp://192.168.1.71:1883", required: true, displayDuringSetup: true
     input name: "username", type: "text", title: "MQTT Username", description: "Keep blank if none", required: false, displayDuringSetup: true
     input name: "password", type: "password", title: "MQTT Password", description: "Keep blank if none", required: false, displayDuringSetup: true
     input name: "device_mqtt_topic", type: "text", title: "MQTT topic prefix for device", description: "MQTT Channel used for exchanging data", required: true, displayDuringSetup: false
  //input name: "publishto", type: "text", title: "Channel to Publish", description: "MQTT Channel used for publishing a message to", required: true, displayDuringSetup: false
  //input name: "publishon", type: "text", title: "Text to Publish for switch ON", description: "Text used for publishing a message to a channel", required: true, displayDuringSetup: false
  //input name: "publishoff", type: "text", title: "Text to Publish for switch OFF", description: "Text used for publishing a message to a channel", required: true, displayDuringSetup: false
  //input name: "statusfrom", type: "text", title: "Channel to get status from", description: "MQTT Channel used for getting status from", required: true, displayDuringSetup: false
  //input name: "statuson", type: "text", title: "Status for switch ON", description: "Text used for getting status for ON state", required: true, displayDuringSetup: false
  //input name: "statusoff", type: "text", title: "Status for switch OFF", description: "Text used for getting status for OFF state", required: true, displayDuringSetup: false
  input name: "pollRate", type: "number", title: "Polling Rate", description: "Poll switch every X minutes. Set to 0 to disable.", required: true, defaultValue: "0"
 }

}

def setHeatingSetpoint(temp){
    log.info "here"
    
    def currentTemp = device.currentValue("heatingSetpoint", true)
    if (Float.valueOf(currentTemp) > Float.valueOf(temp)) {
    sendEvent(name: "heatingSetpoint", value: (currentTemp.toInteger() - 1))
    }
    else if (Float.valueOf(currentTemp) < Float.valueOf(temp)) {
    sendEvent(name: "heatingSetpoint", value: (currentTemp.toInteger() + 1))
    }
}

def jsonConfig(json) {
    def slurper = new groovy.json.JsonSlurper()
    def result = slurper.parseText(json)

    device.setLabel(result.name)
    device.setName(result.uniq_id)
    //log.info label
    sendEvent(name: "Config", value: json)
    sendEvent(name: "supportedThermostatModes", value: result.modes)
    sendEvent(name: "supportedThermostatFanModes", value: result.fan_modes)
    
     sendEvent(name: "mode_cmd_t", value: result.mode_cmd_t)
     sendEvent(name: "mode_stat_t", value: result.mode_stat_t)
     sendEvent(name: "curr_temp_t", value: result.curr_temp_t)
     sendEvent(name: "temp_cmd_t", value: result.temp_cmd_t)
     sendEvent(name: "temp_stat_t", value: result.temp_stat_t)
     sendEvent(name: "fan_mode_command_topic", value: result.fan_mode_command_topic)
     sendEvent(name: "fan_mode_state_topic", value: result.fan_mode_state_topic)
     sendEvent(name: "pl_on", value: result.pl_on)
     sendEvent(name: "pl_off", value: result.pl_off)
     sendEvent(name: "swing_mode_state_topic", value: result.swing_mode_state_topic)
     sendEvent(name: "swing_mode_command_topic", value: result.swing_mode_command_topic)
     sendEvent(name: "power_state_topic", value: result.power_state_topic)
     sendEvent(name: "power_command_topic", value: result.power_command_topic)
}

def setThermostatFanMode(fanmode){
    sendEvent(name: "thermostatFanMode", value: value)
}

def setThermostatMode(value){
    sendEvent(name: "thermostatMode", value: value)
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
 
    
     mqtt = interfaces.mqtt
 try {
  mqtt.connect(settings?.MQTTBroker, device.getDeviceNetworkId(), settings?.username, settings?.password)
  mqtt.subscribe(settings?.device_mqtt_topic)
  //mqtt.publish(settings?.publishto, settings?.publishon, 1, true)
 } catch (e) {
  log.error e.message
 }
    
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
    log.info mqtt.parseMessage(message)
    // log.info mqtt.parseMessage(message).topic

   switch(mqtt.parseMessage(message).topic) { 
   case device.currentValue("curr_temp_t", true): 
       log.info "curr_temp_t" 
       sendEvent(name: "temperature", value: mqtt.parseMessage(message).payload)
       return
   case device.currentValue("mode_cmd_t", true):
       log.info "mode_cmd_t" 
       return
   case device.currentValue("mode_stat_t", true):
       log.info "mode_stat_t" 
       return
   case device.currentValue("curr_temp_t", true):
       log.info "curr_temp_t" 
       return
   case device.currentValue("temp_cmd_t", true):
       log.info "temp_cmd_t" 
       return
   case device.currentValue("temp_stat_t", true):
       log.info "temp_stat_t" 
       sendEvent(name: "heatingSetpoint", value: mqtt.parseMessage(message).payload)
       sendEvent(name: "coolingSetpoint", value: mqtt.parseMessage(message).payload)
       return
   case device.currentValue("fan_mode_command_topic", true):
       log.info "fan_mode_command_topic" 
       return
   case device.currentValue("fan_mode_state_topic", true):
       log.info "fan_mode_state_topic" 
       return
   case device.currentValue("pl_on", true):
       log.info "pl_on" 
       return
   case device.currentValue("pl_off", true):
       log.info "pl_off" 
       return
   case device.currentValue("swing_mode_state_topic", true):
       log.info "swing_mode_state_topic" 
       return
   case device.currentValue("swing_mode_command_topic", true):
       log.info "swing_mode_command_topic"
       return
   case device.currentValue("power_state_topic", true):
       log.info "power_state_topic" 
       return
   case device.currentValue("power_command_topic", true):
       log.info "power_command_topic" 
       return

   default:
       log.info "Unknown topic"
   return 

} 
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
