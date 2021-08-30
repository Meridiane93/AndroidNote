package com.example.sgllight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgllight.db.MyAdapter
import com.example.sgllight.db.MyDbManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initSearchView()
    }
    override fun onResume() {
        super.onResume()
        myDbManager.openDb() // открываем базу
        fillAdapter() // перезаписываем данные
    }

    fun onClickNew(view: View) {
        val i = Intent(this,EditActivity::class.java)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun init(){  // инициализаруем RecyclerView
        rcView.layoutManager = LinearLayoutManager(this) // указал что элементы будут распологаться по вертикали
        val svapHelper = getSwapMg()
        svapHelper.attachToRecyclerView(rcView)
        rcView.adapter = myAdapter // пождключаем адаптер
    }

    fun initSearchView(){
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { // обновляет список только при нажатии на лупу
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { // обновляет список при каждом вводе слова или удалении
                val list = myDbManager.readDbData(newText!!)
                myAdapter.updateAdapter(list)
                return true
            }
        }) // слушатель который замечает изменения внутри searchView
    }

    fun fillAdapter () {
        val list = myDbManager.readDbData("")
        myAdapter.updateAdapter(list)

        if (list.size > 0) tvNoElements.visibility = View.GONE
        else  tvNoElements.visibility = View.VISIBLE
    } // передаём список из базы в адаптер на перезапись

    private fun getSwapMg(): ItemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){ // удаляем по свайпу в правои лево

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { // удаляем по свайпу
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager) // передаём адаптеру позицию удаления ( удаляем с экрана )

            }
        })

}