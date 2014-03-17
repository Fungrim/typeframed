package org.typeframed.protobuf.parser;

import org.parboiled.MatcherContext;
import org.parboiled.matchers.CustomMatcher;

public class CharMatcher extends CustomMatcher {

    private final boolean allowDigits;

	protected CharMatcher(String label, boolean allowDigits) {
        super(label);
		this.allowDigits = allowDigits;
    }

    @Override
    public final boolean isSingleCharMatcher() {
        return true;
    }

    @Override
    public final boolean canMatchEmpty() {
        return false;
    }

    @Override
    public boolean isStarterChar(char c) {
        return acceptChar(c);
    }

    @Override
    public final char getStarterChar() {
        return 'a';
    }

    public final <V> boolean match(MatcherContext<V> context) {
        if (!acceptChar(context.getCurrentChar())) {
            return false;
        }
        context.advanceIndex(1);
        context.createNode();
        return true;
    }

    protected boolean acceptChar(char c) {
    	if(allowDigits) {
    		return Character.isJavaIdentifierPart(c);
    	} else {
    		return Character.isJavaIdentifierStart(c);
    	}
    }
}
