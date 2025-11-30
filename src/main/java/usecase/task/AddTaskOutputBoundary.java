package usecase.task;

public interface AddTaskOutputBoundary {
    void presentSuccess();
    void presentFailure(String errorMessage);
}
