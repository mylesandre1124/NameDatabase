package Objects;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/**
 * Created by Myles on 10/15/16.
 */
public class FileProperty {

    private File file;
    Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public FileProperty(Stage stage) {
        this.stage = stage;
    }

    public FileProperty() {
    }

    public void open(String extensionTitle, String extension, String title)
    {
        ObjectInputStream in;
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionTitle, extension));
        open.setTitle(title);
        file = open.showOpenDialog(getStage());
    }

    public ObjectOutputStream save(String extensionTitle, String extension, String title)
    {
        ObjectOutputStream out;
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionTitle, extension));
        choose.setTitle(title);
        try {
            file = choose.showSaveDialog(getStage());
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            return out;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File saveFile(String extensionTitle, String extension, String title, String emrgFile, Stage primary)
    {
        File file;
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionTitle, extension));
        choose.setTitle(title);
        try {
            file = choose.showSaveDialog(primary);
            return file;
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
            file = new File(emrgFile);
            return file;
        }
    }

    public ObjectInputStream open(File file)
    {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            return in;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObjectOutputStream save(File file)
    {
        try {
            ObjectOutputStream out;
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            return out;
        }
        catch (NullPointerException ex)
        {
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean fileIsEmpty()
    {
        if(file.length() == 0)
        {
            return true;
        }
        else if(file.length() > 0)
        {
            return false;
        }
        else
        {
            return false;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
