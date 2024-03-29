package Utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String AVAILABLE_TASK = "available";
    public final static String PAUSED_TASK = "paused";
    public final static String STOPPED_TASK = "stopped";


    // fxml locations
    public final static String Login_PAGE_FXML_RESOURCE_LOCATION = "/Login/LoginPage.fxml";
    public final static String DASHBOARD_FXML = "/DashBoard/WorkerDashBoard.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/GpupWeb_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
