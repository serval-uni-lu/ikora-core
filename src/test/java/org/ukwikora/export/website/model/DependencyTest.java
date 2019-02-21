package org.ukwikora.export.website.model;

import org.junit.Test;
import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DependencyTest {
    public static List<Dependency> createDependencies(){
        List<Dependency> dependencies = new ArrayList<>(3);

        dependencies.add(new Dependency("Project1", "Project 2", Dependency.Type.UserProject));
        dependencies.add(new Dependency("Project2", "Project 1", Dependency.Type.UserProject));
        dependencies.add(new Dependency("Project1", "Project 3", Dependency.Type.UserProject));

        return dependencies;
    }

    @Test
    public void createJsonArray(){
        List<Dependency> dependencies = createDependencies();

        try {
            String expected = "[{" +
                    "\"source\":\"Project1\"," +
                    "\"target\":\"Project 2\"," +
                    "\"type\":\"UserProject\"" +
                    "}," +
                    "{" +
                    "\"source\":\"Project2\"," +
                    "\"target\":\"Project 1\"," +
                    "\"type\":\"UserProject\"" +
                    "}," +
                    "{" +
                    "\"source\":\"Project1\"," +
                    "\"target\":\"Project 3\"," +
                    "\"type\":\"UserProject\"" +
                    "}]";

            String json = JsonUtils.convertToJsonArray(dependencies);
            assertEquals(expected, json);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}