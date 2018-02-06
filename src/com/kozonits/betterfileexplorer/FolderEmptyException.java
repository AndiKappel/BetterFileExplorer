
package com.kozonits.betterfileexplorer;


//@author Marcel Kozonits, 3CHIF
//@date 06.02.2018
//@url -

public class FolderEmptyException extends Throwable {
    
    private String name = "";
    
    public FolderEmptyException(String s) {
        name = s;
    }
    
}
