package com.example.PWV


//import java.awt.image.BufferedImage

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.Semaphore





class MainActivity : AppCompatActivity() {
    private var processing = true
    private var entries = ArrayList<Entry>()
    private val entryLen = 100
    private val MAX_PREVIEW_WIDTH = 1920
    private val MAX_PREVIEW_HEIGHT = 1080
    private var cameraStatus = true;


    private fun getAvgRed(bitmap: Bitmap): Float {
        var sum = 0f
        var num = 0
        for (x in 0..bitmap.width-1) {
            for (y in 0..bitmap.height-1) {
                sum += Color.red(bitmap.getPixel(x,y))
                num++
            }
        }
        return sum / num
    }

    private val mOnImageAvailableListener: ImageReader.OnImageAvailableListener  = object: ImageReader.OnImageAvailableListener{


        override fun onImageAvailable(reader: ImageReader) {

            val image = reader.acquireNextImage()
            if(image != null){
//                Log.d("RECIEVED", "Received Frame")
                val plane = image.planes[0]
                val buffer = plane.buffer
                val byteArray = ByteArray(buffer.capacity())
                for(i in byteArray.indices){
                    byteArray[i] = buffer[i]
                }
                val bitmap = BitmapFactory.decodeByteArray(
                    byteArray,0, byteArray.size
                )
                Log.d("red", getAvgRed(bitmap).toString())
                val red = getAvgRed(bitmap)

//                entries.removeAt(0)
                entries.add(Entry(entries.size.toFloat(), red))
//                Log.d("bro", entries.toString())
                if(entries.size > entryLen){
                    entries.removeAt(0)
                }
                for(i in 0..entries.size-1){
                    entries[i].x = entries[i].x-1
                }
                val vl = LineDataSet(entries, "Sensor Readings")
//Part4
                vl.setDrawValues(false)
                vl.setDrawFilled(false)
                vl.lineWidth = 3f
                vl.fillColor = R.color.purple_700
                vl.fillAlpha = R.color.purple_200

//Part5
                lineChart?.xAxis?.labelRotationAngle = 0f

//Part6
                lineChart?.data = LineData(vl)

                lineChart?.invalidate()
                image.close()

            }else{
                Log.d("null image", "null image")
            }
        }

    }

    private val mSurfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
    }




    private var mCameraId: String? = null
    private var mTextureView: TextureView? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private var mCameraDevice: CameraDevice? = null
    private var mPreviewSize: Size? = null
    private val mStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release()
            mCameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            mCameraOpenCloseLock.release()
            cameraDevice.close()
            mCameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            mCameraOpenCloseLock.release()
            cameraDevice.close()
            mCameraDevice = null
            finish()
        }
    }

    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null
    private var mImageReader: ImageReader? = null
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mPreviewRequest: CaptureRequest? = null
    private val REQUEST_CAMERA_PERMISSION = 1
    private val mCameraOpenCloseLock: Semaphore = Semaphore(1)
    private var lineChart: LineChart? = null

    override fun onResume() {
        super.onResume()
        startBackgroundThread()

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (mTextureView!!.isAvailable) {
            openCamera(mTextureView!!.width, mTextureView!!.height)
//            mImageReader!!.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler)
        } else {
            mTextureView!!.surfaceTextureListener = mSurfaceTextureListener

        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }


    private fun createGraph(){
        lineChart = findViewById(R.id.activity_main_linechart)

        //Part1


//Part2
        entries.add(Entry(1f, 0f))
//Part3
        val vl = LineDataSet(entries, "Sensor Readings")
//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(false)
        vl.lineWidth = 3f
        vl.fillColor = R.color.purple_700
        vl.fillAlpha = R.color.purple_200

//Part5
        lineChart?.xAxis?.labelRotationAngle = 0f

//Part6
        lineChart?.data = LineData(vl)

//Part7
        lineChart?.axisRight?.isEnabled = false
        lineChart?.getAxisLeft()?.setDrawGridLines(false)
        lineChart?.getXAxis()?.setDrawGridLines(false)
        lineChart?.xAxis?.axisMaximum = entryLen.toFloat()
//Part8
        lineChart?.setTouchEnabled(true)
        lineChart?.setPinchZoom(true)

//Part9
        lineChart?.description?.text = "Time"
        lineChart?.setNoDataText("No data yet!")

//Part10
//        lineChart?.animateX(1800, Easing.EaseInExpo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        hideSystemBars()
        supportActionBar?.hide()        //hide top bar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextureView = findViewById(R.id.textureView2)
        createGraph()
        var button = findViewById<Button>(R.id.button7)
        button.setOnClickListener {
            if (cameraStatus == false) {
                val intent = Intent(this, thank_you_activity::class.java)
                startActivity(intent)
            } else {
                toggleStart();
            }
        }
    }

    private fun openCamera(width: Int, height: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
            return
        }
        setUpCameraOutputs(width, height)
        configureTransform(width, height)
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(mCameraId!!, mStateCallback, mBackgroundHandler)


        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }
    }

    private fun closeCamera() {
        try {
            mCameraOpenCloseLock.acquire()
            if (null != mCaptureSession) {
                mCaptureSession!!.close()
                mCaptureSession = null
            }
            if (null != mCameraDevice) {
                mCameraDevice!!.close()
                mCameraDevice = null
            }
            if (null != mImageReader) {
                mImageReader!!.close()
                mImageReader = null
            }
        } catch (e: InterruptedException) {
            throw java.lang.RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            mCameraOpenCloseLock.release()
        }
    }


    private fun setUpCameraOutputs(width: Int, height: Int) {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                // switch this to change what cam is used
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    continue
                }
                val map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                ) ?: continue

                // For still image captures, we use the largest available size.
                val sizes = map.getOutputSizes(ImageFormat.JPEG)

                val largest = sizes[0]

                mImageReader = ImageReader.newInstance(
                    largest.width / 16, largest.height / 16,
                    ImageFormat.JPEG,  /*maxImages*/2
                )
                Log.d("here", "reached this point")
                Log.d("Is imagereader null lmfao", (mImageReader==null).toString())
                Log.d("then what is imagereader :(", (mImageReader).toString())
                mImageReader!!.setOnImageAvailableListener(
                    mOnImageAvailableListener, mBackgroundHandler
                )
//                Log.d("Is imagereader", (mImageReader!!.acquireLatestImage().toString()))
                val displaySize = Point()
                windowManager.defaultDisplay.getSize(displaySize)
                var maxPreviewWidth: Int = displaySize.x
                var maxPreviewHeight: Int = displaySize.y
                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH
                }
                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT
                }

                // Danger! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                mPreviewSize = chooseOptimalSize(
                    map.getOutputSizes(SurfaceTexture::class.java),
                    width, height, maxPreviewWidth,
                    maxPreviewHeight, largest
                )
                mCameraId = cameraId
                return
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            Log.d(
                "Camera2 API not supported on this device",
               "Sorry"
            )
        }
    }

    fun writeStringAsFile(fileContents: String?, fileName: String?) {
        val context: Context = getApplicationContext()
        try {
            val out = FileWriter(File(context.filesDir, fileName))
            out.write(fileContents)
            out.close()
        } catch (e: IOException) {
            Log.d("eror", "ur mom gay")
        }
    }
    private fun createCameraPreviewSession() {
        try {
            val texture = mTextureView!!.surfaceTexture!!

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)

            // This is the output Surface we need to start preview.
            val surface = Surface(texture)

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewRequestBuilder!!.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH)
            mPreviewRequestBuilder!!.addTarget(surface)
            mPreviewRequestBuilder!!.addTarget(mImageReader!!.surface)


            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice!!.createCaptureSession(
                Arrays.asList(surface, mImageReader!!.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        // The camera is already closed
                        if (null == mCameraDevice) {
                            return
                        }

                        // When the session is ready, we start displaying the preview.
                        mCaptureSession = cameraCaptureSession
                        try {
                            // Auto focus should be continuous for camera preview.
                            mPreviewRequestBuilder!!.set(
                                CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                            )

                            // Finally, we start displaying the camera preview.
                            mPreviewRequest = mPreviewRequestBuilder!!.build()
                            mCaptureSession!!.setRepeatingRequest(
                                mPreviewRequest!!,
                                null, mBackgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(
                        cameraCaptureSession: CameraCaptureSession
                    ) {
                        showToast("Failed")
                    }
                }, null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            AlertDialog.Builder(this@MainActivity)
                .setMessage("R string request permission")
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity, arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which -> finish() })
                .create()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.size != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@MainActivity,
                    "ERROR: Camera permissions not granted",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground")
        mBackgroundThread!!.start()
        mBackgroundHandler = Handler(mBackgroundThread!!.getLooper())
    }

    private fun stopBackgroundThread() {
        mBackgroundThread!!.quitSafely()
        try {
            mBackgroundThread!!.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    private fun chooseOptimalSize(
        choices: Array<Size>, textureViewWidth: Int,
        textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size
    ): Size? {

        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough: MutableList<Size> = ArrayList()
        // Collect the supported resolutions that are smaller than the preview Surface
        val notBigEnough: MutableList<Size> = ArrayList()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight && option.height == option.width * h / w) {
                if (option.width >= textureViewWidth &&
                    option.height >= textureViewHeight
                ) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        return if (bigEnough.size > 0) {
            Collections.min(bigEnough, CompareSizesByArea())
        } else if (notBigEnough.size > 0) {
            Collections.max(notBigEnough, CompareSizesByArea())
        } else {
            Log.e("Camera2", "Couldn't find any suitable preview size")
            choices[0]
        }
    }
    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        if (null == mTextureView || null == mPreviewSize) {
            return
        }
        val rotation = windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0F, 0F, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(
            0F, 0F, mPreviewSize!!.height.toFloat(),
            mPreviewSize!!.width.toFloat()
        )
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 === rotation || Surface.ROTATION_270 === rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                viewHeight.toFloat() / mPreviewSize!!.height,
                viewWidth.toFloat() / mPreviewSize!!.width
            )
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
        } else if (Surface.ROTATION_180 === rotation) {
            matrix.postRotate(180F, centerX, centerY)
        }
        mTextureView!!.setTransform(matrix)
    }

    internal class CompareSizesByArea : Comparator<Size?> {

        override fun compare(p0: Size?, p1: Size?): Int {
            if (p1 != null) {
                if (p0 != null) {
                    return java.lang.Long.signum(
                        p0.width.toLong() * p0.height -
                                p1.width.toLong() * p1.height
                    )
                }
            }
            return 0
        }
    }

    /**
     * Shows a [Toast] on the UI thread.
     *
     * @param text The message to show
     */
    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show() }
    }


    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }


    fun toggleStart() {
        if (processing) {
            processing = false
            val FILENAME = "data_file"
            val string = entries.toString()

            val fos = openFileOutput(FILENAME, MODE_PRIVATE)
            fos.write(string.toByteArray())
            fos.close()
            Log.d("TOGGLE", "untoggled")
            cameraStatus = false;
            closeCamera()
        } else{
            processing = true
            Log.d("TOGGLE", "toggled")
            openCamera(MAX_PREVIEW_HEIGHT, MAX_PREVIEW_WIDTH)
        }
    }
}
