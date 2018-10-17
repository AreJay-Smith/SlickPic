package com.sweetbytesdev.slickpiclib.SlickPic


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.sweetbytesdev.slickpiclib.Models.Img

import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.Tag
import java.util.ArrayList

class CameraPickerHostFragment : Fragment(), View.OnClickListener, ViewPager.OnPageChangeListener {

    private lateinit var mVm: SlickPicViewModel
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mSelectionPreviewBtn: ImageView
    private lateinit var mCaptureBtn: ImageView
    private lateinit var mSendCount: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_picker_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVm = ViewModelProviders.of(activity!!).get(SlickPicViewModel::class.java)
        mSendCount = view.findViewById(R.id.send_count)
        mTabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        mViewPager = view.findViewById<ViewPager>(R.id.view_pager)
        mSelectionPreviewBtn = view.findViewById(R.id.selection_preview_btn)
        mSelectionPreviewBtn.setOnClickListener(this)
        mVm.mSelectionList.observe(activity!!, onSelectionUpdated)
        mCaptureBtn = view.findViewById(R.id.capture_btn)
        mCaptureBtn.setOnClickListener(this)
        activity?.supportFragmentManager
        setUpViewPager()
    }

    var onSelectionUpdated = object : Observer<MutableList<Img>> {
        override fun onChanged(list: MutableList<Img>?) {
            if (list!!.size > 0) {
                var image = list[list.size-1]
                Glide.with(activity!!).load(image.contentUrl).apply(
                        RequestOptions().override(360).transform(CenterCrop()).transform(FitCenter())).into(mSelectionPreviewBtn)
            } else {
                mSelectionPreviewBtn.setImageResource(android.R.color.transparent)
            }
            mSendCount.text = list.size.toString()
        }

    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(pageIndex: Int) {
        if (pageIndex == 1) {
            mCaptureBtn.visibility = View.INVISIBLE
            activity?.title = getString(R.string.previous_photos)
            mVm.mReturnToTag = Tag.GALLERY
        } else {
            mCaptureBtn.visibility = View.VISIBLE
            activity?.title = ""
            mVm.mReturnToTag = Tag.CAMERA
        }
        Toast.makeText(activity, "Looking ag ${mVm.mReturnToTag}", Toast.LENGTH_SHORT).show()
    }

    private fun setUpViewPager() {
        var adapter = ViewPagerAdapter(fragmentManager!!)
        adapter.addFragment(CameraPreviewFragment.getInstance(), "Camera")
        adapter.addFragment(GalleryPickerFragment.getInstance(), "Gallery")
        mViewPager.adapter = adapter
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.addOnPageChangeListener(this)
    }

    class ViewPagerAdapter : FragmentPagerAdapter {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        constructor(manager: FragmentManager): super(manager) {}

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.selection_preview_btn -> mVm.mMessenger.postValue(Message.SELECTED)
            R.id.capture_btn -> mVm.mMessenger.postValue(Message.CAPTURE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, "Destroying Host frag", Toast.LENGTH_SHORT).show()
    }
}
