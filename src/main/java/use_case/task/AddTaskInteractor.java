//package use_case.task;
//
//import entity.Task;
//
//public class AddTaskInteractor implements AddTaskInputBoundary {
//
//    private final TaskDataAccessInterface taskRepo;
//    private final AddTaskOutputBoundary presenter;
//    private static int nextId = 1;
//
//    public AddTaskInteractor(TaskDataAccessInterface taskRepo,
//                             AddTaskOutputBoundary presenter) {
//        this.taskRepo = taskRepo;
//        this.presenter = presenter;
//    }
//
//    @Override
//    public void add(AddTaskInputData inputData) {
//
//        if (inputData.getTitle().trim().isEmpty()) {
//            presenter.presentFailure("Title cannot be empty.");
//            return;
//        }
//
//        if (inputData.getDate() == null) {
//            presenter.presentFailure("Date must not be empty.");
//            return;
//        }
//
//        Task task = new Task(
//                nextId++,
//                inputData.getTitle(),
//                inputData.getDescription(),
//                inputData.getDate(),
//                inputData.getType()
//        );
//
//        taskRepo.saveTask(task);
//
//        presenter.presentSuccess();
//    }
//}
