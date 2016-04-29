/**
 *  SmartThinger
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
 *   4/29/2016 >>> v0.0.20160429 - Alpha test version
 *
 */
definition(
    name: "SmartThinger",
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
    page(name: "pageThen")
    page(name: "pageElse")
    page(name: "pageCondition")
    page(name: "pageConditionGroupL1")
    page(name: "pageConditionGroupL2")
    page(name: "pageConditionGroupL3")
}

def conditionId = 0


def pageMain() {
	configApp()
	dynamicPage(name: "pageMain", title: "SmartThinger Application", uninstall: true, install: true) {
    	section() {
        	label title: "Name", required: true
        	input "description", "string", title: "Description", required: false, defaultValue: "test"
        }
        
        section() {
			href "pageSimulate", title: "Simulate", description: "Execute the actions now", state: complete
        }
        
        section() {
			href "pageIf", title: "If...", description: "Choose your conditions", state: null
			href "pageThen", title: "Then...", description: "Choose what should happen then", state: null, submitOnChange: false
			href "pageElse", title: "Else...", description: "Choose what should happen otherwise", state: null, submitOnChange: false

		}
    }
}


def pageIf(params) {
	log.trace "pageIf()"
    log.info "params = $params"
    cleanUpConditions()
	def condition = state.config.app.conditions
    dynamicPage(name: "pageIf", title: "Main Condition Group", uninstall: false, install: false) {
    	getConditionGroupPageContent(params, condition)
    }
}

def getConditionGroupPageContent(params, condition) {	
	if (condition) {
        def id = condition.id
        def pid = condition.parentId ? condition.parentId : 0
        def nextLevel = (condition.level ? condition.level : 0) + 1
        def cnt = 0
        section() {
        	if (settings["condNegate$id"]) {
                paragraph "NOT ("
            }
            for (c in condition.children) {
            	if (cnt > 0) {
                	paragraph settings["condGrouping$id"]
                }
                def cid = c?.id
                if (c.children != null) {
                    href "pageConditionGroupL${nextLevel}", params: ["conditionId": cid], title: "Condition Group $cid", description: "Edit condition group", state: "error", required: true, submitOnChange: false
                } else {
                    href "pageCondition", params: ["conditionId": cid], title: "Condition $cid", description: "Edit condition", state: "complete", required: true, submitOnChange: false
                }
                cnt++
            }
        	if (settings["condNegate$id"]) {
                paragraph ")"
            }
        }
        section() {
            href "pageCondition", params:["command": "add", "parentConditionId": id], title: "Add a condition", description: "A condition performs a single evaluation of the state of one or multiple similar devices", state: complete, submitOnChange: true
            if (nextLevel <= 3) {
            	href "pageConditionGroupL${nextLevel}", params:["command": "add", "parentConditionId": id], title: "Add a condition group", description: "A condition group is a container for multiple conditions, allowing for more complex logical operations, such as evaluating [A AND (B OR C)]", state: complete, submitOnChange: true
            }
        }
		section(title: "Advanced options", hideable: true, hidden: true) {
           	input "condGrouping$id", "enum", title: "Grouping Method", description: "Choose the logical operation to be applied between all conditions in this group", options: ["AND", "OR", "XOR"], defaultValue: "AND", required: true, submitOnChange: true
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
    	dynamicPage(name: "pageCondition", title: "Conditions", uninstall: false, install: false) {
			section() {
	            input "condType$id", "enum", title: "Condition $id capability", options: ["a", "b", "c", "d"], submitOnChange: true
			}
            
            section(title: "Advanced options", hideable: true, hidden: true) {
                input "condParent$id", "number", title: "Parent ID", range: "$pid..$pid", defaultValue: pid
			}
	    }
    }
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
    	dynamicPage(name: "pageConditionGroupL$level", title: "Condition Group (level $level)", uninstall: false, install: false) {
	    	getConditionGroupPageContent(params, condition)
	    }
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


/* prepare configuration version of app */
def configApp() {
	//TODO: rebuild (object-oriented) app object from settings
	if (!state.config) {
    	//initiate config app, since we have no running version yet (not yet installed)
        state.config = [:]
        state.config.conditionId = 0
    	state.config.app = [:]
    	state.config.app.description = "cough"
        //create the root condition
    	state.config.app.conditions = createCondition(true)
    	state.config.app.actions = [:]
    	state.config.app.actions.whenTrue = []
    	state.config.app.actions.whileTrue = []
    	state.config.app.actions.whenFalse = []
    	state.config.app.actions.whileFalse = []
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
def _cleanUpCondition(condition) {
	if (condition.id > 0) {
    	if (settings["condParent${condition.id}"] == null) {
        	deleteCondition(condition.id);
            return true
        }
    }
    if (condition.children) {
    	def clear = false
    	while (!clear) {
        	clear = true
        	for (child in condition.children) {
	            if (_cleanUpCondition(child)) {
                	clear = false
	            	break
                }
    	    }    
		}
    }
    return false
}


//cleans up conditions - this may be replaced by a complete rebuild of the app object from the settings
def cleanUpConditions() {
	//go through each condition in the state config and delete it if no associated settings exist
    _cleanUpCondition(state.config.app.conditions)
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
    if (state.config.app.conditions) {
    	return _traverseConditions(state.config.app.conditions, conditionId)
    }
	return null
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