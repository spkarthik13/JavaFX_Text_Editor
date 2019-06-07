package textEditor;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import textEditor.CommentModel.AssociatedComment;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

public class readerController implements Initializable {

    @FXML
    private TextArea textArea;
    @FXML
    private ListView <AssociatedComment> readerCommentListView;
    private ObservableList<AssociatedComment> comments = FXCollections.observableArrayList();
    @FXML
    private Label fileName;
    @FXML
    private Label commentLabel;
    @FXML
    private TextArea commentBox;
    @FXML
    private Button addAnnotateButton;
    @FXML
    TextArea readerTextArea;


    public void AddComment(ActionEvent e)throws IOException{
        IndexRange range = textArea.getSelection();
        String selectedText = textArea.getSelectedText();
        String commentedText = promptForComment(selectedText);
        if(selectedText.isEmpty()||commentedText.isEmpty()){return;}
        createComment(range,selectedText,commentedText);
        readerCommentListView.setItems(comments);
    }

    private void createComment(IndexRange range, String selectedText, String commentedText){
        AssociatedComment comment = new AssociatedComment(range, selectedText,commentedText);
        comment.setOnMouseClicked(clickEvent -> readerTextArea.selectRange(comment.getAssociatedRange().getStart(),comment.getAssociatedRange().getEnd()));
        comments.add(comment);
    }



    public void handleClickListViewReader(){
        AssociatedComment item = (AssociatedComment) readerCommentListView.getSelectionModel().getSelectedItem();
        readerTextArea.selectRange(item.getAssociatedRange().getStart(),item.getAssociatedRange().getEnd());
    }

    private String promptForComment(String selectedText){
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Enter your comment");
        inputDialog.setContentText("Enter your comment to: " +selectedText);
        return inputDialog.showAndWait().get();
    }


    public void saveFile(ActionEvent e)throws IOException{
        String text = textArea.getText();
        FileChooser fc = new FileChooser();
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
        fileName.setTextFill(Color.CHOCOLATE);
        String loadPath = parentPath.concat("\\"+file.getName()+"_Comments.txt");
        if (file != null) {

            String codeRead = "";

            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine())
                    codeRead = codeRead + scanner.nextLine() + "\n";
                readerTextArea.setText(codeRead);

            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
        }
        readerTextArea.setEditable(false);
    }

    public void loadReaderFile(ActionEvent e)throws IOException {
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
                readerTextArea.setText(codeRead);

            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
        }
        loadComments(loadPath);
        readerTextArea.setEditable(false);
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
                readerCommentListView.setItems(comments);

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
        readerCommentListView.setCellFactory(param -> new ListCell<AssociatedComment>(){
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
