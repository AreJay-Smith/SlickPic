package com.sweetbytesdev.slickpiclib.Models

import java.io.Serializable

data class Img(var headerDate: String?,
               var contentUrl: String?,
               var url: String?,
               var isSelected: Boolean?,
               var scrollerDate: String?,
               var position: Int?) : Serializable {

    fun isIn(list: MutableList<Img>): Boolean {
        if (list.any { it.url.equals(url) }) {
            return true
        }
        return false
    }
}