#include <jvmti.h>
#include "Crash.h"

static jvmtiEnv *jvmti_env;

// 假设这是一个DLL的入口函数

extern "C" JNIEXPORT jint JNICALL
Java_Crash_getVersion(JNIEnv* jni_env, jclass obj) {
    jint res;
    jvmti_env->GetVersionNumber(&res);
    return res;
}


extern "C"
int init_agent(JavaVM *vm, void *reserved) {
        jint rc;
            /* Get JVMTI environment */
            rc = vm->GetEnv((void **)&jvmti_env, JVMTI_VERSION_1_2);
                if (rc != JNI_OK) {
                            fprintf(stderr, "ERROR: arthas vmtool Unable to create jvmtiEnv, GetEnv failed, error=%d\n", rc);
                                    return -1;
                                        }

                    jvmtiCapabilities capabilities = {0};
                        capabilities.can_tag_objects = 1;
                            jvmtiError error = jvmti_env->AddCapabilities(&capabilities);
                                if (error) {
                                            fprintf(stderr, "ERROR: arthas vmtool JVMTI AddCapabilities failed!%u\n", error);
                                                    return JNI_FALSE;
                                                        }

                                    return JNI_OK;
}

extern "C" JNIEXPORT jint JNICALL
Agent_OnLoad(JavaVM *vm, char *options, void *reserved) {
        return init_agent(vm, reserved);
}

extern "C" JNIEXPORT jint JNICALL
Agent_OnAttach(JavaVM* vm, char* options, void* reserved) {
        return init_agent(vm, reserved);
}

extern "C" JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved) {
        init_agent(vm, reserved);
            return JNI_VERSION_1_6;
}

