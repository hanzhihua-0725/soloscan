package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.BaseMetricUnitExpression;
import org.soloquest.soloscan.BaseSoloExpression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.asm.Type;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.OperatorType;
import org.soloquest.soloscan.runtime.aggfunction.AggInner;
import org.soloquest.soloscan.runtime.lang.*;
import org.soloquest.soloscan.utils.Env;

import java.util.Map;

public interface CodeConstants {


    String OBJECT_OWNER = Type.getInternalName(SObject.class);
    String OBJECT_DESC = Type.getDescriptor(SObject.class);
    String OBJECT_OPERATION_METHOD_DESC = "(" + OBJECT_DESC + Type.getDescriptor(Map.class) + ")" + OBJECT_DESC;

    String OBJECT_LIST_DESC = "[" + OBJECT_DESC;

    String JAVATYPE_OWNER = Type.getInternalName(SJavaType.class);
    String JAVATYPE_DESC = Type.getDescriptor(SJavaType.class);

    String SMERTRIC_OWNER = Type.getInternalName(SMertric.class);
    String SMERTRIC_DESC = Type.getDescriptor(SMertric.class);
    String FCUNTION_OWNER = Type.getInternalName(SFunction.class);
    String FUNCTION_DESC = Type.getDescriptor(SFunction.class);

    String ENV_OWNER = Type.getInternalName(Env.class);
    String ENV_DESC = Type.getDescriptor(Env.class);


    String CONSTRUCTOR_METHOD_NAME = "<init>";

    String CONSTRUCTOR_DESC = "(" + Type.getDescriptor(SoloscanExecutor.class) + Type.getDescriptor(SymbolTable.class) + Type.getDescriptor(String.class) + ")V";

    String NEW_JAVATYPE_DESC = "(" + Type.getDescriptor(String.class) + Type.getDescriptor(SymbolTable.class) + ")V";

    //    String SUPER_CLASSNAME = BaseExpression.class.getCanonicalName().replaceAll("\\.","/");
    String SUPER_CLASSNAME = Type.getInternalName(BaseSoloExpression.class);
    String SUPER_CONSTRUCTOR_DESC = CONSTRUCTOR_DESC;

    String MU_SUPER_CLASSNAME = Type.getInternalName(BaseMetricUnitExpression.class);

    String EXECUTOR_OWNER = Type.getInternalName(SoloscanExecutor.class);
    String EXECUTOR_DESC = Type.getDescriptor(SoloscanExecutor.class);

    String GET_FUNCTION_DESC = "(Ljava/lang/String;)" + FUNCTION_DESC;

    String BIGINT_OWNER = Type.getInternalName(SBigInt.class);

    String BIGINT_VALUEOF_DESC = "(Ljava/lang/String;)L" + BIGINT_OWNER + ";";

    String DECIMAL_OWNER = Type.getInternalName(SDecimal.class);

    String DECIMAL_VALUEOF_DESC = "(Ljava/util/Map;Ljava/lang/String;)L" + DECIMAL_OWNER + ";";

    String DOUBLE_OWNER = Type.getInternalName(SDouble.class);

    String DOUBLE_VALUEOF_DESC = "(D)" + Type.getDescriptor(SDouble.class);

    String LONG_OWNER = Type.getInternalName(SLong.class);

    String LONG_VALUEOF_DESC = "(J)" + Type.getDescriptor(SLong.class);

    String STRING_OWNER = Type.getInternalName(SString.class);

    String BOOLEAN_OWNER = Type.getInternalName(SBoolean.class);
    String BOOLEAN_DESC = Type.getDescriptor(SBoolean.class);

    String NIL_OWNER = Type.getInternalName(SNull.class);
    String NIL_DESC = Type.getDescriptor(SNull.class);

    String EXECUTE_METHOD_NAME = "execute0";
    String EXECUTE_DESC = "(L" + Env.class.getCanonicalName().replaceAll("\\.", "/") + ";)Ljava/lang/Object;";

    String OR_OWNER = OperationRuntime.class.getCanonicalName().replaceAll("\\.", "/");

    String OT_OWNER = Type.getInternalName(OperatorType.class);
    String OT_DESC = Type.getDescriptor(OperatorType.class);

    String OT_EVAL_METHOD_DESC = "(" + OBJECT_DESC + OBJECT_DESC + "Ljava/util/Map;" + OT_DESC + ")" + OBJECT_DESC;
    String OR_EVAL_METHOD_DESC = "(" + OBJECT_DESC + "Ljava/util/Map;" + OBJECT_DESC + OT_DESC + ")" + OBJECT_DESC;
    String OR_EVAL_METHOD_DESC2 = "(" + OBJECT_DESC + OBJECT_DESC + "Ljava/util/Map;" + OT_DESC + ")" + OBJECT_DESC;

    String RU_OWNER = Type.getInternalName(RuntimeUtils.class);
    String RU_OWNER_ASSERTNOTNULL_DESC = "(Ljava/lang/Object;Ljava/util/Map;)" + FUNCTION_DESC;
    String RU_OWNER_GETFUNCTION_DESC = "(" + OBJECT_DESC + ")" + OBJECT_DESC;


    String FILTER_INTERFACE = AggInner.class.getCanonicalName().replaceAll("\\.", "/");
    String CHECK_METHOD_NAME = "check";
    String CHECK_METHOD_DESC = "(" + ENV_DESC + ")Z";
    ;


    String GETCOLUMNVALUE_METHOD_NAME = "getInnerValue";
    String GETCOLUMNVALUE_METHOD_DESC = "(" + ENV_DESC + ")" + Type.getDescriptor(Object.class);


    String GROUPING_METHOD_NAME = "grouping";
    String GROUPING_METHOD_DESC = "(" + ENV_DESC + ")" + Type.getDescriptor(String.class);

    String FILTER_METHOD_NAME = "filter";
    String FILTER_METHOD_DESC = "(" + ENV_DESC + ")" + Type.getDescriptor(boolean.class);

}
