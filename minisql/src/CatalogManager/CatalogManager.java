package CatalogManager;

import java.io.*;
import java.util.*;

public class CatalogManager {
    public static Vector<table> tables = new Vector<table>();

    static String tableCatalobName = "tableCatalog";

    public static void readFromFile(){
        //to be done
        // initialize index
        readTable();
    }

    public static void readTable(){
        File file = new File(tableCatalobName);
        if(file.exists() == false){
            return;
        }
        try{
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            while(dis.available()>0){
                Vector<attribute> tempAttributes = new Vector<attribute>();
                String tempTableName = dis.readUTF();
                int tempAttriNum = dis.readInt();
                int tempTupleNum = dis.readInt();
                System.out.printf("table: %s, %d:\n", tempTableName, tempAttriNum);
                for(int i = 0; i < tempAttriNum; i++){
                    String tempAttriName = dis.readUTF();
                    int tempAttriType = dis.readInt();
                    boolean tempIsPrimary = dis.readBoolean();
                    boolean tempIsUnique = dis.readBoolean();
                    System.out.printf("\t%s, %d, %b, %b\n", tempAttriName, tempAttriType, tempIsPrimary, tempIsUnique);
                    tempAttributes.add(new attribute(tempAttriName, tempAttriType, tempIsPrimary, tempIsUnique));
                }
                tables.add(new table(tempTableName, tempAttributes, tempTupleNum));
            }
            dis.close();
        }catch(IOException error){
            error.printStackTrace();
        }
    }

    public static void writeToFile(){
        writeTable();
    }

    public static void writeTable(){
        try {
            File file = new File(tableCatalobName);
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            for(int i = 0; i < tables.size(); i++){
                dos.writeUTF(tables.get(i).tableName);
                dos.writeInt(tables.get(i).attributeNumber);
                dos.writeInt(tables.get(i).tupleNum);
                for(int j = 0; j < tables.get(i).attributes.size(); j++){
                    dos.writeUTF(tables.get(i).attributes.get(j).name);
                    dos.writeInt(tables.get(i).attributes.get(j).type);
                    dos.writeBoolean(tables.get(i).attributes.get(j).primary);
                    dos.writeBoolean(tables.get(i).attributes.get(j).unique);
                }
            }
            dos.close();
        }catch (IOException error){
            error.printStackTrace();
        }
    }

    public static boolean createTable(table table){
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).tableName.equals(table.tableName)){
                System.out.printf("table named %s already exists\n", table.tableName);
                return false;
            }
        }
        if(table.tableName.equals("")){
            return false;
        }else{
            tables.add(table);
            return true;
        }
    }

    public static int getTableId(String tableName){
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).tableName.equals(tableName)){
                return i;
            }
        }
        return -1;
    }

    public static int getTupleLength(String tableName){
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).tableName.equals(tableName)){
                int length = 0;
                for(int j = 0; j < tables.get(i).attributeNumber; j++){
                    if(tables.get(i).attributes.get(j).type == 0){
                        length += 4;    //int
                    }else if(tables.get(i).attributes.get(j).type == 1){
                        length += 4;    //float
                    }else{
                        length += ((tables.get(i).attributes.get(j).type - 3) * 2);     //char(x)
                    }
                }
                return length;
            }
        }
        return -1;
    }

    public static int getTupleNum(String tableName){
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).tableName.equals(tableName)){
                return tables.get(i).tupleNum;
            }
        }
        return -1;
    }

    public static void addTupleNum(String tableName){
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).tableName.equals(tableName)){
                tables.get(i).tupleNum += 1;
            }
        }
    }

    public static int getAttributeNum(String tableName){
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).tableName.equals(tableName)){
                return tables.get(i).attributeNumber;
            }
        }
        return -1;
    }
}
