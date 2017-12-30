/**
 * Created by kuba on 01.12.2017
 */

import java.lang.reflect.Array;
import java.util.*;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.lang.Object;


abstract class Law {
    List<String> lines;
    Act lawAct;
    Tree<Act> tree;

    abstract public Act matchPatterns(String line);

    public void showArguments(List<CmdLineParser.Argument> arguments, boolean tableOfContents) {
        Tree.Node<Act> from = tree.root();
        Tree.Node<Act> to = tree.root();

        boolean isTo = false;
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).isTo() == true) {
                isTo = true;
                break;
                //i++;
            }
            if (!isTo) {
                if(from ==tree.root() && (arguments.get(0).getuStruct() == UokikStructure.Article || (arguments.get(0).getcStruct() == ConstStructure.Article))){
                    List<Tree.Node<Act>> articlesList = setArticlesList();
                    CmdLineParser.Argument fromArt = arguments.get(0);
                    for (int j = 0; j < articlesList.size(); j++) {
                        Tree.Node<Act> art = articlesList.get(j);
                        if (articlesMatch(art, fromArt)) {
                            from = art;
                            break;
                        }
                    }
                }
                from = from.findChild(from, arguments.get(i));
                //from = from.getChildren().get(arguments.get(i).getNum() - 1);
            }
            if (isTo) {
                to = to.findChild(from, arguments.get(i));
            }
        }
        if (!isTo) {
            if (arguments.size() == 1 && (arguments.get(0).getuStruct() == UokikStructure.Article || (arguments.get(0).getcStruct() == ConstStructure.Article))) {
                List<Tree.Node<Act>> articlesList = setArticlesList();
                CmdLineParser.Argument fromArt = arguments.get(0);
                for (int i = 0; i < articlesList.size(); i++) {
                    Tree.Node<Act> art = articlesList.get(i);
                    if (articlesMatch(art, fromArt)) {
                        if (tableOfContents) {
                            tree.showTableOfContents(art);
                        } else tree.show(art);
                        return;
                    }
                }
            }
            if (tableOfContents) {
                tree.showTableOfContents(from);
            } else tree.show(from);
        }
        if (isTo) {
            showArticlesRange(arguments, tableOfContents);
        }


    }

    public void showArticlesRange(List<CmdLineParser.Argument> arguments, boolean tableOfContents) {
        CmdLineParser.Argument fromArt = arguments.get(0);
        CmdLineParser.Argument toArt = arguments.get(1);

        System.out.println("Artykuły: " + fromArt + " - " + toArt + "\n");

        List<Tree.Node<Act>> articlesList = setArticlesList();
        for (int i = 0; i < articlesList.size(); i++) {
            Tree.Node<Act> art = articlesList.get(i);
            if (articlesMatch(art, fromArt)) {
                if (tableOfContents) {
                    tree.showTableOfContents(art);
                } else tree.show(art);
                i++;
                while (!articlesMatch(art, toArt)&& i< articlesList.size()) {
                    art = articlesList.get(i);
                    if (tableOfContents) {
                        tree.showTableOfContents(art);
                    } else tree.show(art);
                    i++;
                }
                break;
            }
        }
    }


    private List<Tree.Node<Act>> setArticlesList(){
        List<Tree.Node<Act>> articlesList;
        if (tree.root().getData().getUStructure() != null) {
            articlesList = new LinkedList<>();
            for (Tree.Node<Act> part : tree.root().getChildren()) {
                if (part.getChildren().get(0).getData().getUStructure() == UokikStructure.Article)
                    articlesList.addAll(part.getChildren());
                else {
                    for (Tree.Node<Act> chap : part.getChildren()) {
                        articlesList.addAll(chap.getChildren());
                    }
                }
            }
        } else {
            articlesList = new LinkedList<>();
            for (Tree.Node<Act> chap : tree.root().getChildren()) {
                articlesList.addAll(chap.getChildren());
            }
        }
        return articlesList;
    }
    private boolean articlesMatch(Tree.Node<Act> art, CmdLineParser.Argument fromArt) {
        char fLetter;
        char sLetter;
        if(art.getData().getLetter()==null) fLetter = 0;
        else fLetter = art.getData().getLetter();
        if(art.getData().getsLetter()==null) sLetter = 0;
        else sLetter = art.getData().getsLetter();
        return (art.getData().getStructure() == fromArt.getcStruct() && art.getData().getUStructure() == fromArt.getuStruct()
                && art.getData().getNum() == fromArt.getNum()
                && fLetter == fromArt.getLetter()
                && sLetter == fromArt.getsLetter());
    }

    public void objectify() {
        Tree.Node<Act> prev = tree.root();

        for (int i = 0; i < lines.size(); i++) {
            if (matchPatterns(lines.get(i)).isPresent()) {
                if (isBiggerGrading(i, prev)) {
                    List<String> data = new ArrayList<String>();
                    Act art = matchPatterns(lines.get(i));
                    Act act;
                    if (art.getUStructure() == UokikStructure.Article) {
                        act = findArgInTheSameLine(lines, art, i, prev, data);
                        String line = lines.get(i);
                        int numLen;
                        if (art.getNum() == 1) {
                            prev = tree.addN(prev, act.getNum(), act.getName(), act);
                            if (prev.getData().getLetter() == null) {
                                numLen = String.valueOf(act.getNum()).length() + 1;
                            } else numLen = String.valueOf(act.getNum()).length() + 2;
                            if (prev.getData().getsLetter() != null) numLen++;
                            line = line.substring(numLen + 6, line.length());
                            act = matchPatterns(line);
                            data.add(line.substring(2 + String.valueOf(act.getNum()).length()));
                            i++;
                        } else {
                            act = findArgInTheSameLine(lines, art, i, prev, data);
                            addFromTheSameLine(data, lines, act, i);
                        }
                    } else {
                        act = matchPatterns(lines.get(i));
                        addFromTheSameLine(data, lines, act, i);
                    }
                    int j = i + 1;


                    while (!(matchPatterns(lines.get(j)).isPresent()) && (j) < lines.size()) { //j+1
                        data.add(lines.get(j));
                        j++;
                    }

                    act.setData(data);
                    prev = tree.addN(prev, act.getNum(), act.getName(), act);
                    if (j < lines.size()) i = j - 1;
                    else return;

                } else if (isEqualGrading(i, prev)) {
                    List<String> data = new ArrayList<String>();
                    Act art = matchPatterns(lines.get(i));
                    Act act;
                    boolean isSection = false;
                    if (art.getUStructure() == UokikStructure.Article) {
                        act = findArgInTheSameLine(lines, art, i, prev, data);
                        String line = lines.get(i);
                        int numLen;
                        if (art.getNum() == 1) {
                            prev = tree.addN(prev.getParent(), act.getNum(), act.getName(), act);
                            if (prev.getData().getLetter() == null) {
                                numLen = String.valueOf(prev.getData().getNum()).length() + 1;
                            } else numLen = String.valueOf(prev.getData().getNum()).length() + 2;
                            if (prev.getData().getsLetter() != null) numLen++;
                            line = line.substring(numLen + 6, line.length());
                            act = matchPatterns(line);
                            data.add(line.substring(2 + String.valueOf(act.getNum()).length()));
                            i++;
                            isSection = true;
                        } else {
                            act = findArgInTheSameLine(lines, art, i, prev, data);
                            addFromTheSameLine(data, lines, act, i);
                        }
                    } else {
                        act = matchPatterns(lines.get(i));
                        addFromTheSameLine(data, lines, act, i);
                    }
                    int j = i + 1;
                    while (!(matchPatterns(lines.get(j)).isPresent()) && (j + 1) < lines.size()) {
                        data.add(lines.get(j));
                        j++;
                    }
                    act.setData(data);
                    if (isSection) prev = tree.addN(prev, act.getNum(), act.getName(), act);
                    else prev = tree.addN(prev.getParent(), act.getNum(), act.getName(), act);
                    if (j < lines.size()) i = j - 1;
                    else return;

                } else if (isSmallerGrading(i, prev)) {
                    prev = fixParent(i, prev);
                    List<String> data = new ArrayList<String>();
                    Act art = matchPatterns(lines.get(i));
                    Act act;
                    if (art.getUStructure() == UokikStructure.Article) {
                        act = findArgInTheSameLine(lines, art, i, prev, data);
                        String line = lines.get(i);
                        int numLen;
                        if (art.getNum() == 1) {
                            prev = tree.addN(prev, act.getNum(), act.getName(), act);
                            if (prev.getData().getLetter() == null) {
                                numLen = String.valueOf(act.getNum()).length() + 1;
                            } else numLen = String.valueOf(act.getNum()).length() + 2;
                            if (prev.getData().getsLetter() != null) numLen++;
                            line = line.substring(numLen + 6, line.length());
                            act = matchPatterns(line);
                            data.add(line.substring(2 + String.valueOf(act.getNum()).length()));
                            i++;
                        } else {
                            act = findArgInTheSameLine(lines, art, i, prev, data);
                            addFromTheSameLine(data, lines, act, i);
                        }
                    } else {
                        act = matchPatterns(lines.get(i));
                        addFromTheSameLine(data, lines, act, i);
                    }
                    int j = i + 1;
                    while (!(matchPatterns(lines.get(j)).isPresent()) && (j + 1) < lines.size()) {
                        data.add(lines.get(j));
                        j++;
                    }
                    act.setData(data);
                    prev = tree.addN(prev, act.getNum(), act.getName(), act);
                    if (j < lines.size()) i = j - 1;
                    else return;
                }
            }
        }
    }

    public Tree.Node<Act> fixParent(int i, Tree.Node<Act> prev) {
        if (prev.getData().getUStructure() == null) {
            while (matchPatterns(lines.get(i)).getStructure().getGrading() <= prev.getData().getStructure().getGrading()) {
                prev = prev.getParent();
            }
        } else {
            while (matchPatterns(lines.get(i)).getUStructure().getGrading() <= prev.getData().getUStructure().getGrading()) {
                prev = prev.getParent();
            }
        }
        return prev;
    }

    public boolean isEqualGrading(int i, Tree.Node<Act> prev) {
        if (prev.getData().getUStructure() == null)
            return (matchPatterns(lines.get(i)).getStructure().getGrading() == prev.getData().getStructure().getGrading());
        else
            return (matchPatterns(lines.get(i)).getUStructure().getGrading() == prev.getData().getUStructure().getGrading());
    }

    public boolean isBiggerGrading(int i, Tree.Node<Act> prev) {
        if (prev.getData().getUStructure() == null)
            return (matchPatterns(lines.get(i)).getStructure().getGrading() > prev.getData().getStructure().getGrading());
        else
            return (matchPatterns(lines.get(i)).getUStructure().getGrading() > prev.getData().getUStructure().getGrading());
    }

    public boolean isSmallerGrading(int i, Tree.Node<Act> prev) {
        if (prev.getData().getUStructure() == null)
            return (matchPatterns(lines.get(i)).getStructure().getGrading() < prev.getData().getStructure().getGrading());
        else
            return (matchPatterns(lines.get(i)).getUStructure().getGrading() < prev.getData().getUStructure().getGrading());
    }

    public void addFromTheSameLine(List<String> data, List<String> lines, Act act, int i) {
        if (act.getStructure() == ConstStructure.Point || act.getStructure() == ConstStructure.Section || act.getUStructure() == UokikStructure.Section
                || act.getUStructure() == UokikStructure.Point || act.getUStructure() == UokikStructure.SubPoint) {
            int find = 0;
            String line = lines.get(i);
            while (find < line.length()) {
                if (line.charAt(find) == '.' || line.charAt(find) == ')') {
                    break;
                }
                find++;
            }
            data.add(line.substring(find + 1, line.length()));
        }
        if (act.getUStructure() == UokikStructure.Article) {
            String line = lines.get(i);
            int len = String.valueOf(act.getNum()).length();
            if (act.getLetter() != null) len++;
            if (act.getsLetter() != null) len++;
            data.add(line.substring(len + 7, line.length()));

        }
    }

    public Act findArgInTheSameLine(List<String> lines, Act act, int i, Tree.Node<Act> prev, List<String> data) {
        if (act.getUStructure() == UokikStructure.Article) {
            String line = lines.get(i);
            String[] args = line.split(". ");
            String art = args[1];
            int num;
            Character character;
            if (art.matches("\\d+[a-z][a-z]")) {
                num = Integer.parseInt(art.substring(0, art.length() - 2));
                character = art.charAt(art.length() - 1);
                Character sCharacter = art.charAt(art.length() - 2);
                act = new Act(UokikStructure.Article, "Artykuł " + num + character, num, character, sCharacter);
                act.makePresent();
                return act;
            } else if (art.matches("^\\d+[a-z]")) {
                num = Integer.parseInt(art.substring(0, art.length() - 1));
                character = art.charAt(art.length() - 1);
                act = new Act(UokikStructure.Article, "Artykuł " + num + character, num, character);
                act.makePresent();
                return act;
            } else {
                if (art.contains("–")) {
                    return act;
                }
                num = Integer.parseInt(art);
                act = new Act(UokikStructure.Article, "Artykuł " + num, num);
                act.makePresent();
                return act;
            }
        }
        return act;
    }

    public void clean() {
        ListIterator<String> iterator = lines.listIterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.matches("^\\d+[-]\\d+[-]\\d+")) {
                iterator.remove();
            }
            if (line.contains("©Kancelaria Sejmu")) {
                iterator.remove();
            }
            String endOfLine = "[-]$";
            Pattern eOL = Pattern.compile(endOfLine);
            Matcher matchEndOfLine = eOL.matcher(line);
            if (matchEndOfLine.find()) {
                String[] args = line.split(" ");
                iterator.remove();
                String arg = args[args.length - 1];
                arg = arg.substring(0, arg.length() - 1);
                String next = iterator.next();
                String[] nextLine = next.split(" ");
                iterator.remove();
                String nextWord = nextLine[0];
                String newArg = arg + nextWord;
                replaceElements(args, args[args.length - 1], newArg);
                nextLine = removeFirstElement(nextLine);
                String fLine = arrayToString(args);
                iterator.add(fLine);
                String sLine = arrayToString(nextLine);
                if (sLine != "") {
                    iterator.add(sLine);
                    iterator.previous();
                }
            }
        }
    }

    public static String[] replaceElements(String[] input, String deleteMe, String addMe) {
        List result = new LinkedList();
        for (String item : input)
            if (!deleteMe.equals(item))
                result.add(item);
        result.add(addMe);
        return (String[]) result.toArray(input);
    }

    public static String[] removeElements(String[] input, String deleteMe) {
        List result = new LinkedList();
        for (String item : input)
            if (!deleteMe.equals(item))
                result.add(item);
        return (String[]) result.toArray(input);
    }

    public static String[] removeFirstElement(String[] input) {
        List result = new LinkedList();
        for (int i = 1; i < input.length; i++) {
            result.add(input[i]);
        }
        return (String[]) result.toArray(input);
    }

    public String arrayToString(String[] args) {
        String line = "";
        for (String word : args) {
            if (word != null) {
                if (word.equals(args[0]))
                    line += word;
                else
                    line = line + " " + word;
            }
        }
        return line;
    }

}