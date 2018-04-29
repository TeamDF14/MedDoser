package persistenceSQL;

public class SectionInfo {

    /**
     * Contains the code of the section, e.g. '418'.
     */
    private int sectionCode;

    /**
     * Contains the title of the section (if defined), e.g. 'Bedarfsmedikation'.
     */
    private String sectionTitle;

    public SectionInfo(int sectionCode, String sectionTitle) {
        this.sectionCode = sectionCode;
        this.sectionTitle = sectionTitle;
    }

    /**
     * Get the code of the section.
     * @return The code of the section, e.g. '418'.
     */
    public int getSectionCode() {
        return sectionCode;
    }

    /**
     * Set the code of the section.
     * @param sectionCode The code of the section, e.g. '418'.
     */
    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    /**
     * Get the title of the section.
     * @return The title of the section, e.g. 'Bedarfsmedikation'.
     */
    public String getSectionTitle() {
        return sectionTitle;
    }

    /**
     * Set the title of a section.
     * @param sectionTitle The title of a section, e.g. 'Bedarfsmedikation'.
     */
    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }


}
