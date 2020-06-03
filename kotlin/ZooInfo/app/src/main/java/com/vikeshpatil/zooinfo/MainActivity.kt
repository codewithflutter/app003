package com.vikeshpatil.zooinfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.animal_ticket.view.*

class MainActivity : AppCompatActivity() {


    var animalList = ArrayList<Animal>()
    var adapter:AnimalAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animalList.add(Animal("Baboon", "Baboon lives on the trees", R.drawable.baboon, false))
        animalList.add(Animal("Bulldog", "bulldog lives in the house", R.drawable.bulldog, true))
        animalList.add(Animal("Panda", "Panda lives in the hilly areas", R.drawable.panda, false))
        animalList.add(Animal("Swallow", "Swallow bird flies in the sky", R.drawable.swallow_bird, false))
        animalList.add(Animal("White Tiger", "White Tigers are rare", R.drawable.white_tiger, true))
        animalList.add(Animal("Zebra", "Zebras have white and black strips on them", R.drawable.zebra, false))

        adapter = AnimalAdapter(this, animalList)
        LVAnimals.adapter = adapter
    }

    fun deleteItem(index:Int){
        animalList.removeAt(index)
        adapter!!.notifyDataSetChanged()
    }

    inner class AnimalAdapter:BaseAdapter{

        var animalList = ArrayList<Animal>()
        var context:Context? = null

        constructor(context: Context, animalList: ArrayList<Animal>):super(){
            this.animalList = animalList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var animal = animalList[position]

            if(animal.isKiller){
                    var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    var myView = inflater.inflate(R.layout.animal_killer_ticket, null)
                    myView.tvDes.text = animal.des
                    myView.tvTitle.text = animal.name
                    myView.IVAnimal.setImageResource(animal.image!!)
                    myView.IVAnimal.setOnClickListener {
                    val intent = Intent(context, AnimalInfo::class.java)
                    intent.putExtra("name", animal.name)
                    intent.putExtra("des", animal.des)
                    intent.putExtra("image", animal.image!!)
                    context!!.startActivity(intent)
            }
                return myView

            }else{
                var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var myView = inflater.inflate(R.layout.animal_ticket, null)
                myView.tvDes.text = animal.des
                myView.tvTitle.text = animal.name
                myView.IVAnimal.setImageResource(animal.image!!)
                myView.IVAnimal.setOnClickListener {

                    val intent = Intent(context, AnimalInfo::class.java)
                    intent.putExtra("name", animal.name)
                    intent.putExtra("des", animal.des)
                    intent.putExtra("image", animal.image!!)
                    context!!.startActivity(intent)
                }
                return myView
            }
        }

        override fun getItem(position: Int): Any {
            return animalList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return animalList.size
        }
    }
}
