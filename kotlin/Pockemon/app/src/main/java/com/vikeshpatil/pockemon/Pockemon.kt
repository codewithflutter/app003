package com.vikeshpatil.pockemon

import android.location.Location


class Pockemon{
    var name:String? = null
    var description:String? = null
    var image:Int? = null
    var power:Double? = null
    var isCaught:Boolean? = false
    var location:Location? = null

    constructor(name:String, description:String, image:Int, power:Double, lat:Double, lon:Double){
        this.name = name
        this.description = description
        this.image = image
        this.power = power
        this.location = Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = lon
        this.isCaught = false
    }

}