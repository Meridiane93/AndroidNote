package com.example.sgllight.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgllight.EditActivity
import com.example.sgllight.R

class MyAdapter (listMain:ArrayList<ListItem> , contextM: Context) : RecyclerView.Adapter<MyAdapter.MyHolder> () {

    var listArray = listMain
    var context = contextM

    class MyHolder(itemView: View , contextV: Context) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        val context = contextV

        fun setData(item:ListItem) {

            tvTitle.text = item.title // отображаем в ActivityMain только title
            tvTime.text = item.time

            itemView.setOnClickListener {
                val intent = Intent(context,EditActivity::class.java).apply { // открываем EditActivity
                    putExtra(MyIntentConstants.I_TITLE_KEY,item.title) // передаём в EditActivity титл
                    putExtra(MyIntentConstants.I_DESC_KEY, item.desc) // передаём в EditActivity контент
                    putExtra(MyIntentConstants.I_URI_KEY,item.uri) // передаём в EditActivity картинку
                    putExtra(MyIntentConstants.I_ID_KEY,item.id) // передаём в EditActivity id
                }
                context.startActivity(intent) // запускаем интент
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder { // готовим шаблон для рисования
        val inflater = LayoutInflater.from(parent.context) // берёт xml превращает в объект, который будет на экране
        return MyHolder(inflater.inflate(R.layout.rc_item,parent,false), context) // возвращает готовый шаблон
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) { // добавляет данные из массива(списка) в готовый шаблон
        holder.setData(listArray[position])
    }

    override fun getItemCount(): Int = listArray.size // сколько элементов у нас в списке

    fun updateAdapter(listItems: List<ListItem>) { // обновляем данные списка
        listArray.clear()  // чистим список
        listArray.addAll(listItems) // добавляем новые данные
        notifyDataSetChanged() // сообщаем что данные перезапустились

    }

    fun removeItem(pos:Int,dbManager: MyDbManager) { // обновляем данные списка
        dbManager.removeItemFromDb(listArray[pos].id.toString()) // удаляем из базы данных
        listArray.removeAt(pos)  // удаляем заданный список
        notifyItemRangeChanged(0,listArray.size)// говорим какой теперь длины наш список
        notifyItemRemoved(pos) // с какой позиции мы удалили строку

    }
}