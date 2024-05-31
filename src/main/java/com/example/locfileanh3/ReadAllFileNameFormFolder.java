package com.example.locfileanh3;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReadAllFileNameFormFolder implements Initializable {
    Stage stage;
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private String fileReadFormFolder = "";

    @FXML
    private TextField textFieldFileReadFolder;


    @FXML
    private TextArea textAreaFileNameShowAll;

    @FXML
    protected void onSelectFileReadFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if(!textFieldFileReadFolder.getText().trim().isEmpty()){
            File f = new File(textFieldFileReadFolder.getText().trim());
            if(f.exists() && f.isDirectory()){
                directoryChooser.setInitialDirectory(f);
            }
        }

        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory == null) {
            System.out.println("CHƯA CHỌN THƯ MỤC ĐANG LƯU ẢNH");
        } else {
            textFieldFileReadFolder.setText(selectedDirectory.getAbsolutePath());
        }

    }




    @FXML
    protected void onCheckAndReadAllFileFolder() {

        String fromPath = textFieldFileReadFolder.getText();
        if (fromPath.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Chưa chọn thư mục hình", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        File repo = new File(fromPath);

        if (!repo.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Thư mục tìm chọn hình không hợp lệ!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        fileReadFormFolder = "";
        ArrayList<File> fileList = new ArrayList<>(List.of(repo.listFiles()));

        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            String fileName = file.getName();
            String[] namelist = fileName.split("\\.");
            String fileNameNew = fileName.replace("."+namelist[namelist.length-1], "");
            fileReadFormFolder += fileNameNew.toUpperCase();
            fileReadFormFolder += "\n";

        }


        textAreaFileNameShowAll.setText(fileReadFormFolder);
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
