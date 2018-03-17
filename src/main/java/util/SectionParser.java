package util;

import model.ContentObject;
import model.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionParser {

    private String title;
    private List<ContentObjectParser> bodyObjects;

    public SectionParser(Section section){
        this.title = section.getTitle();

        this.bodyObjects = new ArrayList<>();
        List<ContentObject> aux = section.getBodyObjects();
        for(ContentObject contentObject : aux){
            this.bodyObjects.add(new ContentObjectParser(contentObject));
        }
    }

    public Section parseSection(){
        Section aux = new Section();

        aux.setTitle(this.title);

        List<ContentObject> auxList = new ArrayList<>();
        for(ContentObjectParser contentObject : this.bodyObjects){
            auxList.add(contentObject.parseContentObject());
        }
        aux.setBodyObjects(auxList);

        return aux;
    }
}
