package org.ikora.utils;

import com.fasterxml.jackson.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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

    public Object getAdditionalProperty(String name){
        return additionalProperties.get(name);
    }

    public Object getAdditionalProperty(String name, Object defaultValue){
        if(!additionalProperties.containsKey(name)){
            return defaultValue;
        }

        Object value = additionalProperties.get(name);

        if(value.getClass() != defaultValue.getClass()){
            return defaultValue;
        }

        return value;
    }

    public Date getPropertyAsDate(String name) {
        if(!additionalProperties.containsKey(name)){
            return null;
        }

        String dateTime = (String)getAdditionalProperty(name);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return formatter.parse(dateTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getPropertyAsString(String name){
        return (String)getAdditionalProperty(name, "");
    }

    public String getPropertyAsString(String name, String value){
        return (String)getAdditionalProperty(name, value);
    }

    public Map<String, String> getPropertyAsStringMap(String name) {
        if(!additionalProperties.containsKey(name)) {
            return Collections.emptyMap();
        }

        Object value = additionalProperties.get(name);

        if(Map.class.isAssignableFrom(value.getClass())){
            return (Map)value;
        }

        return Collections.emptyMap();
    }
}
