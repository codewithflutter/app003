package com.vikeshpatil.noteapp

class Note{

    var noteId:Int? = null
    var noteTitle: String? = null
    var noteDesc: String? = null

    constructor(noteId:Int, noteTitle:String, noteDesc: String){
        this.noteId = noteId
        this.noteTitle = noteTitle
        this.noteDesc = noteDesc

    }

}