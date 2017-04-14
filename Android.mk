LOCAL_PATH:= $(call my-dir)   
include $(CLEAR_VARS)   
LOCAL_MODULE_TAGS := user  
LOCAL_SRC_FILES := $(call all-subdir-java-files)   
LOCAL_PACKAGE_NAME := zph.zhjx.com.chat 
LOCAL_JNI_SHARED_LIBRARIES := libgeosot 
include $(BUILD_PACKAGE)   
#include $(call all-makefiles-under,$(LOCAL_PATH))