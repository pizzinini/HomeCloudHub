/**
 *  SmartThingers
 *
 *  Copyright 2016 Adrian Caramaliu
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Version history
 *   5/04/2016 >>> v0.0.005.20160504 - Alpha test version - added full list of standard capabilities, attributes and commands, improved condition UI
 *   5/02/2016 >>> v0.0.004.20160502 - Alpha test version - changed license from Apache to GPLv3
 *   5/02/2016 >>> v0.0.003.20160502 - Alpha test version - added mode - simple, latching or else-if
 *   5/02/2016 >>> v0.0.002.20160502 - Alpha test version - added latching rules
 *   4/29/2016 >>> v0.0.001.20160429 - Alpha test version - added condition naming
 *   4/29/2016 >>> v0.0.000.20160429 - Alpha test version
 *
 */
definition(
    name: "SmartThingers",
    namespace: "ady624",
    author: "Adrian Caramaliu",
    description: "Executes conditional actions",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "pageMain")
    page(name: "pageIf")
    page(name: "pageIfOther")
    page(name: "pageThen")
    page(name: "pageElse")
    page(name: "pageCondition")
    page(name: "pageConditionGroupL1")
    page(name: "pageConditionGroupL2")
    page(name: "pageConditionGroupL3")
    page(name: "pageConditionVsTrigger")
    page(name: "pageActionGroup")
}


/* constant definitions */
def capabilities() {
	return [
    	[ name: "accelerationSensor",				display: "Acceleration Sensor",				attribute: "acceleration",				commands: null,																		multiple: true,			],
    	[ name: "alarm",							display: "Alarm",							attribute: "alarm",						commands: ["off", "strobe", "siren", "both"],										multiple: true,			],
        [ name: "battery",							display: "Battery",							attribute: "battery",					commands: null,																		multiple: true,			],
    	[ name: "beacon",							display: "Beacon",							attribute: "presence",					commands: null,																		multiple: true,			],
        [ name: "button",							display: "Button",							attribute: "button",					commands: null,																		multiple: true,			],
    	[ name: "carbonDioxideMeasurement",			display: "Carbon Dioxide Measurement",		attribute: "carbonDioxide",				commands: null,																		multiple: true,			],
        [ name: "carbonMonoxideDetector",			display: "Carbon Monoxide Detector",		attribute: "carbonMonoxide",			commands: null,																		multiple: true,			],
    	[ name: "colorControl",						display: "Color Control",					attribute: "color",						commands: ["setColor", "setHue", "setSaturation"],									multiple: true,			],
        [ name: "colorTemperature",					display: "Color Temperature",				attribute: "colorTemperature",			commands: ["setColorTemperature"],													multiple: true,			],
    	[ name: "configure",						display: "Configure",						attribute: null,						commands: ["configure"],															multiple: true,			],
    	[ name: "consumable",						display: "Consumable",						attribute: "consumable",				commands: ["setConsumableStatus"],													multiple: true,			],
		[ name: "contactSensor",					display: "Contact Sensor",					attribute: "contact",					commands: null,																		multiple: true,			],
    	[ name: "switchLevel",						display: "Dimmer",							attribute: "level",						commands: ["setLevel"],																multiple: true,			],
    	[ name: "doorControl",						display: "Door",							attribute: "door",						commands: ["open", "close"],														multiple: true,			],
    	[ name: "energyMeter",						display: "Energy Meter",					attribute: "energy",					commands: null,																		multiple: true,			],
    	[ name: "garageDoorControl",				display: "Garage Door",						attribute: "door",						commands: ["open", "close"],														multiple: true,			],
        [ name: "illuminanceMeasurement",			display: "Illuminance Measurement",			attribute: "illuminance",				commands: null,																		multiple: true,			],
        [ name: "imageCapture",						display: "Image Capture",					attribute: "image",						commands: ["take"],																	multiple: true,			],
    	[ name: "waterSensor",						display: "Leak Sensor",						attribute: "water",						commands: null,																		multiple: true,			],
        [ name: "lock",								display: "Lock",							attribute: "lock",						commands: ["lock", "unlock"],														multiple: true,			],
    	[ name: "mediaController",					display: "Media Controller",				attribute: "currentActivity",			commands: ["startActivity", "getAllActivities", "getCurrentActivity"],				multiple: true,			],
    	[ name: "momentary",						display: "Momentary",						attribute: null,						commands: ["push"],																	multiple: true,			],
    	[ name: "motionSensor",						display: "Motion Sensor",					attribute: "motion",					commands: null,																		multiple: true,			],
    	[ name: "musicPlayer",						display: "Music Player",					attribute: "status",					commands: ["play", "pause", "stop", "nextTrack", "playTrack", "setLevel", "playText", "mute", "previousTrack", "unmute", "setTrack", "resumeTrack", "restoreTrack"],	multiple: true,			],
    	[ name: "notification",						display: "Notification",					attribute: null,						commands: ["deviceNotification"],													multiple: true,			],
    	[ name: "pHMeasurement",					display: "pH Measurement",					attribute: "pH",						commands: null,																		multiple: true,			],
    	[ name: "polling",							display: "Polling",							attribute: null,						commands: ["poll"],																	multiple: true,			],
        [ name: "powerMeter",						display: "Power Meter",						attribute: "power",						commands: null,																		multiple: true,			],
        [ name: "powerSource",						display: "Power Source",					attribute: "powerSource",				commands: null,																		multiple: true,			],
    	[ name: "presenceSensor",					display: "Presence Sensor",					attribute: "presence",					commands: null,																		multiple: true,			],
    	[ name: "refresh",							display: "Refresh",							attribute: null,						commands: ["refresh"],																multiple: true,			],
    	[ name: "relativeHumidityMeasurement",		display: "Relative Humidity Measurement",	attribute: "humidity",					commands: null,																		multiple: true,			],
    	[ name: "relaySwitch",						display: "Relay Switch",					attribute: "switch",					commands: ["on", "off"],															multiple: true,			],
    	[ name: "shockSensor",						display: "Shock Sensor",					attribute: "shock",						commands: null,																		multiple: true,			],
    	[ name: "signalStrength",					display: "Signal Strength",					attribute: "lqi",						commands: null,																		multiple: true,			],
    	[ name: "alarm",							display: "Siren",							attribute: "alarm",						commands: ["off", "strobe", "siren", "both"],										multiple: true,			],
    	[ name: "sleepSensor",						display: "Sleep Sensor",					attribute: "sleeping",					commands: null,																		multiple: true,			],
    	[ name: "smokeDetector",					display: "Smoke Detector",					attribute: "smoke",						commands: null,																		multiple: true,			],
        [ name: "soundSensor",						display: "Sound Sensor",					attribute: "sound",						commands: null,																		multiple: true,			],
    	[ name: "speechSynthesis",					display: "Speech Synthesis",				attribute: null,						commands: ["speak"],																multiple: true,			],
        [ name: "stepSensor",						display: "Step Sensor",						attribute: "steps",						commands: null,																		multiple: true,			],
    	[ name: "switch",							display: "Switch",							attribute: "switch",					commands: ["on", "off"],															multiple: true,			],
    	[ name: "switchLevel",						display: "Switch Level",					attribute: "level",						commands: ["setLevel"],																multiple: true,			],
        [ name: "soundPressureLevel",				display: "Sound Pressure Level",			attribute: "soundPressureLevel",		commands: null,																		multiple: true,			],
    	[ name: "consumable",						display: "Stock Management",				attribute: "consumable",				commands: null,																		multiple: true,			],
    	[ name: "tamperAlert",						display: "Tamper Alert",					attribute: "tamper",					commands: null,																		multiple: true,			],
    	[ name: "temperatureMeasurement",			display: "Temperature Measurement",			attribute: "temperature",				commands: null,																		multiple: true,			],
        [ name: "thermostat",						display: "Thermostat",						attribute: "temperature",				commands: ["setHeatingSetpoint", "setCoolingSetpoint", "off", "heat", "emergencyHeat", "cool", "setThermostatMode", "fanOn", "fanAuto", "fanCirculate", "setThermostatFanMode", "auto"],	multiple: true,			],
        [ name: "thermostatCoolingSetpoint",		display: "Thermostat Cooling Setpoint",		attribute: "coolingSetpoint",			commands: ["setCoolingSetpoint"],													multiple: true,			],
    	[ name: "thermostatFanMode",				display: "Thermostat Fan Mode",				attribute: "thermostatFanMode",			commands: ["fanOn", "fanAuto", "fanCirculate", "setThermostatFanMode"],				multiple: true,			],
    	[ name: "thermostatHeatingSetpoint",		display: "Thermostat Heating Setpoint",		attribute: "heatingSetpoint",			commands: ["setHeatingSetpoint"],													multiple: true,			],
    	[ name: "thermostatMode",					display: "Thermostat Mode",					attribute: "thermostatMode",			commands: ["off", "heat", "emergencyHeat", "cool", "auto", "setThermostatMode"],	multiple: true,			],
    	[ name: "thermostatOperatingState",			display: "Thermostat Operating State",		attribute: "thermostatOperatingState",	commands: null,																		multiple: true,			],
    	[ name: "thermostatSetpoint",				display: "Thermostat Setpoint",				attribute: "thermostatSetpoint",		commands: null,																		multiple: true,			],
    	[ name: "threeAxis",						display: "Three Axis Sensor",				attribute: "threeAxis",					commands: null,																		multiple: true,			],
    	[ name: "timedSession",						display: "Timed Session",					attribute: "sessionStatus",				commands: ["setTimeRemaining", "start", "stop", "pause", "cancel"],					multiple: true,			],
    	[ name: "tone",								display: "Tone Generator",					attribute: null,						commands: ["tone"],																	multiple: true,			],
    	[ name: "touchSensor",						display: "Touch Sensor",					attribute: "touch",						commands: null,																		multiple: true,			],
    	[ name: "valve",							display: "Valve",							attribute: "contact",					commands: ["open", "close"],														multiple: true,			],
    	[ name: "voltageMeasurement",				display: "Voltage Measurement",				attribute: "voltage",					commands: null,																		multiple: true,			],
    	[ name: "waterSensor",						display: "Water Sensor",					attribute: "water",						commands: null,																		multiple: true,			],
        [ name: "windowShade",						display: "Window Shade",					attribute: "windowShade",				commands: ["open", "close", "presetPosition"],										multiple: true,			],
    ]
}

def attributes() {
	return [
    	[ name: "acceleration",				type: "enum",			range: null,			unit: null,		options: ["active", "inactive"],																			],
    	[ name: "alarm",					type: "enum",			range: null,			unit: null,		options: ["off", "strobe", "siren", "both"],																],
    	[ name: "battery",					type: "number",			range: "0..100",		unit: "%",		options: null,																								],
    	[ name: "beacon",					type: "enum",			range: null,			unit: null,		options: ["present", "not present"],																		],
        [ name: "button",					type: "enum",			range: null,			unit: null,		options: ["held", "pushed"],																				],
    	[ name: "carbonDioxide",			type: "number",			range: "0..*",			unit: null,		options: null,																								],
    	[ name: "carbonMonoxide",			type: "enum",			range: null,			unit: null,		options: ["clear", "detected", "tested"],																	],
    	[ name: "color",					type: "color",			range: null,			unit: null,		options: null,																								],
    	[ name: "hue",						type: "number",			range: "0..100",		unit: "%",		options: null,																								],
    	[ name: "saturation",				type: "number",			range: "0..100",		unit: "%",		options: null,																								],
    	[ name: "hex",						type: "hexcolor",		range: null,			unit: null,		options: null,																								],
    	[ name: "saturation",				type: "number",			range: "0..100",		unit: "%",		options: null,																								],
    	[ name: "level",					type: "number",			range: "0..100",		unit: "%",		options: null,																								],
    	[ name: "switch",					type: "enum",			range: null,			unit: null,		options: ["on", "off"],																						],
    	[ name: "colorTemperature",			type: "number",			range: "2000..7000",	unit: "K",		options: null,																								],
    	[ name: "consumable",				type: "enum",			range: null,			unit: null,		options: ["missing", "good", "replace", "maintenance_required", "order"],									],
    	[ name: "contact",					type: "enum",			range: null,			unit: null,		options: ["open", "closed"],																				],
    	[ name: "door",						type: "enum",			range: null,			unit: null,		options: ["unknown", "closed", "open", "closing", "opening"],												],
    	[ name: "energy",					type: "number",			range: "0..*",			unit: "kWh",	options: null,																								],
    	[ name: "illuminance",				type: "number",			range: "0..*",			unit: "lux",	options: null,																								],
    	[ name: "image",					type: "image",			range: null,			unit: null,		options: null,																								],
    	[ name: "lock",						type: "enum",			range: null,			unit: null,		options: ["locked", "unlocked"],																			],
    	[ name: "activities",				type: "string",			range: null,			unit: null,		options: null,																								],
    	[ name: "currentActivity",			type: "string",			range: null,			unit: null,		options: null,																								],
    	[ name: "motion",					type: "enum",			range: null,			unit: null,		options: ["active", "inactive"],																			],
    	[ name: "status",					type: "string",			range: null,			unit: null,		options: null,																								],								
    	[ name: "mute",						type: "enum",			range: null,			unit: null,		options: ["muted", "unmuted"],																				],
    	[ name: "pH",						type: "decimal",		range: "0..14",			unit: null,		options: null,																								],
    	[ name: "power",					type: "number",			range: "0..*",			unit: "W",		options: null,																								],
    	[ name: "presence",					type: "enum",			range: null,			unit: null,		options: ["present", "not present"],																		],
    	[ name: "humidity",					type: "number",			range: "0..100",		unit: "%",		options: null,																								],
        [ name: "shock",					type: "enum",			range: null,			unit: null,		options: ["detected", "clear"],																				],
    	[ name: "lqi",						type: "number",			range: "0..255",		unit: null,		options: null,																								],
    	[ name: "rssi",						type: "number",			range: "0..100",		unit: "%",		options: null,																								],
    	[ name: "sleeping",					type: "enum",			range: null,			unit: null,		options: ["sleeping", "not sleeping"],																		],
    	[ name: "smoke",					type: "enum",			range: null,			unit: null,		options: ["clear", "detected", "tested"],																	],
    	[ name: "sound",					type: "enum",			range: null,			unit: null,		options: ["detected", "not detected"],																		],
        [ name: "steps",					type: "number",			range: "0..*",			unit: null,		options: null,																								],
    	[ name: "goal",						type: "number",			range: "0..*",			unit: null,		options: null,																								],
    	[ name: "soundPressureLevel",		type: "number",			range: "0..*",			unit: null,		options: null,																								],
    	[ name: "tamper",					type: "enum",			range: null,			unit: null,		options: ["clear", "detected"],																				],
    	[ name: "temperature",				type: "decimal",		range: "*..*",			unit: "F",		options: null,																								],
    	[ name: "thermostatMode",			type: "enum",			range: null,			unit: null,		options: ["off", "auto", "cool", "heat", "emergency heat"],													],
    	[ name: "thermostatFanMode",		type: "enum",			range: null,			unit: null,		options: ["auto", "on", "circulate"],																		],
    	[ name: "thermostatOperatingState",	type: "enum",			range: null,			unit: null,		options: ["idle", "pending cool", "cooling", "pending heat", "heating", "fan only", "vent economizer"],		],
        [ name: "coolingSetpoint",			type: "number",			range: "-127..127",		unit: "F",		options: null,																								],
        [ name: "heatingSetpoint",			type: "number",			range: "-127..127",		unit: "F",		options: null,																								],
        [ name: "thermostatSetpoint",		type: "number",			range: "-127..127",		unit: "F",		options: null,																								],
        [ name: "sessionStatus",			type: "enum",			range: null,			unit: null,		options: ["paused", "stopped", "running", "canceled"],														],
    	[ name: "threeAxis",				type: "threeAxis",		range: "0..1024",		unit: null,		options: null,																								],
    	[ name: "touch",					type: "enum",			range: null,			unit: null,		options: ["touched"],																						],
    	[ name: "valve",					type: "enum",			range: null,			unit: null,		options: ["open", "closed"],																				],
        [ name: "voltage",					type: "decimal",		range: "*..*",			unit: "V",		options: null,																								],
    	[ name: "water",					type: "enum",			range: null,			unit: null,		options: ["dry", "wet"],																					],
    	[ name: "windowShade",				type: "enum",			range: null,			unit: null,		options: ["unknown", "open", "closed", "opening", "closing", "partially open"],								],
    ]
}

def triggerPrefix() {
	//return "♦ "
    //return "¤ "
    return "● "
}

def conditionPrefix() {
	return "◦ "
}

def customAttributePrefix() {
	return "⌂ "
}


def pageMain() {
	configApp()
    cleanUpConditions(true)
	dynamicPage(name: "pageMain", title: "SmartThinger Application", uninstall: true, install: true) {
    	section() {
        	label title: "Name", required: true
        	input "description", "string", title: "Description", required: false, defaultValue: "test"
            input "mode", "enum", title: "SmartThinger Mode", required: true, options: ["Simple", "Latching", "Else-If"], defaultValue: "Simple", submitOnChange: true
            switch (settings.mode) {
            	case "Latching":
            		paragraph "A latching SmartThinger - also known as a bi-stable SmartThinger - uses one set of conditions to achieve a 'true' state and a second set of conditions to revert back to its 'false' state"
                    break
            	case "Else-If":
            		paragraph "An Else-If SmartThinger executes a set of actions if an initial condition set evaluates to true, otherwise executes a second set of actions if a second condition set evaluates to true"
                    break
            }
        }
        
        section() {
			href "pageSimulate", title: "Simulate", description: "Allows you to test the actions manually", state: complete
        }
        
        section() {
			href "pageIf", title: "If...", description: (state.config.app.conditions.children.size() ? "Tap here to edit the main If group or tap on any individual conditions below to edit them directly" : "Tap to select conditions")
            buildIfContent()
        }
        
        section() {
			href "pageActionGroup", params:["conditionId": -1], title: "Then...", description: "Choose what should happen then", state: null, submitOnChange: false
        }
        
        if (settings.mode == "Latching") {
            section() {
                href "pageIfOther", title: "But if...", description: (state.config.app.otherConditions.children.size() ? "" : "Tap to select conditions")
                buildIfOtherContent()
            }
        }

        if (settings.mode == "Else-If") {
            section() {
                href "pageIfOther", title: "Else if...", description: (state.config.app.otherConditions.children.size() ? "" : "Tap to select conditions")
                buildIfOtherContent()
            }
        }

        section() {
			href "pageActionGroup", params:["conditionId": -1], title: ((settings.mode == "Latching") || (settings.mode == "Else-If") ? "Then..." : "Else..."), description: "Choose what should happen otherwise", state: null, submitOnChange: false
		}
        
		section(title: "Advanced options", hideable: true, hidden: true) {
        	input "expert", "bool", title: "Expert Mode", defaultValue: false, submitOnChange: true
		}
	}
}


def pageIf(params) {
    cleanUpConditions(false)
	def condition = state.config.app.conditions
    dynamicPage(name: "pageIf", title: "Main Condition Group", uninstall: false, install: false) {
    	getConditionGroupPageContent(params, condition)
    }
}

def pageIfOther(params) {
    cleanUpConditions(false)
	def condition = state.config.app.otherConditions
    dynamicPage(name: "pageIfOther", title: "Main Group", uninstall: false, install: false) {
    	getConditionGroupPageContent(params, condition)
    }
}
def pageConditionGroupL1(params) {
	pageConditionGroup(params, 1)
}

def pageConditionGroupL2(params) {
	pageConditionGroup(params, 2)
}

def pageConditionGroupL3(params) {
	pageConditionGroup(params, 3)
}

//helper function for condition group paging
def pageConditionGroup(params, level) {
    cleanUpConditions(false)
    def condition = null    
    if (params?.command == "add") {
        condition = createCondition(params?.parentConditionId, true)
    } else {
		condition = getCondition(params?.conditionId ? params?.conditionId : state.config["conditionGroupIdL$level"])
    }
    if (condition) {
    	def id = condition.id
        state.config["conditionGroupIdL$level"] = id
        def pid = condition.parentId
    	dynamicPage(name: "pageConditionGroupL$level", title: "Group $id (level $level)", uninstall: false, install: false) {
	    	getConditionGroupPageContent(params, condition)
	    }
    }
}

def getConditionGroupPageContent(params, condition) {	
	if (condition) {
        def id = condition.id
        def pid = condition.parentId ? condition.parentId : condition.id
        def nextLevel = (condition.level ? condition.level : 0) + 1
        def cnt = 0
        section() {
        	if (settings["condNegate$id"]) {
                paragraph "NOT ("
            }
            for (c in condition.children) {
            	if (cnt > 0) {
                	if (cnt == 1) {
			           	input "condGrouping$id", "enum", title: "", description: "Choose the logical operation to be applied between all conditions in this group", options: ["AND", "OR", "XOR"], defaultValue: "AND", required: true, submitOnChange: true
                    } else {
                		paragraph settings["condGrouping$id"], state: "complete"
                    }
                }
                def cid = c?.id
		        def conditionType = (c.trg ? "trigger" : "condition")
                if (c.children != null) {
                    href "pageConditionGroupL${nextLevel}", /*image: "https://raw.githubusercontent.com/ady624/SmartThingers/master/resources/images/folder.png",*/ params: ["conditionId": cid], title: "Group #$cid", description: getConditionDescription(cid), state: "complete", required: false, submitOnChange: false
                } else {
                    href "pageCondition", /*image: "https://raw.githubusercontent.com/ady624/SmartThingers/master/resources/images/${conditionType}.png",*/ params: ["conditionId": cid], title: (c.trg ? "Trigger" : "Condition") + " #$cid", description: getConditionDescription(cid), state: "complete", required: false, submitOnChange: false
                }
                cnt++
            }
        	if (settings["condNegate$id"]) {
                paragraph ")", state: "complete"
            }
        }
        section() {
            href "pageCondition", params:["command": "add", "parentConditionId": id], title: "Add a condition", description: "A condition watches the state of one or multiple similar devices", state: "complete", submitOnChange: true
            //href "pageCondition", params:["command": "add", "parentConditionId": id], title: "Add a trigger", description: "A trigger is a specialised condition that watches for state changes of one or multiple similar devices", state: "complete", submitOnChange: true
            if (nextLevel <= 3) {
            	href "pageConditionGroupL${nextLevel}", params:["command": "add", "parentConditionId": id], title: "Add a group", description: "A group is a container for multiple conditions and/or triggers, allowing for more complex logical operations, such as evaluating [A AND (B OR C)]", state: "complete", submitOnChange: true
            }
        }
        
        section(title: (condition.trg ? "Trigger" : "Condition") + " Overview") {
            paragraph getConditionDescription(id)
            href "pageActionGroup", params:["conditionId": id], title: "Individual actions", description: "Tap to set individual actions for this condition", state: complete, submitOnChange: true
        }       

//        section() {
//            href "pageConditionVsTrigger", title: "Uh oh, which should I use?", description: "Find out when to use a condition and when to use a trigger", state: complete, submitOnChange: false
//        }

		section(title: "Advanced options", hideable: true, hidden: true) {
           	//input "condGrouping$id", "enum", title: "Grouping Method", description: "Choose the logical operation to be applied between all conditions in this group", options: ["AND", "OR", "XOR"], defaultValue: "AND", required: true, submitOnChange: true
           	input "condNegate$id", "bool", title: "Negate Group", description: "Apply a logical NOT to the whole group", defaultValue: false, required: true, submitOnChange: true
        	if (id) {
            	//add a hidden parameter for any condition other than the main container - this is to maintain the correct grouping
    	        input "condParent$id", "number", title: "Parent ID", range: "$pid..$pid", defaultValue: pid
        	}
        }
    }
}

def pageCondition(params) {
	//get the current edited condition
    def condition = null
    if (params?.command == "add") {
        condition = createCondition(params?.parentConditionId, false)
    } else {   	
		condition = getCondition(params?.conditionId ? params?.conditionId : state.config.conditionId)
    }
    if (condition) {
    	def id = condition.id
        state.config.conditionId = id
        def pid = condition.parentId
    	dynamicPage(name: "pageCondition", title: (condition.trg? "Trigger" : "Condition") + " #$id", uninstall: false, install: false) {
			section() {
            	if (!settings["condDevices$id"] || (settings["condDevices$id"].size() == 0)) {
                	//only display capability selection if no devices already selected
	            	input "condCap$id", "enum", title: "Capability", options: listCapabilities(true, false), submitOnChange: true
                }
                if (settings["condCap$id"]) {
                	def capability = getCapabilityByDisplay(settings["condCap$id"])
                    if (capability) {
                        def devices = settings["condDevices$id"]
                		input "condDevices$id", "capability.${capability.name}", title: "${capability.display} list", required: false, state: (devices ? "complete" : null), multiple: capability.multiple, submitOnChange: true
						if (devices && devices.size()) {
							if (devices.size() > 1) {
                    			input "condMode$id", "enum", title: "Evaluation mode", options: ["Any", "All"], required: true, multiple: false, defaultValue: "All", submitOnChange: true
                            }
                            def evalMode = settings["condMode$id"] == "All" ? "All" : "Any"
                            
                            //Attribute
                            def attribute = cleanUpAttribute(settings["condAttr$id"])
                            if (attribute == null) {
                            	attribute = capability.attribute
                            }
                            //display the Attribute only in expert mode or in basic mode if it differs from the default capability attribute
                            if ((attribute != capability.attribute) || settings["expert"]) {
								input "condAttr$id", "enum", title: "Attribute", options: listCommonDeviceAttributes(devices), required: true, multiple: false, defaultValue: capability.attribute, submitOnChange: true
                            }
                            if (attribute) {                              
                            	//Condition
	                           	def attr = getAttributeByName(attribute)
                                def comparison = cleanUpComparison(settings["condComp$id"])
                    			input "condComp$id", "enum", title: "Comparison", options: listComparisonOptions(attribute, true), required: true, multiple: false, submitOnChange: true                                
                                if (comparison) {                                	
                                	//Value
                                    def comp = getComparisonOption(attribute, comparison)
                                    if (attr && comp) {
                                    	if (comp.parameters >= 1) {
                                			input "condValue$id#1", title: (comp.parameters == 1 ? "Value" : "From"), attr.type, options: attr.options, range: attr.range, required: true, multiple: false, submitOnChange: true
                                        }
                                    	if (comp.parameters >= 2) {
                                			input "condValue$id#2", title: "Through", attr.type, options: attr.options, range: attr.range, required: true, multiple: false, submitOnChange: true
                                        }
                                    }
                                }
                    			//input "condDevice$id", "", title: title, required: true, multiple: false, submitOnChange: true
                            }
                        }
                    }
                }
			}

			section(title: (condition.trg ? "Trigger" : "Condition") + " Overview") {
                paragraph getConditionDescription(id)
	            href "pageActionGroup", params:["conditionId": id], title: "Individual actions", description: "Tap to set individual actions for this condition", state: complete, submitOnChange: true
			}

			section() {
				paragraph "NOTE: To delete this condition, simply remove all devices from the list above and tap Done"
            }
            
            section(title: "Advanced options", hideable: true, hidden: true) {
                input "condParent$id", "number", title: "Parent ID", range: "$pid..$pid", defaultValue: pid
			}
	    }
    }
}


def pageConditionVsTrigger() {
	dynamicPage(name: "pageConditionVsTrigger", title: "Conditions versus Trigers", uninstall: false, install: false) {
    	section() {
			paragraph "All SmartThingers are event-driven. This means that an action is taken whenever something happens while the SmartThinger is watching over. To do so, the SmartThinger subscribes to events from all the devices you use while building your 'If...' and - in case of latching SmartThingers - your 'But if...' statements as well. Since a SmartThinger subscribes to multiple device events, it is evaluated every time such an event occurs. Depending on your conditions, a device event may not necessarily make any change to the evaluated state of the SmartThinger (think OR), but the SmartThinger is evaluated either way, making it possible to execute actions even if the SmartThinger's status didn't change. More about this under the 'Then...' or 'Else...' sections of the SmartThinger." 
	        paragraph "Events tell SmartThingers something has changed. Depending on the logic you are trying to implement, sometimes you need to check that the state of a device is within a certain range, and sometimes you need to react to a device state reaching a certain value, list or range.\n\nLet's start with an example. Say you have a temperature sensor and you want to monitor its temperature. You want to be alerted if the temperature is over 100°F. Now, assume the temperature starts at 99°F and increases steadily at a rate of one degree Fahrenheit per minute.", title: "State vs. State Change"
            paragraph "If you use a condition, the SmartThinger will be evaluated every one minute, as the temperature changes. The first evaluation will result in a false condition as the temperature reaches 100°F. Remember, our condition is for the temperature to be OVER 100°F. The next minute, your temperature is reported at 101°F which will cause the SmartThinger to evaluate true this time. Your 'Then...' actions will now have a chance at execution. The next minute, as the temperature reaches 102°F, the SmartThinger will again evaluate true and proceed to executing your 'Then...' actions. This will happen for as long as the temperature remains over 100°F and will possibly execute your actions every time a new temperature is read that matches that condition. You could use this to pass the information along to another service (think IFTTT) or display it on some sort of screen. But not for turning on a thermostat - you don't neet to turn the thermostat on every one minute, it's very likely already on from your last execution.", title: "Using a Condition"
            paragraph "If you use a trigger, the SmartThinger will now be on the lookout for a certain state change that 'triggers' our evaluation to become true. You will no longer look for a temperature over 100°F, but instead you will be looking for when the temperature exceeds 100°F. This means your actions will only be executed when the temperature actually transitioned from below or equal to 100°F to over 100°F. This means your actions will only execute once and for the SmartThinger to fire your actions again, the temperature would have to first drop at or below 100°F and then raise again to exceed your set threshold of 100°F. Now, this you could use to control a thermostat, right?", title: "Using a Trigger"
		}
    }
}

def pageActionGroup() {
	def conditionId = params?.conditionId
	def value = (conditionId < 0) && (settings.mode == "Simple") ? "false" : "true"
	dynamicPage(name: "pageActionGroup", title: "Actions", uninstall: false, install: false) {
    	section() {
        	paragraph "Add actions below to be executed once, whenever the evaluation of your condition(s) changes to '$value'", title: "Do..."
            href "pageAction", params:["command": "add", "conditionId": id, "branch": "do"], title: "Add an action", description: "Actions allow control of various devices in your ecosystem", required: true, state: "complete", submitOnChange: true
        }
    	section() {
        	paragraph "Add actions below to be executed every time the evaluation of your condition(s) is '$value'", title: "Do While..."
            paragraph "CAUTION: Only use this section if you know what you are doing. Because evaluations may happen whenever various attributes of various devices involved in your condition(s) change, actions in this list may be executed very often and may therefore yield unexpected results\n\nYE BE WARNED!", required: true, state: null
            href "pageAction", params:["command": "add", "conditionId": id, "branch": "doWhile"], title: "Add an action", description: "Actions allow control of various devices in your ecosystem", required: true, state: "complete", submitOnChange: true
        }
        
    }
}

/* prepare configuration version of app */
def configApp() {
	//TODO: rebuild (object-oriented) app object from settings
	//state.config = null
	if (!state.config) {
    	//initiate config app, since we have no running version yet (not yet installed)
        state.config = [:]
        state.config.conditionId = 0
    	state.config.app = [:]
    	state.config.app.description = "cough"
        //create the root condition
    	state.config.app.conditions = createCondition(true)
        state.config.app.conditions.id = 0
    	state.config.app.otherConditions = createCondition(true)
        state.config.app.otherConditions.id = -1
    	state.config.app.actions = [:]
    	state.config.app.actions.whenTrue = []
    	state.config.app.actions.whileTrue = []
    	state.config.app.actions.whenFalse = []
    	state.config.app.actions.whileFalse = []
    }
}





def comparisons() {
	return [
    	[
        	type: "string",
            options: [
            	[ condition: "is", trigger: "changes to", parameters: 1],
            	[ condition: "is not", trigger: "changes away from", parameters: 1],
            	[ trigger: "changes", parameters: 0],
            ],
		],
    	[
        	type: "enum",
            options: [
            	[ condition: "is", trigger: "changes to", parameters: 1],
            	[ condition: "is not", trigger: "changes away from", parameters: 1],
            	[ trigger: "changes", parameters: 0],
            ],
		],
    	[
        	type: "number",
            options: [
            	[ trigger: "changes", parameters: 0],
            	[ condition: "is equal to", trigger: "changes to", parameters: 1],
                [ condition: "is not equal to", trigger: "changes away from", parameters: 1],
                [ condition: "is less than", trigger: "drops below", parameters: 1],
                [ condition: "is less than or equal to", trigger: "drops to or below", parameters: 1],
                [ condition: "is greater than", trigger: "raises above", parameters: 1],
                [ condition: "is greater than or equal to", trigger: "raises to or above", parameters: 1],
                [ condition: "is in range", trigger: "enters range", parameters: 2],
                [ condition: "is outside range", trigger: "exits range", parameters: 2],
                [ condition: "is even", trigger: "changes to an even value", parameters: 0],
                [ condition: "is odd", trigger: "changes to an odd value", parameters: 0],
			],
		],
    	[
        	type: "decimal",
            options: [
            	[ trigger: "changes", parameters: 0],
            	[ condition: "is equal to", trigger: "changes to", parameters: 1],
                [ condition: "is not equal to", trigger: "changes away from", parameters: 1],
                [ condition: "is less than", trigger: "drops below", parameters: 1],
                [ condition: "is less than or equal to", trigger: "drops to or below", parameters: 1],
                [ condition: "is greater than", trigger: "raises above", parameters: 1],
                [ condition: "is greater than or equal to", trigger: "raises to or above", parameters: 1],
                [ condition: "is in range", trigger: "enters range", parameters: 2],
                [ condition: "is outside range", trigger: "exits range", parameters: 2],
                [ condition: "is even", trigger: "changes to an even value", parameters: 0],
                [ condition: "is odd", trigger: "changes to an odd value", parameters: 0],
			],
		],
    	[
        	type: "time",
            options: [
            	[ condition: "is", trigger: "reaches", parameters: 1],
                [ condition: "is before", parameters: 1],
                [ condition: "is after", parameters: 1],
                [ condition: "is between", parameters: 2],
            ],
		],        
    ]
}

def listCapabilities(requireAttributes, requireCommands) {
    def result = []
    for (capability in capabilities()) {
    	if ((requireAttributes && capability.attribute) || (requireCommands && capability.commands) || !(requireAttributes || requireCommands)) {
	    	result.push(capability.display)
		}
    }
    return result
}


def listAttributes() {
    def result = []
    for (attribute in attributes()) {
    	result.push(attribute.name)
    }
    return result.sort()
}

def listComparisonOptions(attributeName, allowTriggers) {
    def conditions = []
    def triggers = []
    def attribute = getAttributeByName(attributeName)
    if (attribute) {
        def attributeType = attribute.type
        for (comparison in comparisons()) {
            if (comparison.type == attributeType) {
                for (option in comparison.options) {
                    if (option.condition) {
                        conditions.push(conditionPrefix() + option.condition)
                    }
                    if (allowTriggers && option.trigger) {
                        triggers.push(triggerPrefix() + option.trigger)
                    }
                }
            }
        }
    }
    return conditions.sort() + triggers.sort()
}

def getComparisonOption(attributeName, comparisonOption) {
    def attribute = getAttributeByName(attributeName)
    if (attribute) {
		def attributeType = attribute.type
        for (comparison in comparisons()) {
            if (comparison.type == attributeType) {
                for (option in comparison.options) {
                    if (option.condition == comparisonOption) {
                        return option
                    }
                    if (option.trigger == comparisonOption) {
                    	return option
                    }
                }
            }
        }
    }
    return null	
}

def isComparisonOptionTrigger(attributeName, comparisonOption) {
    def attribute = getAttributeByName(attributeName)
    if (attribute) {
		def attributeType = attribute.type
        for (comparison in comparisons()) {
            if (comparison.type == attributeType) {
                for (option in comparison.options) {
                    if (option.condition == comparisonOption) {
                        return false
                    }
                    if (option.trigger == comparisonOption) {
                    	return true
                    }
                }
            }
        }
    }
    return null	
}

/*
def listCapabilityValues(capability, triggerAllowed) {
    def result = []
    for (value in capability.values) {
    	if (!value.trigger || triggerAllowed) {
	    	result.push(value.name)
        }
    }
    return result.sort()
}
*/

def listCommonDeviceAttributes(devices) {
	def list = [:]
	for (attribute in attributes()) {
    	list[attribute.name] = 0
    }

    for (device in devices) {
    	def attrs = device.supportedAttributes
        for (attr in attrs) {
       		list[attr.name] = list.containsKey(attr.name) ? list[attr.name] + 1 : -100
        }
    }
    log.debug list
    def result = []
    def custom = []
    for (item in list) {
    	//ZWave Lock reports lock twice - others may do the same, so let's allow multiple instances
    	if (item.value >= devices.size()) {
        	result.push(item.key)
        } else {
        	if (item.value < 0) {
            	custom.push(customAttributePrefix() + item.key)
            }
        }
    }
    return result.sort() + custom.sort()
}

def getCapabilityByName(name) {
    for (capability in capabilities()) {
    	if (capability.name == name) {
        	return capability
        }
    }
    return null
}

def getCapabilityByDisplay(display) {
    for (capability in capabilities()) {
    	if (capability.display == display) {
        	return capability
        }
    }
    return null
}

def getAttributeByName(name) {
    for (attribute in attributes()) {
    	if (attribute.name == name) {
        	return attribute
        }
    }
    return [ name: name, type: "string", range: null, unit: null, options: null]
}

/*
def getCapabilityValueByDisplayAndName(display, valueName) {
    for (capability in capabilities()) {
    	if (capability.display == display) {
        	for (value in capability.values) {
    			if (value.name == valueName) {
                	return value
    			}
            }
        }
    }
    return null
}
*/


def buildDeviceNameList(devices, suffix) {
	def cnt = 1
    def result = ""
	for (device in devices) {
        result += device?.label + (cnt < devices.size() ? (cnt == devices.size() - 1 ? " $suffix " : ", ") : "")
        cnt++
    }
    return result;
}


def buildIfContent() {
	buildIfContent(state.config.app.conditions.id, 0)
}

def buildIfOtherContent() {
	buildIfContent(state.config.app.otherConditions.id, 0)
}

def buildIfContent(id, level) {
	def condition = getCondition(id)
    if (!condition) {
    	return null
    }
    def conditionGroup = (condition.children != null)
    def conditionType = (condition.trg ? "trigger" : "condition")
    level = (level ? level : 0)
    def pre = ""
    def preNot = ""
    def tab = ""
    def aft = ""
    switch (level) {
    	case 1:
        	pre = " ┌ ("
        	preNot = " ┌ NOT ("
        	tab = " │   "
        	aft = " └ )"
	        break;
        case 2:
    	    pre = " │ ┌ ["
    	    preNot = " │ ┌ NOT ["
        	tab = " │ │   "
       		aft = " │ └ ]"
        	break;
        case 3:
	        pre = " │ │ ┌ <"
	        preNot = " │ │ ┌ NOT {"
        	tab = " │ │ │   "
    	    aft = " │ │ └ >"
        	break;
    }
	if (!conditionGroup) {
		href "pageCondition", /*image: "https://raw.githubusercontent.com/ady624/SmartThingers/master/resources/images/${conditionType}.png",*/ params: ["conditionId": id], title: "", description: tab + getConditionDescription(id).trim(), state: "complete", required: false, submitOnChange: false
    } else {
    
        def grouping = settings["condGrouping$id"]
        def negate = settings["condNegate$id"]
    
    	if (pre) {
			href "pageConditionGroupL${level}", /*image: "https://raw.githubusercontent.com/ady624/SmartThingers/master/resources/images/folder.png",*/ params: ["conditionId": id], title: "", description: (negate? preNot : pre), state: "complete", required: true, submitOnChange: false
        }
        
        def cnt = 0
        for (child in condition.children) {
        	buildIfContent(child.id, level + (child.children == null ? 0 : 1))
            cnt++            
            if (cnt < condition.children.size()) {
            	def page = (level ? "pageConditionGroupL${level}" : (id == 0 ? "pageIf" : "pageIfOther"))
            	href page, /*image: "https://raw.githubusercontent.com/ady624/SmartThingers/master/resources/images/" + (level ? "folder.png" : "blank.png"),*/ params: ["conditionId": id], title: "", description: tab + grouping, state: "complete", required: true, submitOnChange: false
            }
        }
        
        if (aft) {
			href "pageConditionGroupL${level}", /*image: "https://raw.githubusercontent.com/ady624/SmartThingers/master/resources/images/folder.png",*/ params: ["conditionId": id], title: "", description: aft, state: "complete", required: true, submitOnChange: false
        }
    }
}

def cleanUpAttribute(attribute) {
	if (attribute) {
		return attribute.replace(customAttributePrefix(), "")
    }
    return null
}

def cleanUpComparison(comparison) {
	if (comparison) {
		return comparison.replace(triggerPrefix(), "").replace(conditionPrefix(), "")
    }
    return null
}

def getConditionDescription(id) {
	return getConditionDescription(id, 0)
}

def getConditionDescription(id, level) {
	def condition = getCondition(id)
    def pre = ""
    def preNot = ""
    def tab = ""
    def aft = ""
    def conditionGroup = (condition.children != null)
    switch (level) {
    	case 1:
        	pre = " ┌ ("
        	preNot = " ┌ NOT ("
        	tab = " │   "
        	aft = " └ )"
	        break;
        case 2:
    	    pre = " │ ┌ ["
    	    preNot = " │ ┌ NOT ["
        	tab = " │ │   "
       		aft = " │ └ ]"
        	break;
        case 3:
	        pre = " │ │ ┌ <"
	        preNot = " │ │ ┌ NOT {"
        	tab = " │ │ │   "
    	    aft = " │ │ └ >"
        	break;
    }
    if (!conditionGroup) {
    	//single condition
        def devices = settings["condDevices$id"]
        if (devices && devices.size()) {
            def evaluation = (devices.size() > 1 ? (condition.mode == "All" ? "Each of " : "Any of ") : "")
            def deviceList = buildDeviceNameList(devices, "or")
            def attribute = condition.attr
            def attr = getAttributeByName(attribute)
            def unit = (attr && attr.unit ? attr.unit : "")
            def comparison = cleanUpComparison(condition.comp)
            def comp = getComparisonOption(attribute, comparison)
            def values = " [ERROR]"
            if (comp) {
            	switch (comp.parameters) {
                	case 0:
                    	values = ""
                        break
                    case 1:
                    	values = " " + condition.val1 + unit
                        break
                    case 2:
                    	values = " " + condition.val1 + unit + " - " + condition.val2 + unit
                        break                       
            	}
            }
            return tab + (condition.trg ? triggerPrefix() : conditionPrefix()) + evaluation + deviceList + " " + attribute + " " + comparison + values
        }
        return "Sorry, incomplete rule"
	} else {
    	//condition group
        def grouping = settings["condGrouping$id"]
        def negate = settings["condNegate$id"]
        def result = (negate ? preNot : pre) + "\n"
        def cnt = 1
        for (child in condition.children) {
        	result += getConditionDescription(child.id, level + (child.children == null ? 0 : 1)) + "\n" + (cnt < condition.children.size() ? tab + grouping + "\n" : "")
            cnt++
        }
        result += aft
        return result
    }
}


//used to get the next id for a condition, action, etc - looks into settings to make sure we're not reusing a previously used id
def getNextId(collection, prefix) {
	def nextId = _getLastId(collection) + 1
    while (settings.findAll { it.key == prefix + "Parent" + nextId }) {
    	nextId++
    }
    return nextId
}

//helper function for getNextId
private _getLastId(parent) {
	if (!parent) {
    	return -1
    }
	def lastId = parent?.id    
    for (child in parent.children) {
        def childLastId = _getLastId(child)
        lastId = lastId > childLastId ? lastId : childLastId
    }
    return lastId
}


//creates a condition (grouped or not)
def createCondition(group) {
    def condition = [:]
    //give the new condition an id
    condition.id = getNextId(state.config.app.conditions, 'cond')
    //initiate the condition type
    if (group) {
    	//initiate children
        condition.children = []
        condition.actions = []
    } else {
    	condition.type = null
    }
    return condition
}

//creates a condition and adds it to a parent
def createCondition(parentConditionId, group) {	
    def parent = getCondition(parentConditionId)
    if (parent) {
		def condition = createCondition(group)
    	//preserve the parentId so we can rebuild the app from settings
    	condition.parentId = parent ? parent.id : null
        //calculate depth for new condition
        condition.level = (parent.level ? parent.level : 0) + 1
   		//add the new condition to its parent, if any
        //set the parent for upwards traversal
   		parent.children.push(condition)
    	//return the newly created condition
    	return condition
	}
    return null
}

//deletes a condition
def deleteCondition(conditionId) {
	def condition = getCondition(conditionId)
    if (condition) {
    	def parent = getCondition(condition.parentId)
        if (parent) {
			parent.children.remove(condition);
        }
    }
}

//helper function for _cleanUpConditions
def _cleanUpCondition(condition, deleteGroups) {
	def result = false

	if (condition.children) {
    	//we cannot use a for each due to concurrent modifications
        //we're using a while instead
        def deleted = true
        while (deleted) {
        	deleted = false
			for (def child in condition.children) {
            	deleted = _cleanUpCondition(child, deleteGroups)
	    		result = result || deleted
                if (deleted) {
                	break
                }
            }
		}
    }

	//if non-root condition
	if (condition.id > 0) {
    	if (condition.children == null) {
        	//if regular condition
        	if (settings["condDevices${condition.id}"] == null) {
	        	deleteCondition(condition.id);
	            return true
	        } else {
            	_updateCondition(condition)
            }
        } else {
        	//if condition group
        	if (deleteGroups && (condition.children.size() == 0)) {
	        	deleteCondition(condition.id);
	            return true
	        }
        }
    }
    return result
}

def _updateCondition(condition) {
	condition.capability = settings["condCap${condition.id}"]
	//condition.dev = settings["condDevices${condition.id}"]
	condition.mode = settings["condMode${condition.id}"]
    condition.attr = cleanUpAttribute(settings["condAttr${condition.id}"])
    if (!condition.attr) {
	    def cap = getCapabilityByDisplay(condition.capability)
    	condition.attr = cap.attribute
    }
    condition.comp = cleanUpComparison(settings["condComp${condition.id}"])
    condition.trg = isComparisonOptionTrigger(condition.attr, condition.comp)
    condition.val1 = settings["condValue${condition.id}#1"]
    condition.val2 = settings["condValue${condition.id}#2"]
    return null
}

//cleans up conditions - this may be replaced by a complete rebuild of the app object from the settings
def cleanUpConditions(deleteGroups) {
	//go through each condition in the state config and delete it if no associated settings exist
    _cleanUpCondition(state.config.app.conditions, deleteGroups)
    _cleanUpCondition(state.config.app.otherConditions, deleteGroups)
}

//finds and returns the condition object for the given condition Id
private _traverseConditions(parent, conditionId) {
    if (parent.id == conditionId) {
        return parent
    }
    for (condition in parent.children) {
        def result = _traverseConditions(condition, conditionId)
        if (result) {
            return result
        }
    }
    return null
}

//returns a condition based on its ID
def getCondition(conditionId) {
	def result = null
    if (state.config.app.conditions) {
    	result =_traverseConditions(state.config.app.conditions, conditionId)
    }
    if (!result && state.config.app.otherConditions) {
    	result = _traverseConditions(state.config.app.otherConditions, conditionId)
    }
	return result
}


//blah blah functions that come with ST
def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    //subscribe(app, appHandler)
}

def appHandler() {
}
// TODO: implement event handlers