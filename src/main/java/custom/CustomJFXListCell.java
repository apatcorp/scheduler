package custom;

import com.jfoenix.controls.*;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class CustomJFXListCell extends JFXListCell<String> {
    private ImageView imageView;
    private JFXTextField textField;

    public CustomJFXListCell() {
        super();

        imageView = new ImageView("file:img/arrow.png");
        imageView.setCache(true);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        textField = new JFXTextField();
        textField.setFocusColor(Paint.valueOf("#31aba2"));
        textField.setOnAction(event -> commitEdit(textField.getText()));

        setGraphic(imageView);

        setupPopup();
    }

    @Override
    public void startEdit() {
        super.startEdit();

        textField.setText(getItem());
        textField.selectAll();

        setText(null);
        setGraphic(textField);
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);

        setText(newValue);
        setGraphic(imageView);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem());
        setGraphic(imageView);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            setGraphic(imageView);
        }
    }

    private void setupPopup () {
        JFXButton button = new JFXButton("Löschen");
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setRipplerFill(Paint.valueOf("#9a9a9a"));
        button.setPadding(new Insets(10));

        VBox vBox = new VBox(button);

        JFXPopup popup = new JFXPopup();
        popup.setPopupContent(vBox);

        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                if (getListView().getSelectionModel().getSelectedItems().size() > 1)
                    popup.show(getListView(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
                else
                    popup.show(this, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
        });

        button.setOnAction(event -> {
            getListView().getItems().removeAll(getListView().getSelectionModel().getSelectedItems());
            popup.hide();
        });
    }
}
