package net.avalith.carDriver.utils;

public class Routes {

    public static final String CITY = "/cities";
    public static final String CITY_UPDATE = "/{name}";

    public static final String COUNTRY = "/countries";
    public static final String COUNTRY_UPDATE = "/{name}";

    public static final String LICENSE = "/licenses";
    public static final String LICENSE_UPDATE = "/{number}";

    public static final String POINTS = "/points";

    public static final String RIDE = "/rides";
    public static final String RIDE_UPDATE = "/{id}";
    public static final String END_RIDE = "/{id}/end";
    public static final String START_RIDE = "/{id}/start";
    public static final String CRASH_RIDE = "/{id}/crash";

    public static final String USER = "/users";
    public static final String USER_UPDATE = "/{dni}";
    public static final String USER_DELETE = "/{dni}";

    public static final String BRAND = "/brands";
    public static final String BRAND_UPDATE = "/{name}";

    public static final String PROVIDER = "/providers";
    public static final String PROVIDER_UPDATE = "/{name}";
    public static final String PROVIDER_DELETE = "/{name}";

    public static final String VEHICLE_MODEL = "/vehicle-models";
    public static final String VEHICLE_MODEL_UPDATE = "/{name}";

    public static final String VEHICLE_CATEGORY = "/category-vehicles";
    public static final String VEHICLE_CATEGORY_UPDATE = "/{name}";
    public static final String VEHICLE_CATEGORY_DELETE = "/{name}";

    public static final String VEHICLE = "/vehicles";
    public static final String VEHICLE_UPDATE = "/{domain}";
    public static final String VEHICLE_DELETE = "/{domain}";

    public static final String MISHAP = "/mishaps";

    public static final String SALES = "/sales";
}
