
package com.kozonits.betterfileexplorer;

import static com.kozonits.betterfileexplorer.Utilities.getSizeOfFile;
import static com.kozonits.betterfileexplorer.Utilities.getSizeOfFolder;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/*
 * @project BetterFileExplorer
 * @author @MKozonits on GitHub/Twitter
 */

public class BetterFileExplorer {
    
    private static String[] files;
    private static String[] folders;
    private static int anz_files = 0;
    private static int anz_folders = 0;
    private static boolean selected_changes = false;
    private static String current_path = "D:";
    private static String old_path = "D:";
    
    private static String history;
    private static String curr_history;
    
    private static boolean PATH_CHANGED_EVENT = false;
    private static boolean FIRST_EVENT = true;
    
    private static long end_time = -1;
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, InterruptedException, IOException {
        
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        UIManager.put("ScrollBar.background", Color.LIGHT_GRAY);
        UIManager.put("Scrollbar.foreground", Color.GRAY);
        UIManager.put("Button.select", new Color(24,127,39));
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("List.focusCellHighlightBorder", new Color(0, 0, 0, 0));
        UIManager.put("ScrollBar.shadow", Color.BLACK);
        UIManager.put("Table.focusCellHighlightBorder", new Color(0, 0, 0, 0));
        UIManager.put("TableHeader.focusCellBackground", Color.GRAY);
        UIManager.put("TableHeader.background", new Color(234, 234, 234));
        
        MainWindow window = new MainWindow();
        window.setVisible(true);
        
        TableColumnModel tcm = window.jTable1.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(20);      //ICON
        tcm.getColumn(1).setPreferredWidth(350);     //Name
        tcm.getColumn(2).setPreferredWidth(80);      //EXT
        tcm.getColumn(3).setPreferredWidth(80);      //Size
        tcm.getColumn(4).setPreferredWidth(200);     //Sonst.
        
        File folder = new File(current_path);
        window.path.setText(folder.toString());
        updateHistory(curr_history);
        
        try {
            listFilesForFolder(folder);
        } catch (FolderEmptyException e) {
            System.exit(1);
        } catch (IsNotAFolderException e) {
            System.exit(1);
        }

        DefaultTableModel dtm = (DefaultTableModel) window.jTable1.getModel();
        dtm.setRowCount(anz_files + anz_folders);

        displayFilesAndFolder(window, folder);
        
        while (true) {
            long start = System.nanoTime(); //START-TIME for FRAME-CALCULATION
            
            if (((!current_path.equals(window.path.getText()) && window.changes == 1) || selected_changes) || PATH_CHANGED_EVENT) {
                
                if (current_path.length() < 4)
                    current_path = current_path.replace(":\\", ":");
                
                System.out.println("Path: " + current_path + " Length: " + current_path.length());
                updateHistory(curr_history);

                selected_changes = false;

                folder = new File(current_path);
                try {
                    listFilesForFolder(folder);
                    dtm.setRowCount(anz_files + anz_folders);

                    displayFilesAndFolder(window, folder);
                    window.path.setText(current_path);
                } catch (FolderEmptyException e) {
                    folder = new File(old_path);
                    current_path = old_path;
                    printMessage(window, 2, "Selected Folder is empty!", 2000);
                } catch (IsNotAFolderException e) {
                    folder = new File(old_path);
                    current_path = old_path;
                    printMessage(window, 1, "Cannot open this File!", 2000);
                }
                PATH_CHANGED_EVENT = false;
                FIRST_EVENT = true;
            }
            
            if (window.PATH_CHANGED == true) {
                old_path = current_path;
                
                File file = new File(folder + "\\" + folders[window.SELECTED_PATH]);
                System.out.println("Folder: " + file.getPath());
                
                if (folders[window.SELECTED_PATH] == null) {
                    if (window.SELECTED_PATH - anz_folders >= 0 && window.SELECTED_PATH - anz_folders < files.length);
                        file = new File(folder + "\\" + files[window.SELECTED_PATH - anz_folders] + window.jTable1.getModel().getValueAt(window.SELECTED_PATH, 2));
                    System.out.println("File: " + file.getPath());
                    if (file.isFile()) {
                        try {
                            Desktop desktop = Desktop.getDesktop();
                            if(file.exists()) desktop.open(file);
                        } catch (IOException ex) {
                            printMessage(window, 2, "Cannot open this File! Go to Settings > Apps > Default-Apps > Select Default App by Type of File > " + window.jTable1.getModel().getValueAt(window.SELECTED_PATH, 2) + "!", 4000);
                        }
                    } else {
                        System.out.println("folder: " + folders[window.SELECTED_PATH]);
                        printMessage(window, 1, "Cannot open this File/Folder!", 2000);
                    }
                } else {
                    current_path = folder + "\\" + folders[window.SELECTED_PATH] + "\\";
                    PATH_CHANGED_EVENT = true;
                }
                
                System.out.println("Path: " + current_path);
                window.PATH_CHANGED = false;
            }
            
            if (window.PATH_CHANGED_NEXT == true) {

                
                
            }
            
            if (window.SHOW_SIZE_FOLDER_EVENT == true) {
                
                for (int i = 0; i < anz_folders; i++) {
                    long buffer = getSizeOfFolder(new File(folder + folders[i]));
                    String buffer_size = "";
                    if (buffer < 1024)
                        buffer_size = buffer + " Bytes";
                    else if (buffer < 1024*1024)
                        buffer_size = buffer/1024 + " KB";
                    else if (buffer < 1024*1024*1024)
                        buffer_size = String.format("%.2f", (double)buffer/1024/1024) + " MB";
                    else if (buffer < (double)1024*1024*1024*1024)
                        buffer_size = String.format("%.2f", (double)buffer/1024/1024/1024) + " GB";
                    else
                        buffer_size = String.format("%.2f", (double)buffer/1024/1024/1024/1024) + " TB";
                    
                    window.jTable1.getModel().setValueAt(buffer_size, i, 3);
                }
                
                window.SHOW_SIZE_FOLDER_EVENT = false;
            }
            
            if (window.PATH_CHANGED_PARENT == true) {
                String buffer;
                buffer = new File(current_path).getParent();
                System.out.println("Buffer: " + buffer + " Current_path: " + current_path);
                if (buffer != null) {
                    current_path = buffer;
                    PATH_CHANGED_EVENT = true;
                }
                window.PATH_CHANGED_PARENT = false;
            }
            
            if ((end_time < System.currentTimeMillis() && end_time != -1) || FIRST_EVENT) {
                resetMessage(window);
                FIRST_EVENT = false;
            }
            
            if (end_time == -1) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date d = new Date();
                window.date_time.setText(dateFormat.format(d));
            } else {
                window.date_time.setText("");
            }
            
            while((System.nanoTime()-start) < 16000); //END-TIME and CHECK for FRAME-CALCULATION -> 16000 ns = max. 60fps
        }
    }
    
    public static void displayFilesAndFolder(MainWindow window, File folder) {
        int i = 0;

        for (i = 0; i < anz_folders; i++) {
            folders[i] = folders[i].substring(folder.toString().length()+1, folders[i].length());
            
            window.jTable1.getModel().setValueAt(folders[i], i, 1);
            window.jTable1.getModel().setValueAt("Ordner", i, 2);
            window.jTable1.getModel().setValueAt("", i, 3);
            window.jTable1.getModel().setValueAt("", i, 4);
            
            window.jTable1.getModel().setValueAt("ordner_list.png", i, 0);
            
            if (Utilities.getHidden(new File(folder + folders[i]))) {
                window.jTable1.getModel().setValueAt("Versteckter Ordner", i, 4);
            }

        }
        for (i = 0; i < anz_files; i++) {

            files[i] = files[i].substring(folder.toString().length()+1, files[i].length());
            window.jTable1.getModel().setValueAt(files[i], i + anz_folders, 1);
            window.jTable1.getModel().setValueAt("", i + anz_folders, 4);
            window.jTable1.getModel().setValueAt("file_list.png", i + anz_folders, 0);
            if (Utilities.getHidden(new File(folder + folders[i]))) {
                window.jTable1.getModel().setValueAt("Versteckte Datei", i + anz_folders, 4);
            }
            
            long buffer = getSizeOfFile(new File(folder + "\\" + files[i]));
            String buffer_size = "";
            if (buffer < 1024)
                buffer_size = buffer + " Bytes";
            else if (buffer < 1024*1024)
                buffer_size = buffer/1024 + " KB";
            else if (buffer < 1024*1024*1024)
                buffer_size = String.format("%.2f", (double)buffer/1024/1024) + " MB";
            else if (buffer < (double)1024*1024*1024*1024)
                buffer_size = String.format("%.2f", (double)buffer/1024/1024/1024) + " GB";
            else
                buffer_size = String.format("%.2f", (double)buffer/1024/1024/1024/1024) + " TB";
            
            window.jTable1.getModel().setValueAt(buffer_size, i + anz_folders, 3);

            if (!(files[i].lastIndexOf('.') == -1)) {
                String att = files[i].substring(files[i].lastIndexOf('.'), files[i].length());

                files[i] = files[i].substring(0, files[i].length() - att.length());
                window.jTable1.getModel().setValueAt(att, i + anz_folders, 2);
            }
        }
    }
    
    public static void listFilesForFolder(final File folder) throws FolderEmptyException, IsNotAFolderException {
        files = new String[1024];
        folders = new String[1024];
        int i_files = 0;
        int i_folders = 0;
        
        if (folder.listFiles() == null)
            throw new FolderEmptyException("Folder empty!");
        if (folder.isDirectory() == false)
            throw new IsNotAFolderException("Not a Folder!");
        
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.equals("")) {
                if (fileEntry.isFile()) {

                    files[i_files] = fileEntry.toString();

                    i_files++;
                } else {
                    folders[i_folders] = fileEntry.toString();
                    i_folders++;
                }
            }
        }
        anz_files = i_files;
        anz_folders = i_folders;
    }
    
    public static void printMessage(MainWindow win, int colorType, String text, int duration) {
        long start_time = System.currentTimeMillis();
        Color bg = new Color(234,234,234);
        Color fg = new Color(255,255,255);
        switch(colorType) {
            case 1: bg = Color.RED; break;
            case 2: bg = new Color(28,150,48); break;
            case 3: bg = Color.BLUE; break;
            default: fg = new Color(50,50,50); break;
        }
        win.stateBar.setBackground(bg);
        win.stateText.setForeground(fg);
        win.stateText.setText(text);
        end_time = start_time + duration;
    }
    
    public static void resetMessage(MainWindow win) {
        win.stateBar.setBackground(new Color(234,234,234));
        win.stateText.setForeground(new Color(50,50,50));
        win.stateText.setText(anz_files + " File(s) and " + anz_folders + " Folder(s)");
    }
    
    public static void updateHistory(String path) {
        if (history == null)
            history = path;
        else {
            if (history.contains(path))
                return;
            else
                history = path;
        }
    }
}
