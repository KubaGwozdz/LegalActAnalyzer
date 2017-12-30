/**
 * Created by kuba on 01.12.2017
 */

import java.util.ArrayList;
import java.util.List;


public class Act {
    private boolean isPresent = false;
    private String name;
    private int num;
    private Character letter;
    private Character sLetter;
    private ConstStructure structure;
    private UokikStructure uStructure;
    private List<String> lines;


    public Act(ConstStructure structure, String name, int num){
        this.structure = structure;
        this.name = name;
        this.num = num;
    }

    public Act(UokikStructure structure, String name, int num){
        this.uStructure = structure;
        this.name = name;
        this.num = num;
    }

    public Act(UokikStructure structure, String name, int num, Character letter){
        this.uStructure = structure;
        this.name = name;
        this.num = num;
        this.letter = letter;
    }
    public Act(UokikStructure structure, String name, int num, Character letter, Character sLetter){
        this.uStructure = structure;
        this.name = name;
        this.num = num;
        this.letter = letter;
        this.sLetter = sLetter;
    }

    public Act() {
    }

    public ConstStructure getStructure() { return structure; }

    public UokikStructure getUStructure() {
        return uStructure;
    }

    public int getNum() {
        return num;
    }

    public Character getLetter() {
        return letter;
    }

    public Character getsLetter() {
        return sLetter;
    }

    public String getName() {
        return name;
    }

    public void setData(List<String> lines){
        this.lines = lines;
    }
    public void makePresent(){
        isPresent = true;
    }

    public boolean isPresent(){
        return isPresent;
    }




    @Override
    public String toString() {
        String data = new String();
        if(lines == null) return name;
        else{
            data = name ;
            for(String line: lines){
                data += "\n" + line;
            }
            return data;
        }
    }
}