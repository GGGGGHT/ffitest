# FFICrash

Use Java22 FFI[^1] call JVMTI[^2] function

## java version 
openjdk version "22" 2024-03-19 <br/>
OpenJDK Runtime Environment GraalVM CE 22+36.1 (build 22+36-jvmci-b02)<br/>
OpenJDK 64-Bit Server VM GraalVM CE 22+36.1 (build 22+36-jvmci-b02, mixed mode, sharing)


### generate dynamic library
```sh
gcc -shared -o libmyjvmti.so test.cpp -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -fPIC
```

### run 
```
java --enable-native-access=ALL-UNNAMED --enable-preview --source 22 Crash.java
```



[^1]: https://openjdk.org/jeps/454
[^2]: https://docs.oracle.com/javase/8/docs/platform/jvmti/jvmti.html