package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Resources;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class ResourcesParser {
    private ResourcesParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "\\[resource\\]");
    }

    public static Resources parse(Token label, Iterator<Token> tokenIterator) {
        Token filePath = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        return new Resources(label, filePath, new ArrayList<>(), Token.empty());
    }
}
