package com.vikeshpatil.mediaplayer

import android.icu.text.CaseMap

class Song{

    var title:String? = null
    var artist:String? = null
    var songUrl:String? = null

    constructor(title: String, artist:String, songUrl:String){
        this.title = title
        this.artist = artist
        this.songUrl = songUrl
    }
}