package util;

import model.ContentObject;
import model.Section;
import java.util.List;

public class SectionParser {

    private String title;
    private List<ContentObject> bodyObjects;

    public SectionParser(Section section){
        this.title = section.getTitle();
        this.bodyObjects = section.getBodyObjects();
    }

    public Section parseSection(){
        Section aux = new Section();

        aux.setTitle(this.title);
        aux.setBodyObjects(this.bodyObjects);

        return aux;
    }
}
