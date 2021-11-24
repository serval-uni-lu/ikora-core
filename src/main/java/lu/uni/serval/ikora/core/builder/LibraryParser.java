package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Library;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibraryParser {
    private LibraryParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "library");
    }

    public static Library parse(Token label, Iterator<Token> tokenIterator) {
        final Token name = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        final List<Token> arguments = new ArrayList<>();

        while (tokenIterator.hasNext()){
            arguments.add(tokenIterator.next());
        }

        return new Library(label, name, arguments);
    }
}
