import kotlinx.cinterop.*
import GodotApi.*
import platform.posix.strcpy
import konan.internal.*

fun createSimpleClass(godotObject: COpaquePointer?, data: COpaquePointer?): COpaquePointer? {
    val userData = nativeHeap.alloc<user_data_struct>()
    strcpy(userData.data, "Hello World!")
    return userData.ptr
}

fun simpleDestructor(godotObject: COpaquePointer?, methodData: COpaquePointer?, userData: COpaquePointer?) {
    nativeHeap.free(userData!!.rawValue)
}

/*fun simpleGetData(pInstance: COpaquePointer?, pMethodData: COpaquePointer?, pUserData: COpaquePointer?,
                  pNumArgs: Int, godotVariant: COpaquePointer?): CValue<godot_variant> {
    //godot_variant **p_args)
    val data = nativeHeap.alloc<godot_string>()
    val ret = nativeHeap.alloc<godot_variant>()
    val userDataPrt = pUserData!!.reinterpret<user_data_struct>()
    val userData = userDataPrt.pointed
    godot_string_new(data.ptr)
    godot_string_parse_utf8(data.ptr, userData.data.toKString())
    godot_variant_new_string(ret.ptr, data.ptr)
    godot_string_destroy(data.ptr)
    return ret.readValue()
}*/

object GDNative {
    var apiPointer: CPointer<godot_gdnative_core_api_struct>? = null
    var initialized: Boolean = false
}

object NativeScript {
    var apiPointer: CPointer<godot_gdnative_ext_nativescript_api_struct>? = null
    var initialized: Boolean = false
}

@ExportForCppRuntime
@konan.internal.CName("godot_gdnative_init")
fun godotGDnativeInit(options: CPointer<godot_gdnative_init_options>?) {
    println("godot_gdnative_init")
    GDNative.apiPointer = options!!.pointed.api_struct
    GDNative.initialized = true

    val api = GDNative.apiPointer!!.pointed

    loop@ for (i in 0 until api.num_extensions) {
        val extension = api.extensions!![i]!!.pointed
        if (extension.type == GDNATIVE_EXT_NATIVESCRIPT) {
            NativeScript.apiPointer = api.extensions!![i]!!.reinterpret()
            NativeScript.initialized = true
        }
    }
}

@ExportForCppRuntime
@konan.internal.CName("godot_nativescript_init")
fun godotNativescriptInit(pHandle: COpaquePointer?) {
    println("godot_nativescript_init")
    val instanceFunc = nativeHeap.alloc<godot_instance_create_func>()
    instanceFunc.create_func = staticCFunction(::createSimpleClass)
    val destoryFunc = nativeHeap.alloc<godot_instance_destroy_func>()
    destoryFunc.destroy_func = staticCFunction(::simpleDestructor)

    val nativeApi = NativeScript.apiPointer?.pointed
    nativeApi?.godot_nativescript_register_class?.invoke(pHandle, CLASS_NAME, REFERENCE_VALUE, instanceFunc.readValue(), destoryFunc.readValue())

    // godot_nativescript_register_property(pHandle, "SIMPLE")

    /* val methodInstance = nativeHeap.alloc<godot_instance_method>()
     methodInstance.method = staticCFunction(::simpleGetData)

     val attributes = nativeHeap.alloc<godot_method_attributes>()
     attributes.rpc_type = godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_DISABLED

     godot_nativescript_register_method(pHandle, "SIMPLE", "get_data",
             attributes.readValue(), methodInstance.readValue())*/

}

@ExportForCppRuntime
@konan.internal.CName("godot_gdnative_terminate")
fun godotGDnativeTerminate(options: CPointer<godot_gdnative_init_options>) {
    println("godot_gdnative_terminate")
    GDNative.apiPointer = null
    GDNative.initialized = false
    NativeScript.apiPointer = null
    NativeScript.initialized = false
}
