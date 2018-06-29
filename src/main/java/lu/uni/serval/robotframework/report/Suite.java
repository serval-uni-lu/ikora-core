package lu.uni.serval.robotframework.report;

import java.util.ArrayList;
import java.util.List;

public class Suite implements ReportElement {
    private String source;
    private String id;
    private String name;
    private List<Suite> suites;
    private List<KeywordStatus> keywords;
    private ReportElement parent;

    public Suite(){
        suites = new ArrayList<>();
        keywords = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Suite> getChildren() {
        return suites;
    }

    @Override
    public int getChildPosition(ReportElement element) {
        for(int i = 0; i < suites.size(); ++i){
            if(suites.get(i) == element){
                return i;
            }
        }

        for(int i = 0; i < keywords.size(); ++i){
            if(keywords.get(i) == element){
                return i;
            }
        }

        return -1;
    }

    @Override
    public ReportElement getParent() {
        return this.parent;
    }

    @Override
    public ReportElement getRootElement(){
        return parent.getRootElement();
    }

    @Override
    public String getSource() {
        return source;
    }

    public List<KeywordStatus> getKeywords() {
        List<KeywordStatus> keywords = new ArrayList<>();

        if(hasSuites()){
            for(Suite suite: this.suites){
                keywords.addAll(suite.getKeywords());
            }
        }

        if(hasKeywords()){
            keywords.addAll(this.keywords);
        }

        return keywords;
    }

    public boolean hasSuites(){
        return suites.size() > 0;
    }

    public boolean hasKeywords(){
        return keywords.size() > 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setChildren(List<Suite> children) {
        this.suites = children;
    }

    public void setKeywords(List<KeywordStatus> keywords) {
        this.keywords = keywords;
    }

    public void setParent(ReportElement parent) {
        this.parent = parent;
    }

    public void addSuite(Suite suite) {
        suite.setParent(this);
        suites.add(suite);
    }

    public void addKeyword(KeywordStatus keyword) {
        keyword.setParent(this);
        keywords.add(keyword);
    }
}
