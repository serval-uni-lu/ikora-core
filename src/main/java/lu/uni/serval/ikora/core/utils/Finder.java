package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.runtime.Runtime;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class Finder {
    private Finder () {}

    public static Set<? super Keyword> findKeywords(Runtime runtime, KeywordCall call){
        return findKeywords(runtime, call.getDefinitionToken(), call.getSourceFile());
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
