package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.asm.ClassWriter;
import org.soloquest.soloscan.compiler.asm.Label;
import org.soloquest.soloscan.compiler.asm.MethodVisitor;
import org.soloquest.soloscan.compiler.asm.Type;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.*;
import org.soloquest.soloscan.compiler.parser.Parser;
import org.soloquest.soloscan.runtime.lang.SFunction;
import org.soloquest.soloscan.utils.ParserUtils;
import org.soloquest.soloscan.utils.Preconditions;
import org.soloquest.soloscan.utils.TypeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.soloquest.soloscan.compiler.asm.Opcodes.*;

public abstract class AbstractRealCodeGenerator<T> implements CodeConstants {

    protected static final AtomicLong CLASS_COUNTER = new AtomicLong();
    protected ClassWriter classWriter;
    protected MethodVisitor mv;
    protected int operandsCount = 0;
    protected Map<String, String> innerVars = Collections.emptyMap();

    protected Map<String, String> innerMetricVars = Collections.emptyMap();
    protected Map<String, String> innerMethodMap = Collections.emptyMap();
    protected Map<Token<?>, String> constantPool = Collections.emptyMap();

    protected Set<String> methodTokens = new HashSet<>();

    protected Set<String> variables = new HashSet<>();

    protected Set<String> metricVariables = new HashSet<>();

    protected Set<Token<?>> constants = new HashSet<>();

    protected static final Label START_LABEL = new Label();
    protected final Map<Label, Map<String, Integer>> labelNameIndexMap = new IdentityHashMap<>();

    protected Label currentLabel = START_LABEL;
    protected String className;
    protected int fieldCounter = 0;
    protected static final String FIELD_PREFIX = "f";
    protected final ArrayDeque<MetricUnitRealCodeGenerator.MethodMetaData> methodMetaDataStack = new ArrayDeque<>();
    protected int maxStacks = 0;
    protected int maxLocals = 2;

    protected List<Token<?>> tokenList;

    protected SoloscanClassloader classLoader;
    protected SoloscanExecutor instance;
    protected Parser parser;
    protected SymbolTable symbolTable;
    protected TokenContainer tokenContainer;
    protected TokenContainer merticsTokenContainer;
    protected TokenContainer groupingTokenContainer;
    protected TokenContainer filterTokenContainer;

    protected Class<T> type;

    public AbstractRealCodeGenerator(final SoloscanExecutor instance, final SoloscanClassloader classLoader, Class<T> type) {
        this.instance = instance;
        this.classLoader = classLoader;
        this.type = type;
    }

    protected void setTokens(TokenContainer merticsTokenContainer, TokenContainer groupingTokenContainer, TokenContainer filterTokenContainer) {
        this.merticsTokenContainer = merticsTokenContainer;
        this.groupingTokenContainer = groupingTokenContainer;
        this.filterTokenContainer = filterTokenContainer;
        setMainTokenContainer();
        this.tokenList = tokenContainer.getTokenList();
        this.variables = tokenContainer.getVariables();
        this.metricVariables = tokenContainer.getMetricsVariables();
        this.methodTokens = tokenContainer.getMethodTokens();
        this.constants = tokenContainer.getConstants();
    }

    protected abstract void setMainTokenContainer();

    protected void setParser(Parser parser) {
        this.parser = parser;
        this.symbolTable = parser.getSymbolTable();
    }

    protected String getInnerName(final String varName) {
        return FIELD_PREFIX + this.fieldCounter++;
    }


    protected void initConstants() {
        if (constants.isEmpty()) {
            return;
        }
        this.constantPool = new HashMap<>(constants.size());
        for (Token<?> token : constants) {
            String fieldName = getInnerName(token.getLexeme());
            this.constantPool.put(token, fieldName);
            this.classWriter.visitField(ACC_PRIVATE + ACC_FINAL, fieldName, OBJECT_DESC, null, null)
                    .visitEnd();
        }
    }

    public void initVariables() {
        if (variables.isEmpty()) {
            return;
        }
        this.innerVars = new HashMap<>(this.variables.size());
        for (String outterVarName : this.variables) {
            String innerVarName = getInnerName(outterVarName);
            this.innerVars.put(outterVarName, innerVarName);
            this.classWriter.visitField(ACC_PRIVATE + ACC_FINAL, innerVarName,
                    JAVATYPE_DESC, null, null).visitEnd();

        }
    }

    public void initMetricVariables() {
        if (metricVariables.isEmpty()) {
            return;
        }
        this.innerMetricVars = new HashMap<>(this.metricVariables.size());
        for (String outterVarName : this.metricVariables) {
            String innerVarName = getInnerName(outterVarName);
            this.innerMetricVars.put(outterVarName, innerVarName);
            this.classWriter.visitField(ACC_PRIVATE + ACC_FINAL, innerVarName,
                    SMERTRIC_DESC, null, null).visitEnd();

        }
    }

    public void initMethods() {
        if (methodTokens.isEmpty()) {
            return;
        }
        this.innerMethodMap = new HashMap<>(methodTokens.size());
        for (String outterMethodName : methodTokens) {
            String innerMethodName = getInnerName(outterMethodName);
            this.innerMethodMap.put(outterMethodName, innerMethodName);
            this.classWriter.visitField(ACC_PRIVATE + ACC_FINAL, innerMethodName,
                    FUNCTION_DESC, null, null).visitEnd();
        }
    }

    protected void methodBody(List<Token<?>> tokenList) {
        methodBody(tokenList, true);
    }

    protected void methodBody(List<Token<?>> tokenList, boolean isLoad) {
        for (Token token : tokenList) {
            switch (token.getType()) {
                case Operator:
                    OperatorToken op = (OperatorToken) token;
                    switch (op.getOperatorType()) {
                        case ADD:
                            onAdd(token);
                            break;
                        case SUB:
                            onSub(token);
                            break;
                        case MULT:
                            onMult(token);
                            break;
                        case DIV:
                            onDiv(token);
                            break;
                        case MOD:
                            onMod(token);
                            break;
                        case EQ:
                            onEq(token);
                            break;
                        case NEQ:
                            onNeq(token);
                            break;
                        case LT:
                            onLt(token);
                            break;
                        case LE:
                            onLe(token);
                            break;
                        case GT:
                            onGt(token);
                            break;
                        case GE:
                            onGe(token);
                            break;
                        case NOT:
                            onNot(token);
                            break;
                        case NEG:
                            onNeg(token);
                            break;
                        case AND:
                            onAndRight(token);
                            break;
                        case OR:
                            onJoinRight(token);
                            break;
                        case FUNC:
                            onMethodInvoke(token);
                            break;
                        case IN:
                            onIn(token);
                            break;
                        case UNION:
                            onUnion(token);
                            break;
                        case DEFAULTOPERATION:
                            onDefaultOperation(token);
                            break;
                    }
                    break;
                case Delegate:
                    DelegateToken delegateToken = (DelegateToken) token;
                    final Token<?> realToken = delegateToken.getToken();
                    switch (delegateToken.getDelegateTokenType()) {
                        case And_Left:
                            onAndLeft(realToken);
                            break;
                        case Join_Left:
                            onJoinLeft(realToken);
                            break;
                        case Method_Name:
                            onMethodName(realToken, isLoad);
                            break;
                        case Method_Param:
                            onMethodParameter(realToken);
                            break;
                    }
                    break;

                default:
                    onConstant(token, isLoad);
                    break;
            }
        }
    }

    public int getLocalIndex() {
        return this.maxLocals++;
    }

    public void onAdd(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.ADD, "add");
    }

    private void loadOpType(final OperatorType opType) {
        this.pushOperand();
        this.mv.visitFieldInsn(GETSTATIC, OT_OWNER,
                opType.name(), OT_DESC);
    }


    protected void popOperand() {
        this.operandsCount--;
    }


    private void popOperand(final int n) {
        this.operandsCount -= n;
    }


    public void onSub(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.SUB, "sub");
    }


    public void onMult(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.MULT, "mult");
    }


    public void onExponent(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.Exponent, "exponent");
    }


    public void onDiv(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.DIV, "div");
    }


    public void onMod(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.MOD, "mod");
    }


    public void onAndLeft(final Token<?> lookhead) {
        loadEnv();
        visitLeftBranch(lookhead, IFEQ, OperatorType.AND);
    }


    private void visitBoolean() {
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "booleanValue", "(Ljava/util/Map;)Z");
    }


    private void pushLabel0(final Label l0) {
        this.l0stack.push(l0);
    }


    public void onAndRight(final Token<?> lookhead) {
        visitRightBranch(lookhead, IFEQ, OperatorType.AND);
        this.popOperand(2); // boolean object and environment
        this.pushOperand();
    }


    private void visitRightBranch(final Token<?> lookhead, final int ints,
                                  final OperatorType opType) {
        loadOpType(opType);
        visitLineNumber(lookhead);
        this.mv.visitMethodInsn(INVOKESTATIC, OR_OWNER,
                "eval",
                OR_EVAL_METHOD_DESC);
        this.popOperand();

    }

    /**
     * Label stack for ternary operator
     */
    private final Stack<Label> l0stack = new Stack<Label>();
    private final Stack<Label> l1stack = new Stack<Label>();

    private Label makeLabel() {
        return new Label();
    }

    public void onTernaryBoolean(final Token<?> lookhead) {
        loadEnv();
        visitLineNumber(lookhead);
        visitBoolean();
        Label l0 = makeLabel();
        Label l1 = makeLabel();
        pushLabel0(l0);
        pushLabel1(l1);
        this.mv.visitJumpInsn(IFEQ, l0);
        this.popOperand();
        this.popOperand();
        this.pushOperand(2); // add two booleans

        this.popOperand(); // pop the last result
    }


    private void pushLabel1(final Label l1) {
        this.l1stack.push(l1);
    }


    public void onTernaryLeft(final Token<?> lookhead) {
        this.mv.visitJumpInsn(GOTO, peekLabel1());
        visitLabel(popLabel0());
        visitLineNumber(lookhead);
        this.popOperand(); // pop one boolean
    }


    private Label peekLabel1() {
        return this.l1stack.peek();
    }


    public void onTernaryRight(final Token<?> lookhead) {
        visitLabel(popLabel1());
        visitLineNumber(lookhead);
        this.popOperand(); // pop one boolean
    }


    public void onTernaryEnd(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        if (this.operandsCount == 0) {
            return;
        }
        while (--this.operandsCount > 0) {
            this.mv.visitInsn(POP);
        }
    }

    private Label popLabel1() {
        return this.l1stack.pop();
    }


    /**
     * Do logic operation "||" right operand
     */

    public void onJoinRight(final Token<?> lookhead) {
        visitRightBranch(lookhead, IFNE, OperatorType.OR);
        this.popOperand(2);
        this.pushOperand();

    }


    private void visitLabel(final Label label) {
        this.mv.visitLabel(label);
        this.currentLabel = label;
    }


    private Label peekLabel0() {
        return this.l0stack.peek();
    }


    private Label popLabel0() {
        return this.l0stack.pop();
    }


    /**
     * Do logic operation "||" left operand
     */

    public void onJoinLeft(final Token<?> lookhead) {
        loadEnv();
        visitLeftBranch(lookhead, IFNE, OperatorType.OR);
    }


    private void visitLeftBranch(final Token<?> lookhead, final int ints, final OperatorType opType) {
        this.popOperand();
    }


    public void onEq(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFNE, OperatorType.EQ);
    }


    public void onNeq(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFEQ, OperatorType.NEQ);
    }


    private void doCompareAndJump(final Token<?> lookhead, final int ints,
                                  final OperatorType opType) {
        visitLineNumber(lookhead);
        loadEnv();
        visitCompare(ints, opType);
    }


    private boolean isEqNe(final int ints) {
        return ints == IFEQ || ints == IFNE;
    }

    private void visitCompare(final int ints, final OperatorType opType) {
        loadOpType(opType);
        this.mv.visitMethodInsn(INVOKESTATIC, OR_OWNER,
                "eval",
                OT_EVAL_METHOD_DESC);
        this.popOperand();

    }


    public void onGe(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFLT, OperatorType.GE);
    }


    public void onGt(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFLE, OperatorType.GT);
    }


    public void onLe(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFGT, OperatorType.LE);

    }


    public void onLt(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFGE, OperatorType.LT);
    }

    private void pushOperand(final int delta) {
        this.operandsCount += delta;
        setMaxStacks(this.operandsCount);
    }


    /**
     * Logic operation '!'
     */

    public void onNot(final Token<?> lookhead) {
        visitUnaryOperator(lookhead, OperatorType.NOT, "not");
    }

    private void visitBinOperator(final Token<?> token, final OperatorType opType,
                                  final String methodName) {
        visitLineNumber(token);
        loadEnv();
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, methodName,
                OBJECT_OPERATION_METHOD_DESC);
    }

    private void visitLineNumber(final Token<?> token) {
        if (token != null && token.getLineNo() > 0) {
            this.mv.visitLineNumber(token.getLineNo(), this.currentLabel);
        }
    }

    private void visitUnaryOperator(final Token<?> lookhead, final OperatorType opType,
                                    final String methodName) {
        visitLineNumber(lookhead);
        this.mv.visitTypeInsn(CHECKCAST, OBJECT_OWNER);
        loadEnv();
        this.popOperand();
    }

    public void onNeg(final Token<?> lookhead) {
        visitUnaryOperator(lookhead, OperatorType.NEG, "neg");
    }

    protected void loadEnv() {
        this.mv.visitVarInsn(ALOAD, 1);
        this.pushOperand();
    }

    private void setMaxStacks(final int newMaxStacks) {
        if (newMaxStacks > this.maxStacks) {
            this.maxStacks = newMaxStacks;
        }
    }

    public void onMethodInvoke(final Token<?> lookhead) {

//        loadEnv();
        visitLineNumber(lookhead);

        final MetricUnitRealCodeGenerator.MethodMetaData methodMetaData = this.methodMetaDataStack.pop();
        final int parameterCount = methodMetaData.parameterCount;
        this.mv.visitMethodInsn(INVOKEINTERFACE, FCUNTION_OWNER,
                "call", getInvokeMethodDesc(parameterCount));
        this.mv.visitMethodInsn(INVOKESTATIC, RU_OWNER, "assertNotNull",
                RU_OWNER_GETFUNCTION_DESC);

        this.popOperand(); // method object
        this.popOperand(); // env map
        this.popOperand(parameterCount);
        this.pushOperand();
    }

    public void onIn(Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.IN, "in");
    }

    public void onUnion(Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.UNION, "union");
    }

    public void onDefaultOperation(Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.DEFAULTOPERATION, "defaultOperation");
    }

    public void onMethodName(final Token<?> lookhead, boolean isLoad) {
        String outtterMethodName = lookhead.getLexeme();
        if (isLoad) {
            String innerMethodName = this.innerMethodMap.get(outtterMethodName);
            Preconditions.checkNotNull(innerMethodName, outtterMethodName + " is not init");
            if (innerMethodName != null) {
                loadFunction(outtterMethodName, innerMethodName);
            }
        } else {
            if (this instanceof MetricUnitRealCodeGenerator) {
//                mv.visitVarInsn(ALOAD, 0);
//                mv.visitFieldInsn(GETFIELD, Type.getInternalName(BaseExpression.class), "instance",
//                        EXECUTOR_DESC);
//                this.mv.visitLdcInsn(outtterMethodName);
////            mv.visitLdcInsn("[]");
//                mv.visitMethodInsn(INVOKEVIRTUAL, EXECUTOR_OWNER,
//                        "getFunction",
//                        GET_FUNCTION_DESC);
                this.mv.visitLdcInsn(outtterMethodName);
                this.mv.visitMethodInsn(INVOKESTATIC, RU_OWNER, "getFunction",
                        "(Ljava/lang/String;)" + Type.getDescriptor(SFunction.class));
            }


//            mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(ListFunction.class));
//            mv.visitInsn(Opcodes.DUP);
//            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(ListFunction.class), "<init>", "()V");
        }
        loadEnv();
        this.methodMetaDataStack.push(new MetricUnitRealCodeGenerator.MethodMetaData(lookhead, outtterMethodName));
    }

    private void loadFunction(final String outterMethodName, final String innerMethodName) {
        Map<String, Integer> name2Index = this.labelNameIndexMap.get(this.currentLabel);
        // Is it stored in local?
        if (name2Index != null && name2Index.containsKey(innerMethodName)) {
            int localIndex = name2Index.get(innerMethodName);
            this.mv.visitVarInsn(ALOAD, localIndex);
            this.pushOperand();
        } else {
            this.mv.visitVarInsn(ALOAD, 0);
            this.mv.visitFieldInsn(GETFIELD, this.className, innerMethodName,
                    FUNCTION_DESC);
            this.pushOperand();
        }
    }

    public void onMethodParameter(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        MetricUnitRealCodeGenerator.MethodMetaData currentMethodMetaData = this.methodMetaDataStack.peek();
        currentMethodMetaData.parameterCount++;
    }

    protected void onConstant(final Token<?> lookhead, final boolean isLoad) {
        if (lookhead == null) {
            return;
        }
        visitLineNumber(lookhead);
        switch (lookhead.getType()) {
            case Number:
                if (loadConstant(lookhead, isLoad)) {
                    return;
                }
                NumberToken numberToken = (NumberToken) lookhead;
                Number number = numberToken.getNumber();

                if (TypeUtils.isBigInt(number)) {
                    this.mv.visitLdcInsn(numberToken.getLexeme());
                    this.mv.visitMethodInsn(INVOKESTATIC, BIGINT_OWNER,
                            "valueOf", BIGINT_VALUEOF_DESC);
                } else if (TypeUtils.isDecimal(number)) {
                    loadEnv();
                    this.mv.visitLdcInsn(numberToken.getLexeme());
                    this.mv.visitMethodInsn(INVOKESTATIC,
                            DECIMAL_OWNER, "valueOf", DECIMAL_VALUEOF_DESC);
                } else if (TypeUtils.isDouble(number)) {
                    this.mv.visitLdcInsn(number);
                    this.mv.visitMethodInsn(INVOKESTATIC, DOUBLE_OWNER,
                            "valueOf", DOUBLE_VALUEOF_DESC);
                } else {
                    this.mv.visitLdcInsn(number);
                    this.mv.visitMethodInsn(INVOKESTATIC, LONG_OWNER,
                            "valueOf", LONG_VALUEOF_DESC);
                }
                break;
            case String:
                if (loadConstant(lookhead, isLoad)) {
                    return;
                }
                this.mv.visitTypeInsn(NEW, STRING_OWNER);
                this.mv.visitInsn(DUP);
                this.mv.visitLdcInsn(lookhead.getJavaValue(null));
                this.mv.visitMethodInsn(INVOKESPECIAL, STRING_OWNER,
                        CONSTRUCTOR_METHOD_NAME, "(Ljava/lang/String;)V");
                break;
            case Variable:
                if (loadConstant(lookhead, isLoad)) {
                    return;
                }
                VariableToken variableToken = (VariableToken) lookhead;

                if (variableToken.equals(VariableToken.TRUE)) {
                    this.mv.visitFieldInsn(GETSTATIC, BOOLEAN_OWNER,
                            "TRUE", BOOLEAN_DESC);
                } else if (variableToken.equals(VariableToken.FALSE)) {
                    this.mv.visitFieldInsn(GETSTATIC, BOOLEAN_OWNER,
                            "FALSE", BOOLEAN_DESC);
                } else if (variableToken.equals(VariableToken.NULL)) {
                    this.mv.visitFieldInsn(GETSTATIC, NIL_OWNER, "NULL",
                            NIL_DESC);
                } else {
                    if (isLoad) {
                        if (ParserUtils.isMetricToken(variableToken)) {
                            String outterVarName = variableToken.getLexeme();
                            String innerVarName = this.innerMetricVars.get(outterVarName);
                            this.mv.visitVarInsn(ALOAD, 0);
                            this.mv.visitFieldInsn(GETFIELD, this.className, innerVarName,
                                    SMERTRIC_DESC);
                        } else {
                            String outterVarName = variableToken.getLexeme();
                            String innerVarName = this.innerVars.get(outterVarName);
                            this.mv.visitVarInsn(ALOAD, 0);
                            this.mv.visitFieldInsn(GETFIELD, this.className, innerVarName,
                                    JAVATYPE_DESC);
                        }
                    } else {
                        String outterVarName = variableToken.getLexeme();

                        this.mv.visitTypeInsn(NEW, JAVATYPE_OWNER);
                        this.mv.visitInsn(DUP);
                        this.mv.visitLdcInsn(outterVarName);
                        this.mv.visitMethodInsn(INVOKESPECIAL, JAVATYPE_OWNER, CONSTRUCTOR_METHOD_NAME,
                                "(" + Type.getDescriptor(String.class) + ")V");
                    }
                }
                break;
        }
    }

    private boolean loadConstant(final Token<?> lookhead, final boolean isLoad) {
        String fieldName;
        if (isLoad && (fieldName = this.constantPool.get(lookhead)) != null) {
            this.mv.visitVarInsn(ALOAD, 0);
            this.mv.visitFieldInsn(GETFIELD, this.className, fieldName, OBJECT_DESC);
            this.pushOperand();
            return true;
        }
        return false;
    }

    protected void pushOperand() {
        this.pushOperand(1);
    }

    private static String getInvokeMethodDesc(final int paramCount) {
        StringBuilder sb = new StringBuilder("(Ljava/util/Map;");
        if (paramCount <= 20) {
            for (int i = 0; i < paramCount; i++) {
                sb.append(OBJECT_DESC);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                sb.append(OBJECT_DESC);
            }
            // variadic params as an array
            sb.append(OBJECT_LIST_DESC);
        }
        sb.append(")");
        sb.append(OBJECT_DESC);
        return sb.toString();
    }

    protected void endVisitClass() {
        this.classWriter.visitEnd();
    }

    public static class MethodMetaData {
        public int parameterCount = 0;

        public int variadicListIndex = -1;

        public final Token<?> token;

        public final String methodName;

        public int funcId = -1;


        public MethodMetaData(final Token<?> token, final String methodName) {
            super();
            this.token = token;
            this.methodName = methodName;
        }
    }

    public abstract T getResult();
}
