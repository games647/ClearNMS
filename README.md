# ClearNMS

## Description

Exposing Minecraft server internals using ASM. The main purpose of this project is to learn and experiment with ASM
and Java ByteCode. Feel free to do the same.

Craftbukkit/Spigot servers are hiding Minecraft server internals
behind `net.minecraft.server.<version>` (NMS) or `org.bukkit.craftbukkit.<version>` (OBC) where `<version>` has a format
of `v1_6R2`. Each new released Minecraft version this version is changed, because those internals a high likely to 
change and therefore cause breaking changes, but the reality is that this often hides some feature that are unlikely
to be in the API. Plugins that target those features are forced to use Reflection in order to access those things
are across multiple versions of the server although only the package name is different.

This project generates classes at runtime using ASM. Those classes can have direct access to those internals without
using Reflection. This provides a similar performance in comparison of having the class compiled for this server 
version explicitly.

## Features

* Liberal license (Feel free to do what ever you want with the existing code)
* Do not break between new server versions
* Generated adapters don't use Reflection at all (better performance)

## Available methods 

* Players:
    * int getPing()

## Requirements

* Spigot 1.8+
* Java 9

## Contributing

Knowledge of Java ByteCode or ASM is recommended, but not really required. In fact you can learn a lot from reading
and generating ASM code of existing Java generated classes. Feel free to use this project for experimenting with ASM.

## Generating ASM code from existing NMS or OBS classes

1. Find the `ASMifier` class inside package `org.objectweb.asm.util` (ASM5), 
`jdk.internal.org.objectweb.asm.util` (JDK 9)
2. Run this file with `-debug PATH_TO_CLASS_FILE.class` 
Note the .class file extension. This means the class have to be compiled first.
3. Now you get the ASMified code in STDOUT
4. To compare the code with bytecode run `javap -v PATH_TO_CLASS_FILE.class` (javap is part of the JDK)

## Modules

* Plugin: Core component for generating and building ASM code
* Demo: How to hook into the plugin
* Generator: Generate ASMified code from Java classes. Run it with mvn package exec:java
