package com.vikeshpatil.todoapp

class TodoItem {
    var description: String? = null
    var isCompleted: Boolean? = null
    var objectId: Int? = null
    constructor(objectId:Int, description: String, isCompleted: Boolean = false){
        this.description = description
        this.isCompleted = isCompleted
        this.objectId = objectId
    }

}