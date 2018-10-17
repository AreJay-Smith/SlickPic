package com.sweetbytesdev.slickpiclib.SlickPic


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import com.sweetbytesdev.slickpiclib.R
//import com.camerakit.CameraKitView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.sweetbytesdev.slickpiclib.Interfaces.OnOrientationChangeListener
import com.sweetbytesdev.slickpiclib.Models.Img
//import com.camerakit.CameraKit
import com.sweetbytesdev.slickpiclib.Utility.Message
import com.sweetbytesdev.slickpiclib.Utility.OrientationManager
import com.sweetbytesdev.slickpiclib.Utility.Utility
import com.wonderkiln.camerakit.CameraView
import android.hardware.SensorManager
import android.opengl.Matrix
import com.wonderkiln.camerakit.CameraKitImage


/**
 * A simple [Fragment] subclass.
 *
 */
class CameraPreviewFragment : Fragment(), OnOrientationChangeListener {

    lateinit var mViewModel: SlickPicViewModel
//    lateinit var mCameraKitView: CameraKitView
    lateinit var mCamera: CameraView
//    lateinit var mCaptureBtn: ImageView
    var mDeviceOrientation = OrientationManager.ScreenOrientation.PORTRAIT


    companion object {

        fun getInstance() = CameraPreviewFragment()
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

//        var orientationManager = OrientationManager(activity!!, SensorManager.SENSOR_DELAY_NORMAL, this)
//        orientationManager.enable()
    }

    var onMessageReceived = object : Observer<Message> {
        override fun onChanged(message: Message?) {
            when (message) {
                Message.CAPTURE -> mCamera.captureImage {
                    if (it.jpeg != null) {
                        synchronized(it) {
//                            var orientedImage = orientImage(it)
                            val photo = Utility.writeImage(it.jpeg)
//                            val photo = Utility.writeImage(orientedImage)
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

    fun orientImage(image: CameraKitImage): Bitmap {
        when (mDeviceOrientation) {
            OrientationManager.ScreenOrientation.LANDSCAPE -> {
                var matrix = android.graphics.Matrix()
                matrix.postRotate(90f)
                return Bitmap.createBitmap(image.bitmap, 0, 0, image.bitmap.width, image.bitmap.height, matrix, true)
            }
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> {

            }
        }
        return image.bitmap
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

    override fun onOrientationChangeListener(orientation: OrientationManager.ScreenOrientation) {
        mDeviceOrientation = orientation
        Toast.makeText(activity, "Orientation is now $orientation", Toast.LENGTH_SHORT).show()
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
