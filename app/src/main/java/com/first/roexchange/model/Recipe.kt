package com.first.roexchange.model

class Recipe {
    var name: String? = null
    var images: String? = null
    var station: String? = null
    var iText: String? = null
    var iImage1: String? = null
    var iImage2: String? = null
    var iImage3: String? = null
    var iImage4: String? = null
    var iImage5: String? = null
    var stars: String? = null
    var ingredients = ArrayList<String>()

    fun Recipe(name: String?, images: String?, station: String?, iText: String?, iImage1: String?, iImage2: String?, iImage3: String?, iImage4: String?, iImage5: String?, stars: String?, ingredients: ArrayList<String>?) {
        this.name = name
        this.images = images
        this.station = station
        this.iText = iText
        this.iImage1 = iImage1
        this.iImage2 = iImage2
        this.iImage3 = iImage3
        this.iImage4 = iImage4
        this.iImage5 = iImage5
        this.stars = stars
        this.ingredients = ingredients!!
    }
}