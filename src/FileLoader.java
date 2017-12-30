/**
 * Created by kuba on 1.12.2017.
 */

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileLoader {
    private Charset charset = Charset.defaultCharset();
    private List <String> lines = new ArrayList<>();

    public void loadFile(String path) {
        try {
            Path txtFile = Paths.get(path);
            lines = Files.readAllLines(txtFile);

        }
        catch (IOException ex){
            System.out.println("IO exception: " + ex);
        }
    }

    public List<String> getLines(){
        return lines;
    }
}
