package lu.uni.serval.utils;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Plugin {

    @JsonProperty("name")
    private String name;
    @JsonProperty("version")
    private String version;

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

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Object getAddictionalProperty(String name){
        return additionalProperties.get(name);
    }

    public Object getAddictionalProperty(String name, Object defaultValue){
        if(!additionalProperties.containsKey(name)){
            return defaultValue;
        }

        Object value = additionalProperties.get(name);

        if(value.getClass() != defaultValue.getClass()){
            return defaultValue;
        }

        return value;
    }
}
