#include <jni.h>
#include "native-lib.h"
#include "fps.h"
#include <cmath>
#include <opencv2/opencv.hpp>
#include "LandmarkDetectorModel.h"
#include "LandmarkDetectorFunc.h"
#include "LandmarkDetectorParameters.h"
#include "LandmarkDetectorUtils.h"
#include <sstream>
LandmarkDetector::FaceModelParameters m_det_parameters;
LandmarkDetector::CLNF m_clnf_model;
Fps *mFps;
JNIEXPORT void JNICALL
Java_org_utils_JniManager_init(JNIEnv *env, jclass type, jstring deepNetFilename_,
                               jstring weightsFilename_, jint inputWidth, jint inputHeight,
                               jint neuralNetType) {
    const char *deepNetFilename = env->GetStringUTFChars(deepNetFilename_, 0);
    const char *weightsFilename = env->GetStringUTFChars(weightsFilename_, 0);

    // TODO
    m_clnf_model = LandmarkDetector::CLNF(std::string(deepNetFilename) + "/main_clnf_ibug_glasses_movile.txt");
    m_det_parameters.model_location = std::string(deepNetFilename) + "/main_clnf_ibug_glasses_movile.txt";
    m_det_parameters.multi_view = false;
    m_det_parameters.track_gaze = false;
    m_det_parameters.validate_detections = false;
    mFps = new Fps();
    env->ReleaseStringUTFChars(deepNetFilename_, deepNetFilename);
    env->ReleaseStringUTFChars(weightsFilename_, weightsFilename);
}

JNIEXPORT void JNICALL
Java_org_utils_JniManager_process(JNIEnv *env, jclass type, jlong colorImage, jlong greyImage) {

    cv::Mat &colorImg = *(cv::Mat *) colorImage;
    cv::Mat outResult;
//    int value = round(mFps->checkFps());
//    std::stringstream ss;
//    ss << "Fps:" << value;
//    cv::putText(colorImg, ss.str().c_str(), cv::Point(20,20),+ CV_FONT_HERSHEY_SIMPLEX, 0.5, cv::Scalar(0,255,0));

}

JNIEXPORT void JNICALL
Java_org_utils_JniManager_start(JNIEnv *env, jclass type) {
    mFps->start();
    // TODO

}

JNIEXPORT void JNICALL
Java_org_utils_JniManager_stop(JNIEnv *env, jclass type) {

    // TODO

}