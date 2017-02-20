/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Sliding Door Lock", namespace: "ady624", author: "ady624") {
		capability "Energy Meter"
		capability "Power Meter"
		capability "Actuator"
		capability "Lock"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"

		command "reset"

		fingerprint deviceId: "0x1001", inClusters: "0x25,0x32,0x27,0x2C,0x2B,0x70,0x85,0x56,0x72,0x86", outClusters: "0x82"
	}

	// simulator metadata
	simulator {
		status "on":  "command: 2003, payload: FF"
		status "off": "command: 2003, payload: 00"

		for (int i = 0; i <= 100; i += 10) {
			status "energy  ${i} kWh": new physicalgraph.zwave.Zwave().meterV2.meterReport(
				scaledMeterValue: i, precision: 3, meterType: 0, scale: 0, size: 4).incomingMessage()
		}

		// reply messages
		reply "2001FF,delay 100,2502": "command: 2503, payload: FF"
		reply "200100,delay 100,2502": "command: 2503, payload: 00"

	}
    
	preferences {
	    input name: "mode", type: "enum", options: ["Safety", "Security"], title: "Lock type", description: "Please select the type of lock", required: true, defaultValue: "Safety", displayDuringSetup: true
	}    

	// tile definitions
	tiles {
		standardTile("lock", "device.lock", width: 2, height: 2, canChangeIcon: true) {
			state "unlocked", label: '${name}', action: "lock.lock", icon: "st.locks.lock.unlocked", backgroundColor: "#ffffff"
			state "locked", label: '${name}', action: "lock.unlock", icon: "st.locks.lock.locked", backgroundColor: "#79b821"
		}
		valueTile("power", "device.power", decoration: "flat") {
			state "default", label:'${currentValue} W'
		}
		valueTile("energy", "device.energy", decoration: "flat") {
			state "default", label:'${currentValue} kWh'
		}
		standardTile("reset", "device.energy", inactiveLabel: false, decoration: "flat") {
			state "default", label:'reset kWh', action:"reset"
		}
		standardTile("configure", "device.power", inactiveLabel: false, decoration: "flat") {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}
		standardTile("refresh", "device.power", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main "lock"
		details(["lock","power","energy","reset","refresh","configure"])
	}
}

def parse(String description) {
	def result = null
	def cmd = zwave.parse(description, [0x20: 1, 0x32: 2])
	if (cmd) {
		log.debug cmd
		result = createEvent(zwaveEvent(cmd))
	}
	return result
}

def zwaveEvent(physicalgraph.zwave.commands.meterv2.MeterReport cmd) {
	log.debug "cmd is $cmd"
	if (cmd.scale == 0) {
		[name: "energy", value: cmd.scaledMeterValue, unit: "kWh"]
	} else if (cmd.scale == 1) {
		[name: "energy", value: cmd.scaledMeterValue, unit: "kVAh"]
	}
	else {
		[name: "power", value: Math.round(cmd.scaledMeterValue), unit: "W"]
	}
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd)
{
	def valueTrue = settings.mode == "Security" ? "unlocked" : "locked"
    def valueFalse = settings.mode == "Security" ? "locked" : "unlocked"
	[
		name: "lock", value: cmd.value ? valueTrue : valueFalse, type: "physical"
	]
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd)
{
	def valueTrue = settings.mode == "Security" ? "unlocked" : "locked"
    def valueFalse = settings.mode == "Security" ? "locked" : "unlocked"
	[
		name: "lock", value: cmd.value ? valueTrue : valueFalse, type: "digital"
	]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	// Handles all Z-Wave commands we aren't interested in
	[:]
}

def unlock() {
	delayBetween([
		zwave.basicV1.basicSet(value: settings.mode == 'Security' ? 0xFF : 0x00).format(),
		zwave.switchBinaryV1.switchBinaryGet().format()
	])
}

def lock() {
	delayBetween([
		zwave.basicV1.basicSet(value: settings.mode == 'Security' ? 0x00 : 0xFF).format(),
		zwave.switchBinaryV1.switchBinaryGet().format()
	])
}

def poll() {
	delayBetween([
		zwave.switchBinaryV1.switchBinaryGet().format(),
		zwave.meterV2.meterGet().format()
	])
}

def refresh() {
	zwave.switchBinaryV1.switchBinaryGet().format()
    zwave.meterV2.meterGet().format()
}

def reset() {
	return [
		zwave.meterV2.meterReset().format(),
		zwave.meterV2.meterGet().format()
	]
}

def configure() {
	log.trace "Configuring..."
	delayBetween([
		zwave.configurationV1.configurationSet(parameterNumber:  91, size: 2, scaledConfigurationValue: 1).format(),   // a change of 1W will trigger report
		zwave.configurationV1.configurationSet(parameterNumber: 101, size: 4, scaledConfigurationValue: 8).format(),   // energy in kWh
		zwave.configurationV1.configurationSet(parameterNumber: 102, size: 4, scaledConfigurationValue: 4).format(),   // power in W
		zwave.configurationV1.configurationSet(parameterNumber: 103, size: 4, scaledConfigurationValue: 0).format(),   // no third report
		zwave.configurationV1.configurationSet(parameterNumber: 111, size: 4, scaledConfigurationValue: 60).format(), // report group 1 every 30 seconds
		zwave.configurationV1.configurationSet(parameterNumber: 112, size: 4, scaledConfigurationValue: 60).format() // report group 2 every 30 seconds
	])
}