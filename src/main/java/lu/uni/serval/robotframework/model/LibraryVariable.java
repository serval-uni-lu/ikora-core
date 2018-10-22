package lu.uni.serval.robotframework.model;

public abstract class LibraryVariable extends ScalarVariable {
    protected enum Format{
        scalar, list, dictionary
    }

    protected Format format;

    public LibraryVariable(){
        this.format = Format.scalar;
        super.setName(toVariable(this.getClass()));
    }

    @Override
    public void setName(String name){
    }

    private static String toVariable(Class<? extends LibraryVariable> variableClass) {
        String name = variableClass.getSimpleName();
        name = name.replaceAll("([A-Z])", " $1").trim().toUpperCase();

        return String.format("${%s}", name);
    }
}
