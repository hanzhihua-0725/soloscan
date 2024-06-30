package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.BaseSoloExpression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.compiler.asm.ClassWriter;
import org.soloquest.soloscan.compiler.asm.Opcodes;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.exception.ExpressionRuntimeException;
import org.soloquest.soloscan.utils.MiscUtils;

import java.lang.reflect.Constructor;
import java.util.Map;

import static org.soloquest.soloscan.compiler.asm.Opcodes.*;

public class SoloExpressionRealCodeGenerator extends AbstractRealCodeGenerator<BaseSoloExpression> implements CodeConstants {


    public SoloExpressionRealCodeGenerator(final SoloscanExecutor instance,
                                           final SoloscanClassloader classLoader, final Class<BaseSoloExpression> type) {
        super(instance, classLoader, type);
        this.className = "So_" + System.currentTimeMillis() + "_" + CLASS_COUNTER.getAndIncrement();
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.classWriter.visit(Opcodes.V1_8, ACC_PUBLIC + ACC_SUPER,
                this.className, null, SUPER_CLASSNAME, null);
    }

    @Override
    protected void setMainTokenContainer() {
        this.tokenContainer = this.merticsTokenContainer;
    }

    @Override
    public BaseSoloExpression getResult() {
        initConstants();
        initVariables();
        initMetricVariables();
        initMethods();
        constructor();

        startVisitMethodCode();
        methodBody(this.tokenList);
        endVisitMethodCode(false);

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
            BaseSoloExpression exp = (BaseSoloExpression) constructor.newInstance(this.instance
                    , this.symbolTable, this.parser.getLexer().getExpression());
            return exp;
        } catch (ExpressionRuntimeException e) {
            throw e;
        } catch (Throwable e) {
            if (e.getCause() instanceof ExpressionRuntimeException) {
                throw (ExpressionRuntimeException) e.getCause();
            }
            throw new ExpressionCompileException("Failed to generate expression", e);
        }
    }

    private void startVisitMethodCode() {
        this.mv = this.classWriter.visitMethod(ACC_PUBLIC + +ACC_FINAL, EXECUTE_METHOD_NAME,
                EXECUTE_DESC,
                EXECUTE_DESC, null);
        this.mv.visitCode();
    }


    private void endVisitMethodCode(final boolean unboxObject) {
        loadEnv();
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "getValue",
                "(Ljava/util/Map;)Ljava/lang/Object;");
        this.mv.visitInsn(ARETURN);
        this.mv.visitMaxs(0, 0);
        this.mv.visitEnd();
    }

    private void constructor() {

        this.mv = this.classWriter.visitMethod(ACC_PUBLIC, CONSTRUCTOR_METHOD_NAME,
                CONSTRUCTOR_DESC,
                null, null);
        this.mv.visitCode();
        this.mv.visitVarInsn(ALOAD, 0);
        this.mv.visitVarInsn(ALOAD, 1);
        this.mv.visitVarInsn(ALOAD, 2);
        this.mv.visitVarInsn(ALOAD, 3);
        this.mv.visitMethodInsn(INVOKESPECIAL, SUPER_CLASSNAME,
                CONSTRUCTOR_METHOD_NAME,
                SUPER_CONSTRUCTOR_DESC);
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

        if (!this.innerMetricVars.isEmpty()) {
            for (Map.Entry<String, String> entry : this.innerMetricVars.entrySet()) {
                String outterName = entry.getKey();
                String innerName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitTypeInsn(NEW, SMERTRIC_OWNER);
                this.mv.visitInsn(DUP);
                this.mv.visitLdcInsn(outterName);
                this.mv.visitVarInsn(ALOAD, 2);
                this.mv.visitMethodInsn(INVOKESPECIAL, SMERTRIC_OWNER, CONSTRUCTOR_METHOD_NAME,
                        NEW_JAVATYPE_DESC);
                this.mv.visitFieldInsn(PUTFIELD, this.className, innerName,
                        SMERTRIC_DESC);
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
            }
        }

        this.mv.visitInsn(RETURN);
        this.mv.visitMaxs(0, 0);
        this.mv.visitEnd();

    }

}