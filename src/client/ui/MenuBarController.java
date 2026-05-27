package client.ui;

import client.draw.CanvasManager;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MenuBarController {
    private final MenuBar menuBar;
    private final CanvasManager canvasManager;

    public MenuBarController(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
        this.menuBar = new MenuBar();
        setupMenu();
    }

    public MenuBar getMenuBar() { return menuBar; }

    private void setupMenu() {
        // --- FILE MENU ---
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Local PNG...");
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(e -> canvasManager.saveCanvasToDisk());

        MenuItem openItem = new MenuItem("Open Local PNG...");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(e -> canvasManager.loadCanvasFromDisk());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(saveItem, openItem, new SeparatorMenuItem(), exitItem);

        // --- EDIT MENU ---
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoItem.setOnAction(e -> canvasManager.undo());

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redoItem.setOnAction(e -> canvasManager.redo());

        editMenu.getItems().addAll(undoItem, redoItem);

        // --- VIEW MENU (NEW) ---
        Menu viewMenu = new Menu("View");
        CheckMenuItem gridItem = new CheckMenuItem("Show Dot Grid");
        gridItem.setOnAction(e -> canvasManager.setGridVisible(gridItem.isSelected()));

        viewMenu.getItems().add(gridItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);
    }
}