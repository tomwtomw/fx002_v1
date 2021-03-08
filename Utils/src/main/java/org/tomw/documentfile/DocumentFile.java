package org.tomw.documentfile;

import javafx.beans.property.*;
import org.tomw.identifiable.Identifiable;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.tomw.utils.TomwStringUtils.BLANK;

@Entity
@Table
public class DocumentFile implements Identifiable {
    //document classes
    public static final String DOCUMENT="document";
    public static final String PERSON_PICTURE="personPicture";
    public static final String CHECK_IMAGE="checkImage";

    private final IntegerProperty id = new SimpleIntegerProperty();

    private final StringProperty externalFileName = new SimpleStringProperty();
    private final StringProperty internalFileName = new SimpleStringProperty();
    private final StringProperty documentTitle = new SimpleStringProperty();
    private final StringProperty documentDescription = new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty documentType = new SimpleStringProperty(BLANK);

    private final StringProperty internalSubdirectory = new SimpleStringProperty();

    private ObjectProperty<LocalDateTime> uploadDateTime = new SimpleObjectProperty<>();

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Column
    public String getExternalFileName() {
        return externalFileName.get();
    }

    public StringProperty externalFileNameProperty() {
        return externalFileName;
    }

    public void setExternalFileName(String externalFileName) {
        this.externalFileName.set(externalFileName);
    }

    @Column
    public String getInternalFileName() {
        return internalFileName.get();
    }

    public StringProperty internalFileNameProperty() {
        return internalFileName;
    }

    public void setInternalFileName(String internalFileName) {
        this.internalFileName.set(internalFileName);
    }

    @Column
    public String getDocumentTitle() {
        return documentTitle.get();
    }

    public StringProperty documentTitleProperty() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle.set(documentTitle);
    }

    @Column
    @Lob
    public String getDocumentDescription() {
        return documentDescription.get();
    }

    public StringProperty documentDescriptionProperty() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription.set(documentDescription);
    }

    @Column
    @Lob
    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    @Column
    public LocalDateTime getUploadDateTime() {
        return uploadDateTime.get();
    }

    public ObjectProperty<LocalDateTime> uploadDateTimeProperty() {
        return uploadDateTime;
    }

    public void setUploadDateTime(LocalDateTime uploadDateTime) {
        this.uploadDateTime.set(uploadDateTime);
    }

    @Column
    public String getDocumentType() {
        return documentType.get();
    }

    public StringProperty documentTypeProperty() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType.set(documentType);
    }

    public void makeCategoryDocument(){
        setDocumentType(DocumentFile.DOCUMENT);
    }
    public void makeCategoryPersonPicture(){
        setDocumentType(DocumentFile.PERSON_PICTURE);
    }

    public boolean hasCategoryGeneralDocument(){
        return DocumentFile.DOCUMENT.equals(getDocumentType());
    }

    public boolean hasCategoryPersonPicture(){
        return DocumentFile.PERSON_PICTURE.equals(getDocumentType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentFile other = (DocumentFile) o;

        return id.get()== other.getId();
    }

    @Override
    public int hashCode() {
        return (new Integer(id.get())).hashCode();
    }

    @Override
    public String toString() {
        return "DocumentFile{" +
                "id=" + id.get() +
                ", documentTitle=" + documentTitle.getValue() +
                ", externalFileName="+externalFileName.getValue()+
                ", internalFileName="+internalFileName.getValue()+
                '}';
    }
}
