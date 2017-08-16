package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Created by Myles on 7/24/17.
 */
public class TableViewPopulator {

    public ObservableList<?> getObjectList(ArrayList data)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i));
        }
        return list;
    }
}
