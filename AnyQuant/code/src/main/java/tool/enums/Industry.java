package tool.enums;

/**
 * Created by Lin on 2016/4/12.
 */
public enum Industry {
    COAL("coal","煤炭"),WINE("wine","酒业"),BANK("bank","银行业"),SOURCE("source","新能源");
    private String id;
    private String name;

    Industry(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public static String getName(String id){
        for(Industry industry:Industry.values()){
            if(industry.getID().equals(id)){
                return industry.getName();
            }
        }
        return null;
    }
}
