package interfaceadapter.task;

import usecase.task.AddTaskOutputBoundary;

public class AddTaskPresenter implements AddTaskOutputBoundary {

    private final AddTaskViewModel viewModel;

    public AddTaskPresenter(AddTaskViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess() {
        // Tell the ViewModel that the add-task attempt succeeded
        viewModel.fireSuccess();
    }

    @Override
    public void presentFailure(String message) {
        // Tell the ViewModel that the add-task attempt failed and why
        viewModel.fireFailure(message);
    }
}
