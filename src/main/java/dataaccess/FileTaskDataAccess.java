//package data_access;
//
//import entity.Task;
//import entity.User;
//import use_case.task.TaskDataAccessInterface;
//import use_case.user.UserDataAccessInterface;
//
//import java.util.List;
//
//public class FileTaskDataAccess implements TaskDataAccessInterface {
//
//    private final UserDataAccessInterface userRepo;
//
//    public FileTaskDataAccess(UserDataAccessInterface userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    @Override
//    public List<Task> loadTasksForUser(String email) {
//        User user = userRepo.getUser(email);
//        return user.getTasks();
//    }
//
//    @Override
//    public void saveTasksForUser(String email, List<Task> tasks) {
//        User user = userRepo.getUser(email);
//        user.setTasks(tasks);
//        userRepo.saveUser(user);
//    }
//}
