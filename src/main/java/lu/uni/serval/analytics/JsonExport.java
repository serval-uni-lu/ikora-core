package lu.uni.serval.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"general statistics", "clones"})
public class JsonExport {
    @JsonProperty("general statistics")
    private StatisticsResults generalStatistics;
    @JsonProperty("clones")
    public  CloneResults clones;

    @JsonProperty("clones")
    public void setClones(CloneResults clones) {
        this.clones = clones;
    }

    @JsonProperty("general statistics")
    public void setGeneralStatistics(StatisticsResults generalStatistics) {
        this.generalStatistics = generalStatistics;
    }

    @JsonProperty("general statistics")
    public StatisticsResults getGeneralStatistics() {
        return generalStatistics;
    }

    @JsonProperty("clones")
    public CloneResults getClones() {
        return clones;
    }
}
