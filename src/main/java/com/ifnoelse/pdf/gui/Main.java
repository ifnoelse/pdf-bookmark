package com.ifnoelse.pdf.gui;

import com.ifnoelse.pdf.PDFUtil;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import java.util.OptionalInt;

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
        Button contentsGenerator = new Button("生成目录");
        bottomPane.setCenter(contentsGenerator);
        Button fileSelectorBtn = new Button("选择文件");


        BorderPane vBox = new BorderPane();
        TextField filePath = new TextField();

        filePath.setEditable(false);
        BorderPane topPane = new BorderPane();
        topPane.setCenter(filePath);


        TextField pageIndexOffset = new TextField();
        topPane.setRight(new HBox(pageIndexOffset, fileSelectorBtn));
        vBox.setTop(topPane);

        pageIndexOffset.setPromptText("页码偏移量");
        pageIndexOffset.setPrefWidth(100);


        TextArea textArea = new TextArea();
        textArea.setPromptText("请在此填入目录内容");
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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("页码偏移量只能为整数");
                    alert.setTitle("错误");
                    alert.setHeaderText("偏移量设置错误");
                    alert.show();
                }

            }
        });

        contentsGenerator.setOnAction(event -> {
            String srcFile = filePath.getText().replaceAll("\\\\", "/");
            String srcFileName = srcFile.substring(srcFile.lastIndexOf("/") + 1);
            String ext = srcFileName.substring(srcFileName.lastIndexOf("."));
            String destFile = srcFile.substring(0, srcFile.lastIndexOf(srcFileName)) + srcFileName.substring(0, srcFileName.lastIndexOf(".")) + "_含目录" + ext;

            String offset = pageIndexOffset.getText();
            PDFUtil.addBookmark(textArea.getText(), srcFile, destFile, Integer.parseInt(offset != null && !offset.isEmpty() ? offset : "0"));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setContentText("文件存储在" + destFile);
            alert.setTitle("通知");
            alert.setHeaderText("添加目录成功！");
            alert.show();

        });
        primaryStage.show();
    }
}
