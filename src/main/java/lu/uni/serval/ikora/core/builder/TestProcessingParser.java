package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.Scope;
import lu.uni.serval.ikora.core.model.TestProcessing;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class TestProcessingParser {
    private TestProcessingParser() {}

    public static boolean is(Token label, Scope scope, TestProcessing.Phase phase){
        String scopeString;

        switch (scope){
            case TEST: scopeString = "test "; break;
            case SUITE: scopeString = "suite "; break;
            default: scopeString = ""; break;
        }

        String phaseString;

        switch (phase){
            case SETUP:  phaseString = "setup"; break;
            case TEARDOWN: phaseString = "teardown"; break;
            case TEMPLATE: phaseString = "template"; break;
            default: return false;
        }

        return StringUtils.matchesIgnoreCase(label, "\\[" + scopeString + phaseString + "\\]");
    }

    public static TestProcessing parse(TestProcessing.Phase phase, LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        final KeywordCall call = tokenIterator.hasNext()
                ? KeywordCallParser.parse(reader, tokenIterator.next(), tokenIterator, false, errors)
                :null;

        return new TestProcessing(TestProcessing.Phase.TEARDOWN, label, call);
    }
}
