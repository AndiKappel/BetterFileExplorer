/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kozonits.betterfileexplorer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.*;
import java.util.Date;

/**
 *
 * @author Andreas
 */
public class Utilities {
    public static boolean copyFile(File src, File dest, int copyOption) throws IOException{
        StandardCopyOption opt = null;
        long size = 0;
        boolean success = false;
        
        switch(copyOption){
            case 0: opt = REPLACE_EXISTING; break;
            case 1: opt = COPY_ATTRIBUTES; break;
            case 2: opt = ATOMIC_MOVE;
            default: opt = REPLACE_EXISTING;
        }
        
        if(src != null && dest != null){
            size = src.length();
            Files.copy(src.toPath(), dest.toPath(), opt);
            if(size - dest.length() == 0)
                success = true;
        }
        return success;
    }
    
    public static boolean copyFileArray(File[] src, Files dest, int copyOption) throws IOException{
        boolean success = true;
        
        for(int i = 0; i < src.length && success == true; i++){
            success = Utilities.copyFile(src[i], new File(dest + src[i].getName()), copyOption);
        }
        
        return success;
    }
    
    public static boolean moveFile(File src, File dest, int copyOption) throws IOException{
        boolean success = false;
        
        success = Utilities.copyFile(src, dest, copyOption);
        success = src.delete();
        
        return success;
    }
    
    public static boolean moveFileArray(File[] src, File dest, int copyOption) throws IOException{
        boolean success = false;
        
        for(int i = 0; i < src.length && success == true; i++){
            success = Utilities.copyFile(src[i], new File(dest + src[i].getName()), copyOption);
            success = src[i].delete();
        }
        return success;
    }

    public static long getSizeOfFile(File f){
        return f.length();
    }
    
    public static long getSizeOfFolder(File directory){
        long length = 0;

            }
        }
        return length;
    }
    
    public static boolean getHidden(File file){
        return file.isHidden();
    }
    
    public static String[][] getProperties(File f){
        String[][] properties = null;
        Date d;
        String date = "";
        properties = new String[4][2];
        properties[0][0] = f.getName();
        properties[0][1] = f.getPath();
        
        properties[1][0] = Long.toString(f.length());
        properties[1][1] = Long.toString(f.getFreeSpace());      
        
        d = new Date(f.lastModified());
        date = date + d.getDay() + "." + d.getMonth() + "." + d.getYear();
        
        properties[2][0] = date;
        properties[2][1] = "" + f.canRead()+ ";" + f.canWrite() + ";" + f.canExecute();
        
        properties[3][0] = Boolean.toString(f.isHidden());
        properties[3][1] = f.getAbsolutePath();
           
        return properties;
    }
    
    public static boolean renameFile(File f, String newName){     
        return f.renameTo(new File(f.getParent() + newName));
    }
    
    public static boolean renameFolder(File f, String newName){
        return f.renameTo(new File(f.getParent()+ newName));
    }
    
    public static boolean deleteFile(File f){
        return f.delete();
    }
    
    public static boolean deleteFileArray(File[] f){
        boolean success = false;
        for(int i = 0; i < f.length; i++)
            success = f[i].delete();
        return success;
    }
    
    public static boolean openCMD(File f){
        String[] cmd = {"C:\\WINDOWS\\system32\\cmd.exe","/c","start", "cd", f.getPath()};
        boolean success = false;
        
        try {        
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(cmd);
            success = true;
        }
        catch (java.io.IOException exception) {
            System.out.println("IOException: " + exception.getMessage());
        }
        return success;
    }
       
    public static boolean createFile(File dir, String fileName) throws IOException{
        File f = new File(dir.getPath() + "/" + fileName);
        if(f.isDirectory())
            return false;
        return f.createNewFile();
    }
    
    public static boolean createDirectory(File dir, String dirName){
        File directory = new File(dir.getPath() + "/" + dirName);
        if(directory.isDirectory())
            return false;
        return directory.mkdir();
    }
}
