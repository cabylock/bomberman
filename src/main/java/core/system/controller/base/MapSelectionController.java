package core.system.controller.base;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import core.system.game.GameControl;
import core.system.setting.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;


public class MapSelectionController {
    @FXML
    private ListView<String> defaultMapList;
    @FXML
    private ListView<String> customMapList;
    private Stage stage;
    private static final String DEFAULT_MAPS_DIR = "src/main/resources/default_levels";
    private static final String CUSTOM_MAPS_DIR = "src/main/resources/custom_levels";

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadMaps() {
        defaultMapList.getItems().clear();
        customMapList.getItems().clear();

        // Load maps from directories
        loadMapsFromDirectory(DEFAULT_MAPS_DIR, defaultMapList);
        loadMapsFromDirectory(CUSTOM_MAPS_DIR, customMapList);

        // Set up selection listeners
        defaultMapList.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                customMapList.getSelectionModel().clearSelection();
            }
        });

        customMapList.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                defaultMapList.getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Helper method to load maps from a directory into a ListView
     * 
     * @param directoryPath Path to map directory
     * @param listView      ListView to populate with map names
     */
    private void loadMapsFromDirectory(String directoryPath, ListView<String> listView) {
        File mapsDir = new File(directoryPath);
        if (mapsDir.exists() && mapsDir.isDirectory()) {
            File[] mapFiles = mapsDir.listFiles((_, name) -> name.endsWith(".txt"));
            if (mapFiles != null) {
                // Sort the files alphabetically
                Arrays.sort(mapFiles, Comparator.comparing(File::getName));

                for (File mapFile : mapFiles) {
                    listView.getItems().add(mapFile.getName().replace(".txt", ""));
                }
            }
        } else {
            System.out.println("Warning: Directory not found: " + directoryPath);
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource() == defaultMapList) {
                String selectedMap = defaultMapList.getSelectionModel().getSelectedItem();
                if (selectedMap != null) {
                    Setting.MAP_TYPE = Setting.DEFAULT_MAP;
                    startGameWithMap(selectedMap);
                }
            } else if (event.getSource() == customMapList) {
                String selectedMap = customMapList.getSelectionModel().getSelectedItem();
                if (selectedMap != null) {
                    Setting.MAP_TYPE = Setting.CUSTOM_MAP;
                    startGameWithMap(selectedMap);
                }
            }
        }
    }

    @FXML
    private void createRandomMap() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/RandomMap.fxml"));
            Parent root = loader.load();

            RandomMapController controller = loader.getController();
            controller.setStage(stage);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Random Map");
            dialogStage.initOwner(stage);

            // Create the scene first with explicit dimensions
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Force minimum size on the stage
            dialogStage.setMinWidth(500);
            dialogStage.setMinHeight(400);

            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            loadMaps();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void playSelectedMap() {

        String selectedDefaultMap = defaultMapList.getSelectionModel().getSelectedItem();
        String selectedCustomMap = customMapList.getSelectionModel().getSelectedItem();

        if (selectedDefaultMap != null) {
            Setting.MAP_TYPE = Setting.DEFAULT_MAP;
            startGameWithMap(selectedDefaultMap);
        } else if (selectedCustomMap != null) {
            Setting.MAP_TYPE = Setting.CUSTOM_MAP;
            startGameWithMap(selectedCustomMap);
        } else {
            showAlert("No Map Selected", "Please select a map to play.", null);
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root, Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSelectedMap() {
        String selectedDefaultMap = defaultMapList.getSelectionModel().getSelectedItem();
        String selectedCustomMap = customMapList.getSelectionModel().getSelectedItem();

        if (selectedDefaultMap != null) {
            showAlert("Cannot Delete Default Map",
                    "Default maps cannot be deleted.",
                    "Please select a custom map if you wish to delete it.");
        } else if (selectedCustomMap != null) {
            boolean confirmed = showConfirmation("Delete Map",
                    "Are you sure you want to delete " + selectedCustomMap + "?",
                    "This action cannot be undone.");
            if (confirmed) {
                deleteMap(selectedCustomMap);
                loadMaps(); // Reload maps to refresh the list
            }
        } else {
            showAlert("No Map Selected", "Please select a map to delete.", null);
        }
    }

    @FXML
    private void joinOnlineGame() {
        try {
            // Ensure client mode is set before opening the network setup
            Setting.GAME_MODE = Setting.CLIENT_MODE;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/NetworkSetup.fxml"));
            Parent root = loader.load();

            NetworkSetupController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGameWithMap(String mapName) {
        try {
            if (mapName != null && !mapName.isEmpty()) {
            } else {
                System.err.println("Error: No map selected");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/Mode.fxml"));
            Parent root = loader.load();

            ModeController modeController = loader.getController();
            modeController.setStage(stage);
            Setting.MAP_NAME = mapName;

            Scene scene = new Scene(root, Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteMap(String mapName) {
        try {
            String dirPath = "src/main/resources/custom_levels";
            File mapFile = new File(dirPath, mapName + ".txt");
            if (mapFile.exists()) {
                boolean deleted = mapFile.delete();
                if (!deleted) {
                    showAlert("Deletion Failed",
                            "Could not delete file: " + mapName,
                            "The file may be in use or protected.");
                }
            } else {
                showAlert("File Not Found",
                        "Could not find file: " + mapName,
                        "The file may have been moved or deleted already.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error",
                    "An error occurred while deleting the map.",
                    e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
