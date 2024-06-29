package org.soloquest.soloscan.compiler;

import lombok.Data;

@Data
public class ValidatorResult {

    private boolean valid = true;
    private String errorMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.valid = false;
    }

}
