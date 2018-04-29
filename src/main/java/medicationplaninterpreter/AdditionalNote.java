package medicationplaninterpreter;

/**
 * Contains the additional note and the free texts under the medication table.
 * Parameter of UKF: 'R'
 */
public class AdditionalNote {

    /**
     * Contains additional note and free texts
     */
    private String note;

    /**
     * Initialize empty variables
     */
    public AdditionalNote(){

        this.note = null;
    }

    /**
     * Get additional note and free texts under the medication table as String
     *
     * @return get additional note and free texts under the medication table as String
     */
    public String getNote() {
        return note;
    }

    /**
     * Set additional note and free texts under the medication table as String
     *
     * @param note set additional note and free texts under the medication table as String
     */
    public void setNote(final String note) {
        this.note = note;
    }
}
