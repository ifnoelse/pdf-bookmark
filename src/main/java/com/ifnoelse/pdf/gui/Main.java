package com.ifnoelse.pdf.gui;

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

/**
 * Created by ifnoelse on 2017/3/2 0002.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("test");
        BorderPane hBox = new BorderPane();
        hBox.setCenter(new Button("生成目录"));
        Button button =  new Button("选择文件");
        FileChooser fileChooser = new FileChooser();

        TextArea textArea = new TextArea();

        BorderPane vBox = new BorderPane();
        TextField filePath = new TextField();

        filePath.setEditable(false);
        BorderPane top = new BorderPane();
        top.setCenter(filePath);
        TextField offset = new TextField();

        offset.setPromptText("页码偏移量");
        offset.setPrefWidth(100);
        top.setRight(new HBox(offset,button));
        vBox.setTop(top);
        textArea.setPromptText("请在此填入目录内容");
        vBox.setCenter(textArea);
        vBox.setBottom(hBox);
        Scene scene = new Scene(vBox,800,600);
        primaryStage.setScene(scene);



        button.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(null);
            System.out.println(file);
            System.out.println(event);
            filePath.setText(file.getPath());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("生成的文件存储在源文件目录下，文件名为：");
            alert.setTitle("通知");
            alert.setHeaderText("添加目录成功！");
            alert.show();



        });
        primaryStage.show();
    }
}
