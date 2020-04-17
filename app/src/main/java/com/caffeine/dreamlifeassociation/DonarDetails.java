package com.caffeine.dreamlifeassociation;

public class DonarDetails {
    String UID, Name, BloodGroup, Gender, District, DOB, LastDonation, Number, SearchText;

    public DonarDetails(){}

    public DonarDetails(String UID, String name, String bloodGroup, String gender, String district, String DOB, String lastDonation, String number, String SearchText) {
        this.UID = UID;
        Name = name;
        BloodGroup = bloodGroup;
        Gender = gender;
        District = district;
        this.DOB = DOB;
        LastDonation = lastDonation;
        this.SearchText = SearchText;
        Number = number;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getLastDonation() {
        return LastDonation;
    }

    public void setLastDonation(String lastDonation) {
        LastDonation = lastDonation;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getSearchText() {
        return SearchText;
    }

    public void setSearchText(String searchText) {
        SearchText = searchText;
    }
}
