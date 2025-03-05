cmake_minimum_required(VERSION 3.10.2)

# Include libopenmtp includes
include_directories(src/main/jni)

# Sets library files.
add_library(
    OpenMPT
    SHARED
    src/main/jni/OpenMPT.cpp
)

# Include libopenmtp so files (must be precompiled)
file(
    GLOB LIBRARIES
    "src/main/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/*.so"
)

#Links against
target_link_libraries(
    OpenMPT
    ${log-lib}
    ${LIBRARIES}
)

