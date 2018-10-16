package lu.uni.serval.robotframework.model;

public abstract class LibraryVariable extends Variable {
    protected enum Format{
        scalar, list, dictionary
    }

    protected Format format;

    public LibraryVariable(){
        this.format = Format.scalar;
    }

    public void setName(){
    }

    @Override
    public String getName(){
        return toVariable(this.getClass());
    }

    public static String toVariable(Class<? extends LibraryVariable> variableClass) {
        String name = variableClass.getSimpleName();
        name = name.replaceAll("([A-Z])", " $1").trim().toUpperCase();

        return String.format("${%s}", name);
    }
}
