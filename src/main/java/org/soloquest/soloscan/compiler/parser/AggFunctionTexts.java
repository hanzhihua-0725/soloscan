package org.soloquest.soloscan.compiler.parser;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AggFunctionTexts {


    public final String expression;
    public final List<AggFunctionText> functionTexts = new ArrayList<>();

    public AggFunctionTexts(String expression) {
        this.expression = expression;
    }

    public void addAggFunctionText(AggFunctionText functionText) {
        functionTexts.add(functionText);
        log.warn("add AggFunctionText, name is {} and  placehold is : {}", functionText.getName(), functionText.getPlaceHolder());
    }

    public List<AggFunctionText> getFunctionTexts() {
        return this.functionTexts;
    }


}
