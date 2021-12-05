package com.vero.c3_ui_3_skin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.vero.skinlib.SkinManager
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread


//apk直接放到/sdcard/Android/data/com.vero.c3_ui_3_skin/files/skin/skinres-debug.apk
class MainActivity : AppCompatActivity() {

    var path=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        path=getExternalFilesDir(null)!!.path+"/skin/"+"skinres-debug.apk"

        ActivityCompat.requestPermissions(this,
            arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE),1);
        findViewById<TextView>(R.id.tv).setOnClickListener {
            change()
        }
        findViewById<TextView>(R.id.restore).setOnClickListener {
            restore()
        }
    }


    fun change() {
        ///sdcard/Download/skinres-debug.apk
        ///sdcard/Android/data/com.vero.c3_ui_3_skin/files/skin/skinres-debug.apk
        val file=File(path)
        Log.e("changechangechange","file=====${file.length()}")

        SkinManager.getInstance().loadSkin(file.path)
    }

    fun restore() {
        SkinManager.getInstance().loadSkin(null);
    }

    fun getIdentifier(resId: Int): String {
        val resName: String = resources.getResourceEntryName(resId)
        val resType: String = resources.getResourceTypeName(resId)

        return "$resName==$resType"
    }

}