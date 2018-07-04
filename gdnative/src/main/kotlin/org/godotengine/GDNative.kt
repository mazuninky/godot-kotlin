import kotlinx.cinterop.*
import godot.*

object GDNative {
    private var api: CPointer<godot_gdnative_core_api_struct>? = null
    private var initialized: Boolean = false

    fun init(options: godot_gdnative_init_options) {
        api = options.api_struct
        initialized = true
    }

    fun terminate() {
        api = null
        initialized = false
    }
}