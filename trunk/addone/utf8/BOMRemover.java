package com.cvo.bom.remove;

import java.io.BufferedReader;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.FileReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.PushbackInputStream;  
import java.util.ArrayList;  
import java.util.HashSet;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Set;  

public class BOMRemover {
	 /** 
     * ant 编译之后的result文件，注意要编译提示错误的文件名要在同一行 可以设置命令提示窗口的缓冲区大小实现 
     *  
     * @param resultFileName 
     */  
    public static Set getFileNamesFromCompileResult(String resultFileName)  
            throws java.io.IOException {  
        Set set = new HashSet();  
        BufferedReader reader = new BufferedReader(new FileReader(  
                resultFileName));  
        String start = "[javac] ";  
        int startLen = start.length();  
        String end = ".java:";  
        int endLen = end.length();  
  
        String errMsg = "//65279";  
        while (reader.ready()) {  
            String line = reader.readLine();  
            int indexStart = line.indexOf(start);  
  
            if (line.indexOf(errMsg) == -1) {  
                continue;  
            }  
            if (indexStart != -1) {  
                int indexEnd = line.indexOf(end);  
                if (indexEnd != -1) {  
                    String name = line.substring(indexStart + startLen,  
                            indexEnd + endLen - 1);  
                    set.add(name.trim());  
                }  
            }  
        }  
        return set;  
  
    }  
  
      
    //trim dir  
    public static void DealSrcFiles(String path) {  
        if (path.charAt(path.length() - 1) != '/') 
        {  
            path += '/';  
        }  
        File file = new File(path);  
        if (!file.exists()) {  
            System.out.println("Error: Path not Existed! Please Check it out!");  
            return;  
        }  
        String[] filelist = file.list();  
        for (int i = 0; i < filelist.length; i++) {  
            File temp = new File(path + filelist[i]);  
            if ((temp.isDirectory() && !temp.isHidden() && temp.exists())) {  
                DealSrcFiles(path + filelist[i]);  
            } else {  
             //   if (filelist[i].endsWith(".xml")) {  
                    try {  
                        // System.out.println(path + filelist[i]);  
                        trimBom(path + filelist[i]);  
                    } catch (Exception eee) {  
                        System.out.println(eee.getMessage());  
                    }  
           //     }  
            }  
        }  
    }  
  
    /** 
     * 读取流中前面的字符，看是否有bom，如果有bom，将bom头先读掉丢弃 
     *  
     * @param in 
     * @return 
     * @throws IOException 
     */  
    public static InputStream getInputStream(InputStream in) throws IOException {  
  
        PushbackInputStream testin = new PushbackInputStream(in);  
        int ch = testin.read();  
        if (ch != 0xEF) {  
            testin.unread(ch);  
        } else if ((ch = testin.read()) != 0xBB) { // if ch==0xef  
            testin.unread(ch);  
            testin.unread(0xef);  
        } else if ((ch = testin.read()) != 0xBF) { // if ch ==0xbb  
            throw new IOException("错误的UTF-8格式文件");  
        } else { // if ch ==0xbf  
            // 不需要做，这里是bom头被读完了  
            // // System.out.println("still exist bom");  
  
        }  
        return testin;  
  
    }  
  
    /** 
     * 根据一个文件名，读取完文件，干掉bom头。 
     *  
     * @param fileName 
     * @throws IOException 
     */  
    public static void trimBom(String fileName) throws IOException {  
  
      
  
        if (noBomFile(fileName)) {  
            System.out.println("skip :" + fileName);  
            return;  
        }  
          
          
          
        FileInputStream fin = new FileInputStream(fileName);  
        // 开始写临时文件  
        InputStream in = getInputStream(fin);  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        byte b[] = new byte[4096];  
  
        int len = 0;  
        while (in.available() > 0) {  
            len = in.read(b, 0, 4096);  
            // out.write(b, 0, len);  
            bos.write(b, 0, len);  
        }  
  
        in.close();  
        fin.close();  
        bos.close();  
  
        // 临时文件写完，开始将临时文件写回本文件。  
        System.out.println("[" + fileName + "]");  
        FileOutputStream out = new FileOutputStream(fileName);  
        out.write(bos.toByteArray());  
        out.close();  
        System.out.println("PROCESS FILE " + fileName);  
    }  
  
    // check the file is or not bom file  
    private static boolean noBomFile(String  fname) throws IOException {  
        FileInputStream fin = new FileInputStream(fname);  
        PushbackInputStream testin = new PushbackInputStream(fin);  
        int ch = testin.read();  
        int ch2 = testin.read();  
        int ch3 = testin.read();  
  
        if (ch == 0xEF && ch2 == 0xBB && ch3 == 0xBF) {  
           
            return false; // is bom file  
        } else {  
           
            return true;  
        }  
    }  
  
    /** 
     * 根据ant编译错误来去除bom 
     *  
     * @param resultFile 
     * @throws IOException 
     */  
    // public static void trimBomByCompileResult(String resultFile)  
    // throws IOException {  
    // Set set = getFileNamesFromCompileResult(resultFile);  
    //  
    // for (Iterator it = set.iterator(); it.hasNext();) {  
    // String fName = it.next().toString();  
    // trimBom(fName);  
    // }  
    //  
    // }  
  
    public static void main(String[] args) throws IOException {  
        // if(args.length==0){  
        // DealSrcFiles(System.getProperty("user.dir"));  
        // }  
        // else{  
        // DealSrcFiles(args[0]);  
        // }  
        String path = "F:\\wscloud\\prm-linux-4sync-company\\code\\client\\corepower\\helloworld-coolsql\\src\\com\\coolsql";  
        DealSrcFiles(path);  
    }  
}  

