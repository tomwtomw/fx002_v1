package org.tomw.rachunki.entities;

import javafx.beans.property.*;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Type;
import org.tomw.documentfile.DocumentFile;
import org.tomw.identifiable.Identifiable;

import javax.persistence.*;

import java.util.Collection;
import java.util.HashSet;

import static org.tomw.utils.TomwStringUtils.BLANK;

@Entity
@Table
public class Konto implements Identifiable {
    private final static Logger LOGGER = Logger.getLogger(Konto.class.getName());

    public static final String LOCAL = "Local";
    public static final String REMOTE="Remote";

    private final IntegerProperty id = new SimpleIntegerProperty();

    private Collection<String> oldIds =  new HashSet<>();

    private final StringProperty shortName = new SimpleStringProperty(BLANK);
    private final StringProperty fullName = new SimpleStringProperty(BLANK);

    private final StringProperty firstName = new SimpleStringProperty(BLANK);

    private final StringProperty lastName = new SimpleStringProperty(BLANK);

    private final StringProperty addressLine1 = new SimpleStringProperty(BLANK);
    private final StringProperty addressLine2 = new SimpleStringProperty(BLANK);
    private final StringProperty addressLine3 = new SimpleStringProperty(BLANK);
    private final StringProperty city = new SimpleStringProperty(BLANK);
    private final StringProperty state = new SimpleStringProperty(BLANK);
    private final StringProperty zip = new SimpleStringProperty(BLANK);
    private final StringProperty country = new SimpleStringProperty(BLANK);

    private final StringProperty phoneNumber1 = new SimpleStringProperty(BLANK);
    private final StringProperty phoneNumber2 = new SimpleStringProperty(BLANK);
    private final StringProperty faxNumber = new SimpleStringProperty(BLANK);
    private final StringProperty email = new SimpleStringProperty(BLANK);
    private final StringProperty webPage = new SimpleStringProperty(BLANK);

    private final StringProperty accountType = new SimpleStringProperty(BLANK);

    private BooleanProperty accountActive = new SimpleBooleanProperty(true);
    private BooleanProperty accountLocal = new SimpleBooleanProperty(false);
    private BooleanProperty accountPrimary = new SimpleBooleanProperty(false);

    private final StringProperty comment = new SimpleStringProperty(BLANK);

    private Collection<DocumentFile> documents =  new HashSet<>();

    private String mostRecentCheckNumber = BLANK;

    public Konto() {
    }

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

    //@OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    @CollectionTable(name="oldKontoIds")
    @Column(name="oldIds")
    public Collection<String> getOldIds() {
        return oldIds;
    }

    public void setOldIds(Collection<String> oldIds) {
        this.oldIds = oldIds;
    }

    @Column
    public String getShortName() {
        return shortName.get();
    }

    public StringProperty shortNameProperty() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName.set(shortName);
    }

    @Column
    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    @Column
    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    @Column
    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    @Column
    public String getAddressLine1() {
        return addressLine1.get();
    }

    public StringProperty addressLine1Property() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1.set(addressLine1);
    }

    @Column
    public String getAddressLine2() {
        return addressLine2.get();
    }

    public StringProperty addressLine2Property() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2.set(addressLine2);
    }

    @Column
    public String getAddressLine3() {
        return addressLine3.get();
    }

    public StringProperty addressLine3Property() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3.set(addressLine3);
    }

    @Column
    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    @Column
    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    @Column
    public String getZip() {
        return zip.get();
    }

    public StringProperty zipProperty() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip.set(zip);
    }

    @Column
    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    @Column
    public String getPhoneNumber1() {
        return phoneNumber1.get();
    }

    public StringProperty phoneNumber1Property() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1.set(phoneNumber1);
    }

    @Column
    public String getPhoneNumber2() {
        return phoneNumber2.get();
    }

    public StringProperty phoneNumber2Property() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2.set(phoneNumber2);
    }

    @Column
    public String getFaxNumber() {
        return faxNumber.get();
    }

    public StringProperty faxNumberProperty() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber.set(faxNumber);
    }

    @Column
    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @Column
    public String getWebPage() {
        return webPage.get();
    }

    public StringProperty webPageProperty() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage.set(webPage);
    }

    @Column
    public String getAccountType() {
        return accountType.get();
    }

    public StringProperty accountTypeProperty() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean isAccountActive() {
        return accountActive.get();
    }

    public BooleanProperty accountActiveProperty() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive.set(accountActive);
    }

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean isAccountLocal() {
        return accountLocal.get();
    }

    public BooleanProperty accountLocalProperty() {
        return accountLocal;
    }

    public void setAccountLocal(boolean accountLocal) {
        this.accountLocal.set(accountLocal);
    }

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean isAccountPrimary() {
        return accountPrimary.get();
    }

    public BooleanProperty accountPrimaryProperty() {
        return accountPrimary;
    }

    public void setAccountPrimary(boolean accountPrimary) {
        this.accountPrimary.set(accountPrimary);
    }

    @Column
    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    @OneToMany(cascade = CascadeType.ALL)
    public Collection<DocumentFile> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<DocumentFile> documents) {
        this.documents = documents;
    }

    @Column
    public String getMostRecentCheckNumber() {
        return mostRecentCheckNumber;
    }

    public void setMostRecentCheckNumber(String mostRecentCheckNumber) {
        this.mostRecentCheckNumber = mostRecentCheckNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Konto that = (Konto) o;

        return id.get() == that.id.get();
    }

    @Override
    public int hashCode() {
        return (new Integer(id.get())).hashCode();
    }

    @Override
    public String toString() {
        return "Konto{" +
                "id=" + id +
                ", shortName=" + shortName.getValue() +
                ", fullName=" + fullName.getValue() +
                '}';
    }

    public String toShortString(){
        return fullName.getValue();
    }
}
