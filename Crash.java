import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.foreign.ValueLayout.*;


public class Crash {
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
		var arena = Arena.global();
		// 获取 JavaVM 指针
		MemorySegment javaVMPtr = getJavaVM();
		if (javaVMPtr == null) {
			System.out.println("Failed to get JavaVM pointer");
			return;
		}
		
		var functions = JavaVM_.functions(javaVMPtr);
		
		// 获取 JVMTI 环境
		MemorySegment jvmtiEnv = getJVMTIEnv(javaVMPtr, functions);
		if (jvmtiEnv == null) {
			System.out.println("Failed to get JVMTI environment");
			return;
		}
		MemorySegment jvmtiFunctions = _jvmtiEnv.functions(jvmtiEnv);
		
		// 分配内存用于存储版本号
		MemorySegment versionPtr = arena.allocate(JAVA_INT);
		
		MemorySegment getVersionNumber = jvmtiInterface_1_.GetVersionNumber(jvmtiFunctions);
		var result = jvmtiInterface_1_.GetVersionNumber.invoke(getVersionNumber, jvmtiEnv, versionPtr);
		// 调用 GetVersionNumber
		// int result = GetVersionNumber(jvmtiEnv, versionPtr);
		
		if (result == 0) {
			int version = versionPtr.get(JAVA_INT, 0);
			System.out.printf("JVMTI Version: %d\n", version);
		} else {
			System.out.printf("outor getting JVMTI version: %d\n", result);
		}
		
		
		MemorySegment addCapabilities = jvmtiInterface_1_.AddCapabilities(jvmtiFunctions);
		MemorySegment cap = arena.allocate(jvmtiCapabilities.sizeof());
		cap.set(JAVA_SHORT, 0L, (short) 1);
		
		int addCapabilitiesResult = jvmtiInterface_1_.AddCapabilities.invoke(addCapabilities, jvmtiEnv, cap);
		System.out.println("add cap result: " + addCapabilitiesResult);
		
		
		// typedef jvmtiIterationControl (JNICALL *jvmtiHeapObjectCallback)
		//     (jlong class_tag,
		//      jlong size,
		//      jlong* tag_ptr,
		//      void* user_data);
		// MemorySegment mh = jvmtiInterface_1_.IterateOverInstancesOfClass.allocate(f, arena);
		MemorySegment mem = jvmtiHeapObjectCallback.allocate((_, _, _, _) -> 0, arena);
		
		MemorySegment interClass = jvmtiInterface_1_.IterateOverInstancesOfClass(jvmtiFunctions);
		// MemorySegment i = arena.allocate(JAVA_INT);
		// i.set(JAVA_INT, 0, 0);
		int invoke = jvmtiInterface_1_.IterateOverInstancesOfClass.invoke(interClass,
				jvmtiEnv,
				classAddress.reinterpret(8),
				3,
				arena.allocate(JAVA_INT),
				MemorySegment.NULL
		);
		
		System.out.println("iterator result: " + invoke);
		
	}
	
	private static MemorySegment getJavaVM() {
		Arena arena = Arena.global();
		// 分配内存用于存储 JavaVM 指针
		MemorySegment vmPtr = arena.allocate(ADDRESS);
		MemorySegment numVMs = arena.allocate(ValueLayout.JAVA_INT);
		numVMs.set(ValueLayout.JAVA_INT, 0, 0);
		
		// 调用 JNI_GetCreatedJavaVMs
		int result = jvmti_h.JNI_GetCreatedJavaVMs(vmPtr, 1, numVMs);
		
		if (result == 0) {
			return vmPtr.get(ADDRESS, 0).reinterpret(JavaVM_.sizeof());
		}
		return null;
	}
	
	private static MemorySegment getJVMTIEnv(MemorySegment vm, MemorySegment functions) {
		Arena arena = Arena.global();
		// 分配内存用于存储 JVMTI 环境指针
		MemorySegment envPtr = arena.allocate(ADDRESS);
		// 调用 GetEnv 获取 JVMTI 环境
		MemorySegment env = JNIInvokeInterface_.GetEnv(functions);
		int result = JNIInvokeInterface_.GetEnv.invoke(env, vm, envPtr, 805371904);
		if (result == 0) {
			return envPtr.get(ADDRESS, 0).reinterpret(_jvmtiEnv.sizeof());
		}
		return null;
		
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
		MemorySegment clazzName = global.allocateFrom(c.getName().replace(".", "/"));
		
		// MemorySegment classNameFp = JNINativeInterface_.FindClass.invoke(findClassFP, jniEnvPointer, clazzName);
		MemorySegment memorySegment = FindClass(c).reinterpret(8);
		// System.out.println("classNameFp: " + classNameFp + ", other: " + memorySegment);
		
		
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
		// getJNINativeInterface_();
	}
	

	
	
	public MemorySegment FindClass(Class<?> c) throws Throwable {
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
	
	
	private static final long ADDRESS_SIZE = ValueLayout.ADDRESS.byteSize();
	
}
