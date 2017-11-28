package org.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.util.Iterator;
import java.util.Map;

/**
 * Created by uelordi on 31/05/17.
 */

public class LoadConfigurationsTask extends android.os.AsyncTask<Void,Void,Boolean> {
    private Context m_context;

    public enum NeuralType{
        NT_CAFFE,
        NT_CAFFE2
    }
    private IOnModelsConfigurationListener m_conf_callback;
    private static final String LOG_TAG = "LoadConfigurationsTask";
    private static Map<Integer,String> m_resources;
    private static String m_filenetDefinition = "";
    private static String m_weightsDefinition = "";
    private int mInputWidth = 0, mInputHeight = 0;
    private  NeuralType mNeuralType = NeuralType.NT_CAFFE;

    private static final boolean FORCE_CONFIG_RESET = true;

    private  boolean isCallbackDefined = false;
    private  boolean areConfigResourcesDefined = false;
    private  boolean areDNNModelsDefined = false;
    private  boolean iSNeuralTypeDefined = false;
    private boolean isInputSizeDefined = false;



    public void setCallback(Context c, IOnModelsConfigurationListener callback ) {
        m_context = c;
        m_conf_callback = callback;
        if(m_conf_callback != null) {
            isCallbackDefined = true;
        } else {
            Log.e(LOG_TAG, "Error your callback referencence is null," +
                    " please recheck your setcallback call");
            isCallbackDefined = false;
        }

    }
    public void setInputSize(int inputWidth, int inputHeight) {
        mInputWidth = inputWidth;
        mInputHeight = inputHeight;
        isInputSizeDefined = true;
    }
    public void setConfigurationResources(Map<Integer,String> resources) {
//        m_resources = resources;
//        Iterator it = resources.entrySet().iterator();
//        Map.Entry pair;
//        pair = (Map.Entry)it.next();
//        m_weightsDefinition = pair.getValue().toString();
//        pair = (Map.Entry)it.next();
//        m_filenetDefinition= pair.getValue().toString();
        areConfigResourcesDefined = true;
        areDNNModelsDefined = true;
    }
    public void setDNNModelsToWrite(String filenetDefinition, String filenetWeights) {
        m_filenetDefinition = filenetDefinition;
        m_weightsDefinition = filenetWeights;
        areDNNModelsDefined = true;
    }
    public void setNeuralType(NeuralType nnt) {
        mNeuralType = nnt;
        iSNeuralTypeDefined = true;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = false;
        if (m_context != null && m_conf_callback != null) {
            String private_path = m_context.getFilesDir().toString();

            if (isCallbackDefined
                    && areDNNModelsDefined
                    && areConfigResourcesDefined
                    && isInputSizeDefined
                    && isCallbackDefined
                    && iSNeuralTypeDefined) {
//                writeSelectedFilesOnStorage(m_resources);
                String[] libraryList_withPrefix = {
                        "opencv_java3",
                        "native-lib"
                };
                JniManager.loadLibraries(libraryList_withPrefix);
                JniManager.init(private_path, mInputWidth, mInputHeight,
                                mNeuralType.ordinal());
            } else {
                Log.e(LOG_TAG, "please define the requierements," +
                        " callback, resources, dnn models, input Sizes");
            }

            return true;
        } else return false;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            m_conf_callback.onConfigurationFinished();
        }
        else {
            if(m_context != null) {
                Toast.makeText(m_context,"bad use of load configuration",Toast.LENGTH_SHORT).show();
            } else {
                Log.v(LOG_TAG, "m_context not defined");
            }

        }
        super.onPostExecute(aBoolean);
    }
    private boolean writeSelectedFilesOnStorage(Map<Integer,String> resources) {
        Iterator it = resources.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Log.v(LOG_TAG, "writting "+pair.getValue()+" in the private storage");
            WritePrivateStorage.writeFileToPrivateStorage(
                                                        m_context,
                                                        (Integer)pair.getKey(),
                                                        (String)pair.getValue());

        }
        return true;
    }

}

