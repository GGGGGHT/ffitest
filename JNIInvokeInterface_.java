// Generated by jextract

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.function.Consumer;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

/**
 * {@snippet lang=c :
 * struct JNIInvokeInterface_ {
 *     void *reserved0;
 *     void *reserved1;
 *     void *reserved2;
 *     jint (*DestroyJavaVM)(JavaVM *);
 *     jint (*AttachCurrentThread)(JavaVM *, void **, void *);
 *     jint (*DetachCurrentThread)(JavaVM *);
 *     jint (*GetEnv)(JavaVM *, void **, jint);
 *     jint (*AttachCurrentThreadAsDaemon)(JavaVM *, void **, void *);
 * }
 * }
 */
public class JNIInvokeInterface_ {

    JNIInvokeInterface_() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        jvmti_h.C_POINTER.withName("reserved0"),
        jvmti_h.C_POINTER.withName("reserved1"),
        jvmti_h.C_POINTER.withName("reserved2"),
        jvmti_h.C_POINTER.withName("DestroyJavaVM"),
        jvmti_h.C_POINTER.withName("AttachCurrentThread"),
        jvmti_h.C_POINTER.withName("DetachCurrentThread"),
        jvmti_h.C_POINTER.withName("GetEnv"),
        jvmti_h.C_POINTER.withName("AttachCurrentThreadAsDaemon")
    ).withName("JNIInvokeInterface_");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout reserved0$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("reserved0"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *reserved0
     * }
     */
    public static final AddressLayout reserved0$layout() {
        return reserved0$LAYOUT;
    }

    private static final long reserved0$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *reserved0
     * }
     */
    public static final long reserved0$offset() {
        return reserved0$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *reserved0
     * }
     */
    public static MemorySegment reserved0(MemorySegment struct) {
        return struct.get(reserved0$LAYOUT, reserved0$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *reserved0
     * }
     */
    public static void reserved0(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(reserved0$LAYOUT, reserved0$OFFSET, fieldValue);
    }

    private static final AddressLayout reserved1$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("reserved1"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *reserved1
     * }
     */
    public static final AddressLayout reserved1$layout() {
        return reserved1$LAYOUT;
    }

    private static final long reserved1$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *reserved1
     * }
     */
    public static final long reserved1$offset() {
        return reserved1$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *reserved1
     * }
     */
    public static MemorySegment reserved1(MemorySegment struct) {
        return struct.get(reserved1$LAYOUT, reserved1$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *reserved1
     * }
     */
    public static void reserved1(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(reserved1$LAYOUT, reserved1$OFFSET, fieldValue);
    }

    private static final AddressLayout reserved2$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("reserved2"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * void *reserved2
     * }
     */
    public static final AddressLayout reserved2$layout() {
        return reserved2$LAYOUT;
    }

    private static final long reserved2$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * void *reserved2
     * }
     */
    public static final long reserved2$offset() {
        return reserved2$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * void *reserved2
     * }
     */
    public static MemorySegment reserved2(MemorySegment struct) {
        return struct.get(reserved2$LAYOUT, reserved2$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * void *reserved2
     * }
     */
    public static void reserved2(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(reserved2$LAYOUT, reserved2$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * jint (*DestroyJavaVM)(JavaVM *)
     * }
     */
    public static class DestroyJavaVM {

        DestroyJavaVM() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            jvmti_h.C_INT,
            jvmti_h.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = jvmti_h.upcallHandle(Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout DestroyJavaVM$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("DestroyJavaVM"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * jint (*DestroyJavaVM)(JavaVM *)
     * }
     */
    public static final AddressLayout DestroyJavaVM$layout() {
        return DestroyJavaVM$LAYOUT;
    }

    private static final long DestroyJavaVM$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * jint (*DestroyJavaVM)(JavaVM *)
     * }
     */
    public static final long DestroyJavaVM$offset() {
        return DestroyJavaVM$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * jint (*DestroyJavaVM)(JavaVM *)
     * }
     */
    public static MemorySegment DestroyJavaVM(MemorySegment struct) {
        return struct.get(DestroyJavaVM$LAYOUT, DestroyJavaVM$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * jint (*DestroyJavaVM)(JavaVM *)
     * }
     */
    public static void DestroyJavaVM(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(DestroyJavaVM$LAYOUT, DestroyJavaVM$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * jint (*AttachCurrentThread)(JavaVM *, void **, void *)
     * }
     */
    public static class AttachCurrentThread {

        AttachCurrentThread() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            jvmti_h.C_INT,
            jvmti_h.C_POINTER,
            jvmti_h.C_POINTER,
            jvmti_h.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = jvmti_h.upcallHandle(Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout AttachCurrentThread$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("AttachCurrentThread"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThread)(JavaVM *, void **, void *)
     * }
     */
    public static final AddressLayout AttachCurrentThread$layout() {
        return AttachCurrentThread$LAYOUT;
    }

    private static final long AttachCurrentThread$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThread)(JavaVM *, void **, void *)
     * }
     */
    public static final long AttachCurrentThread$offset() {
        return AttachCurrentThread$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThread)(JavaVM *, void **, void *)
     * }
     */
    public static MemorySegment AttachCurrentThread(MemorySegment struct) {
        return struct.get(AttachCurrentThread$LAYOUT, AttachCurrentThread$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThread)(JavaVM *, void **, void *)
     * }
     */
    public static void AttachCurrentThread(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(AttachCurrentThread$LAYOUT, AttachCurrentThread$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * jint (*DetachCurrentThread)(JavaVM *)
     * }
     */
    public static class DetachCurrentThread {

        DetachCurrentThread() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            jvmti_h.C_INT,
            jvmti_h.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = jvmti_h.upcallHandle(Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout DetachCurrentThread$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("DetachCurrentThread"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * jint (*DetachCurrentThread)(JavaVM *)
     * }
     */
    public static final AddressLayout DetachCurrentThread$layout() {
        return DetachCurrentThread$LAYOUT;
    }

    private static final long DetachCurrentThread$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * jint (*DetachCurrentThread)(JavaVM *)
     * }
     */
    public static final long DetachCurrentThread$offset() {
        return DetachCurrentThread$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * jint (*DetachCurrentThread)(JavaVM *)
     * }
     */
    public static MemorySegment DetachCurrentThread(MemorySegment struct) {
        return struct.get(DetachCurrentThread$LAYOUT, DetachCurrentThread$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * jint (*DetachCurrentThread)(JavaVM *)
     * }
     */
    public static void DetachCurrentThread(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(DetachCurrentThread$LAYOUT, DetachCurrentThread$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * jint (*GetEnv)(JavaVM *, void **, jint)
     * }
     */
    public static class GetEnv {

        GetEnv() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0, MemorySegment _x1, int _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            jvmti_h.C_INT,
            jvmti_h.C_POINTER,
            jvmti_h.C_POINTER,
            jvmti_h.C_INT
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = jvmti_h.upcallHandle(Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, int _x2) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout GetEnv$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("GetEnv"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * jint (*GetEnv)(JavaVM *, void **, jint)
     * }
     */
    public static final AddressLayout GetEnv$layout() {
        return GetEnv$LAYOUT;
    }

    private static final long GetEnv$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * jint (*GetEnv)(JavaVM *, void **, jint)
     * }
     */
    public static final long GetEnv$offset() {
        return GetEnv$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * jint (*GetEnv)(JavaVM *, void **, jint)
     * }
     */
    public static MemorySegment GetEnv(MemorySegment struct) {
        return struct.get(GetEnv$LAYOUT, GetEnv$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * jint (*GetEnv)(JavaVM *, void **, jint)
     * }
     */
    public static void GetEnv(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(GetEnv$LAYOUT, GetEnv$OFFSET, fieldValue);
    }

    /**
     * {@snippet lang=c :
     * jint (*AttachCurrentThreadAsDaemon)(JavaVM *, void **, void *)
     * }
     */
    public static class AttachCurrentThreadAsDaemon {

        AttachCurrentThreadAsDaemon() {
            // Should not be called directly
        }

        /**
         * The function pointer signature, expressed as a functional interface
         */
        public interface Function {
            int apply(MemorySegment _x0, MemorySegment _x1, MemorySegment _x2);
        }

        private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
            jvmti_h.C_INT,
            jvmti_h.C_POINTER,
            jvmti_h.C_POINTER,
            jvmti_h.C_POINTER
        );

        /**
         * The descriptor of this function pointer
         */
        public static FunctionDescriptor descriptor() {
            return $DESC;
        }

        private static final MethodHandle UP$MH = jvmti_h.upcallHandle(Function.class, "apply", $DESC);

        /**
         * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
         * The lifetime of the returned segment is managed by {@code arena}
         */
        public static MemorySegment allocate(Function fi, Arena arena) {
            return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
        }

        private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

        /**
         * Invoke the upcall stub {@code funcPtr}, with given parameters
         */
        public static int invoke(MemorySegment funcPtr,MemorySegment _x0, MemorySegment _x1, MemorySegment _x2) {
            try {
                return (int) DOWN$MH.invokeExact(funcPtr, _x0, _x1, _x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        }
    }

    private static final AddressLayout AttachCurrentThreadAsDaemon$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("AttachCurrentThreadAsDaemon"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThreadAsDaemon)(JavaVM *, void **, void *)
     * }
     */
    public static final AddressLayout AttachCurrentThreadAsDaemon$layout() {
        return AttachCurrentThreadAsDaemon$LAYOUT;
    }

    private static final long AttachCurrentThreadAsDaemon$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThreadAsDaemon)(JavaVM *, void **, void *)
     * }
     */
    public static final long AttachCurrentThreadAsDaemon$offset() {
        return AttachCurrentThreadAsDaemon$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThreadAsDaemon)(JavaVM *, void **, void *)
     * }
     */
    public static MemorySegment AttachCurrentThreadAsDaemon(MemorySegment struct) {
        return struct.get(AttachCurrentThreadAsDaemon$LAYOUT, AttachCurrentThreadAsDaemon$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * jint (*AttachCurrentThreadAsDaemon)(JavaVM *, void **, void *)
     * }
     */
    public static void AttachCurrentThreadAsDaemon(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(AttachCurrentThreadAsDaemon$LAYOUT, AttachCurrentThreadAsDaemon$OFFSET, fieldValue);
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

