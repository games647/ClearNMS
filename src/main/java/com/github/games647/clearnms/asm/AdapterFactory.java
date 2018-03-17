package com.github.games647.clearnms.asm;

import com.github.games647.clearnms.ServerVersion;
import com.github.games647.clearnms.adapters.PlayerAdapter;

import java.lang.invoke.MethodHandles;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class AdapterFactory<T> implements Opcodes {

    protected static final String PACKAGE_NAME = toExternalName(AdapterFactory.class.getPackageName());
    private static final String CONSTRUCTOR_NAME = "<init>";

    protected final ServerVersion version;

    public AdapterFactory(ServerVersion version) {
        this.version = version;
    }

    public abstract byte[] createAdapterClass();

    public abstract T createAdapter() throws ReflectiveOperationException;

    protected ClassWriter createClassWriter(String className) {
        // arguments for visitMaxs should be computed at compile time (of the dynamic class) with this parameter
        // https://stackoverflow.com/a/41227342
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // class header with Java 9 as version
        String[] interfaces = {Type.getInternalName(PlayerAdapter.class)};
        // class should be public and invoke the super class different for exclusive super method calls
        int accessModifier = ACC_PUBLIC + ACC_SUPER;
        String superClass = Type.getInternalName(Object.class); //all classes inherit from Object
        classWriter.visit(V9, accessModifier, PACKAGE_NAME + className, null, superClass, interfaces);

        //add the source file name for stacktraces
        classWriter.visitSource(toExternalName(className), null);
        return classWriter;
    }

    protected void visitEmptyCons(ClassWriter classWriter) {
        // no parameter with void as return
        MethodVisitor consVisitor = classWriter.visitMethod(ACC_PUBLIC, CONSTRUCTOR_NAME, "()V", null, null);
        consVisitor.visitCode();

        // call the super constructor and store it on the stack
        consVisitor.visitVarInsn(ALOAD, 0);
        consVisitor.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), CONSTRUCTOR_NAME, "()V", false);
        consVisitor.visitInsn(RETURN);

        // Address (-> one stack element) this as the only local variable
        consVisitor.visitMaxs(1, 1);
        consVisitor.visitEnd();
    }

    protected String getNMSPackage() {
       return toInternalName(version.getNMSPackage());
    }

    protected String getOBCPackage() {
        return toInternalName(version.getOBCPackage());
    }

    protected static String toInternalName(String externalName) {
        // java internal classes have a slash instead of a dot
        return externalName.replace('.', '/');
    }

    protected static String toExternalName(String internalName) {
        // java internal classes have a slash instead of a dot
        return internalName.replace('/', '.');
    }

    protected Class<?> defineClass(byte[] classBytes) throws ReflectiveOperationException {
        // the caller have to be in the same package as the to be defined class
        // this method is only available in Java, but provides a way to create the class without Reflection
        // of java base classes and resulting warnings in Java 9
        return MethodHandles.lookup().defineClass(classBytes);
    }
}
