package net.avalith.carDriver.models.enums;

public enum TariffType {
    HOUR("priceHour"), DAY("priceDay"), WEEK("priceWeek");

    private TariffType(String price) { }

    public String getValue(TariffType tariffType){
        String value = "";
        if(tariffType.equals(HOUR))
            value = "priceHour";
        if(tariffType.equals(DAY))
            value = "priceDay";
        if(tariffType.equals(WEEK))
            value = "priceWeek";

        return value;
    }
}
