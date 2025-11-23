package com.github.skills.filebrowser;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A simple JavaFX file browsing application.
 * Allows users to navigate through directories and view files.
 */
public class FileBrowserApp extends Application {

    private TreeView<String> treeView;
    private TextArea fileInfoArea;
    private Label statusLabel;
    private File currentRoot;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX File Browser");

        // Create the main layout
        BorderPane root = new BorderPane();

        // Create top toolbar
        HBox toolbar = createToolbar(primaryStage);
        root.setTop(toolbar);

        // Create tree view for file browsing
        treeView = new TreeView<>();
        treeView.setShowRoot(true);
        treeView.setPrefWidth(300);
        
        // Add selection listener to show file info
        treeView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayFileInfo(newValue)
        );

        // Create text area for file information
        fileInfoArea = new TextArea();
        fileInfoArea.setEditable(false);
        fileInfoArea.setWrapText(true);
        fileInfoArea.setPrefWidth(400);

        // Create split pane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(treeView, fileInfoArea);
        splitPane.setDividerPositions(0.4);
        root.setCenter(splitPane);

        // Create status bar
        statusLabel = new Label("Ready. Select a directory to browse.");
        statusLabel.setPadding(new Insets(5));
        statusLabel.setStyle("-fx-background-color: #f0f0f0;");
        root.setBottom(statusLabel);

        // Set initial directory to user home
        File homeDir = new File(System.getProperty("user.home"));
        loadDirectory(homeDir);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the toolbar with browsing controls
     */
    private HBox createToolbar(Stage stage) {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #e0e0e0;");

        Button browseButton = new Button("Browse Directory...");
        browseButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Directory");
            if (currentRoot != null && currentRoot.exists()) {
                directoryChooser.setInitialDirectory(currentRoot);
            }
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                loadDirectory(selectedDirectory);
            }
        });

        Button homeButton = new Button("Home Directory");
        homeButton.setOnAction(e -> {
            File homeDir = new File(System.getProperty("user.home"));
            loadDirectory(homeDir);
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> {
            if (currentRoot != null) {
                loadDirectory(currentRoot);
            }
        });

        toolbar.getChildren().addAll(browseButton, homeButton, refreshButton);
        return toolbar;
    }

    /**
     * Loads a directory into the tree view
     */
    private void loadDirectory(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            statusLabel.setText("Error: Invalid directory");
            return;
        }

        currentRoot = directory;
        TreeItem<String> rootItem = new TreeItem<>(directory.getAbsolutePath());
        rootItem.setExpanded(true);

        try {
            loadDirectoryContents(rootItem, directory);
            treeView.setRoot(rootItem);
            statusLabel.setText("Loaded: " + directory.getAbsolutePath());
        } catch (Exception e) {
            statusLabel.setText("Error loading directory: " + e.getMessage());
        }
    }

    /**
     * Recursively loads directory contents
     */
    private void loadDirectoryContents(TreeItem<String> parent, File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            // Skip hidden files
            if (file.isHidden()) {
                continue;
            }

            TreeItem<String> item = new TreeItem<>(file.getName());
            parent.getChildren().add(item);

            // If it's a directory, add a placeholder to show expand icon
            if (file.isDirectory()) {
                item.getChildren().add(new TreeItem<>("Loading..."));
                
                // Lazy load children when expanded
                item.expandedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue && item.getChildren().size() == 1 
                            && item.getChildren().get(0).getValue().equals("Loading...")) {
                        item.getChildren().clear();
                        loadDirectoryContents(item, file);
                    }
                });
            }
        }
    }

    /**
     * Displays information about the selected file
     */
    private void displayFileInfo(TreeItem<String> item) {
        if (item == null) {
            fileInfoArea.clear();
            return;
        }

        // Build full path
        StringBuilder pathBuilder = new StringBuilder();
        TreeItem<String> current = item;
        while (current != null) {
            if (pathBuilder.length() > 0) {
                pathBuilder.insert(0, File.separator);
            }
            pathBuilder.insert(0, current.getValue());
            current = current.getParent();
        }

        String fullPath = pathBuilder.toString();
        File file = new File(fullPath);

        if (!file.exists()) {
            fileInfoArea.setText("File does not exist: " + fullPath);
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(file.getName()).append("\n");
        info.append("Path: ").append(file.getAbsolutePath()).append("\n");
        info.append("Type: ").append(file.isDirectory() ? "Directory" : "File").append("\n");
        
        if (file.isFile()) {
            info.append("Size: ").append(formatFileSize(file.length())).append("\n");
        }
        
        info.append("Readable: ").append(file.canRead()).append("\n");
        info.append("Writable: ").append(file.canWrite()).append("\n");
        info.append("Executable: ").append(file.canExecute()).append("\n");
        info.append("Last Modified: ").append(new java.util.Date(file.lastModified())).append("\n");

        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            int fileCount = 0;
            int dirCount = 0;
            if (contents != null) {
                for (File f : contents) {
                    if (f.isDirectory()) {
                        dirCount++;
                    } else {
                        fileCount++;
                    }
                }
            }
            info.append("\nContents:\n");
            info.append("  Directories: ").append(dirCount).append("\n");
            info.append("  Files: ").append(fileCount).append("\n");
        }

        fileInfoArea.setText(info.toString());
    }

    /**
     * Formats file size in human-readable format
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
