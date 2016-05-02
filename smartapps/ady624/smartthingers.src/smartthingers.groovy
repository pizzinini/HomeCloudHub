/**
 *  SmartThingers
 *
 *  Copyright 2016 Adrian Caramaliu
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
 *  Version history
 *   5/02/2016 >>> v0.0.002.20160501 - Alpha test version - added latching rules
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
    page(name: "pageIfLatching")
    page(name: "pageThen")
    page(name: "pageElse")
    page(name: "pageCondition")
    page(name: "pageConditionGroupL1")
    page(name: "pageConditionGroupL2")
    page(name: "pageConditionGroupL3")
    page(name: "pageConditionVsTrigger")
}

def pageMain() {
	configApp()
    cleanUpConditions(true)    
	dynamicPage(name: "pageMain", title: "SmartThinger Application", uninstall: true, install: true) {
    	section() {
        	label title: "Name", required: true
        	input "description", "string", title: "Description", required: false, defaultValue: "test"
            input "latching", "bool", title: "Latching SmartThinger", required: true, defaultValue: false, submitOnChange: true
            paragraph "A latching SmartThinger - also known as a bi-stable SmartThinger - uses one set of conditions to achieve a 'true' state and a second set of conditions to revert back to its 'false' state"
        }
        
        section() {
			href "pageSimulate", title: "Simulate", description: "Allows you to test the actions manually", state: complete
        }
        
        section() {
			href "pageIf", title: "If...", description: (state.config.app.conditions.children.size() ? "" : "Tap to select conditions")
            buildIfContent()
        }
        
        section() {
			href "pageThen", title: "Then...", description: "Choose what should happen then", state: null, submitOnChange: false
        }
        
        if (settings.latching) {
            section() {
                href "pageIfLatching", title: "But if...", description: (state.config.app.latchingConditions.children.size() ? "" : "Tap to select conditions")
                buildIfLatchingContent()
            }
        }
        
        section() {
			href "pageElse", title: (settings.latching ? "Then..." : "Else..."), description: "Choose what should happen otherwise", state: null, submitOnChange: false

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

def pageIfLatching(params) {
    cleanUpConditions(false)
	def condition = state.config.app.latchingConditions
    dynamicPage(name: "pageIfLatching", title: "Main Condition Group", uninstall: false, install: false) {
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
    cleanUpConditions()
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
    	dynamicPage(name: "pageConditionGroupL$level", title: "Condition Group $id (level $level)", uninstall: false, install: false) {
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
                if (c.children != null) {
                    href "pageConditionGroupL${nextLevel}", params: ["conditionId": cid], title: "Condition Group $cid", description: getConditionDescription(cid), state: null, required: false, submitOnChange: false
                } else {
                    href "pageCondition", params: ["conditionId": cid], title: "Condition $cid", description: getConditionDescription(cid), state: null, required: false, submitOnChange: false
                }
                cnt++
            }
        	if (settings["condNegate$id"]) {
                paragraph ")", state: "complete"
            }
        }
        section() {
            href "pageCondition", params:["command": "add", "parentConditionId": id], title: "Add a condition", description: "A condition watches the state of one or multiple similar devices", state: complete, submitOnChange: true
            href "pageCondition", params:["command": "add", "parentConditionId": id], title: "Add a trigger", description: "A trigger is a specialised condition that watches for state changes of one or multiple similar devices", state: complete, submitOnChange: true
            if (nextLevel <= 3) {
            	href "pageConditionGroupL${nextLevel}", params:["command": "add", "parentConditionId": id], title: "Add a group", description: "A group is a container for multiple conditions and/or triggers, allowing for more complex logical operations, such as evaluating [A AND (B OR C)]", state: complete, submitOnChange: true
            }
        }

        section() {
            href "pageConditionVsTrigger", title: "Uh oh, which should I use?", description: "Find out when to use a condition and when to use a trigger", state: complete, submitOnChange: false
        }

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
    	dynamicPage(name: "pageCondition", title: "Condition #$id", uninstall: false, install: false) {
			section() {
            	if (!settings["condDevices$id"] || (settings["condDevices$id"].size() == 0)) {
                	//only display capability selection if no devices already selected
	            	input "condCap$id", "enum", title: "Capability", options: listCapabilities(), submitOnChange: true
                }
                if (settings["condCap$id"]) {
                	def capability = getCapabilityByDisplay(settings["condCap$id"])
                    if (capability) {
                		input "condDevices$id", "capability.${capability.name}", title: "${capability.display} list", required: false, multiple: capability.multiple, submitOnChange: true
                        def devices = settings["condDevices$id"]
                        if (devices && devices.size()) {
	                    	paragraph "NOTE: To delete this condition, simply remove all devices from the list above and tap Done"
                        	if (devices.size() > 1) {
                    			input "condMode$id", "enum", title: "Evaluation mode", options: ["Any", "All"], required: true, multiple: false, defaultValue: "All", submitOnChange: true
                            }
                            def evalMode = settings["condMode$id"] == "All" ? "All" : "Any"
                            def title = (devices.size() > 1 ? (evalMode == "All" ? "Each of " : "Any of ") : "") + buildDeviceNameList(devices, "or") + "..."
                            def momentary = (devices.size() == 1) || (evalMode == "Any")
                    		input "condValues$id", "enum", title: title, options: listCapabilityValues(capability, momentary), required: true, multiple: false, submitOnChange: true
                        }
                    }
                }
			}
            section(title: "Condition Overview") {
                paragraph getConditionDescription(id)
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
    	state.config.app.latchingConditions = createCondition(true)
        state.config.app.latchingConditions.id = -1
    	state.config.app.actions = [:]
    	state.config.app.actions.whenTrue = []
    	state.config.app.actions.whileTrue = []
    	state.config.app.actions.whenFalse = []
    	state.config.app.actions.whileFalse = []
    }
}




def capabilities() {
	return [
    	[
        	name: "contactSensor",
        	display: "Contact Sensor",
            attribute: "contact",
            dataType: "string",
            multiple: true,
            values: [
            	[ name: "is open", value: "open", momentary: false, singular: "is open", plural: "are all open" ],
            	[ name: "is closed", value: "closed", momentary: false, singular: "is closed", plural: "are all open" ],
            	[ name: "opens", value: "open", momentary: true, singular: "opens" ],
            	[ name: "closes", value: "close", momentary: true, singular: "closes" ],
            ]
		],
    	[
        	name: "motionSensor",
        	display: "Motion Sensor",
            attribute: "motion",
            dataType: "string",
            multiple: true,
            values: [
            	[ name: "is active", value: "active", momentary: false, singular: "detects motion", plural: "all detect motion" ],
            	[ name: "is inactive", value: "inactive", momentary: false, singular: "does not detect motion", plural: "do not detect motion" ],
            	[ name: "becomes active", value: "active", momentary: true, singular: "opens" ],
            	[ name: "becomes inactive", value: "inactive", momentary: true, singular: "closes" ],
            ]
		],
    	[
        	name: "switch",
        	display: "Switch",
            attribute: "switch",
            dataType: "string",
            multiple: true,
            values: [
            	[ name: "is on", value: "on", momentary: false, singular: "is ", plural: "are all [name]" ],
            	[ name: "is off", value: "off", momentary: false, singular: "is [name]", plural: "are all [name]" ],
            	[ name: "turns on", value: "on", momentary: true, singular: "opens" ],
            	[ name: "turns off", value: "off", momentary: true, singular: "closes" ],                
            ]
		]
    ]
}

def listCapabilities() {
    def result = []
    for (capability in capabilities()) {
    	result.push(capability.display)
    }
    return result.sort()
}

def listCapabilityValues(capability, momentaryAllowed) {
    def result = []
    for (value in capability.values) {
    	if (!value.momentary || momentaryAllowed) {
	    	result.push(value.name)
        }
    }
    return result.sort()
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

def buildIfLatchingContent() {
	buildIfContent(state.config.app.latchingConditions.id, 0)
}

def buildIfContent(id, level) {
	def condition = getCondition(id)
    if (!condition) {
    	return null
    }
    def conditionGroup = (condition.children != null)
    level = (level ? level : 0)
    def pre = ""
    def aft = ""
    def tab = ""
    switch (level) {
    	case 1:
        	pre = "("
        	aft = ")"
        	tab = "\t"
	        break;
        case 2:
    	    pre = "\t["
       		aft = "\t]"
        	tab = "\t\t"
        break;
        case 3:
	        pre = "\t\t{"
    	    aft = "\t\t}"
        	tab = "\t\t\t"
        	break;
    }
	if (!conditionGroup) {
		href "pageCondition", params: ["conditionId": id], title: "", description: tab + getConditionDescription(id).trim(), state: null, required: false, submitOnChange: false
    } else {
    
        def grouping = settings["condGrouping$id"]
        def negate = settings["condNegate$id"]
    
    	if (pre) {
			href "pageConditionGroupL${level}", params: ["conditionId": id], title: "", description: pre, state: "complete", required: true, submitOnChange: false
        }
        
        def cnt = 0
        for (child in condition.children) {
        	buildIfContent(child.id, level + (child.children == null ? 0 : 1))
            cnt++            
            if (cnt < condition.children.size()) {
            	href "pageConditionGroupL${level}", params: ["conditionId": id], title: "", description: tab + grouping, state: "complete", required: true, submitOnChange: false
            	//paragraph tab + grouping, state: "complete"
            }
        }
        
        if (aft) {
			href "pageConditionGroupL${level}", params: ["conditionId": id], title: "", description: aft, state: "complete", required: true, submitOnChange: false
        }
    }
}

def getConditionDescription(id) {
	return getConditionDescription(id, 0)
}

def getConditionDescription(id, level) {
	def condition = getCondition(id)
    def pre = ""
    def aft = ""
    def tab = ""
    def conditionGroup = (condition.children != null)
    switch (level) {
        case 1:
        pre = "(\n"
        aft = ")"
        tab = "\t"
        break;
        case 2:
        pre = "\t[\n"
        aft = "\t]"
        tab = "\t\t"
        break;
        case 3:
        pre = "\t\t{\n"
        aft = "\t\t}"
        tab = "\t\t\t"
        break;
    }
    if (!conditionGroup) {
    	//single condition
        def devices = settings["condDevices$id"]
        if (devices && devices.size()) {
            def evalMode = settings["condMode$id"] == "All" ? "All" : "Any"
            return tab + (devices.size() > 1 ? (evalMode == "All" ? "Each of " : "Any of ") : "") + buildDeviceNameList(devices, "or") + " " + settings["condValues$id"]
        }
        return "Sorry, incomplete rule"
	} else {
    	//condition group
        def grouping = settings["condGrouping$id"]
        def negate = settings["condNegate$id"]
        def result = (negate ? "NOT " : "") + pre
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


//cleans up conditions - this may be replaced by a complete rebuild of the app object from the settings
def cleanUpConditions(deleteGroups) {
	//go through each condition in the state config and delete it if no associated settings exist
    _cleanUpCondition(state.config.app.conditions, deleteGroups)
    _cleanUpCondition(state.config.app.latchingConditions, deleteGroups)
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
    if (!result && state.config.app.latchingConditions) {
    	result = _traverseConditions(state.config.app.latchingConditions, conditionId)
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
}

// TODO: implement event handlers