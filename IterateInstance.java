import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.lang.foreign.ValueLayout.*;

public class IterateInstance {
	static Arena GLOBAL = Arena.global();
	static long ADDRESS_SIZE = ADDRESS.byteSize();
	
	static {
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
	
	
	public static void main(String[] args) {
		var javaVMPtr = getJavaVM();
		if (Objects.isNull(javaVMPtr)) {
			System.out.println("Failed to get JavaVM pointer");
			
			return;
		}
		
		var JNIInvokeInterface_FunctionsPtr = JavaVM_.functions(javaVMPtr);
		var jvmtiEnvPtr = getJVMTIEnv(javaVMPtr, JNIInvokeInterface_FunctionsPtr);
		if (Objects.isNull(jvmtiEnvPtr)) {
			System.out.println("Failed to get JVMTI pointer");
			
			return;
		}
		
		var jvmtiInterface_FunctionsPtr = _jvmtiEnv.functions(jvmtiEnvPtr);
		
		var AddCapabilitiesFunctionPtr = jvmtiInterface_1_.AddCapabilities(jvmtiInterface_FunctionsPtr);
		var AddCapabilitiesPtr = GLOBAL.allocate(jvmtiCapabilities.sizeof());
		AddCapabilitiesPtr.set(JAVA_SHORT, 0L, (short) 1);
		
		int addCapabilitiesResult = jvmtiInterface_1_.AddCapabilities.invoke(AddCapabilitiesFunctionPtr, jvmtiEnvPtr, AddCapabilitiesPtr);
		if (addCapabilitiesResult != 0) {
			System.out.println("AddCapabilities failed, return: " + addCapabilitiesResult);
			
			return;
		}
		
		var iterateOverInstancesOfClassFunctionPtr = jvmtiInterface_1_.IterateOverInstancesOfClass(jvmtiInterface_FunctionsPtr);
		var jvmtiHeapObjectCallbackFunctionPtr = jvmtiHeapObjectCallback.allocate((_, _, _, _) -> 1, GLOBAL);
		var tag = GLOBAL.allocate(JAVA_INT);
		tag.set(JAVA_INT, 0, 999);
		
		var jniEnvPtr = getJNIEnv(javaVMPtr);
		if (Objects.isNull(jniEnvPtr)) {
			System.out.println("Failed to get JNI env");
			
			return;
		}
		
		var JNINativeInterfaceFunctionsPtr = JNIEnv_.functions(jniEnvPtr);
		var FindClassFunctionPtr = JNINativeInterface_.FindClass(JNINativeInterfaceFunctionsPtr);
		var clazzName = GLOBAL.allocateFrom("java/lang/String");
		var specialClassPtr = JNINativeInterface_.FindClass.invoke(FindClassFunctionPtr, jniEnvPtr, clazzName).reinterpret(ADDRESS_SIZE);
		
		int iterateOverInstancesOfClassResult = jvmtiInterface_1_.IterateOverInstancesOfClass.invoke(
				iterateOverInstancesOfClassFunctionPtr,
				jvmtiEnvPtr,
				specialClassPtr,
				3,
				jvmtiHeapObjectCallbackFunctionPtr,
				tag
		);
		
		if (iterateOverInstancesOfClassResult != 0) {
			System.out.println("IterateOverInstancesOfClass failed, return: " + iterateOverInstancesOfClassResult);
			
			return;
		}
		
		var getObjectsWithTags = jvmtiInterface_1_.GetObjectsWithTags(jvmtiInterface_FunctionsPtr);
		var count = GLOBAL.allocate(JAVA_INT);
		count.setAtIndex(JAVA_INT, 0, 0);
		var instance = GLOBAL.allocate(ADDRESS);
		
		int getObjectsWithTagsResult = jvmtiInterface_1_.GetObjectsWithTags.invoke(getObjectsWithTags,
				jvmtiEnvPtr,
				1,
				tag,
				count,
				instance,
				MemorySegment.NULL
		);
		
		if (getObjectsWithTagsResult != 0) {
			System.out.println("getObjectsWithTags failed, return: " + getObjectsWithTagsResult);
			
			return;
		}
	}
	
	/**
	 * get JavaVM pointer
	 * @return JavaVM pointer or null
	 */
	private static MemorySegment getJavaVM() {
		MemorySegment vmPtr = GLOBAL.allocate(ADDRESS);
		MemorySegment numVMs = GLOBAL.allocate(ValueLayout.JAVA_INT);
		numVMs.set(ValueLayout.JAVA_INT, 0, 0);
		
		int result = jvmti_h.JNI_GetCreatedJavaVMs(vmPtr, 1, numVMs);
		
		return result == 0 ? vmPtr.get(ADDRESS, 0).reinterpret(JavaVM_.sizeof()) : null;
	}
	
	/**
	 * get JVMTIEnv pointer
	 * @param javaVMPtr javaVM ptr
	 * @param functions JNIInvokeInterface ptr
	 * @return jvmtiEnv ptr
	 */
	private static MemorySegment getJVMTIEnv(MemorySegment javaVMPtr, MemorySegment functions) {
		MemorySegment envPtr = GLOBAL.allocate(ADDRESS);
		MemorySegment GetEnvFunctionPtr = JNIInvokeInterface_.GetEnv(functions);
		int result = JNIInvokeInterface_.GetEnv.invoke(GetEnvFunctionPtr, javaVMPtr, envPtr, 805371904);
		
		return result == 0 ? envPtr.get(ADDRESS, 0).reinterpret(_jvmtiEnv.sizeof()) : null;
	}
	
	private static MemorySegment getJNIEnv(MemorySegment javaVMPtr) {
		return jni_util_h.JNU_GetEnv(javaVMPtr, 0x00150000).reinterpret(8);
	}
}
