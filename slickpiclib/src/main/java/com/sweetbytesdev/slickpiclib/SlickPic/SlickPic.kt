package com.sweetbytesdev.slickpiclib.SlickPic

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.Tag
import com.sweetbytesdev.slickpiclib.Utility.hideStatusBar
import com.sweetbytesdev.slickpiclib.Utility.setupStatusBarHidden

class SlickPic : AppCompatActivity() {

    lateinit var mViewModel: SlickPicViewModel

    companion object {

        fun start(context: Activity, requestCode: Int) {
            val i = Intent(context, SlickPic::class.java)
            context.startActivityForResult(i, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStatusBarHidden(this)
        hideStatusBar(this)
        setContentView(R.layout.slick_pic)

        mViewModel = ViewModelProviders.of(this).get(SlickPicViewModel::class.java)
        mViewModel.mMessenger.observe(this, MessageReceiver)

        handleFragmentChange(Tag.HOST)
    }

    var MessageReceiver = Observer<Message> {
        // Message received from fragments
        Toast.makeText(this, "Message received: ${it.toString()}", Toast.LENGTH_SHORT).show()

        when (it) {
            Message.SELECTED -> handleFragmentChange(Tag.SELECTED)
        }
    }

    override fun onBackPressed() {

        when (mViewModel.TAG) {

            Tag.HOST -> finish()

            Tag.SELECTED -> handleFragmentChange(Tag.HOST)
        }
    }

    fun handleFragmentChange(fragTag: String) {

        when(fragTag) {
            Tag.HOST -> {
                var fragHost = supportFragmentManager.findFragmentByTag(Tag.HOST)
                if (fragHost != null) {
                    supportFragmentManager.beginTransaction().show(fragHost).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.frag_container, CameraPickerHostFragment(), Tag.HOST).commit()
                }

                var fragSelected = supportFragmentManager.findFragmentByTag(Tag.SELECTED)
                if (fragSelected != null) {
                    supportFragmentManager.beginTransaction().hide(fragSelected).commit()
                }
                mViewModel.TAG = Tag.HOST
            }

            Tag.SELECTED -> {
                var fragSelected = supportFragmentManager.findFragmentByTag(Tag.SELECTED)
                if (fragSelected != null) {
                    supportFragmentManager.beginTransaction().show(fragSelected).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.frag_container, GalleryPickerFragment(), Tag.SELECTED).commit()
                }

                var fragHost = supportFragmentManager.findFragmentByTag(Tag.HOST)
                if (fragHost != null) {
                    supportFragmentManager.beginTransaction().hide(fragHost).commit()
                }
                mViewModel.TAG = Tag.SELECTED
            }
        }
    }
}
