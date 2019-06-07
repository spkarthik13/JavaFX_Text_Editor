package textEditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController  implements Initializable {

    @FXML
    private Label lblStatus;
    @FXML
    TextField txtUser;
    @FXML
    TextField txtPass;


    @FXML
    public void Login(ActionEvent e) throws IOException {
        if(txtUser.getText().equals("Ria")||txtUser.getText().equals("Karthik")&&txtPass.getText().equals("password")){
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Login Successful");
            Stage authorEditor = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AuthorEditor.fxml"));
            authorEditor.setTitle("Air Editor");
            authorEditor.setScene(new Scene(root, 800, 650));
            authorEditor.show();
            root.getStylesheets().add(getClass().getResource("AreaStyle.css").toExternalForm());

        }
        else{
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Login failed :(");
        }
    }

    @FXML
    public void readerLogin(ActionEvent e) throws IOException {
        Stage authorEditor = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ReaderEditor.fxml"));
        authorEditor.setTitle("Air Reader");
        authorEditor.setScene(new Scene(root, 800, 650));
        root.getStylesheets().add(getClass().getResource("AreaStyle.css").toExternalForm());
        authorEditor.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
