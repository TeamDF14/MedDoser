package guifx;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import medicationplaninterpreter.ActiveSubstance;
import medicationplaninterpreter.IngestionTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * This is the data type used for presenting medications to the UI.
 */
public class MedicationFX {
    public String getPharmacyCentralNumber() {
        return this.pharmacyCentralNumber.get();
    }

    public SimpleStringProperty pharmacyCentralNumberProperty() {
        return this.pharmacyCentralNumber;
    }

    public void setPharmacyCentralNumber(String pharmacyCentralNumber) {
        this.pharmacyCentralNumber.set(pharmacyCentralNumber);
    }

    public String getTradeName() {
        return this.tradeName.get();
    }

    public SimpleStringProperty tradeNameProperty() {
        return this.tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName.set(tradeName);
    }

    public String getNote() {
        return this.note.get();
    }

    public SimpleStringProperty noteProperty() {
        return this.note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public String getReason() {
        return this.reason.get();
    }

    public SimpleStringProperty reasonProperty() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public String getStatus() {
        return this.status.get();
    }

    public SimpleStringProperty statusProperty() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getAssertedDate() {
        return this.assertedDate.get();
    }

    public SimpleStringProperty assertedDateProperty() {
        return this.assertedDate;
    }

    public void setAssertedDate(String assertedDate) {
        this.assertedDate.set(assertedDate);
    }

    public String getWasNotTaken() {
        return this.wasNotTaken.get();
    }

    public SimpleStringProperty wasNotTakenProperty() {
        return this.wasNotTaken;
    }

    public void setWasNotTaken(String wasNotTaken) {
        this.wasNotTaken.set(wasNotTaken);
    }

    public String getReferenceMedicationStatementFX() {
        return this.referenceMedicationStatementFX.get();
    }

    public SimpleStringProperty referenceMedicationStatementFXProperty() {
        return this.referenceMedicationStatementFX;
    }

    public void setReferenceMedicationStatementFX(String referenceMedicationStatementFX) {
        this.referenceMedicationStatementFX.set(referenceMedicationStatementFX);
    }

    public String getReferenceToMedicationFX() {
        return this.referenceToMedicationFX.get();
    }

    public SimpleStringProperty referenceToMedicationFXProperty() {
        return this.referenceToMedicationFX;
    }

    public void setReferenceToMedicationFX(String referenceToMedicationFX) {
        this.referenceToMedicationFX.set(referenceToMedicationFX);
    }

    public ActiveSubstance getActiveSubstance() {
        return this.activeSubstance.get();
    }

    public SimpleObjectProperty<ActiveSubstance> activeSubstanceProperty() {
        return this.activeSubstance;
    }

    public void setActiveSubstance(ActiveSubstance activeSubstance) {
        this.activeSubstance.set(activeSubstance);
    }

    public String getDrugFormName() {
        return this.drugFormName.get();
    }

    public SimpleStringProperty drugFormNameProperty() {
        return this.drugFormName;
    }

    public void setDrugFormName(String drugFormName) {
        this.drugFormName.set(drugFormName);
    }

    public String getDrugFormCode() {
        return this.drugFormCode.get();
    }

    public SimpleStringProperty drugFormCodeProperty() {
        return this.drugFormCode;
    }

    public void setDrugFormCode(String drugFormCode) {
        this.drugFormCode.set(drugFormCode);
    }

    public String getDosageValue() {
        return this.dosageValue.get();
    }

    public SimpleStringProperty dosageValueProperty() {
        return this.dosageValue;
    }

    public void setDosageValue(String dosageValue) {
        this.dosageValue.set(dosageValue);
    }

    public String getDosageUnit() {
        return this.dosageUnit.get();
    }

    public SimpleStringProperty dosageUnitProperty() {
        return this.dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit.set(dosageUnit);
    }

    public String getDosageUnitAsText() {
        return this.dosageUnitAsText.get();
    }

    public SimpleStringProperty dosageUnitAsTextProperty() {
        return this.dosageUnitAsText;
    }

    public void setDosageUnitAsText(String dosageUnitAsText) {
        this.dosageUnitAsText.set(dosageUnitAsText);
    }

    public String getDosageFreeText() {
        return this.dosageFreeText.get();
    }

    public SimpleStringProperty dosageFreeTextProperty() {
        return this.dosageFreeText;
    }

    public void setDosageFreeText(String dosageFreeText) {
        this.dosageFreeText.set(dosageFreeText);
    }

    public eIngestionTime getTimeOfDosage() {
        return this.timeOfDosage.get();
    }

    public SimpleObjectProperty<eIngestionTime> timeOfDosageProperty() {
        return this.timeOfDosage;
    }

    public void setTimeOfDosage(eIngestionTime timeOfDosage) {
        this.timeOfDosage.set(timeOfDosage);
    }

    public String getInformationOfNeeded() {
        return this.informationOfNeeded.get();
    }

    public SimpleStringProperty informationOfNeededProperty() {
        return this.informationOfNeeded;
    }

    public void setInformationOfNeeded(String informationOfNeeded) {
        this.informationOfNeeded.set(informationOfNeeded);
    }

    public String getReferenceMedicationStatement() {
        return this.referenceMedicationStatement.get();
    }

    public SimpleStringProperty referenceMedicationStatementProperty() {
        return this.referenceMedicationStatement;
    }

    public void setReferenceMedicationStatement(String referenceMedicationStatement) {
        this.referenceMedicationStatement.set(referenceMedicationStatement);
    }

    public String getReferenceToMedication() {
        return this.referenceToMedication.get();
    }

    public SimpleStringProperty referenceToMedicationProperty() {
        return this.referenceToMedication;
    }

    public void setReferenceToMedication(String referenceToMedication) {
        this.referenceToMedication.set(referenceToMedication);
    }

    public ObservableList<IngestionTime> getIngestionTimes() {
        return this.ingestionTimes.get();
    }

    public SimpleListProperty<IngestionTime> ingestionTimesProperty() {
        return this.ingestionTimes;
    }

    public void setIngestionTimes(ArrayList<IngestionTime> ingestionTimes) {
        this.ingestionTimes.set(FXCollections.observableList(ingestionTimes));
    }

    public Date getIngestedTime() {
        return ingestedTime.get();
    }

    public SimpleObjectProperty<Date> ingestedTimeProperty() {
        return ingestedTime;
    }

    public void setIngestedTime(Date ingestedTime) {
        this.ingestedTime.set(ingestedTime);
    }

    public SimpleObjectProperty<eIngestedStatus> ingestedStatusProperty() {
        return ingestedStatus;
    }

    public eIngestedStatus getIngestedStatus() {
        return ingestedStatus.get();
    }

    public void setIngestedStatus(eIngestedStatus ingestedStatus) {
        this.ingestedStatus.set(ingestedStatus);
    }

    public String getSectionTitle() {
        return sectionTitle.get();
    }

    public SimpleStringProperty sectionTitleProperty() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle.set(sectionTitle);
    }

    public String getDosageInformationFX() {
        return this.dosageInformationFX.get();
    }
    public SimpleStringProperty dosageInformationFXProperty() {
        return this.dosageInformationFX;
    }

    public void setDosageInformationFX(String dosageInformationFX) {
        this.dosageInformationFX.set(dosageInformationFX);
    }
    public ArrayList<Date> getIngestedTimesUdef() {
        return ingestedTimesUdef.get();
    }

    public SimpleObjectProperty<ArrayList<Date>> ingestedTimesUdefProperty() {
        return ingestedTimesUdef;
    }

    public void setIngestedTimesUdef(ArrayList<Date> ingestedTimesUdef) {
        this.ingestedTimesUdef.set(ingestedTimesUdef);
    }

    public String getSectionFreeText() {
        return sectionFreeText.get();
    }

    public SimpleStringProperty sectionFreeTextProperty() {
        return sectionFreeText;
    }

    public void setSectionFreeText(String sectionFreeText) {
        this.sectionFreeText.set(sectionFreeText);
    }


    // fields from class 'Medication'
    private SimpleStringProperty pharmacyCentralNumber = new SimpleStringProperty(this, "pharmacyCentralNumber");
    private SimpleStringProperty tradeName = new SimpleStringProperty(this, "tradeName");
    private SimpleStringProperty note = new SimpleStringProperty(this, "note");
    private SimpleStringProperty reason = new SimpleStringProperty(this, "reason");
    private SimpleObjectProperty<eIngestedStatus> ingestedStatus = new SimpleObjectProperty<>(this, "ingestedStatus");
    private SimpleObjectProperty<Date> ingestedTime = new SimpleObjectProperty<>(this, "ingestedTime");
    private SimpleStringProperty sectionTitle = new SimpleStringProperty(this, "sectionTitle");
    private SimpleObjectProperty<ArrayList<Date>> ingestedTimesUdef = new SimpleObjectProperty(this, "ingestedTimesUdef");

    private SimpleStringProperty sectionFreeText = new SimpleStringProperty(this, "sectionFreeText");
    private SimpleStringProperty status = new SimpleStringProperty(this, "status");
    private SimpleStringProperty assertedDate = new SimpleStringProperty(this, "assertedDate");
    private SimpleStringProperty wasNotTaken = new SimpleStringProperty(this, "wasNotTaken");
    private SimpleStringProperty referenceMedicationStatementFX = new SimpleStringProperty(this, "referenceMedicationStatement");
    private SimpleStringProperty referenceToMedicationFX = new SimpleStringProperty(this, "referenceToMedication");
    private SimpleObjectProperty<ActiveSubstance> activeSubstance = new SimpleObjectProperty<>(this, "activeSubstance");
    private SimpleListProperty<IngestionTime> ingestionTimes = new SimpleListProperty<>(this, "ingestionTimes");
    private SimpleStringProperty drugFormName = new SimpleStringProperty(this, "drugFormName");
    private SimpleStringProperty drugFormCode = new SimpleStringProperty(this, "drugFormCode");
    // custom field for dosage value and unit
    private SimpleStringProperty dosageInformationFX = new SimpleStringProperty(this, "dosageInformationFX");
    // fields from class 'IngestionTime'
    private SimpleStringProperty dosageValue = new SimpleStringProperty(this, "dosageValue");
    private SimpleStringProperty dosageUnit = new SimpleStringProperty(this, "dosageUnit");
    private SimpleStringProperty dosageUnitAsText = new SimpleStringProperty(this, "dosageUnitAsText");
    private SimpleStringProperty dosageFreeText = new SimpleStringProperty(this, "dosageFreeText");
    private SimpleObjectProperty<eIngestionTime> timeOfDosage = new SimpleObjectProperty<>(this, "timeOfDosage");
    private SimpleStringProperty informationOfNeeded = new SimpleStringProperty(this, "informationOfNeeded");
    private SimpleStringProperty referenceMedicationStatement = new SimpleStringProperty(this, "referenceMedicationStatement");
    private SimpleStringProperty referenceToMedication = new SimpleStringProperty(this, "referenceToMedication");


}
