import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CmdLineParserTest {

    @Test
    void parseConstTest() {
        String[] args = {"konstytucja.txt","art", "1"};
        CmdLineParser parser = new CmdLineParser();
        String path = parser.parse(args);

        assertEquals("konstytucja.txt",path);
        assertEquals(true, parser.isCons());
        assertEquals(false, parser.isUokik());

        FileLoader txtFile = new FileLoader();
        txtFile.loadFile(path);
        List<String> lines = txtFile.getLines();

        assertEquals("©Kancelaria Sejmu",lines.get(0));

        List<CmdLineParser.Argument> arguments;
        Constitution cons = new Constitution(lines);
        arguments = parser.findArguments(cons,args);
        CmdLineParser.Argument arg =arguments.get(0);

        assertEquals(ConstStructure.Article, arg.getcStruct());
        assertEquals(1,arg.getNum());
        assertEquals(0,arg.getLetter());
        assertEquals(0,arg.getsLetter());
    }

    @Test
    void parseUokikTest() {
        String[] args = {"uokik.txt","art", "1"};
        CmdLineParser parser = new CmdLineParser();
        String path = parser.parse(args);

        assertEquals("uokik.txt",path);
        assertEquals(true, parser.isUokik());
        assertEquals(false, parser.isCons());

        FileLoader txtFile = new FileLoader();
        txtFile.loadFile(path);
        List<String> lines = txtFile.getLines();

        assertEquals("©Kancelaria Sejmu s. 1/73",lines.get(0));

        List<CmdLineParser.Argument> arguments;
        Uokik uokik = new Uokik(lines);
        arguments = parser.findArguments(uokik,args);
        CmdLineParser.Argument arg =arguments.get(0);

        assertEquals(UokikStructure.Article, arg.getuStruct());
        assertEquals(1,arg.getNum());
        assertEquals(0,arg.getLetter());
        assertEquals(0,arg.getsLetter());

    }
}