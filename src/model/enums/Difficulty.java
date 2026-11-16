package model.enums;

public enum Difficulty {
    // 1 - 4 : easy
    // 5 - 7 : medium
    // 8 - 10 : hard

    // enum constant
    LEVEL_1("1", 1),
    LEVEL_2("2", 2),
    LEVEL_3("3", 3),
    LEVEL_4("4", 4),
    LEVEL_5("5", 5),
    LEVEL_6("6", 6),
    LEVEL_7("7", 7),
    LEVEL_8("8", 8),
    LEVEL_9("9", 9),
    LEVEL_10("10", 10);

    // attributes
    private final String label;
    private final int weight;

    // constructor
    Difficulty(String label, int weight){
        this.label = label;
        this.weight = weight;
    }

    // getter methods
    public String getLabel(){
        return label;
    }

    public int getWeight(){
        return weight;
    }

    // 
    public static Difficulty fromText(String text){
        for(Difficulty d : Difficulty.values()){
            if(d.name().equalsIgnoreCase(text.trim())){
                return d;
            }
        }
        return LEVEL_5; // defualt
    }

    @Override 
    public String toString(){
        return label;
    }
}
