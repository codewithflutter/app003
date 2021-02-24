package com.vikeshpatil.todoapp

interface ItemRowListener {

    fun ModifyItemState(item: TodoItem, itemObjectId: Int, isDone: Boolean)
    fun onItemDelete(itemObjectId: Int)
}