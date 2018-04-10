package model;

import javafx.scene.image.Image;
import util.ImageUtil;

public class ContentObject {

    public enum Type {
        TEXT,
        IMAGE
    }

    private Type type;
    private Object content;

    public void setContent(String content) {
        this.content = content;
        this.type = Type.TEXT;
    }

    public void setContent(Image content) {
        this.content = content;
        this.type = Type.IMAGE;
    }

    public Image getImage() {
        if (this.type == Type.IMAGE)
            return (Image) this.content;
        else
            return null;
    }

    public String getText() {
        if (this.type == Type.TEXT)
            return (String) this.content;
        else
            return null;
    }

    public String getContent() {
        if (this.type == Type.IMAGE)
            return ImageUtil.getBase64((Image) this.content);
        else if (this.type == Type.TEXT)
            return (String) this.content;
        else
            return null;
    }

    public Type getType() {
        return this.type;
    }
}
