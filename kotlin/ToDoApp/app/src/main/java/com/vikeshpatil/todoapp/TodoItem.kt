package com.vikeshpatil.todoapp

class TodoItem {
    var description: String? = null
    var isCompleted: Boolean? = null

    constructor(description: String, isCompleted: Boolean = false){
        this.description = description
        this.isCompleted = isCompleted
    }

}