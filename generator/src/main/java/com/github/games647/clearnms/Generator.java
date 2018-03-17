package com.github.games647.clearnms;

import com.github.games647.clearnms.asm.NMSPlayer;

import java.io.InputStream;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 * @see org.objectweb.asm.util.ASMifier;
 */
public class Generator {

    private static final Class<?> CLASS = NMSPlayer.class;

    public static void main(String[] args) throws Exception {
        int flags = ClassReader.SKIP_DEBUG;

        // java package path to file system path
        String path = (NMSPlayer.class.getName()).replace('.', '/') + ".class";

        //use / to reference the root folder
        try (InputStream in = Generator.class.getResourceAsStream('/' + path)) {
            ClassReader reader = new ClassReader(in);
            reader.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), flags);
        }
    }
}
