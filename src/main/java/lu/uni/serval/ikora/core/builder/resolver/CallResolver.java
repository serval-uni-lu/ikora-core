package lu.uni.serval.ikora.core.builder.resolver;

import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class CallResolver {
    private CallResolver() {}

    public static void resolve(KeywordCall call, Runtime runtime){
        final Set<? super Keyword> keywords = findKeywords(runtime, call.getDefinitionToken(), call.getSourceFile());

        for(Object keyword: keywords) {
            call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
        }

        if(keywords.isEmpty()){
            runtime.getErrors().registerSymbolError(
                    call.getSource(),
                    ErrorMessages.FOUND_NO_MATCH,
                    Range.fromTokens(call.getTokens())
            );
        }

        ArgumentResolver.resolve(call, runtime);
    }

    private static Set<? super Keyword> findKeywords(Runtime runtime, Token fullName, SourceFile sourceFile) {
        Set<? super Keyword> keywordsFound = findKeywords(runtime, fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = findKeywords(runtime, fullName, sourceFile, true);
        }

        return keywordsFound;
    }

    private static Set<? super Keyword> findKeywords(Runtime runtime, Token fullName, SourceFile sourceFile, boolean allowSplit) {
        String library = "";
        Token name = fullName;

        if(allowSplit){
            Pair<Token, Token> libraryAndName = fullName.splitLibrary();

            library = libraryAndName.getLeft().getText();
            name = libraryAndName.getRight();
        }

        final Set<? super Keyword> keywordsFound = new HashSet<>(sourceFile.findUserKeyword(library, name));

        if(keywordsFound.isEmpty()){
            runtime.findLibraryKeyword(sourceFile.getAllLibraries(), name).ifPresent(keywordsFound::add);
        }

        return keywordsFound;
    }
}
