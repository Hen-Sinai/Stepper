package util;

import com.google.gson.Gson;

public class Constants {
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/stepper";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String AVAILABLE_FLOWS = FULL_SERVER_PATH + "/available_flows";
    public final static String FLOW_MINIMAL_DESCRIPTION = FULL_SERVER_PATH + "/flow_minimal_description";
    public final static String FLOW_DETAILS = FULL_SERVER_PATH + "/flow_details";
    public final static String FLOW_FREE_INPUTS = FULL_SERVER_PATH + "/flow_free_inputs";
    public final static String FLOW_PROGRESS = FULL_SERVER_PATH + "/flow_progress";
    public final static String EXECUTE_FLOW = FULL_SERVER_PATH + "/execute_flow";
    public final static String FLOW_FINAL_DATA = FULL_SERVER_PATH + "/flow_final_data";
    public final static String CONTINUATIONS = FULL_SERVER_PATH + "/continuations";
    public final static String HISTORY_EXECUTIONS = FULL_SERVER_PATH + "/history_executions";
    public final static String LOGIN = FULL_SERVER_PATH + "/login";

    // global constants
    public final static int REFRESH_RATE = 1000;
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
