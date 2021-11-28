package lu.uni.serval.ikora.core.builder.parser;

import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.model.VariableFile;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class VariableFileParser {
    private VariableFileParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "variables");
    }

    public static VariableFile parse(Token label, Iterator<Token> tokenIterator) {
        final Token name = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        final NodeList<Value> arguments = new NodeList<>();

        while (tokenIterator.hasNext()){
            arguments.add(ValueParser.parseValue(tokenIterator.next()));
        }

        return new VariableFile(label, name, arguments);
    }
}
