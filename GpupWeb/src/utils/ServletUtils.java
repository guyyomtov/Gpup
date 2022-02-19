package utils;


import Graph.GraphManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import users.UserManager;



public class ServletUtils {


	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String GRAPH_MANAGER_ATTRIBUTE_NAME = "graphManager";
	private static final Object userManagerLock = new Object();
	private static final Object graphManagerLock = new Object();


	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static GraphManager getGraphManager(ServletContext servletContext) {

		synchronized (graphManagerLock) {

			if (servletContext.getAttribute(GRAPH_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(GRAPH_MANAGER_ATTRIBUTE_NAME, new GraphManager());
			}
		}
		return (GraphManager) servletContext.getAttribute(GRAPH_MANAGER_ATTRIBUTE_NAME);
	}
}
