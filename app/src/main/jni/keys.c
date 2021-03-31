//
// Created by utkar on 02/03/2021.
//

#include <jni.h>



JNIEXPORT jstring JNICALL
Java_com_wanderland_app_LoginSignUp_LoginActivity_getNativeKey1(JNIEnv *env, jobject thiz) {
    // TODO: implement getNativeKey1()
    return (*env)->  NewStringUTF(env, "QwdqWQQ12ddfSVBNiTUYopsJHFLinamvQWE");
}

JNIEXPORT jstring JNICALL
Java_com_wanderland_app_LoginSignUp_LoginActivity_getNativeKey2(JNIEnv *env, jobject thiz) {
    // TODO: implement getNativeKey2()
    return (*env)->  NewStringUTF(env, "67PiAVCMr90IOPKn");
}

JNIEXPORT jstring JNICALL
Java_com_wanderland_app_SplashScreen_SplashScreenActivity_getNativeKeySplashScreen1(JNIEnv *env,
                                                                                   jobject thiz) {
    // TODO: implement getNativeKeySplashScreen()
    return (*env)->  NewStringUTF(env, "QwdqWQQ12ddfSVBNiTUYopsJHFLinamvQWE");
}

JNIEXPORT jstring JNICALL
Java_com_wanderland_app_SplashScreen_SplashScreenActivity_getNativeKeySplashScreen2(JNIEnv *env,
                                                                                    jobject thiz) {
    // TODO: implement getNativeKeySplashScreen2()
    return (*env)->  NewStringUTF(env, "67PiAVCMr90IOPKn");
}

JNIEXPORT jstring JNICALL
Java_com_wanderland_app_LoginSignUp_LogOutActivity_getNativeKeyLogOutScreen1(JNIEnv *env,
                                                                             jclass clazz) {
    // TODO: implement getNativeKeyLogOutScreen1()
    return (*env)->  NewStringUTF(env, "QwdqWQQ12ddfSVBNiTUYopsJHFLinamvQWE");
}

JNIEXPORT jstring JNICALL
Java_com_wanderland_app_LoginSignUp_LogOutActivity_getNativeKeyLogOutScreen2(JNIEnv *env,
                                                                             jclass clazz) {
    // TODO: implement getNativeKeyLogOutScreen2()
    return (*env)->  NewStringUTF(env, "67PiAVCMr90IOPKn");
}