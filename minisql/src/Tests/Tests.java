package Tests;

import IndexManager.*;
import BufferManager.*;
import CatalogManager.*;
import RecordManager.*;

import java.util.Arrays;
import java.util.Vector;

public class Tests {
    public static void createTable(String tableName){
        //create a table for test
        Vector<attribute> testAttributes = new Vector<attribute>();
        testAttributes.add(new attribute("id", 0, true, true)); //id, int, unique, primary key
        testAttributes.add(new attribute("name", 11, false, false)); //name, char(8), not unique, not primary key
        table testTable = new table(tableName, testAttributes);

        CatalogManager.createTable(testTable);
        RecordManager.createTable(tableName);
        System.out.println("table creation finished");
    }

    public static void addTwoTuples(String tableName){
        //add two tuples (1, helen),(2, jeff)
        Vector<tuple> adds = new Vector<tuple>();
        Vector<String > first = new Vector<>();
        first.add("1"); first.add("helen");
        Vector<String > second = new Vector<>();
        second.add("2"); second.add("jeff");
        adds.add(new tuple(first));
        adds.add(new tuple(second));
        RecordManager.addTuple(tableName, adds.get(0));
        RecordManager.addTuple(tableName, adds.get(1));
        System.out.println("tuples added");
    }

}
