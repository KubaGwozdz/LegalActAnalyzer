/**
 * Created by kuba on 01.12.2017
 */



import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Constitution extends Law {
    private List<String> preamble = new ArrayList<>();

    private String chap = "^Rozdział ";
    private String art = "^Art. \\d+";
    private String sctn = "^\\d+[.]";
    private String pnt = "^\\d+[)]";

    private Pattern chapter = Pattern.compile(chap);
    private Pattern article = Pattern.compile(art);
    private Pattern section = Pattern.compile(sctn);
    private Pattern point = Pattern.compile(pnt);


    public Constitution(List<String> lines) {
        super();
        this.lines = lines;
        this.lawAct = new Act(ConstStructure.Constitution, "Konstytucja", 0);
        lawAct.makePresent();
        List<String> preamble = new LinkedList<>();
        preamble.addAll(lines.subList(3, 32));
        lawAct.setData(preamble);
        this.tree = new Tree<Act>(lawAct);

    }


    public Act matchPatterns(String line) {
        Matcher chapterMatcher = chapter.matcher(line);
        Matcher articleMatcher = article.matcher(line);
        Matcher sectionMatchcer = section.matcher(line);
        Matcher pointMatcher = point.matcher(line);

        if (chapterMatcher.find()) {
            RomanDigits romanDigit = new RomanDigits(line);
            int num = romanDigit.getNum();
            Act chap = new Act(ConstStructure.Chapter, "Rozdział" + romanDigit.getRomNum(), num);
            chap.makePresent();
            return chap;
        }

        if (articleMatcher.find()) {
            int num = Integer.parseInt(line.substring(5, line.length() - 1));
            Act art = new Act(ConstStructure.Article, "Artykuł " + num, num);
            art.makePresent();
            return art;
        }

        if (sectionMatchcer.find()) {
            int num;
            if (line.charAt(1) == '.') num = Integer.parseInt(line.substring(0, 1));
            else num = Integer.parseInt(line.substring(0, 2));
            Act sect = new Act(ConstStructure.Section, "Ustęp " + num, num);
            sect.makePresent();
            return sect;
        }

        if (pointMatcher.find()) {
            int num;
            if (line.charAt(2) == ')') num = Integer.parseInt(line.substring(0, 2));
            else num = Integer.parseInt(line.substring(0, 1));
            Act pnt = new Act(ConstStructure.Point, "Punkt " + num, num);
            pnt.makePresent();
            return pnt;
        } else return new Act();
    }
}