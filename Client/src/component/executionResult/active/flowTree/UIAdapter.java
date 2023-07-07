package component.executionResult.active.flowTree;

import DTO.FlowExecutedDataDTO;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {
    private final Consumer<FlowExecutedDataDTO> updateFlowResult;
    private final Runnable hideSpinner;

    public UIAdapter(Consumer<FlowExecutedDataDTO> updateFlowResult, Runnable hideSpinner) {
        this.updateFlowResult = updateFlowResult;
        this.hideSpinner = hideSpinner;
    }

    public void update(FlowExecutedDataDTO flowExecutedDataDTO) {
        Platform.runLater(
                () -> {
                    updateFlowResult.accept(flowExecutedDataDTO);
                }
        );
    }

    public void removeSpinner() {
        Platform.runLater(
                hideSpinner
        );
    }
}
