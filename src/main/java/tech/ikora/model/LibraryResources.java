package tech.ikora.model;

import tech.ikora.error.ErrorManager;
import tech.ikora.libraries.LibraryKeywordInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LibraryResources {
    private Map<String, KeywordObject> keywordsNames;
    private List<LibraryVariable> variables;
    private Map<String, LibraryKeywordInfo> libraryKeywordNames;

    public LibraryResources() {
        this.keywordsNames = new HashMap<>();
        this.variables = new ArrayList<>();
        this.libraryKeywordNames = new HashMap();
    }

    public void loadVariable(Class<? extends LibraryVariable> libraryVariable, ErrorManager errors){
        try {
            variables.add(libraryVariable.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            errors.registerLibraryError(
                    libraryVariable.getName(),
                    String.format("Failed to load library variable: %s", e.getMessage())
            );
        }
    }

    public void loadKeyword(Class<? extends LibraryKeyword> libraryKeyword, ErrorManager errors){
        String keyword = LibraryKeyword.toKeyword(libraryKeyword);
        keywordsNames.put(keyword, new KeywordObject(libraryKeyword));
    }

    public Keyword findKeyword(Token name) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String key = name.toString().toLowerCase();

        LibraryKeyword keyword = libraryKeywordNames.get(key);

        if(keyword == null){
            KeywordObject object = keywordsNames.get(name.getText().toLowerCase());

            if(object != null){
                keyword = object.getKeyword();
            }
        }

        return keyword;
    }

    public Keyword findKeyword(String library, Token name) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(library.isEmpty()){
            return findKeyword(name);
        }

        //TODO: define a way to a set of library names;
        return findKeyword(name);
    }

    public LibraryVariable findVariable(Token name) {
        for(LibraryVariable variable: variables){
            if(variable.matches(name)){
                return variable;
            }
        }

        return null;
    }

    public void addExternalLibraries(List<LibraryInfo> librariesInfo) {
        for(LibraryInfo library: librariesInfo){
            for(LibraryKeywordInfo keyword: library.getKeywords()){
                keyword.setLibrary(library);
                this.libraryKeywordNames.put(keyword.getName(), keyword);
            }
        }
    }

    protected static class KeywordObject {
        private final Class<? extends LibraryKeyword> itemClass;
        private LibraryKeyword itemObject;

        KeywordObject(Class<? extends LibraryKeyword> libraryClass) {
            libraryClass.getPackage().getName();
            this.itemClass = libraryClass;
            this.itemObject = null;
        }

        public LibraryKeyword getKeyword() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
            if(itemObject == null) {
                itemObject = itemClass.getConstructor().newInstance();
            }

            return itemObject;
        }
    }
}
