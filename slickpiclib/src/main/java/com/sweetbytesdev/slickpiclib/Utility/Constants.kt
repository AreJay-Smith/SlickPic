package com.sweetbytesdev.slickpiclib.Utility

import android.provider.MediaStore

object Constants {
    val sScrollbarAnimDuration = 300
    var PROJECTION = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED)
    var URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    var ORDERBY = MediaStore.Images.Media.DATE_TAKEN + " DESC"
}