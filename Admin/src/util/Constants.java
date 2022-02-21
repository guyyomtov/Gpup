package util;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/main/mainFxml.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/login/login.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/GpupWeb_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public static final String UPLOAD_FILE = FULL_SERVER_PATH + "/uploadGraphShortResponse";
    public static final String UPLOAD_TASK = FULL_SERVER_PATH + "/uploadTaskShortResponse";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/adminsListServlet";
    public final static String GRAPHS_LIST = FULL_SERVER_PATH + "/graphsListServlet";
    public final static String TASKS_LIST = FULL_SERVER_PATH + "/tasksListServlet";
    public final static String GRAPHS_VIEW = FULL_SERVER_PATH + "/graphViewResponse";
    public final static String FIND_PATH = FULL_SERVER_PATH + "/findPath";
    public final static String FIND_CIRCLE = FULL_SERVER_PATH + "/findCircle";
    public final static String WHAT_IF_REQUEST = FULL_SERVER_PATH + "/whatIfRequest";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
