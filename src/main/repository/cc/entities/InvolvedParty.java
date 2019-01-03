package repository.cc.entities;



public class InvolvedParty {

    public static class Builder {
        private String firstName = null;
        private String lastName = null;
        private String ssn = null;
        private String dateOfBirth = null;
        private String gender = null;
        private String role = null;
        private String addressOne = null;
        private String city = null;
        private String state = null;
        private String zip = null;
        private String workPhone = null;

        public InvolvedParty build() {
            return new InvolvedParty(this);
        }

        public Builder() {

        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withSSN(String ssn) {
            this.ssn = ssn;
            return this;
        }

        public Builder withDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Builder withAddressOne(String addressOne) {
            this.addressOne = addressOne;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Builder withWorkPhone(String workPhone) {
            this.workPhone = workPhone;
            return this;
        }
    }

    private String firstName = null;
    private String lastName = null;
    private String ssn = null;
    private String dateOfBirth = null;
    private String gender = null;
    private String role = null;
    private String addressOne = null;
    private String city = null;
    private String state = null;
    private String zip = null;
    private String workPhone = null;

    public InvolvedParty(Builder builderDetails) {
        this.firstName = builderDetails.firstName;
        this.lastName = builderDetails.lastName;
        this.ssn = builderDetails.ssn;
        this.dateOfBirth = builderDetails.dateOfBirth;
        this.gender = builderDetails.gender;
        this.role = builderDetails.role;
        this.addressOne = builderDetails.addressOne;
        this.city = builderDetails.city;
        this.state = builderDetails.state;
        this.zip = builderDetails.zip;
        this.workPhone = builderDetails.workPhone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getWorkPhone() {
        return workPhone;
    }
}
