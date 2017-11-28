package org.utils;
import android.util.Log;

/**
 * Created by uelordi on 6/10/17.
 */

public class JniManager {
    static String ext="";


    private static boolean mAreLoadedTheLibraries = false;

    private static final String LOG_TAG = "JniManager";

    public static void loadLibraries(String [] loadLibraryList) {

        for (String aLibraryList_withPrefix : loadLibraryList) {
            try {
                Log.v(LOG_TAG, "loading library -> " + aLibraryList_withPrefix + ext);
                System.loadLibrary(aLibraryList_withPrefix + ext);
            } catch (UnsatisfiedLinkError e) {
                System.err.println("Public native code library '" + aLibraryList_withPrefix + "' failed to load.\n" + e);
                Log.e(LOG_TAG, e.getMessage());
            }
        }
    }

    /**
     * @param resourceDir: path to the resource directory
     */
    public static native void init(String resourceDir,
                                   int inputWidth,
                                   int inputHeight,
                                   int neuralNetType);

    /**
     * \Brief processes the image through the neural net.
     * @param colorImage:
     * @param greyImage
     */
    public static native void process(long colorImage, long greyImage);

    /**
     * \Brief make the call to start processing
     */
    public static native void start();

    /**
     * \Brief make stop function to stop processing:
     */
    public static native void stop();

}
