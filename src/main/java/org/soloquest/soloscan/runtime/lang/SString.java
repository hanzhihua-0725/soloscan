package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.utils.TypeUtils;

import java.util.Map;

public class SString extends SObject<String> {

    private final String lexeme;

    public SString(final String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public SObjectType getSObjectType() {
        return SObjectType.String;
    }

    @Override
    public String getValue(final Map<String, Object> env) {
        return this.lexeme;
    }

    @Override
    public SObject add(final SObject other, final Map<String, Object> env) {
        final StringBuilder sb = new StringBuilder(this.lexeme);
        sb.append(other.getValue(env));
        return new SString(sb.toString());
    }


    @Override
    public int innerCompare(final SObject other, final Map<String, Object> env) {
        final String left = this.lexeme;

        if (other.getSObjectType() == SObjectType.String) {
            final SString otherString = (SString) other;
            final String right = (String) otherString.getValue(env);
            if (left != null && right != null) {
                return left.compareTo(right);
            } else if (left == null && right != null) {
                return -1;
            } else if (left != null && right == null) {
                return 1;
            } else {
                return 0;
            }
        }

        switch (other.getSObjectType()) {
            case JavaType:
                final SJavaType javaType = (SJavaType) other;
                final Object otherJavaValue = javaType.getValue(env);
                if (left == null && otherJavaValue == null) {
                    return 0;
                } else if (left != null && otherJavaValue == null) {
                    return 1;
                }
                if (TypeUtils.isString(otherJavaValue)) {
                    if (left == null) {
                        return -1;
                    } else {
                        return left.compareTo(String.valueOf(otherJavaValue));
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            case Nil:
                if (left == null) {
                    return 0;
                } else {
                    return 1;
                }
            default:
                throw new UnsupportedOperationException();
        }
    }
}
