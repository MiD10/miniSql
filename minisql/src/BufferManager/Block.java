package BufferManager;

import java.io.*;
import java.util.*;

public class Block {
    public static int SIZE = 4096;
    public String fileName = ""; //remember where the block came from
    public int fileOffset = 0; //record this block's offset in file:0 means the first block in file
    public boolean dirty = false;
    public boolean pin = false;
    public int recentlyUsed = 0; //for LRU
    public boolean used = false;

    public byte[] data = new byte[SIZE];

    public void written()
    {
        dirty = true;
        recentlyUsed += 1;
    }

    public boolean writeData(int byteoffset, byte inputdata[], int size) {
        if (byteoffset + size >= 4096)
            return false;
        for (int i = 0; i < size; i++)
            data[byteoffset + i] = inputdata[i];
        dirty = true;
        recentlyUsed += 1;
        return true;
    }

    public void writeInt(int offset, int num){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try{
            dos.writeInt(num);
        }catch(IOException error){
            error.printStackTrace();
        }
        byte[] temp = bos.toByteArray();
        data[offset] = temp[0];
        data[offset + 1] = temp[1];
        data[offset + 2] = temp[2];
        data[offset + 3] = temp[3];
        dirty = true;
        recentlyUsed += 1;
    }

    public void writeFloat(int offset, float num){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try{
            dos.writeFloat(num);
        }catch(IOException error){
            error.printStackTrace();
        }
        byte[] temp = bos.toByteArray();
        data[offset] = temp[0];
        data[offset + 1] = temp[1];
        data[offset + 2] = temp[2];
        data[offset + 3] = temp[3];
        dirty = true;
        recentlyUsed += 1;
    }

    public void writeString(int offset, String str, int len){
        byte[] buf = str.getBytes();
        int i = 0;
        for(;i<buf.length;i++){
            data[offset + i] = buf[i];
        }
        for(;i<len*2;i++){
            data[offset + i] = '&';
        }
        dirty = true;
        recentlyUsed += 1;
    }

    public String readString(int offset, int len){
        byte[] buf = new byte[len * 2];
        for(int i = 0; i < len * 2; i++){
            buf[i] = data[offset + i];
        }
        recentlyUsed += 1;
        String str = new String(buf);
        return str;
    }

    public float readFloat(int offset){
        byte[] temp = new byte[4];
        temp[0] = data[offset];
        temp[1] = data[offset + 1];
        temp[2] = data[offset + 2];
        temp[3] = data[offset + 3];
        ByteArrayInputStream bis = new ByteArrayInputStream(temp);
        DataInputStream dis = new DataInputStream(bis);
        float value = 0;
        try {
            value = dis.readFloat();
        } catch (IOException error) {
            error.printStackTrace();
        }
        recentlyUsed += 1;
        return value;
    }

    public int readInt(int offset){
        byte[] temp = new byte[4];
        temp[0] = data[offset];
        temp[1] = data[offset + 1];
        temp[2] = data[offset + 2];
        temp[3] = data[offset + 3];
        ByteArrayInputStream bis = new ByteArrayInputStream(temp);
        DataInputStream dis = new DataInputStream(bis);
        int value = 0;
        try {
            value = dis.readInt();
        } catch (IOException error) {
            error.printStackTrace();
        }
        recentlyUsed += 1;
        return value;
    }


    public int recordNum = 0;
    public Block next = null;
    public Block previous = null;
    public  void writeInternalKey(int pos,byte[] key,int offset) {
        writeData(pos,key,key.length);
        writeInt(pos+key.length,offset);
        dirty=true;
    }

    public byte[] getBytes(int pos,int length){
        byte[] b = new byte[length];
        for(int i =0;i<length;i++){
            b[i]=data[pos+i];
        }
        return b;
    }

    public  void setInternalKey(int pos,byte[] key,int offset) {
        writeData(pos,key,key.length);
        writeInt(pos+key.length,offset);
        dirty=true;
    }
    public  void setKeydata(int pos,byte[] insertKey,int blockOffset,int offset) {
        writeInt(pos,blockOffset);
        writeInt(pos+4,offset);
        writeData(pos+8,insertKey, insertKey.length);
        dirty=true;
    }




}
