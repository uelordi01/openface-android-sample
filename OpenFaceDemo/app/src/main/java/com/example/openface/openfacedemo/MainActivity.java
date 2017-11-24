package com.example.openface.openfacedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.utils.JniManager;
import org.utils.KTUtils;
import org.utils.LoadConfigurationsTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
    org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2,
    org.utils.IOnModelsConfigurationListener {
    private static final String LOG_TAG = "MainActivity";
    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean isReadyToProcessDNN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        KTUtils.askPermissions(this);
        initResources();
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setMaxFrameSize(640, 480);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }
    private void initResources() {

        // Define your caffe models:
        final String CAFFE_MODEL[] = {
                "",
                ""
        };

        //Define the external resources that are going to write in the internal memory.
        Map<Integer, String> configuration_resources = new HashMap<Integer,String>();
//        configuration_resources.put(R.raw.res10_300x300_ssd_iter_140000_net, CAFFE_MODEL[0]);
//        configuration_resources.put(R.raw.res10_300x300_ssd_iter_140000_weights, CAFFE_MODEL[1]);

        // create a loading configuration task:

        LoadConfigurationsTask loadTask = new LoadConfigurationsTask();
        loadTask.setCallback(getApplicationContext(), this);
        loadTask.setConfigurationResources(configuration_resources);
        loadTask.setDNNModelsToWrite(CAFFE_MODEL[0], CAFFE_MODEL[1]);
        loadTask.setInputSize(300, 300);
        loadTask.setNeuralType(LoadConfigurationsTask.NeuralType.NT_CAFFE);
        loadTask.execute();


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(LOG_TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(LOG_TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    @Override
    public void onCameraViewStarted(int i, int i1) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame cvCameraViewFrame) {
        mRgba = cvCameraViewFrame.rgba();
        mGray = cvCameraViewFrame.gray();
        if (isReadyToProcessDNN) {
            JniManager.process(mRgba.getNativeObjAddr(), mGray.getNativeObjAddr());
        }
        return mRgba;
    }

    @Override
    public void onConfigurationFinished() {
        isReadyToProcessDNN = true;
        Log.v(LOG_TAG, "Configuration finished and loaded correctly");
        JniManager.start();
    }
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(LOG_TAG, "OpenCV loaded successfully");
                    try {
                        mOpenCvCameraView.enableView();
                    } catch (Exception e) {
                        Log.v(LOG_TAG, "Failed loading library mobilenet_dnn");
                    }
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
}

