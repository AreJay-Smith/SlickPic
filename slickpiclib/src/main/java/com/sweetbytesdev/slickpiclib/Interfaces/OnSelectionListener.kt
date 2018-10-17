package com.sweetbytesdev.slickpiclib.Interfaces

import android.view.View
import com.sweetbytesdev.slickpiclib.Models.Img

interface OnSelectionListener {

    fun OnClick(Img: Img, view: View, position: Int)

    fun OnLongClick(img: Img, view: View, position: Int)
}