package com.vikeshpatil.todoapp

class TodoItem {

    companion object Factory{
        fun create(): TodoItem = TodoItem()
    }

    var description: String? = null
    var isCompleted: Boolean? = null
    var objectId: Int? = null

//    constructor(objectId:Int, description: String, isCompleted: Boolean = false){
//        this.description = description
//        this.isCompleted = isCompleted
//        this.objectId = objectId
//    }

}