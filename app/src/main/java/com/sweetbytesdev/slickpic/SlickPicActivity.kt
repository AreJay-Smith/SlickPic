package com.sweetbytesdev.slickpic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sweetbytesdev.slickpiclib.SlickPic.SlickPic

class SlickPicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slick_pic)

        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            SlickPic.start(this, 2, 5)
        }
    }
}
