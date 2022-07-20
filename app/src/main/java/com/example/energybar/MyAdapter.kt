package com.example.energybar

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import yuku.ambilwarna.AmbilWarnaDialog

class MyAdapter(private val context: Activity, private val arrayList: ArrayList<Energy>, val barAdapter: BarAdapter): ArrayAdapter<Energy>(context,R.layout.list_item, arrayList) {
    val appDb = AppDatabase.getDatabase(context)

    fun openColorPicker(btn: Button, pos: Int){
//         val colorPicker = AmbilWarnaDialog(context, btn.solidColor, )
        val colorPicker = AmbilWarnaDialog(context, arrayList[pos].color, true, object:
            AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                arrayList[pos].color = color
                GlobalScope.launch {
                    appDb.energyDao().update(arrayList[pos].start, arrayList[pos].end, arrayList[pos].color)
                }
                notifyDataSetChanged()
                barAdapter.notifyDataSetChanged()
            }

            override fun onCancel(dialog: AmbilWarnaDialog?) {
            }
        })
        colorPicker.show()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        val startBtn: Button = view.findViewById(R.id.start_btn)
        val seekbar: SeekBar = view.findViewById(R.id.seekBar)
        val endBtn: Button = view.findViewById(R.id.end_btn)

        startBtn.text = arrayList[position].start.toString()
        endBtn.text = arrayList[position].end.toString()
        startBtn.setBackgroundColor(arrayList[position].color)
        endBtn.setBackgroundColor(arrayList[position].color)
        seekbar.progress = arrayList[position].end

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbar.min = arrayList[position].start
            seekbar.max = arrayList[position].end
        }

        startBtn.setOnClickListener {
            openColorPicker(startBtn, position)
        }

        endBtn.setOnClickListener {
            openColorPicker(endBtn, position)
        }

        seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,progress: Int, fromUser: Boolean) {
                endBtn.text = progress.toString()

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
//                if(arrayList[position].end - seek.progress <= 2 ){
////                if(seek.progress <= arrayList[position].start + 2 || seek.progress >= arrayList[position].end-2 ){
//                    Toast.makeText(context, "Minimun length is 2", Toast.LENGTH_SHORT).show()
//                }else{
                    if(seek.progress == arrayList[position].start){
                        if(position == 0){
                            arrayList.clear()
                            val en = Energy(null,1,100, context.resources.getColor(R.color.teal_700))
                            arrayList.add(en)

                            GlobalScope.launch {
                                appDb.energyDao().deleteAll()
                                appDb.energyDao().insert(en)
                            }
                        }else{

                            Log.v("vzsf",arrayList[position-1].start.toString())
                            Log.v("vzsf",arrayList[position].end.toString())
                            Log.v("vzsf",arrayList[position-1].color.toString())

                            val st = arrayList[position-1].start
                            val en = arrayList[position].end
                            val stdel = arrayList[position].start
                            GlobalScope.launch {
                                appDb.energyDao().update(st, en, arrayList[position-1].color)
                                appDb.energyDao().delete(stdel)
//                                appDb.energyDao().update(arrayList[position-1].start, arrayList[position].end, arrayList[position-1].color)
//                                appDb.energyDao().delete(arrayList[position].start)
                            }

                            arrayList[position-1].end = arrayList[position].end
                            arrayList.removeAt(position)


                        }
                    }else if(arrayList[position].end - seek.progress <= 2){
                        Toast.makeText(context, "Minimun length is 2", Toast.LENGTH_SHORT).show()
                    }else{
                        val eg= Energy(null,seek.progress+1, arrayList[position].end,  context.resources.getColor(R.color.teal_700))
                        arrayList[position].end = seek.progress
                        arrayList.add(position+1, eg)

                        GlobalScope.launch {
                            appDb.energyDao().update(arrayList[position].start, arrayList[position].end, arrayList[position].color)
                            appDb.energyDao().insert(eg)
                        }
                    }

//                }

                notifyDataSetChanged()
                barAdapter.notifyDataSetChanged()
            }
        })




        return view
    }
}