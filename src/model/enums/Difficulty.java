package model.enums;

public enum Difficulty {
    // 1 - 4 : easy
    // 5 - 7 : medium
    // 8 - 10 : hard

    // enum constant
    LEVEL_1("1", 1.0),
    LEVEL_2("2", 2.0),
    LEVEL_3("3", 3.0),
    LEVEL_4("4", 4.0),
    LEVEL_5("5", 5.0),
    LEVEL_6("6", 6.0),
    LEVEL_7("7", 7.0),
    LEVEL_8("8", 8.0),
    LEVEL_9("9", 9.0),
    LEVEL_10("10", 10.0);

    // attributes
    private final String label;
    private final double weight;

    // constructor
    Difficulty(String label, double weight){
        this.label = label;
        this.weight = weight;
    }

    public static final Difficulty DEFAULT = LEVEL_5;

    // getter methods
    public String getLabel(){
        return label;
    }

    public double getWeight(){
        return weight;
    }

    public static Difficulty fromCsv(int value){
        return switch(value){
            case 1 -> LEVEL_1;
            case 2 -> LEVEL_2;
            case 3 -> LEVEL_3;
            case 4 -> LEVEL_4;
            case 5 -> LEVEL_5;
            case 6 -> LEVEL_6;
            case 7 -> LEVEL_7;
            case 8 -> LEVEL_8;
            case 9 -> LEVEL_9;
            case 10 -> LEVEL_10;
            default -> DEFAULT;
        };
    }

    public static Difficulty fromText(String text){
        if(text == null){
            return DEFAULT;
        }

        return switch(text.trim().toUpperCase()){
            case "LEVEL_1", "1" -> LEVEL_1;
            case "LEVEL_2", "2" -> LEVEL_2;
            case "LEVEL_3", "3" -> LEVEL_3;
            case "LEVEL_4", "4" -> LEVEL_4;
            case "LEVEL_5", "5" -> LEVEL_5;
            case "LEVEL_6", "6" -> LEVEL_6;
            case "LEVEL_7", "7" -> LEVEL_7;
            case "LEVEL_8", "8" -> LEVEL_8;
            case "LEVEL_9", "9" -> LEVEL_9;
            case "LEVEL_10", "10" -> LEVEL_10;
            default -> DEFAULT;
        };
    }

    @Override 
    public String toString(){
        return label;
    }
}
