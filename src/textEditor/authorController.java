package textEditor;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import textEditor.CommentModel.AssociatedComment;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

public class authorController implements Initializable{

    @FXML
    private TextArea textArea;
    @FXML
    private ListView<AssociatedComment> commentListView;

    private ObservableList<AssociatedComment> comments = FXCollections.observableArrayList();
    @FXML
    private Label fileName;

    public void handleClickListView(){
        AssociatedComment item = (AssociatedComment) commentListView.getSelectionModel().getSelectedItem();
        textArea.selectRange(item.getAssociatedRange().getStart(),item.getAssociatedRange().getEnd());
    }

    @FXML
    public void AddComment(ActionEvent e)throws IOException{
        IndexRange range = textArea.getSelection();
        String selectedText = textArea.getSelectedText();
        String commentedText = promptForComment(selectedText);
        if(selectedText.isEmpty()||commentedText.isEmpty()){return;}
        createComment(range,selectedText,commentedText);
        commentListView.setItems(comments);
    }

    @FXML
    private void createComment(IndexRange range, String selectedText, String commentedText){
        AssociatedComment comment = new AssociatedComment(range, selectedText,commentedText);
        comment.setOnMouseClicked(clickEvent -> textArea.selectRange(comment.getAssociatedRange().getStart(),comment.getAssociatedRange().getEnd()));
        comments.add(comment);
    }

    @FXML
    private String promptForComment(String selectedText){
        TextAreaInputDialog inputDialog = new TextAreaInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Enter your comment to: " + selectedText);
        inputDialog.setContentText("Enter your comment to: " +selectedText);
        return inputDialog.showAndWait().get();
    }

    @FXML
    public void RemoveComment(ActionEvent e){
        final int selecteditem = commentListView.getSelectionModel().getSelectedIndex();
        commentListView.getItems().remove(selecteditem);
    }

    @FXML
    public void saveFile(ActionEvent e)throws IOException{
        String text = textArea.getText();
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFiltertxt = new FileChooser.ExtensionFilter("Text Files(*.txt)", "*.txt");
        FileChooser.ExtensionFilter extFilterhtml = new FileChooser.ExtensionFilter("HTML Files (*.html)","*.html");
        FileChooser.ExtensionFilter extFilterpdf = new FileChooser.ExtensionFilter("PDF Files (*.pdf)","*.pdf");
        FileChooser.ExtensionFilter extFilterjava = new FileChooser.ExtensionFilter("Java Files (*.java)","*.java");
        fc.getExtensionFilters().add(extFiltertxt);
        fc.getExtensionFilters().add(extFilterhtml);
        fc.getExtensionFilters().add(extFilterpdf);
        fc.getExtensionFilters().add(extFilterjava);
        File file = fc.showSaveDialog(null);
        String path = file.getParent();
        String savepath = path.concat("\\"+file.getName()+"_Comments.txt");
        System.out.println(path);
        System.out.println(savepath);
        if(file!=null){
            saveContent(text,file);
        }
        saveComments(savepath);
    }

    private void saveContent(String content, File file){
        try{
            FileWriter fw = null;
            fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void saveComments(String filepath)throws IOException{
        Path path = Paths.get(filepath);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<AssociatedComment> iter = comments.iterator();
            while(iter.hasNext()){
                AssociatedComment comment = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        comment.getAssociatedRange(),
                        comment.getAssociatedText(),
                        comment.getAssociatedComment()));
                bw.newLine();
            }
        }finally {
            if(bw!=null){
                bw.close();
            }
        }
    }

    public void loadFile(ActionEvent e)throws IOException {
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(null);
        String parentPath = file.getParent();
        String filename = file.getName();
        fileName.setText(filename);
        fileName.setFont(new Font("Berlin Sans FB",18));
        fileName.setTextFill(Color.valueOf("#24252a"));
        String loadPath = parentPath.concat("\\"+file.getName()+"_Comments.txt");
        if (file != null) {

            String codeRead = "";

            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine())
                    codeRead = codeRead + scanner.nextLine() + "\n";
                textArea.setText(codeRead);

            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
        }
    }

    public void loadAnnotateFile(ActionEvent e)throws IOException {
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(null);
        String parentPath = file.getParent();
        String filename = file.getName();
        fileName.setText(filename);
        fileName.setFont(new Font("Berlin Sans FB",18));
        fileName.setTextFill(Color.valueOf("#24252a"));
        String loadPath = parentPath.concat("\\"+file.getName()+"_Comments.txt");
        if (file != null) {

            String codeRead = "";

            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine())
                    codeRead = codeRead + scanner.nextLine() + "\n";
                textArea.setText(codeRead);

            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
        }
        loadComments(loadPath);
    }


    public void loadComments(String loadPath)throws IOException{
        comments = FXCollections.observableArrayList();
        Path path = Paths.get(loadPath);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while ((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");
                createComment(IndexRange.valueOf(itemPieces[0]),itemPieces[1],itemPieces[2]);
                commentListView.setItems(comments);

            }
        }finally {
            if(br!=null){
                br.close();
            }
        }
    }

    public void closeWindow(ActionEvent e)throws IOException{
        comments.clear();

        Platform.exit();
        System.exit(0);
    }

    public void clearAll(ActionEvent e){
        comments.clear();
        textArea.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        commentListView.setCellFactory(param -> new ListCell<AssociatedComment>(){
            @Override
            protected void updateItem(AssociatedComment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setGraphic(null);
                    setText(null);
                    // other stuff to do...

                }else{

                    // set the width's
                    setMinWidth(param.getWidth());
                    setMaxWidth(param.getWidth());
                    setPrefWidth(param.getWidth());

                    // allow wrapping
                    setWrapText(true);
                    setText(item.getAssociatedText().concat(": "+item.getAssociatedComment()));

                }

            }
        });

    }
}
