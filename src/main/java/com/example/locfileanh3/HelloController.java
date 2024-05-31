package com.example.locfileanh3;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.controlsfx.dialog.ProgressDialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    String fromPath = "";
    String toPath = "";
    Stage stage;


    List<FileCopy> fileCopyList = new ArrayList<>();


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextArea textAreaFileNameList;

    @FXML
    private TextField textFieldFromFolder;
    @FXML
    private Label labelErrorMassage;

    @FXML
    private TextField textFieldToFolder;

    @FXML
    private TableView<FileCopy> listViewFileShowList;
    @FXML
    private TableColumn<FileCopy, Integer> colStt;
    @FXML
    private TableColumn<FileCopy, String> filename;
    @FXML
    private TableColumn<FileCopy, String> status;

    @FXML
    private Label labelProgress;


    @FXML
    protected void omToolReadFileNameFromFolder() throws IOException {
        Stage secondStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("read-all-file-name-form-folder.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        secondStage.setTitle("Phần mềm đọc tên file từ thư mục");
        secondStage.setScene(scene);
        secondStage.show();
    }


    @FXML
    protected void onSelectFromFolderClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if(!textFieldFromFolder.getText().trim().isEmpty()){
            File f = new File(textFieldFromFolder.getText().trim());
            if(f.exists() && f.isDirectory()){
                directoryChooser.setInitialDirectory(f);
            }
        }

        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory == null) {
            System.out.println("CHƯA CHỌN THƯ MỤC ĐANG LƯU ẢNH");
        } else {
            textFieldFromFolder.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    protected void onSelectToFolderClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        if(!textFieldToFolder.getText().trim().isEmpty()){
            File f = new File(textFieldToFolder.getText().trim());
            if(f.exists() && f.isDirectory()){
                directoryChooser.setInitialDirectory(f);
            }
        }

        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory == null) {
            System.out.println("CHƯA CHỌN THƯ MỤC LƯU ẢNH MỚI");
        } else {
            textFieldToFolder.setText(selectedDirectory.getAbsolutePath());
        }
    }


    @FXML
    protected void onCheckAndFindFileClick() {

        fromPath = textFieldFromFolder.getText();
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

        ArrayList<File> fileList = new ArrayList<>(List.of(repo.listFiles()));
        String[] textListFilter = textAreaFileNameList.getText().trim().split("\n");
        fileCopyList.clear();
        int stt = 1;

        StringBuilder nameSearchNotFound = new StringBuilder();
        for (int j = 0; j < textListFilter.length; j++) {
            String textFind = textListFilter[j].trim();
            if (textFind.length() < 4) {
                textFind = "0" + textFind;
            }
            File fileRemoveList = null;
            for (int i = 0; i < fileList.size(); i++) {
                File f = fileList.get(i);
                if (f.isFile()) {
                    if (f.getName().toUpperCase().contains(textFind.toUpperCase())) {
                        FileCopy fileCopy = new FileCopy(stt, f, f.getName());
                        stt++;
                        fileCopyList.add(fileCopy);
                        fileRemoveList = f;
                        break;
                    }
                }
            }

            if (fileRemoveList == null) {
                nameSearchNotFound.append("\n");
                nameSearchNotFound.append(textFind);
            } else {
                fileList.remove(fileRemoveList);
            }
        }

        if (fileCopyList.size() == 0) {
            labelErrorMassage.setText("Không tìm thấy file!");
        } else {
            labelErrorMassage.setText("");
        }

        setColumnProperties();
        listViewFileShowList.getItems().clear();
        listViewFileShowList.getItems().addAll(fileCopyList);
        listViewFileShowList.refresh();

        if (!nameSearchNotFound.toString().isEmpty())
            showDialog("Danh sách tên file không tìm thấy",
                    nameSearchNotFound.toString());


    }

    @FXML
    protected void onFilterAndCopyFileClick() {

        toPath = textFieldToFolder.getText();
        if (toPath.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Chưa chọn thư mục lưu hình!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        File rep = new File(toPath);

        if (!rep.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Thư mục lưu hình không hợp lệ!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        labelProgress.setText("");

        progressDialogue();


        labelProgress.setText("     Đã thực hiện thành công");

        listViewFileShowList.getItems().clear();
        listViewFileShowList.getItems().addAll(fileCopyList);
        listViewFileShowList.refresh();


    }

    private void progressDialogue() {
        Task copyWorker = createWorker();
        ProgressDialog dialog = new ProgressDialog(copyWorker);
        dialog.initStyle(StageStyle.TRANSPARENT);

        dialog.setGraphic(null);
        //stage.initStyle(StageStyle.TRANSPARENT);
        dialog.initStyle(StageStyle.TRANSPARENT);
        //dialog.setContentText("Files are Uploading");
        //dialog.setTitle("Files Uploading");
        //dialog.setHeaderText("This is demo");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.initStyle(StageStyle.UTILITY);
        new Thread(copyWorker).start();
        dialog.showAndWait();
    }


    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                String fileCopyError = "";

                for (int i = 0; i < fileCopyList.size(); i++) {
                    FileCopy fileCopy = fileCopyList.get(i);
                    File f = fileCopy.getFile();

                    try {
                        Files.copy(f.toPath(),
                                (new File(toPath + "\\" + f.getName())).toPath(),
                                StandardCopyOption.REPLACE_EXISTING);


                        double percent = 1.0 * i / fileCopyList.size() * 100;
//                        labelProgress.setText();
                        fileCopy.setStatus("Đã copy");

                        Thread.sleep(100);
                        updateMessage("   Đang thực hiện (" + (int) percent + "%) Copy file " + f.getName());
                        updateProgress(i + 1, fileCopyList.size());

                    } catch (IOException e) {
                        fileCopyError += "\n";
                        fileCopyError += f.getName();

                        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK, ButtonType.CANCEL);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.CANCEL) {
                            break;
                        }

                        throw new RuntimeException(e);
                    }
                }

                if (!fileCopyError.isEmpty())
                    showDialog("Danh sách file copy thất bại",
                            fileCopyError);

                return true;
            }
        };
    }

    private void setColumnProperties() {
        colStt.setCellValueFactory(new PropertyValueFactory<FileCopy, Integer>("stt"));
        filename.setCellValueFactory(new PropertyValueFactory<FileCopy, String>("filename"));
        status.setCellValueFactory(new PropertyValueFactory<FileCopy, String>("status"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void showDialog(String title,String text) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextArea from = new TextArea();
        from.setText(text);

        gridPane.add(from, 0, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> from.requestFocus());

//        // Convert the result to a username-password-pair when the login button is clicked.
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == loginButtonType) {
//                return new Pair<>(from.getText(), to.getText());
//            }
//            return null;
//        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

    public class FileCopy {
        private File file;
        private int stt;
        private String filename;
        private String status;

        public FileCopy() {
        }

        public FileCopy(int stt, File file, String filename) {
            this.stt = stt;
            this.file = file;
            this.filename = filename;
            this.status = "Đã tìm thấy";
        }

        public int getStt() {
            return this.stt;
        }

        public void setStt(int stt) {
            this.stt = stt;
        }

        public File getFile() {
            return this.file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}