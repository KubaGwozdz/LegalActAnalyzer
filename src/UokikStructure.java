/**
 * Created by kuba on 16.12.2017
 */

public enum UokikStructure {
    Statute   ("Root",0),
    Part     ("Dział",1),
    Chapter  ("Rozdział",2),
    Article  ("Art.",3),
    Section  ("Ustęp",4),
    Point    ("pkt",5),
    SubPoint ("ppkt",6);


    private String type;
    private int grading;
    UokikStructure (String type, int grading) {
        this.type = type;
        this.grading = grading;
    }

    public int getGrading() { return this.grading; }


}
