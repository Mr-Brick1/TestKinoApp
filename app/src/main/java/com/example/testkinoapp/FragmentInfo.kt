package com.example.testkinoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testkinoapp.databinding.FragmentInfoBinding
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.lang.Math.floor

class FragmentInfo : Fragment() {

    lateinit var binding: FragmentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater)
        //return inflater.inflate(R.layout.fragment_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        println(bundle?.getString("localized_name","Отсутствует"))
        println(bundle?.getString("name","Don't have"))
        println(bundle?.getString("year","0000"))
        println(bundle?.getFloat("rating",2.22f))
        println(bundle?.getString("image_url", ""))
        println(bundle?.getString("description", "Отстуствует"))
        println(bundle?.getStringArrayList("genres"))

        val transformation: Transformation = RoundedTransformationBuilder()
            .cornerRadiusDp(4f)
            .build()

        Picasso.get()
            .load(bundle?.getString("image_url", ""))
            .resize(132,201)
            .error(R.drawable.ic_baseline_collections_240)
            .transform(transformation)
            .into(binding.imageView)

        binding.textView.text = bundle?.getString("localized_name","Отсутствует")

        val str: ArrayList<String> = bundle?.getStringArrayList("genres") as ArrayList<String>
        var string: String = ""

        for (item in str) {
            string += "$item, "
        }

        binding.textView2.text = "${string}${bundle?.getString("year","0000")} год"

        val value: Float? = bundle?.getFloat("rating",0.00f)

        val d1 = floor(value?.times(10.0) ?: 0.00) / 10.0

        binding.textView4.text = d1.toString()
        binding.textView5.text = bundle?.getString("description", "Отстуствует")

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentInfo()
    }
}