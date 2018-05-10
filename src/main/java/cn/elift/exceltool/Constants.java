package cn.elift.exceltool;

/**
 * author: Leo Zhang
 */
public class Constants {
    // Spring profile for development, production and "fast" "schedule"
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_DOCKER = "docker";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";

    public static final String SPRING_PROFILE_SCHEDULER = "schedule";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final int PAGE_SIZE_10 = 10;
    public static final int PAGE_SIZE_20 = 20;
    public static final int PAGE_SIZE_30 = 30;
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String MONTH_PATTERN2 = "yyyy年MM月";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String MONTH_TIME_PATTERN = "MM-dd HH:mm";
    public static final String DATE_FULL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_PATTERN2 = "yyyy/MM/dd HH:mm";
    public static final String DATE_FULL_TIME_PATTERN2 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_BATCH_NUMBER = "yyyyMMdd";

    public static final String CHANNEL = "Edianti";

    //REST Service Exception code;
    public static final int SERVER_ERROR = -1;
    public static final int SUCCESS = 0;
    public static final int USER_MOBILE_ERROR = 1001;
    public static final int USER_CERT_ERROR = 1002;

    public static final String CENTER_NAME = "客服";
    public static final String CENTER_PHONE = "4009-300-316";

    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";

    // Search Result Type
    public static class SearchResultType {
        public static final String TENANT = "tenant";
        public static final String ESTATE = "estate";
        public static final String VENDOR = "vendor";
        public static final String PLACE = "place";
    }

    // Frontend URI PR
    public static class Platform {
        public static final String WX = "/wx";
        public static final String CGJ = "/cgj";
        public static final String WX_PREFIX = "wx";
        public static final String CGJ_PREFIX = "cgj";
    }

    public static class Image {
        public static final String REPAIR_BEFORE = "维修前";
        public static final String REPAIR_AFTER = "维修后";
        public static final String DIAGNOSTIC = "diagnostic";
    }

    private Constants() {
    }
}
