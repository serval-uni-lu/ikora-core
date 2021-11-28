package lu.uni.serval.ikora.core.builder.parser;

import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class ReturnParser {
    private ReturnParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "\\[return\\]");
    }

    public static NodeList<Value> parse(Token label, Iterator<Token> tokenIterator) {
        NodeList<Value> returnValues = new NodeList<>(label.setType(Token.Type.LABEL));

        while(tokenIterator.hasNext()){
            returnValues.add(ValueParser.parseValue(tokenIterator.next()));
        }

        return returnValues;
    }
}
