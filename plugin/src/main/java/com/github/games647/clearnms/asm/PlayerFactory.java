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
        return (PlayerAdapter) defineClass(createAdapterClass()).getConstructor().newInstance();
    }

    @Override
    public byte[] createAdapterClass() {
        ClassWriter classWriter = createClassWriter(PLAYER_CLASS);

        // content
        visitEmptyCons(classWriter);
        visitPingMethod(classWriter);

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
}
