#include <jni.h>
#include <string>

extern "C"
{
JNIEXPORT void JNICALL
    Java_org_utils_JniManager_init(JNIEnv *env, jclass type, jstring resourceDir,
                                          jint inputWidth,
                                          jint inputHeight,
                                          jint neuralType);
JNIEXPORT void JNICALL
    Java_org_utils_JniManager_process(JNIEnv *env, jclass type, jlong colorImage,
                                             jlong greyImage);
JNIEXPORT void JNICALL
    Java_org_utils_JniManager_start(JNIEnv *env, jclass type);

JNIEXPORT void JNICALL
    Java_org_utils_JniManager_stop(JNIEnv *env, jclass type);
}