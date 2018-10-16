package com.sweetbytesdev.slickpiclib.SlickPic

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.sweetbytesdev.slickpiclib.Interfaces.WorkFinish
import com.sweetbytesdev.slickpiclib.Models.Img
import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.PermUtil
import com.sweetbytesdev.slickpiclib.Utility.Tag
import com.sweetbytesdev.slickpiclib.Utility.Utility
import com.sweetbytesdev.slickpiclib.Utility.Utility.hideStatusBar
import com.sweetbytesdev.slickpiclib.Utility.Utility.setupStatusBarHidden
import java.util.*

class SlickPic : AppCompatActivity() {

    lateinit var mViewModel: SlickPicViewModel

    companion object {
        private val SELECTION = "selection"

        fun start(context: Activity, requestCode: Int, selectionCount: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermUtil.checkForCamara_WritePermissions(context, object : WorkFinish {
                    override fun onWorkFinish(check: Boolean) {
                        val i = Intent(context, SlickPic::class.java)
                        i.putExtra(SELECTION, selectionCount)
                        context.startActivityForResult(i, requestCode)
                    }
                })
            } else {
                val i = Intent(context, SlickPic::class.java)
                i.putExtra(SELECTION, selectionCount)
                context.startActivityForResult(i, requestCode)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStatusBarHidden(this)
        hideStatusBar(this)
        setContentView(R.layout.slick_pic)

        initialize()

        mViewModel = ViewModelProviders.of(this).get(SlickPicViewModel::class.java)
        mViewModel.mMessenger.observe(this, MessageReceiver)

        handleFragmentChange(Tag.HOST)
    }

    private fun initialize() {
        Utility.getScreenSize(this)
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
                    supportFragmentManager.beginTransaction().add(R.id.frag_container, SelectedPicsFragment(), Tag.SELECTED).commit()
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
