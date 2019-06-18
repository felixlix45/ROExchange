package com.first.roexchange.model

class Monster {
    var name: String? = null
    var image: String? = null
    var race : String? = null
    var element: String? = null
    var size: String? = null
    var lvl: String? = null

    fun Monster(name: String?, image: String?, race: String?, element: String?, size: String?, lvl: String?) {
        this.name = name
        this.image = image
        this.race = race
        this.element = element
        this.size = size
        this.lvl = lvl
    }
}
