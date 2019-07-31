package com.first.roexchange.model

class Item {
    var name: String? = null
    var type: Int = 0
    var lastPrice: Int = 0
    var lastDate: String? = null
    var types: String? = null

    fun Item(types: String) {
        this.types = types
    }

    fun Item(name: String, type: Int, lastPrice: Int, lastDate: String) {
        this.name = name
        this.type = type
        this.lastPrice = lastPrice
        this.lastDate = lastDate
    }


}
