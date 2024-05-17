import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Files;
import java.nio.file.Path;


public class Crash {
	public native boolean isInterface(Class clazz);

	static SymbolLookup symbolLookup = SymbolLookup.loaderLookup();
	static Linker linker = Linker.nativeLinker();
	static FunctionDescriptor JNI_GetCreatedJavaVMs_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS);
	static FunctionDescriptor GET_JNIENV_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
	static FunctionDescriptor GetEnv_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
	static FunctionDescriptor FindClass_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
	static FunctionDescriptor IsInterface_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
	
	static {
        System.setProperty("java.library.path", ".");
		Runtime.getRuntime().loadLibrary("myjvmti");
		Runtime.getRuntime().loadLibrary("java");
		String javaHomePath = System.getProperty("java.home", "");
		if (javaHomePath.isBlank()) {
			throw new RuntimeException("cant find java.home!");
		}
		String libName = System.mapLibraryName("jvm");
		
		String jvmPath = javaHomePath + "/lib/server/" + libName;
		if (!Files.exists(Path.of(javaHomePath + "/lib/server/" + libName))) {
			jvmPath = javaHomePath + "/bin/server/" + libName;
		}
		Runtime.getRuntime().load(jvmPath);
	}
	
	void main() throws Throwable {
		MemorySegment jniGetCreatedJavaVM_FP = symbolLookup.find("JNI_GetCreatedJavaVMs").get();
		MethodHandle JNI_GetCreatedJavaVM_MH = linker.downcallHandle(JNI_GetCreatedJavaVMs_DESCRIPTOR).bindTo(jniGetCreatedJavaVM_FP);
		Arena global = Arena.global();
		MemorySegment vm = global.allocate(ValueLayout.ADDRESS);
		MemorySegment numVMs = global.allocate(ValueLayout.JAVA_INT);
		numVMs.set(ValueLayout.JAVA_INT, 0, 0);
		int i = (int) JNI_GetCreatedJavaVM_MH.invokeExact(vm, 1, numVMs);
		MemorySegment JavaVM_ = vm.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		long ADDRESS_SIZE = ValueLayout.ADDRESS.byteSize();
		var JavaVM_functions = JavaVM_.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		var GetEnvFP = JavaVM_functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 6);
		
		MemorySegment JNU_GetEnv_FP = symbolLookup.find("JNU_GetEnv").get();
		MethodHandle GET_JNIENV_MH = linker.downcallHandle(GET_JNIENV_DESCRIPTOR).bindTo(JNU_GetEnv_FP);
		
		MemorySegment jniEnvPointer = ((MemorySegment) GET_JNIENV_MH.invokeExact(JavaVM_, 0x00150000)).reinterpret(Long.MAX_VALUE);
		var JNI_Functions = jniEnvPointer.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		var FindClassFp = JNI_Functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 6);
		
		MethodHandle getEnv_MH = linker.downcallHandle(GetEnv_DESCRIPTOR).bindTo(GetEnvFP);
		var JVMTI_ENV = global.allocate(ValueLayout.ADDRESS);
		var _ = (int) getEnv_MH.invokeExact(JavaVM_, JVMTI_ENV, 0x30010000);
		MemorySegment jvmTiEnvPointer = JVMTI_ENV.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		var jvmti_functions = jvmTiEnvPointer.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		MemorySegment IsInterfaceS = jvmti_functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 54);
		
		MethodHandle FindClassMH = linker.downcallHandle(FindClass_DESCRIPTOR).bindTo(FindClassFp);
		MemorySegment classAddress = (MemorySegment) FindClassMH.invokeExact(jniEnvPointer, global.allocateFrom(Runnable.class.getName().replace(".", "/")));
		
		MemorySegment bool = global.allocate(ValueLayout.JAVA_BOOLEAN);
		
		MethodHandle IsInterface_MH = linker.downcallHandle(IsInterface_DESCRIPTOR).bindTo(IsInterfaceS);
		
		var res = (int) IsInterface_MH.invokeExact(jvmTiEnvPointer, classAddress.reinterpret(8), bool);
		System.out.println(res);
		System.out.printf("Runnable is interface by FFI: %s.\n", bool.get(ValueLayout.JAVA_BOOLEAN, 0));
		System.out.printf("Runnable is interface by JNI: %s.\n ", isInterface(Runnable.class));
	}
}
