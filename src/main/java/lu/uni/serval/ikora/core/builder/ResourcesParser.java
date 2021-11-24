package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Resources;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourcesParser {
    private ResourcesParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "resource");
    }

    public static Resources parse(Token label, Iterator<Token> tokenIterator) {
        final Token filePath = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        final List<Token> arguments = new ArrayList<>();

        while (tokenIterator.hasNext()){
            arguments.add(tokenIterator.next());
        }

        return new Resources(label, filePath, arguments);
    }
}
