package episim.view;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import episim.core.CompartmentConfig;
import episim.core.ModelConfig;
import episim.core.SimulationConfig;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Modèle de la vue de l'accueil
 */
public class HomeViewModel implements ViewModel {
    static class ModelCompProperty {
        private final DoubleProperty value = new SimpleDoubleProperty();
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty color = new SimpleStringProperty();
        public ModelCompProperty(double value, String name, String color) {
            this.value.set(value);
            this.name.set(name);
            this.color.set(color);
        }
        public DoubleProperty value() {
            return value;
        }
        public StringProperty color() {
            return color;
        }
        public StringProperty name() {
            return name;
        }
    }

    @InjectScope
    private ConfigurationScope configScope;

    private final ObservableList<String> models = FXCollections.observableArrayList();
    private final IntegerProperty selectedModelId = new SimpleIntegerProperty();
    private final ObservableList<ModelCompProperty> modelComps = FXCollections.observableArrayList();
    private final DoubleProperty modelBirth = new SimpleDoubleProperty();
    private final DoubleProperty modelDeath = new SimpleDoubleProperty();
    private final DoubleProperty popSize = new SimpleDoubleProperty();
    private final DoubleProperty infecPct = new SimpleDoubleProperty();

    public void initialize() {
        selectedModelId.addListener((observable, oldValue, newValue) -> {
            loadModelConfig(selectedModelId.get());
        });

        loadConfig();
        bindConfig();
    }

    public void startSimulation() {
        configScope.publish(ConfigurationScope.SIMULATION);
    }

    public ObservableList<String> modelsProperty() {
        return models;
    }
    public IntegerProperty selectedModelId() {
        return selectedModelId;
    }
    public ObservableList<ModelCompProperty> modelCompsProperty() {
        return modelComps;
    }
    public DoubleProperty modelBirthProperty() {
        return modelBirth;
    }
    public DoubleProperty modelDeathProperty() {
        return modelDeath;
    }
    public DoubleProperty popSizeProperty() {
        return popSize;
    }
    public DoubleProperty infecPctProperty() {
        return infecPct;
    }

    private ModelConfig getModelConfig(int modelId) {
        return configScope.getSimulationConfig().getModels().get(modelId);
    }
    private CompartmentConfig getCompConfig(int modelId, int compId) {
        return getModelConfig(modelId).getCompartments().get(compId);
    }

    /**
     * Connecte les propriétés de {@code HomeViewModel} au attributs de {@code SimulationConfig}
     */
    private void bindConfig() {
        bindModels();
        popSize.addListener((observable, oldValue, newValue) -> {
            configScope.getSimulationConfig().setPopulationSize(newValue.intValue());
        });
        infecPct.addListener((observable, oldValue, newValue) -> {
            configScope.getSimulationConfig().setInitialInfectious(newValue.doubleValue() / 100.0);
        });
    }
    private void bindModels() {
        modelComps.addListener((ListChangeListener.Change<? extends ModelCompProperty> c) -> {
            bindModelComps();
        });
        modelBirth.addListener((observable, oldValue, newValue) -> {
            getModelConfig(selectedModelId.get()).setBirth(newValue.doubleValue());
        });
        modelDeath.addListener((observable, oldValue, newValue) -> {
            getModelConfig(selectedModelId.get()).setDeath(newValue.doubleValue());
        });
    }
    private void bindModelComps() {
        for(int i = 0; i < modelComps.size(); i++) {
            var comp = modelComps.get(i);
            int compId = i;
            comp.value().addListener((observable, oldValue, newValue) -> {
                getCompConfig(selectedModelId.get(), compId).setParam(newValue.doubleValue());
            });
            comp.name().addListener((observable, oldValue, newValue) -> {
                getCompConfig(selectedModelId.get(), compId).setName(newValue);
            });
            comp.color().addListener((observable, oldValue, newValue) -> {
                getCompConfig(selectedModelId.get(), compId).setColor(newValue);
            });
        }
    }

    /**
     * Charge les attributs de {@code SimulationConfig} dans {@code HomeViewModel}
     */
    private void loadConfig() {
        var config = configScope.getSimulationConfig();
        models.clear();
        for(var model : config.getModels()) {
            models.add(model.getName());
        }
        loadModelConfig(selectedModelId.get());
        popSize.set(config.getPopulationSize());
        infecPct.set(config.getInitialInfectious() * 100.0);
    }
    private void loadModelConfig(int modelId) {
        var config = configScope.getSimulationConfig();
        var selectedModel = config.getModels().get(modelId);
        modelComps.clear();
        for(var comp : selectedModel.getCompartments()) {
            modelComps.add(new ModelCompProperty(comp.getParam(), comp.getName(), comp.getColor()));
        }
        modelBirth.set(selectedModel.getBirth());
        modelDeath.set(selectedModel.getDeath());
    }
}
