package ProjectResources;

// OnHandTiles
// Programmer: Prakrit Saetang
// Last Modified: 9/28/16

import ProjectResources.PlayTiles.PlayTile;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * To handle play tiles on player hands
 * @author Prakrit
 */
public class OnHandTiles implements AMathConstants {
    //private static final int MAX_AMOUNT_ON_HAND = 8;
    private ArrayList<PlayTile> onHandPlayTileArray;
    private ArrayList<ImageView> onHandImageViewArray;
    private Pane pane;


    OnHandTiles () {
        onHandPlayTileArray = new ArrayList<>();
        onHandImageViewArray = new ArrayList<>();
        pane = new Pane();

        pane.setPrefWidth(200);
        pane.setPrefHeight(100);
        pane.setPadding(new Insets(30, 0, 0, 0));
    }

    public Pane getPane() {
        return pane;
    }

    public void add(PlayTile playTile) {
        if (onHandPlayTileArray.size() >= MAX_AMOUNT_ON_HAND) {
            return;
        }
        onHandPlayTileArray.add(playTile);

        ImageView temp = new ImageView(playTile.getTileImage());
        temp.setFitHeight(40);
        temp.setFitWidth(40);

        if (onHandImageViewArray.size() != 0) {
            double newX = onHandImageViewArray.get(onHandImageViewArray.size() - 1).getLayoutX() + 41;
            //System.out.println(newX);
            temp.relocate(newX, 50);
            //System.out.println(onHandImageViewArray.size());
        }
        else {
            temp.relocate(25, 50);
            //System.out.println(temp.getLayoutY());
        }


        onHandImageViewArray.add(temp);
        pane.getChildren().add(temp);
        setImageViewListener(onHandImageViewArray.get(onHandImageViewArray.size() - 1),
                onHandPlayTileArray.get(onHandPlayTileArray.size() - 1));
    }

    public void addAll(ArrayList<PlayTile> playTilesList) {
        if ((onHandPlayTileArray.size() + playTilesList.size()) >= MAX_AMOUNT_ON_HAND) {
            return;
        }

        onHandPlayTileArray.addAll(playTilesList);
    }

    public int getTileSelected() {
        int indexSelected = -1;
        for (int i = 0; i < onHandPlayTileArray.size(); i++) {
            if (onHandPlayTileArray.get(i).isUsed() == true)
                return i;
        }
        return indexSelected;
    }

    public void clearSelected() {
        for (int i = 0; i < onHandPlayTileArray.size(); i++) {
            if (onHandPlayTileArray.get(i).isUsed() == true) {
                onHandPlayTileArray.get(i).setUsed(false);
            }
        }
    }


    private void setImageViewListener(ImageView imageView, PlayTile playTile) {


        imageView.setOnMouseClicked(event -> {

            if (getTileSelected() != -1) {
                double tempX = 0;
                double tempY = 0;
                int tempIndex = getTileSelected();
                tempX = imageView.getLayoutX();
                tempY = imageView.getLayoutY();

                imageView.relocate(onHandImageViewArray.get(tempIndex).getLayoutX(),
                        onHandImageViewArray.get(tempIndex).getLayoutY());

                onHandImageViewArray.get(tempIndex).relocate(tempX, tempY);
                onHandImageViewArray.get(tempIndex).setLayoutY(imageView.getLayoutY() + 5);
                playTile.setUsed(false);
                onHandPlayTileArray.get(tempIndex).setUsed(false);
            }
            else {
                imageView.setLayoutY(imageView.getLayoutY() - 5);
                playTile.setUsed(true);
            }
        });


        imageView.setOnMouseEntered(event -> {
            imageView.setLayoutY(imageView.getLayoutY() - 5);
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, white, 10, 0, 0, 0);");
        });

        imageView.setOnMouseExited(event -> {
            imageView.setLayoutY(imageView.getLayoutY() + 5);
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, white, 0, 0, 0, 0);");
        });
    }

    public void updatePane() {

        pane.getChildren().clear();

        for (int i = 0; i < onHandImageViewArray.size(); i++) {

            //double startX = 25, startY = 50;

            if (i != 0) {
                double newX = onHandImageViewArray.get(i - 1).getLayoutX() + 41;
                //System.out.println(newX);
                onHandImageViewArray.get(i).relocate(newX, 50);
                //System.out.println(onHandImageViewArray.size());
            } else {
                onHandImageViewArray.get(i).relocate(25, 50);
                //System.out.println(onHandImageViewArray.get(i).getLayoutY());
            }
        }

        pane.getChildren().addAll(onHandImageViewArray);

    }

    public PlayTile getTile(int index) {
        return onHandPlayTileArray.get(index);
    }

    public void remove(int index) {
        onHandPlayTileArray.remove(index);
        onHandImageViewArray.remove(index);
    }

    public void replace(int index, PlayTile playTile) {
        onHandPlayTileArray.set(index, playTile);
        ImageView imageView = new ImageView(playTile.getTileImage());
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        setImageViewListener(imageView, playTile);
        onHandImageViewArray.set(index, imageView);
    }

    public ArrayList<PlayTile> getOnHandPlayTileArray() {
        return onHandPlayTileArray;
    }

    public ArrayList<ImageView> getOnHandImageViewArray() {
        return onHandImageViewArray;
    }

    public void clearUsed() {
        for (PlayTile playTile : onHandPlayTileArray) {
            playTile.setUsed(false);
        }
    }

}
