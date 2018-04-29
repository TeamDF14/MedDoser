package medicationplaninterpreter;

import help.Help;

/**
 * This class contains the characteristics of the doctor and the medical practice.
 * Parameter of UKF: 'A'
 */
public class Author {

    /**
     * Contains the lifelong number of the doctor
     */
    private String lifelongDoctorNumber;
    /**
     * Contains the pharmacy Identification number
     */
    private String pharmacyIdentificationNumber;
    /**
     * Contains the hospital Identification number
     */
    private String hospitalIdentificationNumber;
    /**
     * Contains the full name of the doctor
     */
    private String nameOfExhibitor;
    /**
     * Contains the road from the medical practice
     * In german it means 'Aussteller'
     */
    private String street;
    /**
     * Contains the postCode of the medical practice
     */
    private String postCode;
    /**
     * Contains the city of the medical practice
     */
    private String city;
    /**
     * Contains the telephone number of the medical practice
     */
    private String telephoneNumber;
    /**
     * Contains the eMail adress of the doctor
     */
    private String eMailAdress;
    /**
     * Contains the date of the medication plan
     */
    private String creationDate;

    /**
     * The constructor initializes the global variables
     */
    public Author(){
        this.lifelongDoctorNumber = null;
        this.pharmacyIdentificationNumber = null;
        this.hospitalIdentificationNumber = null;
        this.nameOfExhibitor = null;
        this.street = null;
        this.postCode = null;
        this.city = null;
        this.telephoneNumber = null;
        this.eMailAdress = null;
        this.creationDate = null;
    }

    /**
     * Return the lifelong doctor number as String
     *
     * @return Return the lifelong doctor number as String as String
     */
    public String getLifelongDoctorNumber() {
        return lifelongDoctorNumber;
    }

    /**
     * Set the lifelong doctor number as String
     *
     * @param lifelongDoctorNumber Set the lifelong doctor number as String
     */
    public void setLifelongDoctorNumber(final String lifelongDoctorNumber) {
        this.lifelongDoctorNumber = lifelongDoctorNumber;
    }

    /**
     * Return the pharmacy identification number of the doctor as String
     *
     * @return Return the pharmacy identification number of the doctor as  as String
     */
    public String getPharmacyIdentificationNumber() {
        return pharmacyIdentificationNumber;
    }

    /**
     * Set the pharmacy ID number as String
     *
     * @param pharmacyIdentificationNumber Set the pharmacy ID number as String
     */
    public void setPharmacyIdentificationNumber(final String pharmacyIdentificationNumber) {
        this.pharmacyIdentificationNumber = pharmacyIdentificationNumber;
    }

    /**
     * Return the pharmacy identification number
     *
     * @return Return the pharmacy identification number
     */
    public String getHospitalIdentificationNumber() {
        return hospitalIdentificationNumber;
    }

    /**
     * Set the hospital ID number as String
     *
     * @param hospitalIdentificationNumber Set the hospital ID number as String
     */
    public void setHospitalIdentificationNumber(String hospitalIdentificationNumber) {
        this.hospitalIdentificationNumber = hospitalIdentificationNumber;
    }

    /**
     * Return the full name of the doctor
     *
     * @return Return the full name of the doctor as String
     */
    public String getNameOfExhibitor() {
        return nameOfExhibitor;
    }

    /**
     * Set the full name of the doctor
     *
     * @param nameOfTheExhibitor Set the full name of the doctor
     */
    public void setNameOfExhibitor(final String nameOfTheExhibitor) {
        this.nameOfExhibitor = nameOfTheExhibitor;
    }

    /**
     * Returns the street of medical practice
     *
     * @return Returns the street of medical practice as String
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set the street of medical practice
     *
     * @return Set the street of medical practice
     */
    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * Returns the post code of medical practice
     *
     * @return Returns the post code of medical practice as String
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Set the post code of medical practice
     *
     * @return Set the post code of medical practice
     */
    public void setPostCode(final String postCode) {
        this.postCode = postCode;
    }

    /**
     * Returns the city of medical practice
     *
     * @return Returns the city of medical practice as String
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the city of medical practice
     *
     * @return Set the city of medical practice
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Returns the telephone number of medical practice
     *
     * @return Returns the telephone number of medical practice as String
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Set the telephone number of medical practice
     *
     * @param telephoneNumber Set the telephone number of medical practice
     */
    public void setTelephoneNumber(final String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Returns the eMail adress of the doctor
     *
     * @return Returns the eMail adress of doctor as String
     */
    public String geteMailAdress() {
        return eMailAdress;
    }

    /**
     * Set the eMail adress of the doctor
     *
     * @return Set the eMail adress of doctor
     */
    public void seteMailAdress(final String eMailAdress) {
        this.eMailAdress = eMailAdress;
    }

    /**
     * Returns creation date of the medication plan
     *
     * @return Returns creation date of the medication plan as String
     */
    public String getCreationDate() {

        String[] arrayTime = null;

        if(!util.String.isEmpty(this.creationDate)) {

            arrayTime = this.creationDate.split("T");
        }

        if(arrayTime != null){

            String[] date = arrayTime[0].split("-");

            return date[2] + "." + date[1] + "." + date[0];
        } else {
            return null;
        }
    }


    /**
     * Set creation date of the medication plan
     *
     * @return Set creation date of the medication plan as Date
     */
    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }
}
