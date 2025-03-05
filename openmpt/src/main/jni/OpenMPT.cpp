#include "OpenMPT.h"

#include <libopenmpt/libopenmpt.h>


#ifdef __cplusplus
extern "C" {
#endif

openmpt_module *p_module = nullptr;

extern "C" JNIEXPORT jboolean JNICALL Java_com_notnotme_openmpt_OpenMPT_load(JNIEnv * env, jobject obj, jbyteArray data) {
    (void) obj;

    if (p_module != nullptr) {
        openmpt_module_destroy(p_module);
        p_module = nullptr;
    }

    const auto size = env->GetArrayLength(data);
    const auto data_ptr = env->GetByteArrayElements(data, nullptr);

    p_module = openmpt_module_create_from_memory2(data_ptr, size, nullptr, nullptr, nullptr, nullptr, nullptr, nullptr, nullptr);
    openmpt_module_ctl_set_text(p_module, "play.at_end", "continue");
    openmpt_module_ctl_set_boolean(p_module, "render.resampler.emulate_amiga", true);
    //openmpt_module_set_render_param(p_module, OPENMPT_MODULE_RENDER_INTERPOLATIONFILTER_LENGTH, 1);
    //openmpt_module_set_render_param(p_module, OPENMPT_MODULE_RENDER_STEREOSEPARATION_PERCENT, 50);
    //openmpt_module_set_render_param(p_module, OPENMPT_MODULE_RENDER_VOLUMERAMPING_STRENGTH, 5);
    //openmpt_module_set_render_param(p_module, OPENMPT_MODULE_RENDER_MASTERGAIN_MILLIBEL, 0);
    env->ReleaseByteArrayElements(data, data_ptr, JNI_ABORT);
    return p_module != nullptr;
}

extern "C" JNIEXPORT void JNICALL Java_com_notnotme_openmpt_OpenMPT_close(JNIEnv * env, jobject obj) {
    (void) env;
    (void) obj;

    if (p_module != nullptr) {
        openmpt_module_destroy(p_module);
        p_module = nullptr;
    }
}

extern "C" JNIEXPORT void JNICALL Java_com_notnotme_openmpt_OpenMPT_decode(JNIEnv * env, jobject obj, jfloatArray buffer, jint freq) {
    (void) env;
    (void) obj;

    if (p_module == nullptr) {
        return;
    }

    const auto buffer_size = env->GetArrayLength(buffer);
    const auto buffer_frame_count = buffer_size / 2;
    const auto data_ptr = env->GetFloatArrayElements(buffer, nullptr);
    auto frame_reads = openmpt_module_read_interleaved_float_stereo(p_module, freq, buffer_frame_count, data_ptr);

    const auto size = static_cast<jsize>(frame_reads * 2);
    env->SetFloatArrayRegion(buffer, 0, size, data_ptr);
}

extern "C" JNIEXPORT jint JNICALL Java_com_notnotme_openmpt_OpenMPT_getPattern(JNIEnv * env, jobject obj) {
    (void) env;
    (void) obj;

    return p_module == nullptr ? 0 : openmpt_module_get_current_pattern(p_module);
}

extern "C" JNIEXPORT jint JNICALL Java_com_notnotme_openmpt_OpenMPT_getRow(JNIEnv * env, jobject obj) {
    (void) env;
    (void) obj;

    return p_module == nullptr ? 0 : openmpt_module_get_current_row(p_module);
}


#ifdef __cplusplus
}
#endif
