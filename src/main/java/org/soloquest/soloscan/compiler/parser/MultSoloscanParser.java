package org.soloquest.soloscan.compiler.parser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MultSoloscanParser<T> {

//    private final SoloscanExecutor instance;
//    private final SoloscanClassloader classLoader;
//    private final String expressionString;
//    private final Map<String, FutureTask<AggInner>> aggUnitMap;
//
////    private final static char MERGE_CHAR = '+';
//    private final String[] operators = new String[]{"union","default"};
//
//    private Token<?> lookhead;
//
//    private List<Object> objects = new ArrayList<>();
//    private LinkedList operatorsStack = new LinkedList();
//
//    public T parse() {
//        SoloscanLexer lexer = new SoloscanLexer(instance, expressionString);
//        MetricUnitRealCodeGenerator<Expression> realCodeGenerator = new MetricUnitRealCodeGenerator(instance, classLoader, Expression.class);
//        CodeGeneratorProxy<Expression> codeGenerator = new CodeGeneratorProxy<Expression>(instance, classLoader, realCodeGenerator);
//        SoloscanParser<Expression> parser = new SoloscanParser(instance, lexer, codeGenerator);
//        Expression expression = parser.parseMetricUnit();
//        assembleAggUnit(expression, parser);
//        this.lookhead = parser.afterExpressionToken;
//        if (lookhead == null) {
//            return (T) expression;
//        }
//        objects.add(expression);
//        processUnion(new StringBuilder(expressionString));
////        while (true) {
////            if (isOperator()) {
////                String operatorString = this.lookhead.getLexeme();
////                newExpressionString = newExpressionString.substring(this.lookhead.getStartIndex() + operatorString.length());
////                if (MiscUtils.isBlank(newExpressionString)) {
////                    throw new ExpressionCompileException(String.format("'%s' is invalid,'%s' is the string of end", expressionString,operatorString));
////                }
////                lexer = new SoloscanLexer(instance, newExpressionString);
////                realCodeGenerator = new ExpressionRealCodeGenerator(instance, classLoader, Expression.class);
////                codeGenerator = new SoloscanCodeGenerator(instance, classLoader, realCodeGenerator);
////                parser = new SoloscanParser(instance, lexer, codeGenerator);
////                expression = parser.parseExpression();
////                assembleAggUnit(expression, parser);
////                objects.add(expression);
////                this.lookhead = parser.afterExpressionToken;
////                if (lookhead == null) {
////                    objects.add(operatorString);
////                    break;
////                }
////            } else {
////                throw new ExpressionCompileException("expressionString '" + expressionString + "' is invalid!");
////            }
////        }
//        MultiExpression multiExpression = new MultiExpression(objects, expressionString);
//        return MiscUtils.forciblyCast(multiExpression);
//    }
//
//    private void processUnion(final StringBuilder stringBuilder) {
//        processDefault(stringBuilder);
//        while (true) {
//            if(this.lookhead == null){
//                return ;
//            }
//            if (isUnionOperator()) {
//                String operatorString = this.lookhead.getLexeme();
//                stringBuilder.delete(0,this.lookhead.getStartIndex() + operatorString.length());
//                if (MiscUtils.isBlank(stringBuilder.toString())) {
//                    throw new ExpressionCompileException(String.format("'%s' is invalid,'%s' is the string of end", expressionString, operatorString));
//                }
//                SoloscanLexer lexer = new SoloscanLexer(instance, stringBuilder.toString());
//                MetricUnitRealCodeGenerator realCodeGenerator = new MetricUnitRealCodeGenerator(instance, classLoader, Expression.class);
//                CodeGeneratorProxy codeGenerator = new CodeGeneratorProxy(instance, classLoader, realCodeGenerator);
//                SoloscanParser<Expression> parser = new SoloscanParser(instance, lexer, codeGenerator);
//                Expression expression = parser.parseMetricUnit();
//                assembleAggUnit(expression, parser);
//                objects.add(expression);
//                this.lookhead = parser.afterExpressionToken;
//                if (lookhead == null) {
//                    objects.add(operatorString);
//                    break;
//                } else {
//                    processDefault(stringBuilder);
//                    objects.add(operatorString);
//                }
//            } else {
//                throw new ExpressionCompileException("expressionString '" + expressionString + "' is invalid!");
//            }
//        }
//    }
//
//
//        private void processDefault(final StringBuilder stringBuilder){
//            while (true) {
//                if (isDefaultOperator()) {
//                    String operatorString = this.lookhead.getLexeme();
//                    stringBuilder.delete(0,this.lookhead.getStartIndex() + operatorString.length());
//                    if (MiscUtils.isBlank(stringBuilder.toString())) {
//                        throw new ExpressionCompileException(String.format("'%s' is invalid,'%s' is the string of end", expressionString,operatorString));
//                    }
//                    SoloscanLexer lexer = new SoloscanLexer(instance, stringBuilder.toString());
//                    MetricUnitRealCodeGenerator realCodeGenerator = new MetricUnitRealCodeGenerator(instance, classLoader, Expression.class);
//                    CodeGeneratorProxy codeGenerator = new CodeGeneratorProxy(instance, classLoader, realCodeGenerator);
//                    SoloscanParser<Expression> parser = new SoloscanParser(instance, lexer, codeGenerator);
//                    Expression expression = parser.parseMetricUnit();
//                    assembleAggUnit(expression, parser);
//                    objects.add(expression);
//                    objects.add(operatorString);
//                    this.lookhead = parser.afterExpressionToken;
//                    if (lookhead == null) {
//                        break;
//                    }
//                } else{
//                    break;
//                }
//            }
//    }
//
//    private boolean expectOperator(final String string) {
//        if (this.lookhead == null) {
//            return false;
//        }
//        return this.lookhead.getType() == Token.TokenType.Variable && ((VariableToken) this.lookhead).getLexeme().equalsIgnoreCase(string);
//    }
//
//    private boolean isOperator() {
//        if (this.lookhead == null) {
//            return false;
//        }
//        for(String operator : operators){
//            if(this.lookhead.getType() == Token.TokenType.Variable && ((VariableToken) this.lookhead).getLexeme().equalsIgnoreCase(operator)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isUnionOperator() {
//        if (this.lookhead == null) {
//            return false;
//        }
//        if(this.lookhead.getType() == Token.TokenType.Variable && (((VariableToken) this.lookhead).getLexeme().equalsIgnoreCase("union"))
//                || (this.lookhead.getType() == Token.TokenType.Variable && ((VariableToken) this.lookhead).getLexeme().equalsIgnoreCase("divide"))
//            ){
//            return true;
//        }else if(this.lookhead.getType() == Token.TokenType.Char && ((this.lookhead.getLexeme().equalsIgnoreCase("+"))
//                || (this.lookhead.getType() == Token.TokenType.Char && this.lookhead.getLexeme().equalsIgnoreCase("/"))
//        )){
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isDefaultOperator() {
//        if (this.lookhead == null) {
//            return false;
//        }
//        if(this.lookhead.getType() == Token.TokenType.Variable && ((VariableToken) this.lookhead).getLexeme().equalsIgnoreCase("default")){
//            return true;
//        }
//        return false;
//    }
//
////    private boolean isNoPop(String operatorString){
////        if(operatorsStack.size()>0){
////            String operatorString1 = (String) operatorsStack.peek();
////        }
////    }
//
//    private void assembleAggUnit(Expression expression, SoloscanParser parser) {
//        try {
//            AggFunctionUnit aggFunctionUnit;
//            List<AggFunctionText> aggFunctionTexts = parser.getAggFunctionTexts().getFunctionTexts();
//            for (AggFunctionText aggFunctionText : aggFunctionTexts) {
//                Function<AggFunctionText, ? extends AggFunction> function = instance.getAggFunction(aggFunctionText.getName());
//                if (MiscUtils.isBlank(aggFunctionText.getInnerString())) {
//                    aggFunctionUnit = AggFunctionUnit.newAggFunctionUnit(aggFunctionText, function, null);
//                } else if (aggUnitMap.containsKey(aggFunctionText.getInnerString())) {
//                    FutureTask<AggInner> task = aggUnitMap.get(aggFunctionText.getInnerString());
//                    aggFunctionUnit = AggFunctionUnit.newAggFunctionUnit(aggFunctionText, function, task.get());
//                } else {
//                    FutureTask<AggInner> task = new FutureTask<>(new Callable<AggInner>() {
//                        @Override
//                        public AggInner call() throws Exception {
//                            return genAggInner(aggFunctionText);
//                        }
//                    });
//                    FutureTask<AggInner> exitTask = aggUnitMap.putIfAbsent(aggFunctionText.getInnerString(), task);
//                    if (exitTask != null) {
//                        task = exitTask;
//                    } else {
//                        task.run();
//                    }
//
//                    aggFunctionUnit = AggFunctionUnit.newAggFunctionUnit(aggFunctionText, function, task.get());
//                }
////                ((SoloExpression) expression).addAggUnit(aggFunctionUnit);
//            }
//        } catch (Exception e) {
//            if (e instanceof ExecutionException) {
//                if (e.getCause() instanceof ExpressionRuntimeException) {
//                    throw (ExpressionRuntimeException) e.getCause();
//                }
//            }
//            throw new ExpressionCompileException(e);
//        }
//    }
//
//    private AggInner genAggInner(AggFunctionText aggFunctionText) {
//        String filterString = aggFunctionText.getInnerString();
//        if (!MiscUtils.isBlank(filterString)) {
//            SoloscanLexer lexer = new SoloscanLexer(this.instance, filterString);
//            AggInnerRealCodeGenerator<AggInner> realCodeGenerator = new AggInnerRealCodeGenerator(instance, classLoader, AggInner.class);
//            CodeGeneratorProxy<AggInner> codeGenerator = new CodeGeneratorProxy(this.instance, this.classLoader, realCodeGenerator);
//            SoloscanParser<AggInner> parser = new SoloscanParser(this.instance, lexer, codeGenerator, true);
//            AggInner aggInner = null;
//            if (isXAggFunction(aggFunctionText)) {
//                aggInner = parser.parseXAggFunctionInner();
//            } else {
//                aggInner = parser.parseAggFunctionInner();
//            }
//            return aggInner;
//        }
//        return null;
//    }
//
//    private boolean isXAggFunction(AggFunctionText aggFunctionText) {
//        return aggFunctionText.getName().endsWith("X") || aggFunctionText.getName().endsWith("x");
//    }


}
