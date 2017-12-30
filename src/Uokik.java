import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kuba on 20.12.2017
 */


public class Uokik extends Law {
    private Act uokik;
    private Tree<Act> uokikTree;

    public Uokik(List<String> lines) {
        super();
        this.lines = lines;
        this.lawAct = new Act(UokikStructure.Statute, "UOKIK", 0);
        lawAct.makePresent();
        List<String> preamble = new LinkedList<>();
        preamble.addAll(lines.subList(3, 6));
        lawAct.setData(preamble);
        this.tree = new Tree<Act>(lawAct);
    }

    private String prt   = "^DZIAŁ ";
    private String chap  = "^Rozdział \\d+";
    private String art   = "^Art. \\d+";
    private String sctn  = "^\\d+[.]";
    private String pnt   = "^\\d+[a-z]?[)]";
    private String sbPnt = "^[a-z][)]";

    private Pattern part     = Pattern.compile(prt);
    private Pattern chapter  = Pattern.compile(chap);
    private Pattern article  = Pattern.compile(art);
    private Pattern section  = Pattern.compile(sctn);
    private Pattern point    = Pattern.compile(pnt);
    private Pattern subPoint = Pattern.compile(sbPnt);

    @Override
    public  Act matchPatterns(String line) {
        Matcher partMatcher    = part.matcher(line);
        Matcher chapterMatcher = chapter.matcher(line);
        Matcher articleMatcher = article.matcher(line);
        Matcher sectionMathcer = section.matcher(line);
        Matcher pointMatcher   = point.matcher(line);
        Matcher spMatcher      = subPoint.matcher(line);


        if( partMatcher.find()){
            RomanDigits romanDigit;
            Act part;
            if(line.substring(line.length()-1,line.length()).matches("[A-H]")){
                romanDigit = new RomanDigits(line.substring(0,line.length()-1));
                int num = romanDigit.getNum();
                Character character = line.charAt(line.length()-1);
                part = new Act(UokikStructure.Part, "Dział " + romanDigit.getRomNum() + character , num, character);
            }
            else {
                romanDigit = new RomanDigits(line);
                int num = romanDigit.getNum();
                part = new Act(UokikStructure.Part, "Dział " + romanDigit.getRomNum(), num);
            }
            part.makePresent();
            return part;
        }

        if (chapterMatcher.find()) {
            int num;
            Character character;
            if( line.substring(9,line.length()).matches("\\d+[a-z]$")){
                num = Integer.parseInt(line.substring(9, line.length() - 1));
                character = line.charAt(line.length()-1);
                Act chapter = new Act(UokikStructure.Chapter, "Rozdział" + num + character, num, character );
                return chapter;
            }
            else {
                num = Integer.parseInt(line.substring(9, line.length()));
                Act chapter = new Act(UokikStructure.Chapter, "Rozdział " + num, num);
                chapter.makePresent();
                return chapter;
            }
        }

        if (articleMatcher.find()) {
            int num = 0;
            Character character;
            String[] args = line.split("[.]");

            if(args.length>=1) {
                String arg = args[2]; //2
                if (arg.matches(" \\d+")) {
                    num = 1;                                    //jest cos w tej samej lini
                }
            }
            Act article = new Act(UokikStructure.Article,"Artykuł",num);
            article.makePresent();
            return article;
        }

        if (sectionMathcer.find()) {
            int num;
            if (line.charAt(1) == '.') num = Integer.parseInt(line.substring(0, 1));
            else num = Integer.parseInt(line.substring(0, 2));
            Act sect = new Act(UokikStructure.Section, "Ustęp " + num, num);
            sect.makePresent();
            return sect;
        }

        if (pointMatcher.find()) {
            int num;
            String[] args = line.split("[)]");
            String arg = args[0];
            Character character;
            if( arg.matches("\\d+[a-z]$")){
                num = Integer.parseInt(arg.substring(0, arg.length() - 1));
                character = line.charAt(arg.length()-1);
                Act point = new Act(UokikStructure.Point, "Punkt " + num + character, num, character );
                point.makePresent();
                return point;
            }
            else {
                num = Integer.parseInt(arg);
                Act point = new Act(UokikStructure.Point, "Punkt " + num, num);
                point.makePresent();
                return point;
            }
        }
        if (spMatcher.find()) {
            Character character = line.charAt(0);
            int num = character - 96;
            Act sect = new Act(UokikStructure.SubPoint, "Podpunkt " + character,num );
            sect.makePresent();
            return sect;
        }
        else return new Act();
    }

}


