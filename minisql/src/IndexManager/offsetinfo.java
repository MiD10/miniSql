package IndexManager;

import java.util.Vector;

public class offsetinfo {
    public Vector<Integer> offsetInfile;
    public Vector<Integer> offsetInBlock;
    public int length;
    public offsetinfo(){
        offsetInfile = new Vector<Integer>();
        offsetInBlock = new Vector<Integer>();
        length=0;
    }
}