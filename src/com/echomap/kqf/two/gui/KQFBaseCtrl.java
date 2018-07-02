package com.echomap.kqf.two.gui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class KQFBaseCtrl {
	private final static Logger LOGGER = LogManager.getLogger(KQFBaseCtrl.class);

	static public final DateFormat myDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	File lastSelectedDirectory = null;
	Stage primaryStage = null;
	Properties appProps = null;

	void showPopupMessage(final String msg, final boolean error) {
		this.showPopupMessage(null, msg, error);
	}

	void showPopupMessage(final String msg1, final String msg2, final boolean error) {
		final Stage dialog = new Stage();
		dialog.setTitle("KQF Message Dialog");
		dialog.setResizable(true);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setWidth(420);
		dialog.setHeight(280);
		if (primaryStage != null)
			dialog.initOwner(primaryStage);

		final Button closeButton = new Button();
		closeButton.setText("_Close");
		closeButton.setMnemonicParsing(true);
		// closeButton.setStyle("-fx-padding: 8; -fx-margin: 8;");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Node source = (Node) event.getSource();
				final Stage stage = (Stage) source.getScene().getWindow();
				// doCleanup();
				stage.close();
			}
		});
		closeButton.setDefaultButton(true);

		final Label text1;
		if (msg1 != null) {
			text1 = new Label(msg1);
			final StringBuilder cssBorder = new StringBuilder();
			cssBorder.append("-fx-padding: 4 12 4 12;");
			cssBorder.append("-fx-border-style: solid inside;");
			cssBorder.append("-fx-border-width: 2;");
			cssBorder.append("-fx-border-insets: 5;");
			cssBorder.append("-fx-border-radius: 5;");
			cssBorder.append("-fx-border-color: black;");
			text1.setStyle(cssBorder.toString());
			text1.autosize();
		} else
			text1 = null;

		final TextArea text = new TextArea();
		text.appendText(msg2);
		text.setWrapText(true);
		text.setEditable(false);
		text.autosize();
		text.setFocusTraversable(false);
		text.setStyle(
				"-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
		// text.setStyle("-fx-background-color: #EEEEA4;");
		// final Text text = new Text(msg);
		// text.autosize();

		final VBox dialogVbox = new VBox(10);
		// dialogVbox.setAlignment(Pos.CENTER);
		dialogVbox.setStyle(
				"-fx-border-color: #2e8b57;&#10;-fx-border-width: 2px;&#10;-fx-border-insets: 5;&#10;-fx-border-style: solid;&#10; ");
		VBox.setMargin(closeButton, new Insets(4, 8, 8, 4));

		if (text1 != null)
			dialogVbox.getChildren().add(text1);
		dialogVbox.getChildren().add(text);
		dialogVbox.getChildren().add(closeButton);
		dialogVbox.autosize();
		final Scene dialogScene = new Scene(dialogVbox, 300, 75);
		// dialogScene.
		dialog.setScene(dialogScene);
		// dialog.sizeToScene();
		dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(final WindowEvent we) {
				LOGGER.debug("SubStage is cleaning up...");
				LOGGER.debug("SubStage is closing");
				dialog.close();
			}
		});
		closeButton.requestFocus();
		dialog.show();
	}

	protected boolean locateDir(final ActionEvent event, final String title, final TextInputControl textField) {
		return locateDir(event, title, textField, textField);
	}

	protected String locateDir(final ActionEvent event, final String title, final String textFieldToSet,
			final String textFieldDefault) {
		final DirectoryChooser chooser = new DirectoryChooser();

		String textFieldForInitialDir;
		if (StringUtils.isBlank(textFieldToSet))
			textFieldForInitialDir = textFieldDefault;
		else
			textFieldForInitialDir = textFieldToSet;
		final File lastDir1 = new File(textFieldForInitialDir);
		final File lastDir2 = lastSelectedDirectory;
		if (lastDir1 != null && lastDir1.exists()) {
			if (lastDir1.isDirectory())
				chooser.setInitialDirectory(lastDir1);
			else if (lastDir1.getParentFile() != null && lastDir1.getParentFile().exists()
					&& lastDir1.getParentFile().isDirectory())
				chooser.setInitialDirectory(lastDir1.getParentFile());
		} else if (lastDir2 != null && lastDir2.exists()) {
			if (lastDir2.isDirectory())
				chooser.setInitialDirectory(lastDir2);
			else if (lastDir2.getParentFile() != null && lastDir2.getParentFile().exists()
					&& lastDir2.getParentFile().isDirectory())
				chooser.setInitialDirectory(lastDir2.getParentFile());
		}

		// if (textField.getText() != null && textField.getText().length() > 0)
		// chooser.setInitialDirectory(new File(textField.getText()));
		// else
		// chooser.setInitialDirectory(lastSelectedDirectory);
		chooser.setTitle(title);
		final File file = chooser.showDialog(new Stage());
		if (file == null) {
			// textField.setText("");
			// lastSelectedDirectory = null;
			return null;
		} else {
			// textFieldToSet.setText(file.getAbsolutePath());
			lastSelectedDirectory = file;
			return file.getAbsolutePath();
		}
	}

	protected boolean locateDir(final ActionEvent event, final String title, final TextInputControl textFieldToSet,
			final TextInputControl textFieldDefault) {
		final DirectoryChooser chooser = new DirectoryChooser();

		TextInputControl textFieldForInitialDir;
		if (StringUtils.isBlank(textFieldToSet.getText()))
			textFieldForInitialDir = textFieldDefault;
		else
			textFieldForInitialDir = textFieldToSet;
		final File lastDir1 = new File(textFieldForInitialDir.getText());
		final File lastDir2 = lastSelectedDirectory;
		if (lastDir1 != null && lastDir1.exists()) {
			if (lastDir1.isDirectory())
				chooser.setInitialDirectory(lastDir1);
			else if (lastDir1.getParentFile() != null && lastDir1.getParentFile().exists()
					&& lastDir1.getParentFile().isDirectory())
				chooser.setInitialDirectory(lastDir1.getParentFile());
		} else if (lastDir2 != null && lastDir2.exists()) {
			if (lastDir2.isDirectory())
				chooser.setInitialDirectory(lastDir2);
			else if (lastDir2.getParentFile() != null && lastDir2.getParentFile().exists()
					&& lastDir2.getParentFile().isDirectory())
				chooser.setInitialDirectory(lastDir2.getParentFile());
		}

		// if (textField.getText() != null && textField.getText().length() > 0)
		// chooser.setInitialDirectory(new File(textField.getText()));
		// else
		// chooser.setInitialDirectory(lastSelectedDirectory);
		chooser.setTitle(title);
		final File file = chooser.showDialog(new Stage());
		if (file == null) {
			// textField.setText("");
			// lastSelectedDirectory = null;
			return false;
		} else {
			textFieldToSet.setText(file.getAbsolutePath());
			lastSelectedDirectory = file;
			return true;
		}
	}

	protected void locateFile(final ActionEvent event, final String title, final TextField textField) {
		final FileChooser chooser = new FileChooser();
		if (!StringUtils.isEmpty(textField.getText())) {
			// if (textField.getText() != null && textField.getText().length() >
			// 0) {
			lastSelectedDirectory = new File(textField.getText());
			if (!lastSelectedDirectory.isDirectory())
				lastSelectedDirectory = lastSelectedDirectory.getParentFile();
			if (!lastSelectedDirectory.isDirectory())
				lastSelectedDirectory = null;
		} else {
			// lastSelectedDirectory = FormatDao
		}
		chooser.setInitialDirectory(lastSelectedDirectory);
		chooser.setTitle(title);
		// chooser.setInitialFileName("ChapterCount1.csv");
		// System.out.println("lastSelectedDirectory = '" +
		// lastSelectedDirectory + "'");

		final File file = chooser.showOpenDialog(new Stage());
		if (file == null) {
			textField.setText("");
			// lastSelectedDirectory = null;
		} else {
			textField.setText(file.getAbsolutePath());
			// lastSelectedDirectory = file.getParentFile();
		}
	}

	protected void chooseFile(final ActionEvent event, final String title, final TextField textField,
			final String defaultName) {
		final FileChooser chooser = new FileChooser();
		if (!StringUtils.isEmpty(textField.getText())) {
			// if (textField.getText() != null && textField.getText().length() >
			// 0) {
			lastSelectedDirectory = new File(textField.getText());
			if (!lastSelectedDirectory.isDirectory())
				lastSelectedDirectory = lastSelectedDirectory.getParentFile();
			if (!lastSelectedDirectory.isDirectory())
				lastSelectedDirectory = null;
		} else {
			// lastSelectedDirectory = FormatDao
		}
		chooser.setInitialDirectory(lastSelectedDirectory);
		chooser.setTitle(title);
		chooser.setInitialFileName(defaultName);
		chooser.setSelectedExtensionFilter(new ExtensionFilter("JSON", "json"));
		// System.out.println("lastSelectedDirectory = '" +
		// lastSelectedDirectory + "'");

		final File file = chooser.showSaveDialog(new Stage());
		if (file == null) {
			textField.setText("");
			// lastSelectedDirectory = null;
		} else {
			textField.setText(file.getAbsolutePath());
			// lastSelectedDirectory = file.getParentFile();
		}
	}

}
