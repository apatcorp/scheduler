package custom;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.util.List;

public class CustomListCell extends TextFieldListCell<String> {
    private ImageView imageView;

    public CustomListCell(StringConverter<String> converter) {
        super(converter);

        Image image = new Image("file:img/arrow.png");
        imageView = new ImageView(image);
        imageView.setImage(image);
        imageView.setCache(true);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);

        setGraphic(imageView);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Löschen");
        delete.setOnAction(event -> {
            List<String> selectedItems = getListView().getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 0) {
                getListView().getItems().removeAll(selectedItems);
            }
        });
        contextMenu.getItems().add(delete);

        setContextMenu(contextMenu);
        setOnContextMenuRequested(event -> contextMenu.show(this, event.getScreenX(), event.getScreenY()));
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
}
