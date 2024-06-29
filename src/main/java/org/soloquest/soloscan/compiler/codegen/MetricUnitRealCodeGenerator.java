package org.soloquest.soloscan.compiler.codegen;


import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.BaseMetricUnitExpression;
import org.soloquest.soloscan.MetricUnitExpression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.compiler.asm.ClassWriter;
import org.soloquest.soloscan.compiler.asm.Label;
import org.soloquest.soloscan.compiler.asm.Opcodes;
import org.soloquest.soloscan.compiler.asm.Type;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.lexer.token.VariableToken;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.MiscUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import static org.soloquest.soloscan.compiler.asm.Opcodes.*;

@Slf4j
public class MetricUnitRealCodeGenerator extends AbstractRealCodeGenerator<BaseMetricUnitExpression> implements CodeConstants {


    public MetricUnitRealCodeGenerator(final SoloscanExecutor instance,
                                       final SoloscanClassloader classLoader, final Class<BaseMetricUnitExpression> type) {
        super(instance, classLoader, type);
        this.className = "So_Mu_" + System.currentTimeMillis() + "_" + CLASS_COUNTER.getAndIncrement();
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.classWriter.visit(Opcodes.V1_7, ACC_PUBLIC + ACC_SUPER,
                this.className, null, MU_SUPER_CLASSNAME, null);


    }

    @Override
    public BaseMetricUnitExpression getResult() {
        initConstants();
        initVariables();
        initMetricVariables();
        initMethods();
        constructor();

        startVisitMethodCode();
        methodBody(this.tokenList);
        endVisitMethodCode(false);

        visitGroupingMethod();

        visitFilterMethod();

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
            BaseMetricUnitExpression exp = (BaseMetricUnitExpression) constructor.newInstance(this.instance
                    , this.symbolTable, this.parser.getLexer().getExpression());
            return exp;
        } catch (ExpressionExecuteException e) {
            throw e;
        } catch (Throwable e) {
            if (e.getCause() instanceof ExpressionExecuteException) {
                throw (ExpressionExecuteException) e.getCause();
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
        this.mv.visitMethodInsn(INVOKESPECIAL, MU_SUPER_CLASSNAME,
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

    private void visitGroupingMethod() {
        if (this.groupingTokenContainer.tokenList.size() == 0) {
            return;
        }
        this.mv = this.classWriter.visitMethod(ACC_PUBLIC, GROUPING_METHOD_NAME,
                GROUPING_METHOD_DESC,
                GROUPING_METHOD_DESC, null);
        this.mv.visitCode();
        Label start = new Label();
        mv.visitLabel(start);

        Label end = new Label();
        mv.visitLabel(end);
        mv.visitLocalVariable("rowEnv", ENV_DESC, ENV_DESC, start, end, 1);


        List<Token<?>> tokens = this.groupingTokenContainer.tokenList;
        int localIndex = 2;
        int paramCount = 0;
        for (Token token : tokens) {
            if (token instanceof VariableToken) {
                VariableToken variableToken = (VariableToken) token;

                Label ifLabel = new Label();
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitLdcInsn(token.getLexeme());
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "containsKey",
                        "(Ljava/lang/Object;)Z");

                mv.visitJumpInsn(IFNE, ifLabel);

                mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
                mv.visitInsn(DUP);
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitLdcInsn("Key not found: ");
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
                mv.visitLdcInsn(token.getLexeme());
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
                        "()Ljava/lang/String;");
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>",
                        "(Ljava/lang/String;)V");
                mv.visitInsn(ATHROW);

                mv.visitLabel(ifLabel);


                mv.visitLocalVariable(variableToken.getLexeme() + "_arg" + paramCount, Type.getDescriptor(String.class), null, start, end, localIndex);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitLdcInsn(token.getLexeme());
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ENV_OWNER, "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
                mv.visitMethodInsn(INVOKESTATIC, RU_OWNER, "covertString",
                        "(Ljava/lang/Object;)Ljava/lang/String;");
                mv.visitVarInsn(Opcodes.ASTORE, localIndex);
                paramCount++;
                localIndex++;
            } else {
                log.warn("{} is not variable,please check it", token);
                throw new ExpressionCompileException();
            }
        }

        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");

        int startIndex = 2;
        for (int i = 0; i < paramCount; i++) {
            mv.visitVarInsn(Opcodes.ALOAD, startIndex++);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
            if (i < paramCount - 1) {
                mv.visitLdcInsn("__");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
            }
        }

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitInsn(Opcodes.ARETURN);

        mv.visitMaxs(0, 0);

        this.mv.visitEnd();
    }


    private void visitFilterMethod() {
        if (this.filterTokenContainer.tokenList.size() == 0) {
            return;
        }
        this.mv = this.classWriter.visitMethod(ACC_PUBLIC + ACC_FINAL, FILTER_METHOD_NAME,
                FILTER_METHOD_DESC,
                FILTER_METHOD_DESC, null);
        this.mv.visitCode();
        mv.visitLocalVariable("rowEnv", ENV_DESC, ENV_DESC, new Label(), new Label(), 1);


        methodBody(this.filterTokenContainer.tokenList, false);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "getValue",
                "(Ljava/util/Map;)Ljava/lang/Object;");
        this.mv.visitMethodInsn(INVOKESTATIC, RU_OWNER, "covertBoolean",
                "(Ljava/lang/Object;)Z");
        this.mv.visitInsn(IRETURN);

        mv.visitMaxs(0, 0);

        this.mv.visitEnd();
    }

}
