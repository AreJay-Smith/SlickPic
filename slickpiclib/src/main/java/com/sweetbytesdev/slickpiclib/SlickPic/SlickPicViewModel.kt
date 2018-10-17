package com.sweetbytesdev.slickpiclib.SlickPic

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sweetbytesdev.slickpiclib.Models.Img
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.Tag
import com.sweetbytesdev.slickpiclib.Utility.Utility

class SlickPicViewModel : ViewModel() {

    var mSelectionList = MutableLiveData<MutableList<Img>>()

    init {
        mSelectionList.value = mutableListOf()
    }

    val mMessenger: MutableLiveData<Message> by lazy {
        MutableLiveData<Message>()
    }

    var TAG = Tag.HOST
    var mReturnToTag = Tag.CAMERA
}