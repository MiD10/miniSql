package RecordManager;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import BufferManager.*;
import CatalogManager.CatalogManager;

public class RecordManager {
    public static boolean createTable(String tableName){
        File file = new File(tableName);
        if(file.exists()){
            return false;
        }
        else{
            try{
                file.createNewFile();
            }catch(IOException error) {
                error.printStackTrace();
            }
        }

        Block block = BufferManager.allocateBlockForFile(tableName, 0);
        block.writeInt(0, 0);   //free list pointer
        return true;
    }

    public static boolean addTuple(String tableName, tuple newTuple){
        int recordLength = 4 + CatalogManager.getTupleLength(tableName);
        int tupleNumInBlock = Block.SIZE / recordLength;
        Block headBlock = BufferManager.allocateBlockForFile(tableName, 0);
        int freeOffset = headBlock.readInt(0); //record the index of tuple
        Block operateBlock;
        if(freeOffset > 0){
            operateBlock = BufferManager.allocateBlockForFile(tableName, freeOffset / tupleNumInBlock);
            int nextFreeOffset = operateBlock.readInt(recordLength * (freeOffset % tupleNumInBlock));
            headBlock.writeInt(0, nextFreeOffset);
        } else{
            freeOffset = 1 + CatalogManager.getTupleNum(tableName);
            operateBlock = BufferManager.allocateBlockForFile(tableName, freeOffset / tupleNumInBlock);
        }

        int freeOffsetInByte = recordLength * (freeOffset % tupleNumInBlock);
        operateBlock.writeInt(freeOffsetInByte, -1);
        freeOffsetInByte += 4;

        int tableId = CatalogManager.getTableId(tableName);
        for(int i = 0; i < newTuple.values.size(); i++){
            int type = CatalogManager.tables.get(tableId).attributes.get(i).type;
            if(type == 0){//is int
                operateBlock.writeInt(freeOffsetInByte, Integer.parseInt(newTuple.values.get(i)));
                freeOffsetInByte += 4;
            }else if(type == 1){//is float
                operateBlock.writeFloat(freeOffsetInByte, Float.parseFloat(newTuple.values.get(i)));
                freeOffsetInByte += 4;
            }else{
                operateBlock.writeString(freeOffsetInByte, newTuple.values.get(i), type - 3);
                freeOffsetInByte += (type - 3) * 2;
            }
        }
        CatalogManager.addTupleNum(tableName);
        return true;
    }

    public static tuple getTuple(String tableName, int tupleOffset){
        int recordLength = 4 + CatalogManager.getTupleLength(tableName);
        int tupleNumInBlock = Block.SIZE / recordLength;
        Block block = BufferManager.getBlock(tableName, tupleOffset / tupleNumInBlock);
        int byteOffset = recordLength * (tupleOffset % tupleNumInBlock);
        if(block.readInt(byteOffset) >= 0){
            return null;
        }

        byteOffset += 4;
        tuple tempTuple = new tuple();
        int tableId = CatalogManager.getTableId(tableName);
        for(int i = 0; i < CatalogManager.getAttributeNum(tableName); i++){
            int type = CatalogManager.tables.get(tableId).attributes.get(i).type;
            if(type == 0){//is int
                tempTuple.values.add(String.valueOf(block.readInt(byteOffset)));
                byteOffset += 4;
            }else if(type == 1){//is float
                tempTuple.values.add(String.valueOf(block.readFloat(byteOffset)));
                byteOffset += 4;
            }else{
                tempTuple.values.add(block.readString(byteOffset, type-3));
                byteOffset += (type - 3) * 2;
            }
        }
        return tempTuple;
    }
}
