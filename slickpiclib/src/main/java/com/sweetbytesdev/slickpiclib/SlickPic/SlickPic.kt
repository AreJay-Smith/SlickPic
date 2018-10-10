package com.sweetbytesdev.slickpiclib.SlickPic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sweetbytesdev.slickpiclib.R

class SlickPic : AppCompatActivity() {

    companion object {

        fun start(context: Activity, requestCode: Int) {
            val i = Intent(context, SlickPic::class.java)
            context.startActivityForResult(i, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slick_pic)

        supportFragmentManager.beginTransaction().add(R.id.frag_container, CameraPreviewFragment()).commit()
    }
}
