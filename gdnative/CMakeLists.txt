cmake_minimum_required(VERSION 3.8)

set(CMAKE_MODULE_PATH ${CMAKE_CURRENT_LIST_DIR}/KotlinCMakeModule)

project(gdnative Kotlin)

cinterop(
        NAME godot
        DEF_FILE src/main/c_interop/godot.def
        COMPILER_OPTS "-I${CMAKE_CURRENT_LIST_DIR}/godot_headers")

konanc_library(
        NAME gdnative
        SOURCES src/main/kotlin/org/godotengine/GDNative.kt
        LIBRARIES godot)

