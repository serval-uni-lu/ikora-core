package lu.uni.serval.analytics;

public class CloneIndex {
    private final double treeThreshold;
    private final double keywordThreshold;
    private final double treeRatio;
    private final double keywordRatio;

    public CloneIndex(double keywordRatio, double treeRatio) {
        this.treeRatio = treeRatio;
        this.keywordRatio = keywordRatio;

        this.treeThreshold = 0.7;
        this.keywordThreshold = 0.7;
    }

    public CloneIndex(double keywordRatio, double treeRatio, double keywordThreshold, double treeThreshold) {
        this.treeRatio = treeRatio;
        this.keywordRatio = keywordRatio;

        this.treeThreshold = treeThreshold;
        this.keywordThreshold = keywordThreshold;
    }

    public boolean isClone(){
        return treeRatio > treeThreshold;
    }

    public boolean isHomonym(){
        return !isClone() && keywordRatio > keywordThreshold;
    }

    public boolean isSynonym(){
        return isClone() && keywordRatio < keywordThreshold;
    }

    public boolean isSame(){
        return isClone() && keywordRatio > keywordThreshold;
    }

    public double getTreeRatio(){
        return treeRatio;
    }

    public double getKeywordRatio(){
        return keywordRatio;
    }
}
