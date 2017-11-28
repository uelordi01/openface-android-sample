cmake_minimum_required(VERSION 3.4.1)
SET(OPENFACE_DIR ../../Openface_reduced/lib/local/LandmarkDetector)
SET(NATIVE_CODE_DIR ../../ios/CaptureAndRender/NativeCode)
SET(OpenCV_FOUND true )
FIND_PACKAGE(OpenCV REQUIRED)
ADD_SUBDIRECTORY(${dlib_DIR} ${CMAKE_CURRENT_BINARY_DIR}/dlib)
# Load openface sources
FILE(GLOB OPENFACE_SOURCES
    "${OPENFACE_DIR}/src/*.cpp"
 )
MESSAGE("source ${OPENFACE_SOURCES} ")
SET(GLOB OPENFACE_HEADERS
     "${OPENFACE_DIR}/include/*.h"
    )
# Load native code sources
FILE(GLOB NATIVECODE_SOURCES
    "${NATIVE_CODE_DIR}/*.h"
    "${NATIVE_CODE_DIR}/*.cpp"
)

add_library( # Sets the name of the library.
             native-lib
             SHARED
            ${OPENFACE_SOURCES}
            ${OPENFACE_HEADERS}
            ${NATIVECODE_SOURCES}
            src/main/cpp/native-lib.cpp )

find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

INCLUDE_DIRECTORIES(src/main/cpp/
                  ${OpenCV_INCLUDE_DIRS}
                  ${NATIVE_CODE_DIR}
                  ${OPENFACE_DIR}/include
                 )

target_link_libraries( native-lib
                       ${log-lib}
                       dlib
                       ${OpenCV_LIBS}
                       )