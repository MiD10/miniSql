package CatalogManager;

import java.util.*;

public class table {
    public String tableName;
    public int attributeNumber;
    public Vector<attribute> attributes;

    public int tupleNum;

    //this is for initialize a table
    //while tupleNum is 0
    public table(String tableName, Vector<attribute> attributes){
        this.tableName = tableName;
        this.attributes = attributes;
        this.attributeNumber = attributes.size();
        this.tupleNum = 0;
    }

    //this is for catalogManager
    //while reading a table from file, tupleNum should be known
    public table(String tableName, Vector<attribute> attributes, int tupleNum){
        this.tableName = tableName;
        this.attributes = attributes;
        this.attributeNumber = attributes.size();
        this.tupleNum = tupleNum;
    }
}
