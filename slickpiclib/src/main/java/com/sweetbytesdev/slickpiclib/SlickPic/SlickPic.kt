package com.sweetbytesdev.slickpiclib.SlickPic

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.sweetbytesdev.slickpiclib.Interfaces.WorkFinish
import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.*

class SlickPic : AppCompatActivity() {

    lateinit var mViewModel: SlickPicViewModel

    companion object {
        private val SELECTION = "selection"
        private val MODE = "mode"
        val IMAGE_RESULTS = "image_results"

        fun start(context: Activity, requestCode: Int, selectionCount: Int, mode: Mode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermUtil.checkForCamara_WritePermissions(context, object : WorkFinish {
                    override fun onWorkFinish(check: Boolean) {
                        val i = Intent(context, SlickPic::class.java)
                        i.putExtra(SELECTION, selectionCount)
                        i.putExtra(MODE, mode)
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
        setContentView(R.layout.slick_pic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initialize()

        mViewModel = ViewModelProviders.of(this).get(SlickPicViewModel::class.java)
        mViewModel.mMessenger.observe(this, MessageReceiver)
        mViewModel.mMode = intent.extras.getSerializable(MODE) as Mode

        mViewModel.TAG = Tag.HOST
        handleFragmentChange()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialize() {
        Utility.getScreenSize(this)
    }

    var MessageReceiver = Observer<Message> {
        // Message received from fragments
        when (it) {
            Message.SELECTED -> {
                mViewModel.TAG = Tag.SELECTED
                handleFragmentChange()
            }
        }
    }

    override fun onBackPressed() {

        when (mViewModel.TAG) {

            Tag.HOST -> finish()

            Tag.SELECTED -> {
                mViewModel.TAG = Tag.HOST
                handleFragmentChange()
            }
        }
    }

    fun handleFragmentChange() {

        when (mViewModel.TAG) {
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
                if (mViewModel.mReturnToTag.equals(Tag.CAMERA)) {
                    title = ""
                } else {
                    title = getString(R.string.previous_photos)
                }
                mViewModel.TAG = Tag.HOST
            }

            Tag.SELECTED -> {
                var fragSelected = supportFragmentManager.findFragmentByTag(Tag.SELECTED)
                if (fragSelected != null) {
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_out, R.anim.abc_fade_in)
                            .show(fragSelected).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_out, R.anim.abc_fade_in)
                            .add(R.id.frag_container, SelectedPicsFragment(), Tag.SELECTED).commit()
                }

                var fragHost = supportFragmentManager.findFragmentByTag(Tag.HOST)
                if (fragHost != null) {
                    supportFragmentManager.beginTransaction().hide(fragHost).commit()
                }
                mViewModel.TAG = Tag.SELECTED
                title = getString(R.string.selected_photos)
            }
        }
    }
}
