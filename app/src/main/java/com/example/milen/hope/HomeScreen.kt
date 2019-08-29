package com.example.milen.hope

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

    }

    fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.lists_button -> {
                val intent = Intent(this, ActivityList::class.java)
                startActivity(intent)
            }
        }// do nothing
    }

    fun readStudyHours() : Int {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val studyHours = sharedPref.getInt("studyHours", 0)
        return studyHours
    }

    fun writeStudyHours(newValue: Int) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("studyHours", newValue)
            apply()
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

}
