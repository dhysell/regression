package repository.cc.framework.gw.cc.constants;

public class ExposureTypes {
    public static final String COLLISION = "Collision";
    public static final String COMPREHENSIVE = "Comprehensive";
    public static final String LIABILITY_ATUO_BODILY_INJURY = "Liability - Auto Bodily Injury";
    public static final String LIABILITY_AUTO_PROPERTY_DAMAGE = "Liability - Auto Property Damage";
    public static final String AUTO_PROPERTY_DAMAGE_PROPERTY_DAMAGE = "Auto Property Damage - Property Damage";
    public static final String AUTO_PROPERTY_DAMAGE_VEHICLE_DAMAGE = "Auto Property Damage - Vehicle Damage";
    public static final String MEDICAL_PAYMENTS = "Medical Payments";
    public static final String RENTAL_REIMBURSEMENT = "Rental Reimbursement";
    public static final String ROADSIDE_ASSISTANCE = "Roadside Assistance";
    public static final String UNDERINSURED_MOTORIST_BODILY_INJURY_DAMAGE = "Underinsured Motorist - Bodily Injury Damage";
    public static final String UNINSURED_MOTORIST_BODILY_INJURY_DAMAGE = "Uninsured Motorist - Bodily Injury Damage";

    public static String[] getAllExposureTypes() {
        return new String[]{COLLISION, COMPREHENSIVE, LIABILITY_ATUO_BODILY_INJURY, LIABILITY_AUTO_PROPERTY_DAMAGE, AUTO_PROPERTY_DAMAGE_PROPERTY_DAMAGE,
                AUTO_PROPERTY_DAMAGE_VEHICLE_DAMAGE, MEDICAL_PAYMENTS, RENTAL_REIMBURSEMENT, ROADSIDE_ASSISTANCE, UNDERINSURED_MOTORIST_BODILY_INJURY_DAMAGE, UNINSURED_MOTORIST_BODILY_INJURY_DAMAGE};
    }

}
