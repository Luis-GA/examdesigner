package util;

import model.Choice;
import model.ContentObject;
import model.Section;

import java.util.ArrayList;
import java.util.List;

public class ChoiceParser {

    private String title;
    private List<ContentObjectParser> bodyObjects;

    public ChoiceParser(Choice choice){
        this.title = choice.getTitle();

        this.bodyObjects = new ArrayList<>();
        List<ContentObject> aux = choice.getBodyObjects();
        for(ContentObject contentObject : aux){
            this.bodyObjects.add(new ContentObjectParser(contentObject));
        }
    }

    public Choice parseChoice(){
        Choice aux = new Choice();

        aux.setTitle(this.title);

        List<ContentObject> auxList = new ArrayList<>();
        for(ContentObjectParser contentObject : this.bodyObjects){
            auxList.add(contentObject.parseContentObject());
        }
        aux.setBodyObjects(auxList);

        return aux;
    }
}
