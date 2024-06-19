package org.soloquest.soloscan.compiler.lexer.token;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class AbstractToken<T> implements Token<T> {

    protected final int lineIndex;

    protected final int lineNo;


    @Override
    public int getLineNo() {
        return this.lineNo;
    }


    protected String lexeme;
    private Map<String, Object> metaMap;


    @Override
    public Map<String, Object> getMetaMap() {
        return this.metaMap;
    }


    public void setMetaMap(final Map<String, Object> metaMap) {
        assert (metaMap == null || metaMap instanceof IdentityHashMap);
        this.metaMap = metaMap;
    }


    @Override
    public Token<T> withMeta(final String name, final Object v) {
        if (this.metaMap == null) {
            this.metaMap = new IdentityHashMap<>();
        }
        this.metaMap.put(name, v);
        return this;
    }


    @Override
    public <V> V getMeta(final String name, final V defaultVal) {
        if (this.metaMap == null) {
            return defaultVal;
        }
        V val = (V) this.metaMap.get(name);
        if (val == null) {
            return defaultVal;
        }
        return val;
    }

    @Override
    public <V> V getMeta(final String name) {
        if (this.metaMap == null) {
            return null;
        }
        return (V) this.metaMap.get(name);
    }


    public AbstractToken(final String lexeme, final int lineNo, final int lineIdex) {
        super();
        this.lineNo = lineNo;
        this.lineIndex = lineIdex;
        this.lexeme = lexeme;
    }


    @Override
    public String getLexeme() {
        return this.lexeme;
    }


    @Override
    public int getStartIndex() {
        return this.lineIndex;
    }


    @Override
    public String toString() {
        String ci = childInfo().length() > 0 ? "," + childInfo() : "";
        return "[type='" + getType().name() + "',lexeme='" + getLexeme() + "',index=" + this.lineIndex + ",line=" + this.lineNo
                + ci +
                "]";
    }

    public String childInfo() {
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.lexeme == null) ? 0 : this.lexeme.hashCode());
        result = prime * result + this.lineIndex;
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractToken<?> other = (AbstractToken<?>) obj;
        if (this.lexeme == null) {
            if (other.lexeme != null) {
                return false;
            }
        } else if (!this.lexeme.equals(other.lexeme)) {
            return false;
        }
        if (this.lineIndex != other.lineIndex) {
            return false;
        }
        return true;
    }

}
