package util;

import com.google.gson.Gson;

public class Constants {
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/stepper";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String XML_LOADER = FULL_SERVER_PATH + "/upload_file";
    public final static String FLOW_FINAL_DATA = FULL_SERVER_PATH + "/flow_final_data";
    public final static String HISTORY_EXECUTIONS = FULL_SERVER_PATH + "/history_executions";
    public final static String STATS = FULL_SERVER_PATH + "/stats";
    public final static String ROLE = FULL_SERVER_PATH + "/role";
    public final static String ROLES = FULL_SERVER_PATH + "/roles";
    public final static String USER_ROLES = FULL_SERVER_PATH + "/user_roles";
    public final static String USERS = FULL_SERVER_PATH + "/users";
    public final static String USER = FULL_SERVER_PATH + "/user";
    public final static String AVAILABLE_FLOWS = FULL_SERVER_PATH + "/available_flows";
    public final static String EXECUTED_FLOWS = FULL_SERVER_PATH + "/executed_flows";
    public final static String ADMIN_LOGIN = FULL_SERVER_PATH + "/admin_login";

    // global constants
    public final static int REFRESH_RATE = 1000;
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
