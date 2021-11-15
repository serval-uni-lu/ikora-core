package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Documentation;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.core.utils.StringUtils;

public class DocumentationParser {
    private DocumentationParser() {}

    public static boolean is(Token label){
        return StringUtils.compareNoCase(label, "\\[documentation\\]");
    }

    public static Documentation parse(Token label, Tokens tokens){
        return new Documentation(label, tokens.withoutTag("\\[documentation\\]"));
    }
}
