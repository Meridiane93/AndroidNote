package com.example.sgllight

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sgllight.db.MyDbManager
import com.example.sgllight.db.MyIntentConstants
import kotlinx.android.synthetic.main.edit_activity.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    var id = 0
    val imageRequestCode = 10
    var isEditState = false
    var tempImageUri = "empty"
    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        getMyIntents()

    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb() // открываем базу
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
        }
    }

    fun onClickAddImage(view: View) {
        mainImageLayout.visibility = View.VISIBLE
        fbAddImage.visibility = View.GONE
    }

    fun onClickDeletImage(view: View) {
        mainImageLayout.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
        tempImageUri = "empty"
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, imageRequestCode)
    }

    fun onClickSave(view: View) {
        val myTitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()

        if (isEditState){
            myDbManager.updateItem(myTitle, myDesc, tempImageUri, id,getCurrentTime())
        }
        else {
            if (myTitle == "" || myDesc == "") {
                Toast.makeText(this, "Не заполнены поля", Toast.LENGTH_SHORT).show()
            } else {
                if (myTitle != "" && myDesc != "") {
                    myDbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
                    Toast.makeText(this, "Данные добавлены", Toast.LENGTH_SHORT).show()

                }
            }
        }
        finish()
    }

    fun onEditEnable(view: View) {
        edTitle.isEnabled = true // разблокируем ввод при нажатии на титл
        edDesc.isEnabled = true // разблокируем ввод при нажатии на поле
        floatingActionButton.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
        if (tempImageUri == "empty") return
        imButtonEditImage.visibility = View.VISIBLE
        imButtonDeletImage.visibility = View.VISIBLE

    }


    fun getMyIntents() {
        val i = intent

        if (i != null) {

            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                fbAddImage.visibility = View.GONE
                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                floatingActionButton.visibility = View.VISIBLE
                isEditState = true
                edTitle.isEnabled = false // блокируем ввод при нажатии на титл
                edDesc.isEnabled = false // блокируем ввод при нажатии на поле
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)

                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    mainImageLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    imMainImage.setImageURI(Uri.parse(tempImageUri))
                    imButtonDeletImage.visibility = View.GONE
                    imButtonEditImage.visibility = View.GONE
                }
            }
        }
    }

    // сохранение времени
    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-mm-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}


