package ProjectResources;

import ProjectResources.PlayTiles.PlayTile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

// ChangeTile
// Programmer: Prakrit Saetang
// Date created: 10/27/16

/**
 * ChangeTile Class - to handle changing tiles event
 * @author Prakrit
 */
public class ChangeTile {

    private boolean isTileChanged = false;

    /**
     * Get isTileChanged to indicate if a player changed tiles
     * @return boolean - isTileChanged
     */
    public boolean isTileChanged() {
        return isTileChanged;
    }

    /**
     * Display a dialog for changing tiles
     * @param onHandTiles
     * @param bag
     */
    public void display(OnHandTiles onHandTiles, Bag bag) {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        HBox tilesPane = new HBox();

        // Get Play Tiles from onHandTiles
        // Set Action Listener to the selected tile
        for (PlayTile playTile : onHandTiles.getOnHandPlayTileArray()) {
            ImageView imageView = new ImageView(playTile.getTileImage());
            imageView.setOnMouseClicked(event -> {

                if (playTile.isUsed()) {
                    imageView.setOpacity(1);
                    playTile.setUsed(false);
                }
                else {
                    imageView.setOpacity(0.5);
                    playTile.setUsed(true);
                }
            });
            tilesPane.getChildren().add(imageView);
        }

        // Set up pane
        VBox mainPane = new VBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setSpacing(10);
        mainPane.setPadding(new Insets(10,10,10,10));

        // Submit Button
        Button btSubmit = new Button("Submit");
        btSubmit.setPrefWidth(200);

        mainPane.getChildren().addAll(tilesPane, btSubmit);

        btSubmit.setOnAction(event -> {

            // Confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Your turn will be passed after you change the tile(s)\nWould you like to change the tile(s)?");

            Optional<ButtonType> result = alert.showAndWait();

            // user chose OK
            if (result.get() == ButtonType.OK){

                ArrayList<Integer> selectedIndex = new ArrayList<Integer>();
                for (int i = 0; i < onHandTiles.getOnHandPlayTileArray().size(); i++) {
                    if (onHandTiles.getTile(i).isUsed())
                        selectedIndex.add(i);
                }

                for (int i = 0; i < selectedIndex.size(); i++) {

                    PlayTile playTile = onHandTiles.getTile(i);
                    bag.returnTiles(playTile);

                    playTile = bag.draw();
                    onHandTiles.replace(selectedIndex.get(i), playTile);
                }

                isTileChanged = true;
            } else {
                // user chose CANCEL or closed the dialog
            }

            onHandTiles.clearUsed();
            onHandTiles.updatePane();
            stage.close();
        });

        // Clear used flags when the dialog closed
        stage.setOnCloseRequest(event -> {
            onHandTiles.clearUsed();
        });


        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
        stage.setTitle("Change Tiles");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
}
