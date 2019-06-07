package textEditor.CommentModel;

import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

    public class AssociatedComment extends TextFlow {
    private IndexRange associatedRange;
    private Text associatedText, associatedComment;

    public AssociatedComment(IndexRange range, String text, String comment){
        associatedRange = range;
        associatedText = new Text(text);
        associatedText.setFill(Color.STEELBLUE);
        associatedComment = new Text(comment);
        getChildren().setAll(associatedText,new Text(" : "), associatedComment);
    }

    public IndexRange getAssociatedRange(){
        return  associatedRange;
    }

    public String getAssociatedText(){
        return associatedText.getText();
    }

    public String getAssociatedComment(){
        return associatedComment.getText();
    }
}