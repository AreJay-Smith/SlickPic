package com.sweetbytesdev.slickpiclib.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.sweetbytesdev.slickpiclib.Interfaces.SectionIndexer
import com.sweetbytesdev.slickpiclib.Models.Img
import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.HeaderItemDecoration
import com.sweetbytesdev.slickpiclib.Utility.Utility
import java.util.ArrayList

class GalleryPickerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>, HeaderItemDecoration.StickyHeaderInterface, SectionIndexer {

    companion object {
        val HEADER = 1
        val ITEM = 2
        val SPAN_COUNT = 3
        private val MARGIN = 2
    }

    private val context: Context
    private val list: ArrayList<Img>
    private val layoutParams: FrameLayout.LayoutParams
    private val glide: RequestManager
    private val options: RequestOptions

    constructor(context: Context) {
        this.context = context
        this.list = arrayListOf()

        var size = Utility.WIDTH / SPAN_COUNT
        layoutParams = FrameLayout.LayoutParams(size, size)
        layoutParams.setMargins(MARGIN, MARGIN - 1, MARGIN, MARGIN - 1)
        options = RequestOptions().override(360).transform(CenterCrop()).transform(FitCenter())
        glide = Glide.with(context)
    }

    fun updateImageList(images: ArrayList<Img>) {
        list.clear()
        list.addAll(images)
        notifyDataSetChanged()
    }

    fun getItemList(): ArrayList<Img> {
        return list
    }

    override fun getItemViewType(position: Int): Int {
        val i = list[position]
        return if (i.contentUrl.equals("",true))
            HEADER
        else
            ITEM
    }

    fun select(selection: Boolean, pos: Int) {
        list[pos].isSelected = selection
        notifyItemChanged(pos)
    }

    override fun getItemId(position: Int): Long {
        return list[position].contentUrl?.hashCode()!!.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER) {
            return HeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_row, parent, false))
        } else {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.main_image, parent, false)
            return Holder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var image = list[position]
        if (holder is Holder) {
            glide.load(image.contentUrl).apply(options).into(holder.preview)
//            holder.selection.visibility = if (image.isSelected!!) View.VISIBLE else View.GONE
        } else if (holder is HeaderHolder) {
            holder.header.text = image.headerDate
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var position = itemPosition
        do {
            if (this.isHeader(position)) {
                headerPosition = position
                break
            }
            position -= 1
        } while (position >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.header_row
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        val image = list[headerPosition]
        (header.findViewById<View>(R.id.header) as TextView).setText(image.headerDate)
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return getItemViewType(itemPosition) == 1
    }

    override fun getSectionText(position: Int): String {
        return list[position].headerDate!!
    }

    fun getSectionMonthYearText(position: Int): String {
        return list[position].scrollerDate!!
    }

    inner class Holder : RecyclerView.ViewHolder, View.OnClickListener, View.OnLongClickListener {
        internal var preview: ImageView
        internal var selection: ImageView

        constructor(itemView: View): super(itemView) {
            preview = itemView.findViewById(R.id.preview)
            selection = itemView.findViewById(R.id.selection)
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            preview.layoutParams = layoutParams
        }

        override fun onClick(view: View) {
            val id = this.layoutPosition
//            onSelectionListener?.OnClick(list[id], view, id)
        }

        override fun onLongClick(view: View): Boolean {
            val id = this.layoutPosition
//            onSelectionListener?.OnLongClick(list[id], view, id)
            return true
        }
    }

    inner class HeaderHolder : RecyclerView.ViewHolder {
        var header: TextView

        constructor(itemView: View):super(itemView) {
            header = itemView.findViewById(R.id.header)
        }
    }
}