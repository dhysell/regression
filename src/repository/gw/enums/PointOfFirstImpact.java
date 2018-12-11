package repository.gw.enums;

public enum PointOfFirstImpact {
    RightFront("Right Front"),
    RightFrontPillar("Right Front Pillar"),
    RightTBONE("Right T-Bone"),
    RightQuarterPost("Right Quarter Post"),
    RightRear("Right Rear"),
    Rear("Rear"),
    LeftRear("Left Rear"),
    LeftQuarterPost("Left Quarter Post"),
    LeftTBONE("Left T-Bone"),
    LeftFrontPillar("Left Front Pillar"),
    LeftFront("Left Front"),
    Front("Front"),
    HoodRoofWindShield("Hood / Roof / Windshield"),
    Undercarriage("Undercarriage"),
    Other("Other");
    
    private String poi;
    
    PointOfFirstImpact(String poi) {
        this.poi = poi;
    }
    
    public String getPOI(){
        return poi;
    }
    
    public static PointOfFirstImpact stringToEnum(String text) {
        if( text != null) {
            for( PointOfFirstImpact poi : PointOfFirstImpact.values()){
                if(text.equalsIgnoreCase(poi.getPOI())){
                    return poi;
                }
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
