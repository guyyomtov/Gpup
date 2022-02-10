package DashBoardAdmin.onlineAdminsComponent;

import api.HttpStatusUpdate;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineAdminsController implements Closeable {

    @FXML private ListView<String> adminsListView;
    private Timer timer;
    private TimerTask listRefresher;
    private HttpStatusUpdate httpStatusUpdate;
    private final IntegerProperty totalUsers;
    private final BooleanProperty autoUpdate;


    public OnlineAdminsController() {
        autoUpdate = new SimpleBooleanProperty(true);
        totalUsers = new SimpleIntegerProperty();
    }

    public void setHttpStatusUpdate(HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;

    }

    private void updateUsersList(List<String> usersNames) {
        Platform.runLater(() -> {
            ObservableList<String> items = adminsListView.getItems();
            items.clear();
            items.addAll(usersNames);
            totalUsers.set(usersNames.size());
        });
    }

    public void startListRefresher() {
        listRefresher = new UserListRefresher(
                autoUpdate,
                httpStatusUpdate::updateHttpLine,
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    @Override
    public void close() {
        adminsListView.getItems().clear();
        totalUsers.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
}