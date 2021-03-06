package episim.view.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import jfxtras.styles.jmetro.MDL2IconFont;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Composant contenant le bouton de sélection d'un modèle avec l'option d'édition et de suppression
 */
public class ModelSelect implements Initializable {
    /**
     * Crée un {@code ModelSelect}
     * @return retourne le {@code FXMLLoader}
     * @throws IOException Génère une erreur si la ressource est inaccessible
     */
    public static FXMLLoader load() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ModelComp.class.getResource("/episim/view/component/ModelSelect.fxml"));
        fxmlLoader.load();
        return fxmlLoader;
    }

    @FXML
    private ToggleButton toggle;

    @FXML
    private Button editBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private BorderPane editPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MDL2IconFont editIcon = new MDL2IconFont("\uE70F");
        editBtn.setGraphic(editIcon);
        MDL2IconFont removeIcon = new MDL2IconFont("\uE74D");
        removeBtn.setGraphic(removeIcon);

        toggle.selectedProperty().addListener((observable, oldValue, newValue) -> displayEdit(newValue));
        displayEdit(toggle.isSelected());
    }

    /**
     * Propriété de l'état sélectionné de ce {@code ModelSelect}
     * @return La propriété de l'état sélectionné
     */
    public BooleanProperty selectedProperty() {
        return toggle.selectedProperty();
    }

    /**
     * Définie le groupe du {@code ToggleButton}
     * @param group Le {@code ToggleGroup}
     */
    public void setToggleGroup(ToggleGroup group) {
        toggle.setToggleGroup(group);
    }

    /**
     * Retourne le {@code Toggle} sous-jacent
     * @return Le {@code Toggle} sous-jacent
     */
    public Toggle getToggle() {
        return toggle;
    }

    /**
     * Définie le nom du modèle
     * @param name Le nom du modèle
     */
    public void setName(String name) {
        toggle.setText(name);
    }

    /**
     * Définit si le boutton d'édition doit être affiché ou non
     * @param visible Visibilité du boutton
     */
    public void setEditButtonVisible(boolean visible) {
        editBtn.setVisible(visible);
    }

    /**
     * Définit si le boutton de suppression doit être affiché ou non
     * @param visible Visibilité du boutton
     */
    public void setRemoveButtonVisible(boolean visible) {
        removeBtn.setVisible(visible);
    }

    /**
     * L'action du bouton d'édition qui est invoquée lorsque le bouton est activé.
     * @return La propriété d'action du bouton d'édition.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onEditActionProperty() {
        return editBtn.onActionProperty();
    }

    /**
     * L'action du bouton de suppression qui est invoquée lorsque le bouton est activé.
     * @return La propriété d'action du bouton de suppression.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onRemoveActionProperty() {
        return removeBtn.onActionProperty();
    }

    private void displayEdit(boolean visible) {
        if(visible) {
            editPane.setVisible(true);
            editPane.setPrefWidth(BorderPane.USE_COMPUTED_SIZE);
        } else {
            editPane.setVisible(false);
            editPane.setPrefWidth(0);
        }
    }
}
