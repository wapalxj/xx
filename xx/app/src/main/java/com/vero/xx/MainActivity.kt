package com.vero.xx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setContentView(R.layout.test_flowlayout)

        //BootClassLoader
        val stringLoader=String::class.java.classLoader

        //PathClassLoader
        val path=classLoader

        //PathClassLoader
        val appLoader= application.classLoader

        Log.e("ClassLoader000 ","stringLoader====$stringLoader")
        Log.e("ClassLoader000 ","classLoader ====$path")
        Log.e("ClassLoader000 ","appLoader ====$appLoader")

//        Glide.with(this)


    }
}