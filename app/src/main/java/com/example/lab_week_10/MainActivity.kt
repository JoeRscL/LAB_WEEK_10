package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()
        val t = db.totalDao().getTotal(ID)
        if (t != null) {
            Toast.makeText(this, t.total.date, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        val obj = TotalObject(
            value = viewModel.total.value ?: 0,
            date = Date().toString()
        )
        db.totalDao().update(Total(ID, obj))
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) {
            updateText(it)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val t = db.totalDao().getTotal(ID)
        if (t == null) {
            val obj = TotalObject(0, Date().toString())
            db.totalDao().insert(Total(ID, obj))
        } else {
            viewModel.setTotal(t.total.value)
        }
    }

    companion object {
        const val ID: Long = 1
    }
}