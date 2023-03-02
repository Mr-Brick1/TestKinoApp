package com.example.testkinoapp

import java.time.Year

class Film(var localized_name: String,
           var image_url: String?,
           var type_film: Int,
           var id_film: Long,
           var foreignName: String,
           var year: Int,
           var rating: Float,
           var description: String,
           var genres: ArrayList<String>
           ): Item {

    fun setLocName(locname: String){
        localized_name = locname
    }

    fun getName(): String {
        return localized_name
    }

    fun setUrl(url: String?){
        image_url = url
    }

    fun getUrl(): String?{
        return image_url
    }

    fun setType(type: Int){
        type_film = type
    }

    fun setId(id: Long){
        id_film = id
    }

    override fun getItemType(): Int {
        return type_film
    }

    override fun getId(): Long {
        return id_film
    }
}