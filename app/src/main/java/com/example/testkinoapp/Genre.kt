package com.example.testkinoapp

public class Genre(var title_genr: String,var type_genr: Int,var id_genr: Long): Item{

    fun setTitle(title: String){
        title_genr = title
    }

    fun getTitle(): String{
        return title_genr
    }

    fun setType(type: Int){
        type_genr = type
    }

    fun setId(id: Long){
        id_genr = id
    }

    override fun getItemType(): Int {
        return type_genr
    }

    override fun getId(): Long {
        return id_genr
    }
}