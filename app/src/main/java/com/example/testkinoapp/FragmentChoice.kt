package com.example.testkinoapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testkinoapp.databinding.FragmentChoiceBinding
import com.example.testkinoapp.retrofit.FilmApi
import com.example.testkinoapp.retrofit.Films
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentChoice : Fragment(), Adapter.Listener {

    lateinit var onClickImage: ChangeFragment

    var Items: ArrayList<Item> = arrayListOf()
    lateinit var binding: FragmentChoiceBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onClickImage = context as ChangeFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChoiceBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        init()


        super.onViewCreated(view, savedInstanceState)
    }

    fun init(){
        val recycler: RecyclerView = binding.recyclerView12
        val glmanager = GridLayoutManager(requireContext(),2) // Layout с двумя колонками----------------------------------------------------

        var uniqueSortedGenres: List<String> // Уникальный список жанров

        CoroutineScope(Dispatchers.IO).launch {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://s3-eu-west-1.amazonaws.com/sequeniatesttask/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val filmApi = retrofit.create(FilmApi::class.java)

            val response = filmApi.getNestedFilms()

            withContext(Dispatchers.Main) {

                val items = response.body()?.films
                val Allgenres: MutableList<String> = mutableListOf() // Все имеющиеся жанры


                if(items != null){
                    for (i in 0 until items.count()){
                        Allgenres += items[i].genres

                    }

                } else{
                    Log.e("RETROFIT_EROR", response.toString())
                }

                val SortedGenres = Allgenres.sorted() // Сортируется по алфавиту ( с повторами )

                val Unique = LinkedHashSet<String>(SortedGenres) // // Отсортированный список с уникальными значениями (без повторов)

                uniqueSortedGenres = Unique.toList()

                Fill_List(uniqueSortedGenres,items) // Заполнение списка


                val adapter = Adapter(Items,this@FragmentChoice,onClickImage)//-------------------------------------------------------------------------------

                val onSpanSizeLookup: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() { // Различное отображение элемнтов в зависимости от типа элемента
                    override fun getSpanSize(position: Int): Int {

                        if (adapter.getItemViewType(position) == 0 ) { return 2 } // Если заголовок, то во всю ширину

                        if (adapter.getItemViewType(position) == 1) { return 2 } // Если жанр, то во всю ширину
                        else{ return 1 }// Если фильм то в пол-ширины
                    }
                }

                glmanager.spanSizeLookup = onSpanSizeLookup;

                recycler.layoutManager = glmanager

                recycler.adapter = adapter
            }
        }
    }

    private fun Fill_List(UniqueSortedGenres: List<String>, ListFilms: List<Films>?){

        Items.add(Genre("Жанры",0,0)) // Заголовок "Жанры"

        // Добавление в список уникальных, отсортированных по алфавиту элементов жанра
        for (i in 0 until UniqueSortedGenres.size)
        {
            Items.add(
                Genre(
                    UniqueSortedGenres[i].substring(0,1).uppercase() + UniqueSortedGenres[i].substring(1),
                    1,
                    1 + i.toLong()))
        }
        Items.add(Genre("Фильмы",0,(Items.size + 1).toLong())) // Заголовок "Фильмы"

        if (ListFilms!=null) {
            for (i in 0 until ListFilms.count()) { // Добавление элементов в список фильмов (localized_name, image_url)
                Items.add(
                    Film(ListFilms[i].localized_name, ListFilms[i].image_url ?: "N/A", 2, (i+UniqueSortedGenres.size).toLong(),ListFilms[i].name ?: "N/A",
                        ListFilms[i].year,ListFilms[i].rating ?: 0.0F,ListFilms[i].description ?: "N/A",
                        (ListFilms[i].genres as ArrayList<String> ?: "N/A") as ArrayList<String>
                    ))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentChoice()
    }

    override fun onClickInfo() {

    }
}