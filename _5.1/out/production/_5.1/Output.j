.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 istore 0
 invokestatic Output/read()I
 istore 1
 goto L1
L1:
 iload 0
 ldc 20
 if_icmpgt L3
 goto L4
L3:
 ldc 2
 istore 2
 goto L2
L4:
 ldc 1
 istore 2
 goto L2
L2:
 goto L5
L5:
 iload 0
 iload 1
 if_icmpgt L7
 goto L8
L7:
 iload 0
 ldc 0
 if_icmpgt L9
 goto L10
L9:
 iload 0
 iload 2
 isub 
 istore 0
 goto L11
L11:
 iload 0
 invokestatic Output/print(I)V
 goto L7
L10:
 goto L6
L8:
 iload 0
 iload 1
 if_icmplt L12
 goto L13
L12:
 iload 1
 ldc 0
 if_icmpgt L14
 goto L15
L14:
 iload 1
 iload 2
 isub 
 istore 1
 goto L16
L16:
 iload 1
 invokestatic Output/print(I)V
 goto L12
L15:
 goto L6
L13:
 goto L6
L6:
 goto L17
L17:
 iload 0
 invokestatic Output/print(I)V
 iload 1
 invokestatic Output/print(I)V
 iload 2
 ldc 2
 imul 
 ldc 2
 imul 
 invokestatic Output/print(I)V
 goto L0
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

