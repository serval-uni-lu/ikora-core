package lu.uni.serval.utils;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "outputFile"
})

public class Plugin {

    @JsonProperty("name")
    private String name;
    @JsonProperty("output file")
    private String outputFile;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("outputFile")
    public String getOutputFile() {
        return outputFile;
    }

    @JsonProperty("outputFile")
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
