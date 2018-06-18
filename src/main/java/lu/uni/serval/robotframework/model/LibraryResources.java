package lu.uni.serval.robotframework.model;

import java.util.HashMap;
import java.util.Map;

public class LibraryResources {
    Map<String, LibraryElement> libraryKeywordsNames;

    public LibraryResources() {
        this.libraryKeywordsNames = new HashMap<>();
    }

    public void loadClass(Class<? extends LibraryKeyword> libraryClass){
        String keyword = LibraryKeyword.toKeyword(libraryClass);
        libraryKeywordsNames.put(keyword, new LibraryElement(libraryClass));
    }

    public Keyword findKeyword(String name) throws InstantiationException, IllegalAccessException {
        LibraryElement element = libraryKeywordsNames.get(name.toLowerCase());

        if(element == null){
            return null;
        }

        return element.getLibraryObject();
    }

    protected class LibraryElement {
        private final Class<? extends LibraryKeyword> libraryClass;
        LibraryKeyword libraryObject;

        LibraryElement(Class<? extends LibraryKeyword> libraryClass) {
            this.libraryClass = libraryClass;
            this.libraryObject = null;
        }

        public LibraryKeyword getLibraryObject() throws IllegalAccessException, InstantiationException {
            if(libraryObject == null) {
                libraryObject = libraryClass.newInstance();
            }

            return libraryObject;
        }
    }
}
