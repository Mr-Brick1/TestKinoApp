package com.example.testkinoapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.testkinoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

interface ChangeFragment{
    fun changeFragment(localized_name: String,
                       name: String,
                       year: Int,
                       rating: Float,
                       image_url: String?,
                       description: String,
                       genres: ArrayList<String>)
}

class MainActivity : AppCompatActivity(), ChangeFragment{

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Фильмы"
        binding.progressBar?.isVisible = true

        Loading()
    }

    fun Loading(){

        if (isOnline(this@MainActivity)) {
            binding.progressBar?.isVisible = true
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding.progressBar?.isVisible = false
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.place_holder, FragmentChoice.newInstance())
                        .commit()
                },
                4000)
        } else {
            binding.progressBar?.isVisible = true

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressBar?.isVisible = false


            val snackbar = Snackbar.make(binding.placeHolder, "Ошибка подключения сети", Snackbar.LENGTH_INDEFINITE)
                .setAction("ПОВТОРИТЬ", View.OnClickListener {
                    binding.progressBar?.isVisible = true
                    if (isOnline(this@MainActivity)){
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                binding.progressBar?.isVisible = false
                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.place_holder, FragmentChoice.newInstance())
                                    .commit()
                            },
                            2000)
                    }
                    else
                    {
                        binding.progressBar?.isVisible = true
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                binding.progressBar?.isVisible = false
                                val snackbar2 = Snackbar.make(binding.placeHolder, "Ошибка подключения сети", Snackbar.LENGTH_INDEFINITE).show()

                            }, 2000
                        )
                    }
                }).show()

            },2000)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            android.R.id.home -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.title = "Фильмы"

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.place_holder, FragmentChoice.newInstance())
                    .commit()
            }
        }
        return true
    }


    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun changeFragment(localized_name: String, name: String,
                                year: Int, rating: Float, image_url: String?,
                                description: String, genres: ArrayList<String>) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        val bundle = Bundle()
        bundle.putString("localized_name",localized_name)
        bundle.putString("name",name)
        bundle.putString("year",year.toString())
        bundle.putFloat("rating", rating)
        bundle.putString("image_url",image_url)
        bundle.putString("description", description)
        bundle.putStringArrayList("genres", genres)

        val fragment = (FragmentInfo.newInstance())
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.place_holder, fragment)
            .commit()
    }
}