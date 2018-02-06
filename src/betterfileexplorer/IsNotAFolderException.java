
package betterfileexplorer;


//@author Marcel Kozonits, 3CHIF
//@date 06.02.2018
//@url -

public class IsNotAFolderException extends Exception {
    
    private String name = "";

    public IsNotAFolderException(String c) {
        name = c;
    }
    
}
