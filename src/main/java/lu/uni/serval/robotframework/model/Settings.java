package lu.uni.serval.robotframework.model;

public class Settings {
    private ResourcesTable resourcesTable;

    public Settings(ResourcesTable resourcesTable) {
        this.resourcesTable = resourcesTable;
    }

    public ResourcesTable getResources() {
        return resourcesTable;
    }
}
