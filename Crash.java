import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.foreign.ValueLayout.*;


public class Crash {
	public static Arena GLOBAL = Arena.global();
	private static final int JVMTI_ERROR_NONE = 0;
	static SymbolLookup symbolLookup = SymbolLookup.loaderLookup();
	static Linker linker = Linker.nativeLinker();
	static FunctionDescriptor JNI_GetCreatedJavaVMs_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS);
	static FunctionDescriptor GET_JNIENV_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
	static FunctionDescriptor GetEnv_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
	
	
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
	
	public static native int getVersion();
	
	public static void test(MemorySegment classAddress) throws NoSuchMethodException, IllegalAccessException {
	
	}
	

	public static void main(String[] args) throws Throwable {
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
		
		jniEnvPointer = ((MemorySegment) GET_JNIENV_MH.invokeExact(JavaVM_, 0x00150000)).reinterpret(Long.MAX_VALUE);
		// jniEnvPointer = jniEnvPointer;
		int a = 4;
		MemorySegment functions = jniEnvPointer.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		var _ = functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * a++);
		var _ = functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * a++);
		FindClassFp = functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * a++);
		
		var JNI_Functions = jniEnvPointer.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		MethodHandle getEnv_MH = linker.downcallHandle(GetEnv_DESCRIPTOR).bindTo(GetEnvFP);
		var JVMTI_ENV = global.allocate(ValueLayout.ADDRESS);
		var _ = (int) getEnv_MH.invokeExact(JavaVM_, JVMTI_ENV, 0x30010000);
		MemorySegment jvmTiEnvPointer = JVMTI_ENV.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		var jvmti_functions = jvmTiEnvPointer.get(ValueLayout.ADDRESS, 0).reinterpret(Long.MAX_VALUE);
		
		MemorySegment findClassFP = JNINativeInterface_.FindClass(JNI_Functions);
		Class c = ThreadPoolExecutor.class;
		
		System.out.println(c.getName().replace(".","/"));
		MemorySegment clazzName = global.allocateFrom(c.getName().replace(".", "/"));
		
		MemorySegment classNameFp = JNINativeInterface_.FindClass.invoke(findClassFP, jniEnvPointer, clazzName);
		MemorySegment memorySegment = FindClass(c).reinterpret(8);
		System.out.println("classNameFp: " + classNameFp + ", other: " + memorySegment);
		
		
		var GetVersionNumberPointer = jvmti_functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 87);
		var GetSourcePointer = jvmti_functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 49);
		var GetCapabilitiesPointer = jvmti_functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 88);
		var AddCapabilitiesPointer = jvmti_functions.get(ValueLayout.ADDRESS, ADDRESS_SIZE * 141);
		var GetVersionNumber_MH = linker.downcallHandle(FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS)).bindTo(GetVersionNumberPointer);
		var version = global.allocate(ValueLayout.JAVA_INT);
		var _ = (int) GetVersionNumber_MH.invokeExact(jvmTiEnvPointer, version);
		
		var newversion = global.allocate(ValueLayout.JAVA_INT);
		var _ = jvmtiInterface_1_.GetVersionNumber.invoke(GetVersionNumberPointer, jvmTiEnvPointer, newversion);
		// System.out.println(getVersion());
		System.out.println("jni: " + getVersion());
		System.out.println("ffi: " + version.get(ValueLayout.JAVA_INT, 0));
		System.out.println("FFI: " + newversion.get(ValueLayout.JAVA_INT, 0));
		test(memorySegment);
	}
	
	public static MemorySegment FindClass(Class<?> c) throws Throwable {
		return (MemorySegment) FindClassMH.invokeExact(
				FindClassFp,
				jniEnvPointer,
				Arena.global().allocateFrom(c.getName().replace(".", "/")));
	}
	
	final static MethodHandle FindClassMH = Linker.nativeLinker().downcallHandle(FunctionDescriptor.of(
			/*jclass*/ValueLayout.ADDRESS,
			/*JNIEnv *env */ValueLayout.ADDRESS,
			/*const char *name*/ ValueLayout.ADDRESS
	));
	
	static MemorySegment FindClassFp;
	static MemorySegment jniEnvPointer = null;
}
