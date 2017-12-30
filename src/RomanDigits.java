import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kuba on 08.12.2017
 */


public class RomanDigits {
    private int num;
    private String romNum;

    private String one = "\\bI\\b\\z";
    private String two = " \\bII\\b\\z";
    private String three = "\\bIII\\b\\z";
    private String four = "\\bIV\\b\\z";
    private String five = "\\bV\\b\\z";
    private String six = "\\bVI\\b\\z";
    private String seven = "\\bVII\\b\\z";
    private String eight = "\\bVIII\\b\\z";
    private String nine = "\\bIX\\b\\z";
    private String ten = "\\bX\\b\\z";
    private String eleven = "\\bXI\\b\\z";
    private String twelve = "\\bXII\\b\\z";
    private String thirteen = "\\bXIII\\b\\z";
    private String threeA = "\\bIIIA\\b\\z";

    private Pattern pOne = Pattern.compile(one);
    private Pattern pTwo = Pattern.compile(two);
    private Pattern pThree = Pattern.compile(three);
    private Pattern pFour = Pattern.compile(four);
    private Pattern pFive = Pattern.compile(five);
    private Pattern pSix = Pattern.compile(six);
    private Pattern pSeven = Pattern.compile(seven);
    private Pattern pEight = Pattern.compile(eight);
    private Pattern pNine = Pattern.compile(nine);
    private Pattern pTen = Pattern.compile(ten);
    private Pattern pEleven = Pattern.compile(eleven);
    private Pattern pTwelve = Pattern.compile(twelve);
    private Pattern pThirteen = Pattern.compile(thirteen);
    private Pattern pThreeA = Pattern.compile(threeA);





    public RomanDigits (String line){
        Matcher mOne = pOne.matcher(line);
        if(mOne.find()) {
            num = 1;
            romNum = "I";
        }
        Matcher mTwo = pTwo.matcher(line);
        if(mTwo.find()) {
            num = 2;
            romNum="II";
        }
        Matcher mThree = pThree.matcher(line);
        if(mThree.find()) {
            num = 3;
            romNum="III";
        }
        Matcher mFour = pFour.matcher(line);
        if(mFour.find()) {
            num = 4;
            romNum="IV";
        }
        Matcher mFive = pFive.matcher(line);
        if(mFive.find()) {
            num = 5;
            romNum="V";
        }
        Matcher mSix = pSix.matcher(line);
        if(mSix.find()) {
            num = 6;
            romNum="VI";
        }
        Matcher mSeven = pSeven.matcher(line);
        if(mSeven.find()) {
            num = 7;
            romNum="VII";
        }
        Matcher mEight = pEight.matcher(line);
        if(mEight.find()) {
            num = 8;
            romNum="VIII";
        }
        Matcher mNine = pNine.matcher(line);
        if(mNine.find()) {
            num = 9;
            romNum="IX";
        }
        Matcher mTen = pTen.matcher(line);
        if(mTen.find()) {
            num = 10;
            romNum = "X";
        }
        Matcher mEleven = pEleven.matcher(line);
        if(mEleven.find()) {
            num = 11;
            romNum="XI";
        }
        Matcher mTwelve = pTwelve.matcher(line);
        if(mTwelve.find()) {
            num = 12;
            romNum="XII";
        }
        Matcher mThirteen = pThirteen.matcher(line);
        if(mThirteen.find()) {
            num = 13;
            romNum="XIII";
        }
        Matcher mThreeA = pThreeA.matcher(line);
        if(mThirteen.find()) {
            num = 14;
            romNum="XIIIA";
        }




    }

    public String getRomNum() {return romNum;}
    public int getNum() {return num;}



}