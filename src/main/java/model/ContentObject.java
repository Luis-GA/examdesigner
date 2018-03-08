package model;

import javafx.scene.image.Image;

public class ContentObject {

    public enum Type {
        TEXT,
        IMAGE
    }

    private Type type;
    private Object content;

    public void setContent(String content){
        this.content = content;
        this.type = Type.TEXT;
    }

    public void setContent(Image content){
        this.content = content;
        this.type = Type.IMAGE;
    }

    public Object getContent(){
        return this.content;
    }

    public Type getType(){
        return this.type;
    }
}
