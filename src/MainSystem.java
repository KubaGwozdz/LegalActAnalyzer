/**
 * Created by kuba on 30.11.2017.
 */

import java.util.List;


public class MainSystem {
    public static void main(String[] args){
        CmdLineParser parser = new CmdLineParser();
        String path = parser.parse(args);
        if(path == null)return;
        FileLoader txtFile = new FileLoader();
        txtFile.loadFile(path);
        List<String> lines = txtFile.getLines();
        List<CmdLineParser.Argument> arguments;
        if(parser.isCons()) {
            Constitution cons = new Constitution(lines);
            arguments = parser.findArguments(cons,args);
            cons.clean();
            cons.objectify();
            cons.showArguments(arguments,parser.isTabOfContents());
        }
        else if(parser.isUokik()){
            Uokik uokik = new Uokik(lines);
            arguments = parser.findArguments(uokik,args);
            uokik.clean();
            uokik.objectify();
            uokik.showArguments(arguments,parser.isTabOfContents());
        }
        else parser.showOptions();

    }
}
