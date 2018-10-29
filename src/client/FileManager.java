package client;

import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

class FileManager {
    public static String selectFile(){
        String fileName = new String(); 
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\Lennox\\Documents"));
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("PDF Files", "*.pdf"),
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("All Files", "*."));
        File chosen = fc.showOpenDialog(null);
        
        if(chosen != null){
            fileName = chosen.getAbsolutePath(); 
            System.out.println(fileName);
        }
        return fileName;
    }
    
}
