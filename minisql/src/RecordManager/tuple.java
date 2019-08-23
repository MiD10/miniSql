package RecordManager;

import java.util.Vector;

public class tuple {
    public Vector<String> values = new Vector<>();

    public tuple(Vector<String> values){
        this.values = values;
    }

    public tuple() { values = new Vector<String>(); }
}
