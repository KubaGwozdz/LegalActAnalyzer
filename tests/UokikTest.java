import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UokikTest {

    @Test
    void UokikTest() {
    String[] args = {"uokik.txt","art", "1"};
    CmdLineParser parser = new CmdLineParser();
    String path = parser.parse(args);
    FileLoader txtFile = new FileLoader();
    txtFile.loadFile(path);
    List<String> lines = txtFile.getLines();
    List<CmdLineParser.Argument> arguments;
    Uokik uokik = new Uokik(lines);
    arguments = parser.findArguments(uokik,args);
    uokik.objectify();
    Tree.Node<Act> root = uokik.tree.root();
    Act lawAct = root.getData();

    assertEquals(0,lawAct.getNum());
    assertEquals(UokikStructure.Statute,lawAct.getUStructure());
    assertEquals("UOKIK", lawAct.getName());
    assertTrue(lawAct.isPresent());

    }
    @Test
    void patternMatcherTest() {
        String[] args = {"uokik.txt","art", "1"};
        CmdLineParser parser = new CmdLineParser();
        String path = parser.parse(args);
        FileLoader txtFile = new FileLoader();
        txtFile.loadFile(path);
        List<String> lines = txtFile.getLines();
        List<CmdLineParser.Argument> arguments;
        Uokik uokik = new Uokik(lines);
        String article = "Art. 1. 1. Ustawa określa warunki rozwoju i ochrony konkurencji oraz zasady\n";
        Act art = uokik.matchPatterns(article);

        assertEquals(UokikStructure.Article,art.getUStructure());
        assertEquals(1,art.getNum());
        assertEquals(null,art.getLetter());
        assertEquals(null,art.getsLetter());


    }
}