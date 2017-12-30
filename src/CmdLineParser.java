/**
 * Created by kuba on 1.12.2017.
 */

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CmdLineParser {
    private String path;
    private boolean cons = false;
    private boolean uokik = false;
    private boolean tabOfContents = false;
    private ConstStructure structure;
    private List<Argument> from;
    private List<Argument> to;

    //---------------------- ARGUMENT -----------------------
    public class Argument {
        private ConstStructure cStruct;
        private UokikStructure uStruct;
        private int num;
        private boolean to = false;
        private char letter;
        private char sLetter;

        public Argument(ConstStructure cStruct, int num) {
            this.num = num;
            this.cStruct = cStruct;
        }

        public Argument(UokikStructure uStruct, int num) {
            this.num = num;
            this.uStruct = uStruct;
        }

        public Argument(UokikStructure uStruct, int num, char letter) {
            this.num = num;
            this.uStruct = uStruct;
            this.letter = letter;
        }

        public Argument(UokikStructure uStruct, int num, char letter, char sLetter) {
            this.num = num;
            this.uStruct = uStruct;
            this.letter = letter;
            this.sLetter = sLetter;
        }

        public Argument() {

        }

        public Argument(boolean to) {
            this.to = true;
        }

        public int getNum() {
            return num;
        }

        public char getLetter() {
            return letter;
        }

        public char getsLetter() {
            return sLetter;
        }

        public void setTo(boolean to) {
            this.to = to;
        }

        public boolean isTo() {
            return to;
        }

        public ConstStructure getcStruct() {
            return cStruct;
        }

        public UokikStructure getuStruct() {
            return uStruct;
        }

        @Override
        public String toString() {
            if (cons) {
                if (letter == 0) {
                    return cStruct + " " + num;
                } else {
                    if (sLetter == 0) return cStruct + " " + num + letter;
                    else return cStruct + " " + num + letter + sLetter;
                }
            } else if (uokik) {
                if (letter == 0) {
                    return uStruct + " " + num;
                } else {
                    if (sLetter == 0) return uStruct + " " + num + letter;
                    else return uStruct + " " + num + letter + sLetter;
                }
            } else return "BRAK ARGUMENTÓW";
        }
    }
//-----------------------------------------------------

    public String parse(String[] args) {
        for (String arg : args) {
            if (arg.contains("konstytucja.txt")) {
                cons = true;
                path = arg;
                break;
            }
            if (arg.contains("uokik.txt")) {
                uokik = true;
                path = arg;
                break;
            } else showOptions();
            //System.out.println(str);
        }

        return path;
    }

    public boolean isCons() {
        return cons;
    }

    public boolean isUokik() {
        return uokik;
    }

    public List<Argument> findArguments(Law law, String[] args) {
        if (cons) {
            boolean isArg = false;
            from = new LinkedList<>();
            to = new LinkedList<>();
            boolean isTo = false;
            for (int i = 0; i < args.length; i++) {
                Argument argument = new Argument();
                if (args[i].contains("spis")) {
                    tabOfContents = true;
                    isArg = true;
                }
                if (args[i].toLowerCase().contains("roz") || args[i].toLowerCase().contains("rozdział")) {
                    findNum(i, argument, args);
                    argument.cStruct = ConstStructure.Chapter;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("art") || args[i].toLowerCase().contains("artykuł")) {
                    findNum(i, argument, args);
                    argument.cStruct = ConstStructure.Article;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("ust") || args[i].toLowerCase().contains("ustęp")) {
                    findNum(i, argument, args);
                    argument.cStruct = ConstStructure.Section;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("pkt") || args[i].toLowerCase().contains("punkt")) {
                    findNum(i, argument, args);
                    argument.cStruct = ConstStructure.Point;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].contains("-")) {
                    isTo = true;
                    continue;
                }

            }
            if (isTo) {
                from.get(0).setTo(true);
                from.addAll(to);
                if (from.size() != 2) {
                    showOptions();
                    return null;
                }
            }
            if (!isArg) {
                showOptions();
                Argument argument = new Argument();
                argument.cStruct = ConstStructure.Constitution;
                isArg = true;
                setArgs(isTo, argument);
                from.add(argument);
                System.out.println("\n Nie wprowadzono argumentów! \n" +
                        "zostanie wyświtlona cała Konstytucja \n");
                return from;
            } else return from;
        } else if (uokik) {
            boolean isArg = false;
            from = new LinkedList<>();
            to = new LinkedList<>();
            boolean isTo = false;
            for (int i = 0; i < args.length; i++) {
                Argument argument = new Argument();
                if (args[i].contains("spis")) {
                    tabOfContents = true;
                    isArg = true;
                }
                if (args[i].toLowerCase().contains("dz") || args[i].toLowerCase().contains("dział")) {
                    findNum(i, argument, args);
                    argument.uStruct = UokikStructure.Part;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("roz") || args[i].toLowerCase().contains("rozdział")) {
                    findNum(i, argument, args);
                    argument.uStruct = UokikStructure.Chapter;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("art") || args[i].toLowerCase().contains("artykuł")) {
                    findNum(i, argument, args);
                    argument.uStruct = UokikStructure.Article;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("ust") || args[i].toLowerCase().contains("ustęp")) {
                    findNum(i, argument, args);
                    argument.uStruct = UokikStructure.Section;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("pkt") || args[i].toLowerCase().contains("punkt")) {
                    findNum(i, argument, args);
                    argument.uStruct = UokikStructure.Point;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].toLowerCase().contains("ppkt") || args[i].toLowerCase().contains("podpunkt")) {
                    findNum(i, argument, args);
                    argument.uStruct = UokikStructure.SubPoint;
                    isArg = true;
                    setArgs(isTo, argument);
                }
                if (args[i].contains("-")) {
                    isTo = true;
                    continue;
                }

            }
            if (isTo) {
                from.get(0).setTo(true);
                from.addAll(to);
                if (from.size() != 2) {
                    showOptions();
                    return null;
                }
            }
            if (!isArg) {
                showOptions();
                Argument argument = new Argument();
                argument.uStruct = UokikStructure.Statute;
                isArg = true;
                setArgs(isTo, argument);
                from.add(argument);
                System.out.println("\n Nie wprowadzono argumentów! \n" +
                        "zostanie wyświtlona cała ustawa: \n");
                return from;
            } else return from;
        } else {
            showOptions();
            return null;
        }
    }


    private void setArgs(boolean isTo, Argument argument) {
        try {

            if (isTo) {
                if (to.size() == 0) {
                    to.add(argument);
                    return;
                }
                if (argument.uStruct != null && to.get(to.size() - 1).uStruct.getGrading() >= argument.uStruct.getGrading())
                    showOptions();
                if (argument.cStruct != null && to.get(to.size() - 1).cStruct.getGrading() >= argument.cStruct.getGrading())
                    showOptions();
                to.add(argument);
            } else {
                if (from.size() == 0) {
                    from.add(argument);
                    return;
                }
                if (argument.cStruct != null && from.get(from.size() - 1).cStruct.getGrading() >= argument.cStruct.getGrading())
                    showOptions();
                if (argument.uStruct != null && from.get(from.size() - 1).uStruct.getGrading() >= argument.uStruct.getGrading())
                    showOptions();
                from.add(argument);
            }
        } catch (IndexOutOfBoundsException ex1) {
            showOptions();
        }
    }

    public void findNum(int i, Argument argument, String[] args) {
        String oneLetter = "\\d+[a-z]";
        String twoLetters = "\\d+[a-z][a-z]";
        Pattern oneLP = Pattern.compile(oneLetter);
        Pattern twoLP = Pattern.compile(twoLetters);
        Matcher oneLetterMatcher = oneLP.matcher(args[i + 1]);
        Matcher twoLetterMatcher = twoLP.matcher(args[i + 1]);
        if (args[i].matches("\\d+")) {
            argument.num = Integer.parseInt(args[i]);
        } else if (oneLetterMatcher.find()) {
            argument.num = Integer.parseInt(args[i + 1].substring(0, args[i + 1].length() - 1));
            argument.letter = args[i + 1].charAt(args[i + 1].length() - 1);
        } else if (twoLetterMatcher.find()) {
            argument.num = Integer.parseInt(args[i + 1].substring(0, args[i + 1].length() - 2));
            argument.letter = args[i + 1].charAt(args[i + 1].length() - 2);
            argument.sLetter = args[i + 1].charAt(args[i + 1].length() - 1);
        } else if (i + 1 < args.length) {// || args[i+1].matches("\\d+")){
            argument.num = Integer.parseInt(args[i + 1]);
        } else showOptions();
    }

    public boolean isTabOfContents() {
        return tabOfContents;
    }

    public void showOptions() {
        System.out.println("\nPOMOC: " +
                "Proszę wprowadzic dane: nazwa_pliku zakres(oddzielony znakiem - ) artukułów do wyświetlenia \n" +
                " lub konkretny element pliku, podając pełną jego ścieżkę \n" +
                "   przykład: konstytucja.txt rozdział 1 art 12 \n" +
                " aby nie wyswietlic treści tylko spis na końcu prosze dodac spis \n" +
                "   przykład: konstytucja.txt art 4 - art 64 spis treści");

    }

}
