package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import org.fluttercode.datafactory.impl.DataFactory;
import persistence.globaldatarepo.entities.AddressTemp2;
import repository.gw.enums.AddressType;
import repository.gw.enums.Location;
import repository.gw.enums.PhoneType;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class AddressInfo {
    private DataFactory generateDataFactory = new DataFactory();

    private int number = 123;
    private String line1 = "1292 El Rancho Blvd";
    private String line2 = "";
    private String city = "Pocatello";
    private State state = State.Idaho;
    private String zip = "83201-2807";
    private String zip4 = "";
    private String county = "Bannock";
    private String country = "United States of America";
    private repository.gw.enums.AddressType type = repository.gw.enums.AddressType.Mailing;
    private String description = "Default Address";
    private String phoneBusiness = "2082394111";
    private String phoneWork = null;
    private String phoneHome = null;
    private String phoneMobile = null;
    private String phoneFax = null;
    private repository.gw.enums.PhoneType phonePrimary = repository.gw.enums.PhoneType.Business;
    private String phoneSpeedDial = "123456";
    private InternetAddress emailAddress = null;
    private Date validUntil = null;
    private ArrayList<repository.gw.generate.custom.AddressInfoDeliveryOption> deliveryOptionList = new ArrayList<repository.gw.generate.custom.AddressInfoDeliveryOption>();
    private boolean randomAddressInfo = false;
    private boolean isPrimaryAddress = false;
    private String optionalUniqueKeyword = null;

    private BigDecimal longitude;
    private BigDecimal latitude;
    private String protectionClass;
    private String territoryCode;
    private String postalCode;
    private String officeNumber;
    private boolean standardized;
    private AddressTemp2 dbAddress;//to be used for updating Database info only.


    public AddressInfo() {
        setEmailAddress("none@none.com");
    }

    public AddressInfo(Boolean random) {
        AddressTemp2 newAddress;
        try {
            newAddress = AddressTemp2.getRandomAddress();
            setLine1(newAddress.getAddressLine1());
            setLine2(newAddress.getAddressLine2());
            setCity(newAddress.getCity());
            setState(State.valueOf(newAddress.getState2()));
            setZip(newAddress.getZip());
            setZip4(newAddress.getZip4());
            setPostalCode(newAddress.getPostalCode());
            setCounty(newAddress.getCounty());
            setEmailAddress("none@none.com");
            setRandomAddressInfo(random);
            setLongitude(newAddress.getLongitudeFbm());
            setLatitude(newAddress.getLatitudeFbm());
            setProtectionClass(newAddress.getProtectionClass());
            setTerritoryCode(newAddress.getTerritoryCode());
            this.setDbAddress(newAddress);
        } catch (Exception e) {
            System.out.println("SOMETHING FAILED GETTING A RANDOM ADDRESS SO YOU ARE STUCK WITH COR'S ADDRESS");
        }
    }

    public AddressInfo(Location.ProtectionClassCode protetionClassCode) {
        AddressTemp2 newAddress;
        try {
            newAddress = AddressTemp2.getRandomAddressByProtectionClass(protetionClassCode.getValue());
            setLine1(newAddress.getAddressLine1());
            setLine2(newAddress.getAddressLine2());
            setCity(newAddress.getCity());
            setState(State.valueOf(newAddress.getState2()));
            setZip(newAddress.getZip());
            setZip4(newAddress.getZip4());
            setPostalCode(newAddress.getPostalCode());
            setCounty(newAddress.getCounty());
            setEmailAddress("none@none.com");
            setRandomAddressInfo(true);
            setLongitude(newAddress.getLongitudeFbm());
            setLatitude(newAddress.getLatitudeFbm());
            setProtectionClass(newAddress.getProtectionClass());
            setTerritoryCode(newAddress.getTerritoryCode());
            this.setDbAddress(newAddress);
        } catch (Exception e) {
            System.out.println("SOMETHING FAILED GETTING A RANDOM ADDRESS SO YOU ARE STUCK WITH COR'S ADDRESS");
        }
    }


    public AddressInfo(String line1, String line2, String city, State state, String zip, String county, String country,
                       repository.gw.enums.AddressType type) {
        setLine1(line1);
        setLine2(line2);
        setCity(city);
        setState(state);
        setZip(zip);
        if ((county != null) && (state == State.Idaho)) {
            setCounty(county);
        } else {
            setCounty("");
        }
        setCountry(country);
        setType(type);
        setEmailAddress("none@none.com");
    }

    public AddressInfo(String line1, String line2, String city, State state, String zip, CountyIdaho county,
                       String country, repository.gw.enums.AddressType type) {
        setLine1(line1);
        setLine2(line2);
        setCity(city);
        setState(state);
        setZip(zip);
        if ((county != null) && (state == State.Idaho)) {
            setCounty(county.getValue());
        } else {
            setCounty("");
        }
        setCountry(country);
        setType(type);
        setEmailAddress("none@none.com");
    }

    public AddressInfo(String line1, String city, State state, String zip) {
        setLine1(line1);
        setCity(city);
        setState(state);
        setZip(zip);
        setCounty("");
        setEmailAddress("none@none.com");
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) throws Exception {
        if (number < 1 || number > 999) {
            throw new Exception("Address Number Must be Between 001 and 999");
        }
        this.number = number;
    }

    public void setAddress(AddressInfo newAddress) {
        setLine1(newAddress.line1);
        setLine2(newAddress.line2);
        setCity(newAddress.city);
        setState(newAddress.state);
        setZip(newAddress.zip);
        setCounty(newAddress.county);
        setCountry(newAddress.country);
        setType(newAddress.type);
        setEmailAddress("none@none.com");
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public String getZipNoDashes() {
        return zip.replace("-", "");
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCounty(CountyIdaho county) {
        this.county = county.getValue();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public repository.gw.enums.AddressType getType() {
        return type;
    }

    public void setType(AddressType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String phoneFormatted(String phone) {
        phone = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);

        return phone;
    }

    public String getPhoneBusiness() {
        return this.phoneBusiness;
    }

    public String getPhoneBusinessFormatted() {
        return phoneFormatted(this.phoneBusiness);
    }

    public void setPhoneBusiness(String phoneBusiness) {
        this.phoneBusiness = phoneBusiness;
    }

    public String getPhoneHome() {
        return this.phoneHome;
    }

    public String getPhoneHomeFormatted() {
        return phoneFormatted(this.phoneHome);
    }

    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneWork() {
        return this.phoneWork;
    }

    public String getPhoneWorkFormatted() {
        return phoneFormatted(this.phoneWork);
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getPhoneMobile() {
        return this.phoneMobile;
    }

    public String getPhoneMobileFormatted() {
        return phoneFormatted(this.phoneMobile);
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhoneFax() {
        return this.phoneFax;
    }

    public String getPhoneFaxFormatted() {
        return phoneFormatted(this.phoneFax);
    }

    public void setPhoneFax(String phoneFax) {
        this.phoneFax = phoneFax;
    }

    public repository.gw.enums.PhoneType getPhonePrimary() {
        return phonePrimary;
    }

    public void setPhonePrimary(PhoneType phonePrimary) {
        this.phonePrimary = phonePrimary;
    }

    public String getPhoneSpeedDial() {
        return phoneSpeedDial;
    }

    public void setPhoneSpeedDial(String phoneSpeedDial) throws Exception {
        if (phoneSpeedDial.length() > 6) {
            throw new Exception("Phone Speed Dial Cannot Be Longer than 6 Digits");
        }
        this.phoneSpeedDial = phoneSpeedDial;
    }

    public InternetAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(InternetAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        try {
            this.emailAddress = new InternetAddress(emailAddress);
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public ArrayList<repository.gw.generate.custom.AddressInfoDeliveryOption> getDeliveryOptionList() {
        return deliveryOptionList;
    }

    public void setDeliveryOptionList(ArrayList<AddressInfoDeliveryOption> deliveryOptionList) {
        this.deliveryOptionList = deliveryOptionList;
    }

    public String getDropdownAddressFormat() {
        return getLine1() + ", " + getCity() + ", " + getState().getAbbreviation();
    }

    public void setRandomAddress() {
        line1 = generateDataFactory.getAddress();
        line2 = "";
        // city = generateDataFactory.getCity();
    }

    public boolean isRandomAddressInfo() {
        return randomAddressInfo;
    }

    public void setRandomAddressInfo(boolean randomAddressInfo) {
        this.randomAddressInfo = randomAddressInfo;
    }

    public void setIsPrimaryAddress(boolean _isPrimaryAddress) {
        this.isPrimaryAddress = _isPrimaryAddress;
    }

    public boolean getIsPrimaryAddress() {
        return this.isPrimaryAddress;
    }

    public String getAddresslisting() {
        return getLine1() + ", " + getCity() + ", " + getState().getAbbreviation();
    }

    public String getZip4() {
        return zip4;
    }

    public void setZip4(String zip4) {
        this.zip4 = zip4;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getProtectionClass() {
        return protectionClass;
    }

    public void setProtectionClass(String protectionClass) {
        this.protectionClass = protectionClass;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public boolean isStandardized() {
        return standardized;
    }

    public void setStandardized(boolean standardized) {
        this.standardized = standardized;
    }

    public void setPrimaryAddress(boolean isPrimaryAddress) {
        this.isPrimaryAddress = isPrimaryAddress;
    }

    /**
     * @author iclouser
     * @description removes all default values of person class. So it's essentially an empty person object. Primary Use for portals but can be used for everyone.
     * Uses java reflection.
     */
    public void removeDefaultValues() {
        Field[] addressInfoFields = AddressInfo.class.getDeclaredFields();
        for (Field field : addressInfoFields) {
            try {
                if (field.getType().getCanonicalName() == "boolean") {
                    field.set(this, false);
                } else if (field.getType().getCanonicalName() == "char") {
                    field.set(this, '\u0000');
                } else if ((field.getType().isPrimitive())) {
                    field.set(this, 0);
                } else {
                    field.set(this, null);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    public AddressTemp2 getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(AddressTemp2 dbAddress) {
        this.dbAddress = dbAddress;
    }
    
    public String getOptionalKeyword() {
        return this.optionalUniqueKeyword;
    }

    public void setOptionalKeyword(String _optionalUniqueKeyword) {
        this.optionalUniqueKeyword = _optionalUniqueKeyword;
    }
}
