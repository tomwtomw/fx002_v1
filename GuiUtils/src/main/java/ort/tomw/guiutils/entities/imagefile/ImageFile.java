package ort.tomw.guiutils.entities.imagefile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.UUID;

/**
 * wrapper class around an image. Contains some metadata like name
 * and comment
 */
public class ImageFile {

    public static final String FILE_KEY = "File";
    public static final String ID_KEY = "Id";
    public static final String COMMENT_KEY = "Comment";
    public static final String NAME_KEY = "Name";

    private String id;
    private File file;
    private String comment;
    private String name;

    private  StringProperty idProperty = new SimpleStringProperty();
    private  StringProperty fileNameProperty = new SimpleStringProperty();
    private  StringProperty nameProperty = new SimpleStringProperty();
    private  StringProperty commentProperty = new SimpleStringProperty();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setIdProperty(id);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if(file!=null) {
            this.file = file;
            setFileNameProperty(file.toString());
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        setCommentProperty(comment);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setNameProperty(name);
    }

    public String getIdProperty() {
        return idProperty.get();
    }

    public StringProperty idPropertyProperty() {
        return idProperty;
    }

    public void setIdProperty(String id) {
        this.idProperty.set(id);
    }

    public String getFileNameProperty() {
        return fileNameProperty.get();
    }

    public StringProperty fileNamePropertyProperty() {
        return fileNameProperty;
    }

    public void setFileNameProperty(String fileName) {
        this.fileNameProperty.set(fileName);
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }

    public void setNameProperty(String name) {
        this.nameProperty.set(name);
    }

    public String getCommentProperty() {
        return commentProperty.get();
    }

    public StringProperty commentPropertyProperty() {
        return commentProperty;
    }

    public void setCommentProperty(String comment) {
        this.commentProperty.set(comment);
    }

    public ImageFile() {
        this(UUID.randomUUID().toString());
    }

    public ImageFile(String inputId) {
        this.id = inputId;
    }

    @JsonCreator
    public ImageFile(
            @JsonProperty(ID_KEY) String id,
            @JsonProperty(NAME_KEY) String name,
            @JsonProperty(FILE_KEY) String fileName,
            @JsonProperty(COMMENT_KEY) String comment) {
        this(id, name, new File(fileName), comment);
        packProperties();
    }

    public ImageFile(String id, String name, File file, String comment){
        this.id=id;
        this.name=name;
        this.file=file;
        this.comment=comment;
        packProperties();
    }

    public ImageFile(String name, File file, String comment){
        this();
        this.name=name;
        this.file=file;
        this.comment=comment;
        packProperties();
    }

    private void packProperties(){
        this.nameProperty.set(this.name);
        this.fileNameProperty.set(this.file.toString());
        this.commentProperty.set(this.comment);
    }

    public Image getImage(){
        return new Image(getFile().toURI().toString());
    }

    /**
     * Convert ImageFile to json representation
     * @return ImageFile as json object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject(){
        JSONObject json = new JSONObject();
        json.put(ID_KEY,getId());
        json.put(NAME_KEY,getName());
        json.put(COMMENT_KEY,getComment());
        json.put(FILE_KEY,getFile().toString());
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageFile imageFile = (ImageFile) o;

        if (id != null ? !id.equals(imageFile.id) : imageFile.id != null) return false;
        if (file != null ? !file.equals(imageFile.file) : imageFile.file != null) return false;
        if (comment != null ? !comment.equals(imageFile.comment) : imageFile.comment != null) return false;
        return name != null ? name.equals(imageFile.name) : imageFile.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
