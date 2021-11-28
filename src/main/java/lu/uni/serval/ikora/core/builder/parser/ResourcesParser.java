package lu.uni.serval.ikora.core.builder.parser;

import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Resources;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class ResourcesParser {
    private ResourcesParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "resource");
    }

    public static Resources parse(Token label, Iterator<Token> tokenIterator) {
        final Token filePath = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        final NodeList<Value> arguments = new NodeList<>();

        while (tokenIterator.hasNext()){
            arguments.add(ValueParser.parseValue(tokenIterator.next()));
        }

        return new Resources(label, filePath, arguments);
    }
}
