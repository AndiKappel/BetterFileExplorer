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
    }//
    
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
    
    public static String[][] getProperties(File f){
        String[][] properties = null;
        
        properties = new String[2][2];
        properties[0][0] = f.getName();
        properties[0][1] = f.getPath();
        
        properties[1][0] = Long.toString(f.length());
        properties[1][1] = Long.toString(f.getFreeSpace());      
        
        properties[2][0] = Long.toString(f.lastModified());
        properties[2][1] = "" + f.canRead()+ ";" + f.canWrite() + ";" + f.canExecute();
        
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
}
