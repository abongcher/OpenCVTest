#include <jni.h>
#include <string>

#include "opencv2/core.hpp"
#include "opencv2/imgproc.hpp"

#include <android/log.h>

using namespace std;
using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_digitems_opencvtest_MainActivity_testFunc(JNIEnv *env, jclass type, jlong addrRgba) {
    __android_log_print(ANDROID_LOG_INFO, "Native", "called : %s", "testFunc()");

    Mat &img =  *(Mat *) addrRgba;
    cvtColor(img, img, COLOR_RGB2GRAY);

}