package com.sweetbytesdev.slickpiclib.SlickPic


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import com.sweetbytesdev.slickpiclib.R
//import com.camerakit.CameraKitView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.sweetbytesdev.slickpiclib.Models.Img
//import com.camerakit.CameraKit
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.Utility
import com.wonderkiln.camerakit.CameraView


/**
 * A simple [Fragment] subclass.
 *
 */
class CameraPreviewFragment : Fragment(), OrientationListener {
    override fun onOrientationChanged(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var mViewModel: SlickPicViewModel
//    lateinit var mCameraKitView: CameraKitView
    lateinit var mCamera: CameraView
    lateinit var mCaptureBtn: ImageView
    lateinit var mOrientationManager: OrientationManager


    companion object {

        fun getInstance() = CameraPreviewFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(SlickPicViewModel::class.java)
        mViewModel.mMessenger.observe(activity!!, onMessageReceived)
//        mCameraKitView = view.findViewById<CameraKitView>(R.id.camera)
        mCamera = view.findViewById(R.id.camera)
        view.findViewById<Button>(R.id.test_button).setOnClickListener {
            mViewModel.mMessenger.postValue(Message.SELECTED)
        }
    }

    var onMessageReceived = object : Observer<Message> {
        override fun onChanged(message: Message?) {
            when (message) {
                Message.CAPTURE -> mCamera.captureImage {
                    if (it.jpeg != null) {
                        synchronized(it) {
                            val photo = Utility.writeImage(it.jpeg)
                            var freshlyTakenImage = Img("", photo.getAbsolutePath(), "", true, "", -1)
                            mViewModel.mSelectionList.value!!.add(freshlyTakenImage)
                            mViewModel.mSelectionList.postValue(mViewModel.mSelectionList.value)
                        }
                    } else {
                        Toast.makeText(activity, "Unable to Get The Image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
//        mCameraKitView.onResume()
        mCamera.start()
    }

    override fun onPause() {
        super.onPause()
//        mCameraKitView.onPause()
        mCamera.stop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        mCameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, "Destroying camera frag", Toast.LENGTH_SHORT).show()
    }
}
