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

interface ChangeFragment {
    fun changeFragment(
        localized_name: String,
        name: String,
        year: Int,
        rating: Float,
        image_url: String?,
        description: String,
        genres: ArrayList<String>
    )
}

class MainActivity : AppCompatActivity(), ChangeFragment {

    lateinit var binding: ActivityMainBinding
    private var retryHandler: Handler? = null
    private var retryRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Фильмы"

        startWaitingForNetwork()
    }

    private fun startWaitingForNetwork() {
        binding.progressBar?.isVisible  = true

        if (isOnline(this)) {
            loadFilmsFragment()
            return
        }

        showNoNetworkSnackbar()

        retryHandler = Handler(Looper.getMainLooper())
        retryRunnable = object : Runnable {
            override fun run() {
                if (isOnline(this@MainActivity)) {
                    stopNetworkCheck()
                    loadFilmsFragment()
                } else {
                    retryHandler?.postDelayed(this, 3000)
                }
            }
        }
        retryHandler?.postDelayed(retryRunnable!!, 3000)
    }

    private fun showNoNetworkSnackbar() {
        val snackbar = Snackbar.make(
            binding.placeHolder,
            "Нет подключения к интернету",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("ПОВТОРИТЬ") {

            if (isOnline(this)) {
                stopNetworkCheck()
                loadFilmsFragment()
            } else {
                Snackbar.make(binding.placeHolder, "Всё ещё нет сети", Snackbar.LENGTH_SHORT).show()
            }
        }
        snackbar.show()
    }

    private fun stopNetworkCheck() {
        retryRunnable?.let { retryHandler?.removeCallbacks(it) }
        retryHandler = null
        retryRunnable = null
    }

    private fun loadFilmsFragment() {
        binding.progressBar?.isVisible = false
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.place_holder, FragmentChoice.newInstance())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNetworkCheck()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun changeFragment(
        localized_name: String,
        name: String,
        year: Int,
        rating: Float,
        image_url: String?,
        description: String,
        genres: ArrayList<String>
    ) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        val bundle = Bundle()
        bundle.putString("localized_name", localized_name)
        bundle.putString("name", name)
        bundle.putString("year", year.toString())
        bundle.putFloat("rating", rating)
        bundle.putString("image_url", image_url)
        bundle.putString("description", description)
        bundle.putStringArrayList("genres", genres)

        val fragment = FragmentInfo.newInstance()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.place_holder, fragment)
            .commit()
    }
}