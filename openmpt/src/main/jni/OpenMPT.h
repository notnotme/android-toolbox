#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif


extern "C" JNIEXPORT jboolean JNICALL Java_com_notnotme_openmpt_OpenMPT_load(JNIEnv * env, jobject obj, jbyteArray data);
extern "C" JNIEXPORT void JNICALL Java_com_notnotme_openmpt_OpenMPT_close(JNIEnv * env, jobject obj);
extern "C" JNIEXPORT void JNICALL Java_com_notnotme_openmpt_OpenMPT_decode(JNIEnv * env, jobject obj, jfloatArray buffer, jint freq);
extern "C" JNIEXPORT jint JNICALL Java_com_notnotme_openmpt_OpenMPT_getPattern(JNIEnv * env, jobject obj);
extern "C" JNIEXPORT jint JNICALL Java_com_notnotme_openmpt_OpenMPT_getRow(JNIEnv * env, jobject obj);


#ifdef __cplusplus
}
#endif
