/**
 * Created by kuba on 01.12.2017
 */

public enum ConstStructure {
    Constitution ("Root",0),
    Chapter  ("Rozdział",1),
    Article  ("Art.",2),
    Section  ("Ustęp",3),
    Point    ("pkt",4);


    private String type;
    private int grading;
    ConstStructure(String type, int grading) {
        this.type = type;
        this.grading = grading;
    }

    public int getGrading() { return this.grading; }

}
