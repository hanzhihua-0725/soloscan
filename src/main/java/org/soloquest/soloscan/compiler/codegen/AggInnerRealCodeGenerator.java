package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.compiler.asm.ClassWriter;
import org.soloquest.soloscan.compiler.asm.Opcodes;
import org.soloquest.soloscan.compiler.asm.Type;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.runtime.aggfunction.AggInner;
import org.soloquest.soloscan.utils.MiscUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.soloquest.soloscan.compiler.asm.Opcodes.*;

public class AggInnerRealCodeGenerator extends AbstractRealCodeGenerator<AggInner> implements CodeConstants {

    public AggInnerRealCodeGenerator(final SoloscanExecutor instance, final SoloscanClassloader classLoader, final Class<AggInner> type) {
        super(instance, classLoader, type);
        this.className = "So_AggInner_" + System.currentTimeMillis() + "_" + CLASS_COUNTER.getAndIncrement();
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_7, ACC_PUBLIC,
                className, null, "java/lang/Object", new String[]{FILTER_INTERFACE});
        classWriter.visitSource(className, null);
    }

    @Override
    public AggInner getResult() {
        initConstants();
        initVariables();
        initMethods();
        constructor();
        startVisitMethodCode();
        methodBody(this.filterTokenContainer.tokenList);
        endVisitMethodCode();
        visitGetColumnValueMethodCode();
        endVisitClass();

        byte[] bytes = this.classWriter.toByteArray();
        if (SoloscanOptions.getOption(SoloscanOptions.GENERATE_CLASS)) {
            MiscUtils.generateClassFile(className, bytes);
        }

        try {
            Class<?> defineClass =
                    ClassDefiner.defineClass(this.className, type, bytes, this.classLoader);
            Constructor<?> constructor =
                    defineClass.getConstructor(SoloscanExecutor.class, SymbolTable.class, String.class);
            AggInner aggInner = (AggInner) constructor.newInstance(this.instance
                    , this.symbolTable, this.parser.getLexer().getExpression());
            return aggInner;
        } catch (ExpressionExecuteException e) {
            throw e;
        } catch (Throwable e) {
            if (e.getCause() instanceof ExpressionExecuteException) {
                throw (ExpressionExecuteException) e.getCause();
            }
            throw new ExpressionCompileException("define class error", e);
        }
    }


    private void constructor() {

        this.mv = this.classWriter.visitMethod(ACC_PUBLIC, CONSTRUCTOR_METHOD_NAME,
                CONSTRUCTOR_DESC,
                null, null);
        this.mv.visitCode();
        this.mv.visitVarInsn(ALOAD, 0);
        this.mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), CONSTRUCTOR_METHOD_NAME, "()V");
        if (!this.innerVars.isEmpty()) {
            for (Map.Entry<String, String> entry : this.innerVars.entrySet()) {
                String outterName = entry.getKey();
                String innerName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitTypeInsn(NEW, JAVATYPE_OWNER);
                this.mv.visitInsn(DUP);
                this.mv.visitLdcInsn(outterName);
                this.mv.visitVarInsn(ALOAD, 2);
                this.mv.visitMethodInsn(INVOKESPECIAL, JAVATYPE_OWNER, CONSTRUCTOR_METHOD_NAME,
                        NEW_JAVATYPE_DESC);
                this.mv.visitFieldInsn(PUTFIELD, this.className, innerName,
                        JAVATYPE_DESC);
            }
        }
        if (!this.innerMethodMap.isEmpty()) {
            for (Map.Entry<String, String> entry : this.innerMethodMap.entrySet()) {
                String outterName = entry.getKey();
                String innerName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitVarInsn(ALOAD, 1);
                this.mv.visitLdcInsn(outterName);
                this.mv.visitMethodInsn(INVOKEVIRTUAL, EXECUTOR_OWNER,
                        "getFunction",
                        GET_FUNCTION_DESC);
                this.mv.visitFieldInsn(PUTFIELD, this.className, innerName,
                        FUNCTION_DESC);
            }
        }

        if (!this.constantPool.isEmpty()) {
            for (Map.Entry<Token<?>, String> entry : this.constantPool.entrySet()) {
                Token<?> token = entry.getKey();
                String fieldName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                onConstant(token, false);
                this.mv.visitFieldInsn(PUTFIELD, this.className, fieldName, OBJECT_DESC);
                this.popOperand();
            }
        }

        this.mv.visitInsn(RETURN);
        this.mv.visitMaxs(0, 0);
        this.mv.visitEnd();

    }

    private void startVisitMethodCode() {

        this.mv = classWriter.visitMethod(ACC_PUBLIC + +ACC_FINAL, CHECK_METHOD_NAME,
                FILTER_METHOD_DESC,
                FILTER_METHOD_DESC, null);
        this.mv.visitCode();
    }

    private void endVisitMethodCode() {
        loadEnv();
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "getValue",
                "(Ljava/util/Map;)Ljava/lang/Object;");
        this.mv.visitMethodInsn(INVOKESTATIC, RU_OWNER, "covertBoolean",
                "(Ljava/lang/Object;)Z");
        this.mv.visitInsn(IRETURN);
        this.mv.visitMaxs(this.maxStacks, this.maxLocals);
        this.mv.visitEnd();
    }

    private void visitGetColumnValueMethodCode() {
        this.mv = classWriter.visitMethod(ACC_PUBLIC + +ACC_FINAL, GETCOLUMNVALUE_METHOD_NAME,
                GETCOLUMNVALUE_METHOD_DESC,
                GETCOLUMNVALUE_METHOD_DESC, null);
        this.mv.visitCode();
        if (this.merticsTokenContainer.tokenList.size() > 0) {
            methodBody(this.merticsTokenContainer.tokenList);
        } else {
            List list = new ArrayList<>(this.innerVars.keySet());
            Collections.sort(list);
            String innerVarName = this.innerVars.get(list.get(0));
            this.mv.visitVarInsn(ALOAD, 0);
            this.mv.visitFieldInsn(GETFIELD, this.className, innerVarName,
                    JAVATYPE_DESC);
        }
        this.loadEnv();
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "getValue",
                "(Ljava/util/Map;)Ljava/lang/Object;");
        this.mv.visitInsn(ARETURN);
        this.mv.visitMaxs(0, 0);
        this.mv.visitEnd();
    }

}
