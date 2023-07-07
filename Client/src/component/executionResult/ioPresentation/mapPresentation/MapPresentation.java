package component.executionResult.ioPresentation.mapPresentation;

import dd.impl.mapping.MappingData;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;

public class MapPresentation<K, V> extends VBox {
    private final TextArea textArea;

    public MapPresentation(MappingData data) {
        textArea = createTextArea(mapToString(data.get()));

        VBox.setVgrow(textArea, Priority.ALWAYS);

        getChildren().addAll(textArea);

        setFillWidth(true); // Enable filling the available width
    }

    private TextArea createTextArea(String textInput) {
        TextArea textArea = new TextArea(textInput);
        textArea.setWrapText(true);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);
        textArea.setEditable(false);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);

        Text text = new Text();
        text.setFont(textArea.getFont());
        text.setText(textInput);
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        double lineHeight = text.getLineSpacing();
        int prefRowCount = (int) Math.ceil((textHeight + lineHeight) / textArea.getFont().getSize());
        textArea.setPrefRowCount(prefRowCount);

        return textArea;
    }

    private String mapToString(Map<K, V> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public TextArea getTextArea() {
        return this.textArea;
    }
}