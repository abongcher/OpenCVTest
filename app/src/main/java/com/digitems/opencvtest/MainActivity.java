package com.digitems.opencvtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OpenCV";
    JavaCameraView javaCameraView;

    private BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
//            super.onManagerConnected(status);
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    System.loadLibrary("native-lib");
                    Log.i(TAG, "onManagerConnected: success");
                    javaCameraView.enableView();
                    break;
                case LoaderCallbackInterface.INIT_FAILED:
                    Log.e(TAG, "onManagerConnected: init failed" );
                    break;
                case LoaderCallbackInterface.INSTALL_CANCELED:
                    Log.e(TAG, "onManagerConnected: install cancelled" );
                    break;

                    default:
                        Log.i(TAG, "onManagerConnected: default");
                        break;


            }
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView) findViewById(R.id.cameraView);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native static void testFunc(long addrRgba);

    Mat rgba;

    @Override
    public void onCameraViewStarted(int width, int height) {
        rgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        rgba = inputFrame.rgba();
        testFunc(rgba.getNativeObjAddr());
        return rgba;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: called");
        super.onResume();

        if(!OpenCVLoader.initDebug()){
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION , this, mBaseLoaderCallback);
            if(success){
                Log.d(TAG, "OpenCVLoader: init");
            }else{
                Log.e(TAG, "OpenCVLoader: failed");
            }
        }else{
            Log.d(TAG, "OpenCVLoader: called");
            mBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}
