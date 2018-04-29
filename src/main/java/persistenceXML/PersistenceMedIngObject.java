package persistenceXML;

import enums.eIngestedStatus;
import enums.eIngestionTime;

import java.util.Date;

/**
 * This class contains detailed information about the medication ingestion.
 */
public class PersistenceMedIngObject {

    /**
     * It contains the medication statement reference
     */
    private String medStatementReference;
    /**
     * It contains the code scheduled time of medication
     */
    private eIngestionTime scheduledTimeCode;
    /**
     * It contains the  scheduled time of medication
     */
    private Date scheduledTime;
    /**
     * It contains the status of type eIngestedStatus
     */
    private eIngestedStatus status;
    /**
     * It contains the scheduled date
     */
    private Date scheduledDate;
    /**
     * It contains the time of ingestion
     */
    private Date time;

    /**
     * Initialize empty variables
     */
    public PersistenceMedIngObject(){
        // Initialize variables
        this.medStatementReference = null;
        this.scheduledTime = null;
        this.scheduledTimeCode = null;
        this.scheduledDate = null;
        this.time = null;
        this.status = eIngestedStatus.DEFAULT;
    }

    /**
     * It returns the reference statement of medication
     *
     * @return the reference statement of medication
     */
    public String getMedStatementReference() {
        return medStatementReference;
    }

    /**
     * It sets the reference statement of medication
     *
     * @param medStatementReference It expect the reference statement of medication
     */
    public void setMedStatementReference(String medStatementReference) {
        this.medStatementReference = medStatementReference;
    }

    /**
     * It returns the scheduled time of medication
     *
     * @return the scheduled time of medication
     */
    public eIngestionTime getScheduledTimeCode() {
        return scheduledTimeCode;
    }

    /**
     * It sets the scheduled time of medication
     *
     * @param scheduledTimeCode It expect the scheduled time of medication
     */
    public void setScheduledTimeCode(eIngestionTime scheduledTimeCode) {
        this.scheduledTimeCode = scheduledTimeCode;
    }

    /**
     * It returns the scheduled date of medication
     *
     * @return the scheduled date of medication
     */
    public Date getScheduledDate() {
        return scheduledDate;
    }

    /**
     * It sets the scheduled date of medication
     *
     * @param scheduledDate It expect the scheduled date of medication
     */
    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    /**
     * It returns the ingestion time of medication
     *
     * @return the ingestion time of medication
     */
    public Date getTime() {
        return time;
    }

    /**
     * It sets the ingestion time of medication
     *
     * @param time It expect the ingestion time of medication
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * It returns the ingestion status of medication
     *
     * @return the ingestion status of medication
     */
    public eIngestedStatus getStatus() {
        return status;
    }

    /**
     * It sets the ingestion status of medication
     *
     * @param status it expect the ingestion medication of ingestion
     */
    public void setStatus(eIngestedStatus status) {
        this.status = status;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
