package BufferManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferManager {
    public static int SIZE = 20;

    public static Block[] blocks = new Block[SIZE]; //the whole Buffer

    public static void initialize(){
        for(int i = 0; i < SIZE;i++){
            blocks[i] = new Block();
        }
    }

    public static boolean readFromFile(String fileName, int fileOffset, int blockId){
        blocks[blockId].fileName = fileName;
        blocks[blockId].fileOffset = fileOffset;
        blocks[blockId].recentlyUsed = 1;
        blocks[blockId].used = true;
        blocks[blockId].dirty = false;
        blocks[blockId].pin = false;
        for (int i = 0; i < Block.SIZE; i++){
            blocks[blockId].data[i] = 0;
        }
        try{
            File file = new File(fileName);
            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
            if(randomFile.length() >= (fileOffset * Block.SIZE + Block.SIZE)) {
                randomFile.seek(fileOffset * Block.SIZE);
                randomFile.read(blocks[blockId].data, 0, Block.SIZE);
            }else{
                for(int j = 0; j < Block.SIZE; j++){
                    blocks[blockId].data[j] = 0;
                }
            }
            randomFile.close();
        }catch (IOException error){
            error.printStackTrace();
        }
        return true;
    }

    public static void writeBackFile(int i){
        if(blocks[i].used == true){
            if(blocks[i].dirty == false){
                blocks[i].used = false;
            }
            else{
                try{
                    File file = new File(blocks[i].fileName);
                    RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    randomFile.seek(blocks[i].fileOffset * SIZE);
                    randomFile.write(blocks[i].data);
                    randomFile.close();
                }catch(IOException error){
                    error.printStackTrace();
                }
                blocks[i].used = false;
            }
        }
    }

    public static void writeAllBackFile(){
        for(int i = 0; i < SIZE; i++){
            writeBackFile(i);
        }
    }

    public static Block allocateBlockForFile(String fileName, int fileOffset){
        for(int i = 0; i < SIZE; i++){
            if(blocks[i].fileName.equals(fileName) && (blocks[i].fileOffset == fileOffset)){
                return blocks[i];
            }
        }

        try {
            int num = findFreeBlock();
            if(num == -1){
                throw new Exception("all blocks are pinned!");
            }
            readFromFile(fileName, fileOffset, num);
            return blocks[num];

        }catch(Exception error){
            System.out.println("Can not allocate a block!");
            error.printStackTrace();
        }
        return blocks[0];
    }

    public static int findFreeBlock(){
        int min = Integer.MAX_VALUE;
        int min_id = -1;
        for(int i = 0; i < SIZE; i++){
            if(blocks[i].used == false){
                return i;
            }
            if(blocks[i].pin == false){
                if(blocks[i].recentlyUsed <= min){
                    min = blocks[i].recentlyUsed;
                    min_id = i;
                }
            }
        }
        if(min_id != -1){
            writeBackFile(min_id);
        }
        return min_id;
    }

    public static Block getBlock(String fileName, int fileOffset) {
        // 指定文件名和第几个block，返回一个BLOCK
        int num = findBlock(fileName, fileOffset);
        if (num != -1)
            return blocks[num];
        else {
            num = findFreeBlock();
            File file = new File(fileName);
            /*if (!file.exists()) {
                blocks[num].fileOffset= fileOffset;
                blocks[num].fileName = fileName;
                for (int i = 0; i < Block.SIZE; i++)
                    blocks[num].data[i] = 0;
                return blocks[num];
            }*/
            readFromFile(fileName, fileOffset, num);
            return blocks[num];
        }
    }

    private static int findBlock(String fileName, int fileOffset) {
        for (int i = 0; i < SIZE; i++)
            if (blocks[i].used)
                if((blocks[i].fileName.equals(fileName)) && (blocks[i].fileOffset == fileOffset)) {
                    return i;
                }
        return -1;
    }

}
