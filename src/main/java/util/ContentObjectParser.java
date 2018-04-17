package util;

import javafx.scene.image.ImageView;
import model.ContentObject;

public class ContentObjectParser {

    protected ContentObject.Type type;
    protected String content;

    public ContentObjectParser(ContentObject contentObject) {
        this.type = contentObject.getType();
        this.content = contentObject.getContent();
    }

    public ContentObject parseContentObject() {
        ContentObject aux = new ContentObject();

        if (this.type == ContentObject.Type.IMAGE)
            aux.setContent(new ImageView(ImageUtil.getImage(this.content)));
        else if (this.type == ContentObject.Type.TEXT)
            aux.setContent(this.content);

        return aux;
    }
}
