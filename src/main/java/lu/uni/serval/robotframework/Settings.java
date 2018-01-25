package lu.uni.serval.robotframework;

public class Settings {
    private ResourcesTable resourcesTable;

    public Settings(ResourcesTable resourcesTable) {
        this.resourcesTable = resourcesTable;
    }

    public ResourcesTable getResources() {
        return resourcesTable;
    }
}
