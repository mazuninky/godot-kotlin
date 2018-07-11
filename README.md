Godot-Kotlin
========
Kotlin language bindings for the [Godot Engine](https://godotengine.org/)'s [GDNative API](https://github.com/GodotNativeTools/godot_headers).

**WARNING:** These bindings are currently still under development

# Setup

1.Clone the repository
```bash
git clone https://github.com/zerotwoone/godot-kotlin
```
2.Build libraries
 ```bash
./gradlew gdnative:build
 ```
3.Install klib into local repository
 ```bash
klib install gdnative/build/konan/libs/${ARCH}/gdnative.klib
 ```
Where ${ARCH} your architecture

 # Tutorial

ASAP
