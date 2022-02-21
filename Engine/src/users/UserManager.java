package users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<String> usersAndJobSet;
    private final Set<String> userNamesWithoutJob;

    public UserManager() {

        usersAndJobSet = new HashSet<>();
        userNamesWithoutJob = new HashSet<>();
    }

    public synchronized void addUser(String usernameAndJob) {

        usersAndJobSet.add(usernameAndJob);

        String onlyUserName = this.getOnlyUserNameFrom(usernameAndJob);

        userNamesWithoutJob.add(onlyUserName);
    }

    private String getOnlyUserNameFrom(String usernameAndJob) {

        // Can only be -->  worker || admin
        String res = usernameAndJob.replace("Worker", "");
        res = res.replace("Admin", "");

        return res;
    }

    public synchronized void removeUser(String username) {
        usersAndJobSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersAndJobSet);
    }

    public boolean isUserExists(String usernameAndJob) {

        String onlyUserName = this.getOnlyUserNameFrom(usernameAndJob);

        return userNamesWithoutJob.contains(onlyUserName);
    }


}
