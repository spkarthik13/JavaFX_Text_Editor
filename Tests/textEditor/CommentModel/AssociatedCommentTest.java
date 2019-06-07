package textEditor.CommentModel;

import javafx.scene.control.IndexRange;
import javafx.scene.text.Text;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssociatedCommentTest {

    private IndexRange associatedRange;
    private Text associatedText;
    private Text associatedComment;

    public AssociatedCommentTest(IndexRange range, Text associatedText, Text associatedComment){
        this.associatedRange = range;
        this.associatedText = associatedText;
        this.associatedComment = associatedComment;
    }


    @Test
    public void getAssociatedRange() {
    }

    @Test
    public void getAssociatedText() {
    }

    @Test
    public void getAssociatedComment() {
    }

}