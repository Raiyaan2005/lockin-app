//package data_access;
//
//import entity.User;
//import com.google.gson.Gson;
//import use_case.user.UserDataAccessInterface;
//
//import java.io.*;
//
//public class FileUserDataAccess implements UserDataAccessInterface {
//
//    private final File userFolder = new File("data/users/");
//    private final Gson gson = new Gson();
//
//    public FileUserDataAccess() {
//        if (!userFolder.exists()) {
//            userFolder.mkdirs();
//        }
//    }
//
//    @Override
//    public boolean userExists(String email) {
//        return new File(userFolder, email + ".json").exists();
//    }
//
//    @Override
//    public User getUser(String email) {
//        File f = new File(userFolder, email + ".json");
//        if (!f.exists()) return null;
//
//        try (Reader reader = new FileReader(f)) {
//            return gson.fromJson(reader, User.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void saveUser(User user) {
//        File f = new File(userFolder, user.getEmail() + ".json");
//
//        try (Writer writer = new FileWriter(f)) {
//            gson.toJson(user, writer);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
