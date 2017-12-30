import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ConstitutionTest {

    @Test
    void ConstTest() {
        String[] args = {"konstytucja.txt","art", "1"};
        CmdLineParser parser = new CmdLineParser();
        String path = parser.parse(args);
        FileLoader txtFile = new FileLoader();
        txtFile.loadFile(path);
        List<String> lines = txtFile.getLines();
        List<CmdLineParser.Argument> arguments;
        Constitution cons = new Constitution(lines);
        arguments = parser.findArguments(cons,args);
        cons.objectify();
        Tree.Node<Act> root = cons.tree.root();
        Act lawAct = root.getData();

        assertEquals(0,lawAct.getNum());
        assertEquals(ConstStructure.Constitution,lawAct.getStructure());
        assertEquals("Konstytucja", lawAct.getName());
        assertTrue(lawAct.isPresent());

    }
    @Test
    void patternMatcherTest() {
        String[] args = {"konstytucja.txt","art", "1"};
        CmdLineParser parser = new CmdLineParser();
        String path = parser.parse(args);
        FileLoader txtFile = new FileLoader();
        txtFile.loadFile(path);
        List<String> lines = txtFile.getLines();
        List<CmdLineParser.Argument> arguments;
        Constitution constitution = new Constitution(lines);
        String section = "1. Ustrój terytorialny Rzeczypospolitej Polskiej zapewnia decentralizację władzy";
        Act sct = constitution.matchPatterns(section);

        assertEquals(ConstStructure.Section,sct.getStructure());
        assertEquals(1,sct.getNum());
        assertEquals(null,sct.getLetter());
        assertEquals(null,sct.getsLetter());
        assertEquals("Ustęp 1", sct.getName());


    }
    
}