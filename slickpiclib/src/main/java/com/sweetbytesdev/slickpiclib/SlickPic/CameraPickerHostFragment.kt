package com.sweetbytesdev.slickpiclib.SlickPic


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.sweetbytesdev.slickpiclib.Models.Img
import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.SlickPic.SlickPic.Companion.IMAGE_RESULTS
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.Tag

class CameraPickerHostFragment : Fragment(), View.OnClickListener, ViewPager.OnPageChangeListener {

    private lateinit var mVm: SlickPicViewModel
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mSelectionPreviewBtn: ImageView
    private lateinit var mCaptureBtn: ImageView
    private lateinit var mSendCount: TextView
    private lateinit var mSendBtn: FrameLayout
    private lateinit var mBottomBar: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_picker_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVm = ViewModelProviders.of(activity!!).get(SlickPicViewModel::class.java)
        mSendCount = view.findViewById(R.id.send_count)
        mSendBtn = view.findViewById(R.id.sendButton)
        mSendBtn.setOnClickListener(this)
        mBottomBar = view.findViewById(R.id.bottom_bar_container)
        mTabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        mViewPager = view.findViewById<ViewPager>(R.id.view_pager)
        mSelectionPreviewBtn = view.findViewById(R.id.selection_preview_btn)
        mSelectionPreviewBtn.setOnClickListener(this)
        mCaptureBtn = view.findViewById(R.id.capture_btn)
        mCaptureBtn.setOnClickListener(this)
        activity?.supportFragmentManager
        setUpViewPager()

        mVm.mSelectionList.observe(activity!!, onSelectionUpdated)
        mVm.mSelectionList.postValue(mVm.mSelectionList.value)
    }

    fun hideBottomBar() {
        mBottomBar.setBackgroundColor(Color.parseColor("#00000000"))
        mSelectionPreviewBtn.visibility = View.INVISIBLE
        mSendCount.visibility = View.INVISIBLE
        mSendBtn.visibility = View.INVISIBLE
    }

    fun showBottomBar() {
        mBottomBar.setBackgroundColor(resources.getColor(R.color.colorPrimarySlickPicTransparent))
        mSelectionPreviewBtn.visibility = View.VISIBLE
        mSendCount.visibility = View.VISIBLE
        mSendBtn.visibility = View.VISIBLE
    }

    var onSelectionUpdated = object : Observer<MutableList<Img>> {
        override fun onChanged(list: MutableList<Img>?) {
            if (list!!.size > 0) {
                var image = list[list.size-1]
                Glide.with(activity!!).load(image.contentUrl).apply(
                        RequestOptions().override(360).transform(CenterCrop()).transform(FitCenter())).into(mSelectionPreviewBtn)
                if (!mSelectionPreviewBtn.isShown) {
                    showBottomBar()
                }
            } else {
                mSelectionPreviewBtn.setImageResource(android.R.color.transparent)
                hideBottomBar()
            }
            mSendCount.text = list.size.toString()

            mCaptureBtn.setImageDrawable(resources.getDrawable(R.drawable.ring))
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
            R.id.capture_btn -> {
                mCaptureBtn.setImageDrawable(resources.getDrawable(R.drawable.ring_click))
                mVm.mMessenger.postValue(Message.CAPTURE)}
            R.id.sendButton -> {
                val list = ArrayList<String>()
                mVm.mSelectionList.value?.forEach {
                    list.add(it.url!!)
                }

                val resultIntent = Intent()
                resultIntent.putStringArrayListExtra(IMAGE_RESULTS, list)
                activity!!.setResult(Activity.RESULT_OK, resultIntent)
                activity!!.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
