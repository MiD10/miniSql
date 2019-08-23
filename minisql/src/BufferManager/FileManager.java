package BufferManager;
import java.io.File;
public class FileManager {

    public static boolean findFile(String filename) {
        File file = new File(filename);
        if (!file.exists())
            return false;
        return true;
    }

    public static void creatFile(String filename) {
        try {
            File myFile = new File(filename);
            // 判断文件是否存在，如果不存在则调用createNewFile()方法创建新目录，否则跳至异常处理代码
            if (!myFile.exists())
                myFile.createNewFile();
            else
                // 如果不存在则扔出异常
                throw new Exception("The new file already exists!");
        } catch (Exception ex) {
            System.out.println("File creation failed.");
            ex.printStackTrace();
        }
    }

    public static void dropFile(String filename) {
        File f=new File(filename);
        if(f.exists())f.delete();
    }

}
