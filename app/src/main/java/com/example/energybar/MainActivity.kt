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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var energyArray: ArrayList<Energy> = ArrayList()
    private lateinit var appDb: AppDatabase
    lateinit var barAdapter: BarAdapter
    lateinit var adapter: MyAdapter

    fun getResponse(){
        GlobalScope.launch {
            val response = appDb.energyDao().getAll() as ArrayList<Energy>

            withContext(Dispatchers.Main){
                if(response.size == 0){
                    val en = Energy(null,1, 100, resources.getColor(R.color.teal_700))
                    energyArray.add(en)
                    GlobalScope.launch {
                        appDb.energyDao().insert(en)
                    }

                }else{
//                    energyArray = response
                    energyArray.addAll(response.sortedBy { it.start })

                }

                adapter.notifyDataSetChanged()
                barAdapter.notifyDataSetChanged()
                Log.v("fasdf",response.toString())
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        appDb = AppDatabase.getDatabase(this)



        getResponse()

        barAdapter = BarAdapter(this, energyArray)
        adapter = MyAdapter(this, energyArray, barAdapter)

        binding.listview.adapter = adapter
        binding.energyBar.adapter = barAdapter

        binding.button.setOnClickListener {
            GlobalScope.launch {
                val ener = appDb.energyDao().getAll()
                Log.v("fasfdas", ener.toString())
            }
        }
    }

    fun writeDate(){
        val start = 1
        val end =3
        val energy = Energy(null,start, end, 90)
        GlobalScope.launch(Dispatchers.IO){
            appDb.energyDao().insert(energy)
        }

        //delete
        GlobalScope.launch {
            appDb.energyDao().deleteAll()
        }
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