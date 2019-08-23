import IndexManager.*;
import BufferManager.*;
import CatalogManager.*;
import RecordManager.*;
import Tests.*;

import java.util.Arrays;
import java.util.Vector;

public class Main {

    public static byte[]IntToByte(int num){
        byte[]bytes=new byte[4];
        bytes[0]=(byte) ((num>>24)&0xff);
        bytes[1]=(byte) ((num>>16)&0xff);
        bytes[2]=(byte) ((num>>8)&0xff);
        bytes[3]=(byte) (num&0xff);
        return bytes;
    }

    public static void main(String[] args) {

        System.out.println("Hello World!");
        BufferManager.initialize();
        CatalogManager.readFromFile();

        System.out.println("initialize finished");

        //test creation of table
        String testTableName = "testTable";
        Tests.createTable(testTableName);

        //test add tuples in the table
        Tests.addTwoTuples(testTableName);

        System.out.printf("%s, %s, %s\n",
                CatalogManager.tables.get(0).tableName,
                CatalogManager.tables.get(0).attributes.get(0).name,
                CatalogManager.tables.get(0).attributes.get(1).name);

        System.out.printf("length: %d\n", CatalogManager.getTupleLength(testTableName));
        System.out.printf("number of tuples: %d\n", CatalogManager.getTupleNum(testTableName));

        tuple tempTuple1 = RecordManager.getTuple(testTableName, 1);
        tuple tempTuple2 = RecordManager.getTuple(testTableName, 2);
        tuple tempTuple3 = RecordManager.getTuple(testTableName, 3);
        tuple tempTuple4 = RecordManager.getTuple(testTableName, 4);
        tuple tempTuple5 = RecordManager.getTuple(testTableName, 5);
        tuple tempTuple6 = RecordManager.getTuple(testTableName, 6);
        tuple tempTuple7 = RecordManager.getTuple(testTableName, 7);
        tuple tempTuple8 = RecordManager.getTuple(testTableName, 8);

//        System.out.println(BufferManager.blocks[0].readInt(24));
//        System.out.println(BufferManager.blocks[0].readInt(28));
//        System.out.println(BufferManager.blocks[0].readString(32, 8));
//
//        System.out.println(BufferManager.blocks[0].readInt(48));
//        System.out.println(BufferManager.blocks[0].readInt(52));
//        System.out.println(BufferManager.blocks[0].readString(56, 8));
//
//
//        index i=new index("index_test","table_test","att_test");
//        i.columnLength=4;
//        BPlusTree tree=new BPlusTree(i,0);
//        byte []b=IntToByte(5);
//        System.out.println(b);
//        System.out.println(Arrays.toString(b));
//        tree.insert(b,2,4);
//        System.out.println(Arrays.toString(tree.myRootBlock.data));
//        System.out.println(tree.getmax());
//        System.out.println(tree.getmin());


        BufferManager.writeAllBackFile();
        CatalogManager.writeToFile();
    }


}
