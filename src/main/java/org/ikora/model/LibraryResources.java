package org.ikora.model;

import org.ikora.error.ErrorManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryResources {
    private Map<String, Item<LibraryKeyword>> keywordsNames;
    private List<LibraryVariable> variables;

    public LibraryResources() {
        this.keywordsNames = new HashMap<>();
        this.variables = new ArrayList<>();
    }

    public <T> void loadClass(Class<T> libraryClass, ErrorManager errors){
        if(LibraryKeyword.class.isAssignableFrom(libraryClass)){
            String keyword = LibraryKeyword.toKeyword((Class<? extends LibraryKeyword>) libraryClass);
            keywordsNames.put(keyword, new Item<>((Class<? extends LibraryKeyword>) libraryClass));
        }
        else if(LibraryVariable.class.isAssignableFrom(libraryClass)){
            try {
                variables.add((LibraryVariable) libraryClass.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                errors.registerLibraryError(
                        libraryClass.getName(),
                        String.format("Failed to load library variable: %s", e.getMessage())
                );
            }
        }
        else{
            errors.registerLibraryError(
                    libraryClass.getName(),
                    "Unknown library element"
            );
        }
    }

    public Keyword findKeyword(String name) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Item<LibraryKeyword> keyword = keywordsNames.get(name.toLowerCase());

        if(keyword == null){
            return null;
        }

        return keyword.getObject();
    }

    public Keyword findKeyword(String library, String name) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(library.isEmpty()){
            return findKeyword(name);
        }

        //TODO: define a way to a set of library names;
        return findKeyword(name);
    }

    public Variable findVariable(String name) {
        for(LibraryVariable variable: variables){
            if(variable.matches(name)){
                return variable;
            }
        }

        return null;
    }

    protected class Item<T> {
        private final Class<? extends T> itemClass;
        T itemObject;

        Item(Class<? extends T> libraryClass) {
            this.itemClass = libraryClass;
            this.itemObject = null;
        }

        public T getObject() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
            if(itemObject == null) {
                itemObject = itemClass.getConstructor().newInstance();
            }

            return itemObject;
        }
    }
}
