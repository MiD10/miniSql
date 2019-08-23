package CatalogManager;

public class attribute {
    public String name;
    public int type;    //int = 0, float = 1, char(x) = 3 + x
    public boolean unique;
    public boolean primary;



    public attribute(String name, int type, boolean unique, boolean primary){
        this.name = name;
        this.type = type;
        this.unique = unique;
        this.primary = primary;
    }
}
