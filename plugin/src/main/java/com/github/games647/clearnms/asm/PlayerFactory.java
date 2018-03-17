package com.github.games647.clearnms.asm;

import com.github.games647.clearnms.ServerVersion;
import com.github.games647.clearnms.adapters.PlayerAdapter;

import org.bukkit.entity.Player;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class PlayerFactory extends AdapterFactory<PlayerAdapter> {

    private static final String PLAYER_CLASS = "NMSPlayer";

    public PlayerFactory(ServerVersion version) {
        super(version);
    }

    @Override
    public PlayerAdapter createAdapter() throws ReflectiveOperationException {
        Class<?> playerClazz;
        try {
            //we are only allowed to define a class once. This loads already generated classes since last JVM startup
            playerClazz = Class.forName(getClass().getPackageName() + '.' + PLAYER_CLASS);
        } catch (ClassNotFoundException classNotFoundEx) {
            playerClazz = defineClass(createAdapterClass());
        }

        return (PlayerAdapter) playerClazz.getConstructor().newInstance();
    }

    @Override
    public byte[] createAdapterClass() {
        ClassWriter classWriter = createClassWriter(PLAYER_CLASS);

        // content
        visitEmptyCons(classWriter);
        visitPingMethod(classWriter);
        visitCreditsMethod(classWriter);

        // build class bytes
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private void visitPingMethod(ClassWriter classWriter) {
        // player as parameter and Integer as return
        String methodDesc = '(' + Type.getDescriptor(Player.class) + ")I";
        MethodVisitor pingVisitor = classWriter.visitMethod(ACC_PUBLIC, "getPing", methodDesc, null, null);
        pingVisitor.visitCode();

        // cast to CraftPlayer and save on stack
        pingVisitor.visitVarInsn(ALOAD, 1);
        String craftPlayerType = getOBCPackage() + "/entity/CraftPlayer";
        pingVisitor.visitTypeInsn(CHECKCAST, craftPlayerType);

        // fetch the NMS EntityPlayer from OBC CraftPlayer
        String descriptor = "()L" + getNMSPackage() + "/EntityPlayer;";
        pingVisitor.visitMethodInsn(INVOKEVIRTUAL, craftPlayerType, "getHandle", descriptor, false);

        // Get the field and return the integer
        pingVisitor.visitFieldInsn(GETFIELD, getNMSPackage() + "/EntityPlayer", "ping", "I");
        pingVisitor.visitInsn(IRETURN);

        // We only need to store one thing at the same time (-> 1 stack) and two local variables this and player
        pingVisitor.visitMaxs(1, 2);
        pingVisitor.visitEnd();
    }

    private void visitCreditsMethod(ClassWriter classWriter) {
        // player as parameter and boolean as return
        String methodDesc = '(' + Type.getDescriptor(Player.class) + ")Z";
        MethodVisitor creditsVisitor = classWriter.visitMethod(ACC_PUBLIC, "isViewingCredits", methodDesc, null, null);
        creditsVisitor.visitCode();

        // cast to CraftPlayer and save on stack
        creditsVisitor.visitVarInsn(ALOAD, 1);
        String craftPlayerType = getOBCPackage() + "/entity/CraftPlayer";
        creditsVisitor.visitTypeInsn(CHECKCAST, craftPlayerType);

        // fetch the NMS EntityPLayer form OBC CraftPlayer
        String descriptor = "()L" + getNMSPackage() + "/EntityPlayer;";
        creditsVisitor.visitMethodInsn(INVOKEVIRTUAL, craftPlayerType, "getHandle", descriptor, false);

        // fetch the boolean field and return
        creditsVisitor.visitFieldInsn(GETFIELD, getNMSPackage() + "/EntityPlayer", "viewingCredits", "Z");
        creditsVisitor.visitInsn(IRETURN);

        // one stack element is enought and two local variables for player and this
        creditsVisitor.visitMaxs(1, 2);
        creditsVisitor.visitEnd();
    }
}
