/*
 * MocketInputStreamWrapper.h
 *
 * This file is part of the IHMC Mockets Library
 * Copyright (c) IHMC. All Rights Reserved.
 *
 * Usage restricted to not-for-profit use only.
 * Contact IHMC for other types of licenses.
 */

#ifndef INCL_MOCKET_INPUT_STREAM_WRAPPER
#define INCL_MOCKET_INPUT_STREAM_WRAPPER

#include "jni.h"

#ifdef __cplusplus
    extern "C" {
#endif

JNIEXPORT void JNICALL Java_us_ihmc_mockets_MocketInputStream_init (JNIEnv *pEnv, jobject joThis, jobject joMocket);
JNIEXPORT jint JNICALL Java_us_ihmc_mockets_MocketInputStream_read (JNIEnv *pEnv, jobject joThis);
JNIEXPORT jint JNICALL Java_us_ihmc_mockets_MocketInputStream_nativeRead (JNIEnv *pEnv, jobject joThis, jbyteArray jbaBuf, jint jiOff, jint jiLen);

#ifdef __cplusplus
    }
#endif

#endif //INCL_MOCKET_INPUT_STREAM_WRAPPER

