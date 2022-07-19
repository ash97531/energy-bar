package com.example.energybar

import android.app.Activity
import android.content.res.ColorStateList
import android.database.DataSetObserver
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.energybar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var energyArray: ArrayList<Energy> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        energyArray.add(Energy(1, 100, resources.getColor(R.color.teal_700)))

        val barAdapter = BarAdapter(this, energyArray)
        val adapter = MyAdapter(this, energyArray, barAdapter)
        binding.listview.adapter = adapter

        binding.energyBar.adapter = barAdapter
    }
}

class BarAdapter(private val context: Activity, private val arrayList: ArrayList<Energy>): ArrayAdapter<Energy>(context,R.layout.bar_item, arrayList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.bar_item, null)

        var progressBar: ProgressBar = view.findViewById(R.id.bar)
        progressBar.progress = arrayList[position].end
//        progressBar.setBackgroundColor(arrayList[position].color)
        progressBar.progressTintList = ColorStateList.valueOf(arrayList[position].color)

        return view;
    }
}