package tech.ikora.model;

import tech.ikora.libraries.LibraryKeywordInfo;

import java.util.Set;

public class LibraryInfo {
    private String name;
    private Set<LibraryKeywordInfo> keywords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LibraryKeywordInfo> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<LibraryKeywordInfo> keywords) {
        this.keywords = keywords;
    }
}
