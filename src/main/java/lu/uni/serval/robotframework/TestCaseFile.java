package lu.uni.serval.robotframework;

public interface TestCaseFile {
    public KeywordTable keywords();
    public ImportTable imports();
    public String name();
}
