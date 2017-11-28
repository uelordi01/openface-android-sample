package com.example.openface.openfacedemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.utils.JniManager;
import org.utils.KTUtils;
import org.utils.LoadConfigurationsTask;
import org.utils.WritePrivateStorage;

import java.util.HashMap;
import java.util.Iterator;
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
        mOpenCvCameraView.setMaxFrameSize(320, 240);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }
    private void initResources() {

        // Define your caffe models:
        final String CAFFE_MODEL[] = {
                "main_clnf_inner.txt",
                "ccnf_patches_1_ibug_glasses.txt",
                "main_clnf_general_back.txt",
                "validator_cnn.txt",
                "tris_68_full.txt",
                "ccnf_patches_1_wild.txt",
                "clnf_right_synth.txt",
                "ccnf_patches_100_inner.txt",
                "left_ccnf_patches_1.50_synth_lid_.txt",
                "main_clnf_ibug_glasses_movile.txt",
                "main_clnf_synth_left.txt",
                "ccnf_patches_1.50_synth_lid_.txt",
                "in_the_wild_aligned_pdm_68.txt",
                "ccnf_patches_05_wild.txt",
                "ccnf_patches_0.25_ibug_glasses.txt",
                "main_clnf_wild.txt",
                "clnf_inner.txt",
                "left_ccnf_patches_1.00_synth_lid_.txt",
                "clnf_left_synth.txt",
                "ccnf_patches_0.5_general.txt",
                "haar_align.txtt",
                "ccnf_patches_0.25_general.txt",
                "clnf_wild.txt",
                "clnf_general.txt",
                "pdm_68_aligned_ibug_glasses.txt",
                "ccnf_patches_0.35_ibug_glasses.txt",
                "ccnf_patches_0.25_wild.txt",
                "pdm_51_inner.txt",
                "clnf_ibug_glasses.txt",
                "ccnf_patches_1.00_synth_lid_.txt",
                "ccnf_patches_035_general.txt",
                "tris_68.txt",
                "validator_general_68.txt",
                "pdm_28_eye_3d_closed.txt",
                "ccnf_patches_0.35_wild.txt",
                "ccnf_patches_0.5_ibug_glasses.txt",
                "main_clnf_synth_right.txt",
                "pdm_28_l_eye_3d_closed.txt"
        };

        //Define the external resources that are going to write in the internal memory.
        Map<Integer, String> resources = new HashMap<Integer,String>();
        resources.put(R.raw.main_clnf_inner, "main_clnf_inner.txt");
        resources.put(R.raw.ccnf_patches_1_ibug_glasses, "ccnf_patches_1_ibug_glasses.txt");
        resources.put(R.raw.main_clnf_general_back, "main_clnf_general_back.txt");
        resources.put(R.raw.validator_cnn, "validator_cnn.txt");
        resources.put(R.raw.tris_68_full, "tris_68_full.txt");
        resources.put(R.raw.ccnf_patches_1_wild, "ccnf_patches_1_wild.txt");
        resources.put(R.raw.clnf_right_synth, "clnf_right_synth.txt");
        resources.put(R.raw.ccnf_patches_100_inner, "ccnf_patches_100_inner.txt");
        resources.put(R.raw.left_ccnf_patches_150_synth_lid_, "left_ccnf_patches_1.50_synth_lid_.txt");
        resources.put(R.raw.main_clnf_ibug_glasses_movile, "main_clnf_ibug_glasses_movile.txt");
        resources.put(R.raw.main_clnf_synth_left, "main_clnf_synth_left.txt");
        resources.put(R.raw.ccnf_patches_150_synth_lid_, "ccnf_patches_1.50_synth_lid_.txt");
        resources.put(R.raw.in_the_wild_aligned_pdm_68, "in_the_wild_aligned_pdm_68.txt");
        resources.put(R.raw.ccnf_patches_05_wild, "ccnf_patches_05_wild.txt");
        resources.put(R.raw.ccnf_patches_025_ibug_glasses, "ccnf_patches_0.25_ibug_glasses.txt");
        resources.put(R.raw.main_clnf_wild, "main_clnf_wild.txt");
        resources.put(R.raw.clnf_inner, "clnf_inner.txt");
        resources.put(R.raw.left_ccnf_patches_100_synth_lid_, "left_ccnf_patches_1.00_synth_lid_.txt");
        resources.put(R.raw.clnf_left_synth, "clnf_left_synth.txt");
        resources.put(R.raw.ccnf_patches_05_general, "ccnf_patches_0.5_general.txt");
        resources.put(R.raw.haar_align, "haar_alignn.txt");
        resources.put(R.raw.ccnf_patches_025_general, "ccnf_patches_0.25_general.txt");
        resources.put(R.raw.clnf_wild, "clnf_wild.txt");
        resources.put(R.raw.clnf_general, "clnf_general.txt");
        resources.put(R.raw.pdm_68_aligned_ibug_glasses, "pdm_68_aligned_ibug_glasses.txt");
        resources.put(R.raw.ccnf_patches_035_ibug_glasses, "ccnf_patches_0.35_ibug_glasses.txt");
        resources.put(R.raw.ccnf_patches_025_wild, "ccnf_patches_0.25_wild.txt");
        resources.put(R.raw.pdm_51_inner, "pdm_51_inner.txt");
        resources.put(R.raw.clnf_ibug_glasses, "clnf_ibug_glasses.txt");
        resources.put(R.raw.ccnf_patches_100_synth_lid_, "ccnf_patches_1.00_synth_lid_.txt");
        resources.put(R.raw.ccnf_patches_035_general, "ccnf_patches_035_general.txt");
        resources.put(R.raw.tris_68, "tris_68.txt");
        resources.put(R.raw.validator_general_68, "validator_general_68.txt");
        resources.put(R.raw.pdm_28_eye_3d_closed, "pdm_28_eye_3d_closed.txt");
        resources.put(R.raw.ccnf_patches_035_wild, "ccnf_patches_0.35_wild.txt");
        resources.put(R.raw.ccnf_patches_05_ibug_glasses, "ccnf_patches_0.5_ibug_glasses.txt");
        resources.put(R.raw.main_clnf_synth_right, "main_clnf_synth_right.txt");
        resources.put(R.raw.pdm_28_l_eye_3d_closed, "pdm_28_l_eye_3d_closed.txt");

        Iterator it = resources.entrySet().iterator();
        WritePrivateStorage privateFileHandler = new WritePrivateStorage();
        Context context = getApplicationContext();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            privateFileHandler.writeFileToPrivateStorage(context, (Integer)pair.getKey(),(String)pair.getValue());
        }

//        configuration_resources.put(R.raw.res10_300x300_ssd_iter_140000_net, CAFFE_MODEL[0]);
//        configuration_resources.put(R.raw.res10_300x300_ssd_iter_140000_weights, CAFFE_MODEL[1]);

        // create a loading configuration task:

        LoadConfigurationsTask loadTask = new LoadConfigurationsTask();
        loadTask.setCallback(getApplicationContext(), this);
        loadTask.setConfigurationResources(resources);
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

