package Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by mandre3 on 8/24/16.
 */
public class CreateDialog {

    private String fxmlFilename;
    private String title;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public CreateDialog(String fxmlFilename, String title) {
        this.fxmlFilename = fxmlFilename;
    }

    public CreateDialog() {
    }

    public String getFxmlFilename() {
        return fxmlFilename;
    }

    public void setFxmlFilename(String fxmlFilename) {
        this.fxmlFilename = fxmlFilename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void showDialog() throws IOException {
        if (fxmlFilename != null)
        {
            Stage stage = new Stage();
            Parent root;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(this.fxmlFilename));
            loader.setClassLoader(getClass().getClassLoader());
            root = loader.load();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.showAndWait();

        }
    }

    public void showDialog(String fxmlFilename, String title) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFilename));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilename));

        /*loader.setLocation(getClass().getResource(fxmlFilename));
        loader.setClassLoader(getClass().getClassLoader());*/
        //root = loader.load();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        setStage(stage);
        stage.showAndWait();
        }

}
