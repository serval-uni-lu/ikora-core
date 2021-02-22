package lu.uni.serval.ikora.error;

public class LibraryError extends Error {
    private final String library;

    public LibraryError(String message, String library) {
        super(message);
        this.library = library;
    }

    public String getLibrary() {
        return library;
    }
}
