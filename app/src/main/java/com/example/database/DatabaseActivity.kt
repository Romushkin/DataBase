package com.example.database

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DatabaseActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var enterNameET: EditText
    private lateinit var enterRoleSpinner: Spinner
    private lateinit var enterPhoneET: EditText
    private lateinit var saveDataBTN: Button
    private lateinit var getDataBTN: Button
    private lateinit var removeDataBTN: Button
    private lateinit var nameTV: TextView
    private lateinit var roleTV: TextView
    private lateinit var phoneTV: TextView

    private val db = DBHelper(this, null)

    private val roles = mutableListOf(
        "Должность",
        "Менеджер",
        "Кассир",
        "Продавец",
        "Директор",
        "Бухгалтер"
    )

    @SuppressLint("Range", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_database)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        setSupportActionBar(toolbar)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        enterRoleSpinner.adapter = adapter

        saveDataBTN.setOnClickListener {
            if (enterNameET.text.isEmpty() || enterRoleSpinner.selectedItem == "Должность" || enterPhoneET.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Заполните все поля",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val name = enterNameET.text.toString()
            val role = enterRoleSpinner.selectedItem.toString()
            val phone = enterPhoneET.text.toString()

            db.addName(name, role, phone)
            Toast.makeText(
                this,
                "$name, $role и  $phone добавлены в базу данных",
                Toast.LENGTH_LONG
            ).show()
            enterNameET.text.clear()
            enterRoleSpinner.setSelection(0)
            enterPhoneET.text.clear()
        }

        getDataBTN.setOnClickListener {
            val cursor = db.getInfo()
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()
                nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                roleTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
                phoneTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n")
            }
            while (cursor!!.moveToNext()) {
                nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                roleTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
                phoneTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n")
            }
            cursor.close()
        }

        removeDataBTN.setOnClickListener {
            db.removeAll()
            nameTV.text = ""
            roleTV.text = ""
            phoneTV.text = ""
        }
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        enterNameET = findViewById(R.id.enterNameET)
        enterRoleSpinner = findViewById(R.id.enterRoleSpinner)
        enterPhoneET = findViewById(R.id.enterPhoneET)
        saveDataBTN = findViewById(R.id.saveDataBTN)
        getDataBTN = findViewById(R.id.getDataBTN)
        removeDataBTN = findViewById(R.id.removeDataBTN)
        nameTV = findViewById(R.id.nameTV)
        roleTV = findViewById(R.id.roleTV)
        phoneTV = findViewById(R.id.phoneTV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_db, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exitMenu) finishAffinity()
        return super.onOptionsItemSelected(item)
    }

}