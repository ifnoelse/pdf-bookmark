package com.ifnoelse.pdf.gui;

import com.ifnoelse.pdf.PDFContents;
import com.ifnoelse.pdf.PDFUtil;
import com.itextpdf.text.exceptions.BadPasswordException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by ifnoelse on 2017/3/2 0002.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("pdf bookmark");

        BorderPane bottomPane = new BorderPane();
        Button contentsGenerator = new Button("Build directory");
        Button getContents = new Button("Get directory");

        getContents.setDisable(true);
        HBox h = new HBox(20, getContents, contentsGenerator);

        h.setAlignment(Pos.CENTER);

        bottomPane.setCenter(h);

        Button fileSelectorBtn = new Button("Select the file");


        BorderPane vBox = new BorderPane();
        TextField filePath = new TextField();

        filePath.setEditable(false);
        filePath.setPromptText("Please select a PDF file");

        BorderPane topPane = new BorderPane();
        topPane.setCenter(filePath);


        TextField pageIndexOffset = new TextField();
        topPane.setRight(new HBox(pageIndexOffset, fileSelectorBtn));
        vBox.setTop(topPane);

        pageIndexOffset.setPromptText("Page offset");
        pageIndexOffset.setPrefWidth(100);


        TextArea textArea = new TextArea();


        textArea.setPromptText("Please fill in the contents of the directory here.");

        textArea.setOnDragEntered(e -> {
            Dragboard dragboard = e.getDragboard();
            File file = dragboard.getFiles().get(0); // Get the dragged file
            String fileName = file.getName();
            if (fileName.matches("[\\s\\S]+.[pP][dD][fF]$")) {
                filePath.setText(file.getPath());
            }
        });


        textArea.textProperty().addListener(event -> {
            if (textArea.getText().trim().startsWith("http")) {
                getContents.setDisable(false);
            } else {
                getContents.setDisable(true);
            }
        });

        vBox.setCenter(textArea);


        vBox.setBottom(bottomPane);
        Scene scene = new Scene(vBox, 600, 400);
        primaryStage.setScene(scene);

        fileSelectorBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", "*.pdf"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                filePath.setText(file.getPath());
            }


        });


        pageIndexOffset.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!observable.getValue()) {
                String offset = pageIndexOffset.getText();
                if (offset != null && offset.length() > 0 && !offset.matches("[0-9]+")) {
                    showDialog("error", "Offset setting error", "Page offset can only be an integer", Alert.AlertType.ERROR);
                }

            }
        });

        getContents.setOnAction(event -> {
            String contents = PDFContents.getContentsByUrl(textArea.getText());
            textArea.setText(contents);
        });

        contentsGenerator.setOnAction(event -> {
            String fp = filePath.getText();
            if (fp == null || fp.isEmpty()) {
                showDialog("error", "The pdf file path is empty", "Pdf file path cannot be empty，Please select a pdf file", Alert.AlertType.ERROR);
                return;
            }
            String srcFile = fp.replaceAll("\\\\", "/");
            String srcFileName = srcFile.substring(srcFile.lastIndexOf("/") + 1);
            String ext = srcFileName.substring(srcFileName.lastIndexOf("."));
            String destFile = srcFile.substring(0, srcFile.lastIndexOf(srcFileName)) + srcFileName.substring(0, srcFileName.lastIndexOf(".")) + "_including directory" + ext;

            String offset = pageIndexOffset.getText();
            String content = textArea.getText();
            if (content != null && !content.isEmpty()) {
                try {
                    PDFUtil.addBookmark(textArea.getText(), srcFile, destFile, Integer.parseInt(offset != null && !offset.isEmpty() ? offset : "0"));
                } catch (Exception e) {
                    String errInfo = e.toString();
                    if (e.getCause().getClass() == BadPasswordException.class) {
                        errInfo = "PDF is encrypted and cannot be modified";
                    }
                    showDialog("error", "Add directory error", errInfo, Alert.AlertType.INFORMATION);
                    return;
                }
                showDialog("Notice", "Add directory successfully！", "File is stored in" + destFile, Alert.AlertType.INFORMATION);
            } else {
                showDialog("error", "Directory content is empty", "The directory can not be empty, please fill in the pdf book directory url or fill in the directory text", Alert.AlertType.ERROR);
            }


        });
        primaryStage.show();
    }

    private void showDialog(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }
}
