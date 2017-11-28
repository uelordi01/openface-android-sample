#include <jni.h>
#include "native-lib.h"
#include "fps.h"
#include <cmath>
#include <opencv2/opencv.hpp>
#include <sstream>

#include "NativeCode.h"

#include <android/log.h>

#define  LOG_TAG    "OpenFaceJNI"

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

Native* m_nativeCode = nullptr;
bool m_isInitFinished = false;

JNIEXPORT void JNICALL
Java_org_utils_JniManager_init(JNIEnv *env, jclass type, jstring resourceDir_,
                               jint inputWidth, jint inputHeight,
                               jint neuralNetType) {
    if (!m_isInitFinished) {
        LOGE("Start initialization process");
        const char *resourceDir = env->GetStringUTFChars(resourceDir_, 0);

        // TODO
        std::string resourceDirStr = std::string(resourceDir);
        m_nativeCode = new Native(resourceDirStr);
        m_nativeCode->init(std::string(), std::string());
        LOGE("Finished init call!");

//    m_clnf_model = LandmarkDetector::CLNF(std::string(deepNetFilename) + "/main_clnf_ibug_glasses_movile.txt");
//    m_det_parameters.model_location = std::string(deepNetFilename) + "/main_clnf_ibug_glasses_movile.txt";
//    m_det_parameters.multi_view = false;
//    m_det_parameters.track_gaze = false;
//    m_det_parameters.validate_detections = false;
//    mFps = new Fps();

        m_isInitFinished = true;
        env->ReleaseStringUTFChars(resourceDir_, resourceDir);
    }
}

JNIEXPORT void JNICALL
Java_org_utils_JniManager_process(JNIEnv *env, jclass type, jlong colorImage, jlong greyImage) {
    cv::Mat &colorImg = *(cv::Mat *) colorImage;
    cv::Mat outResult;

    if (m_isInitFinished) m_nativeCode->processFrame(colorImg);
}

JNIEXPORT void JNICALL
Java_org_utils_JniManager_start(JNIEnv *env, jclass type) {

}

JNIEXPORT void JNICALL
Java_org_utils_JniManager_stop(JNIEnv *env, jclass type) {

    // TODO

}