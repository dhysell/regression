package repository.gw.generate.cc;


import com.idfbins.enums.YesOrNo;
import repository.gw.enums.PointOfFirstImpact;

public class BackendIncidentDetails {
    private YesOrNo              wasVehicleParked     = YesOrNo.No;
    private YesOrNo              safeToDrive          = YesOrNo.No;
    private YesOrNo              didAirbagDeploy      = YesOrNo.No;
    private YesOrNo              equipmentFailure     = YesOrNo.No;
    private repository.gw.enums.PointOfFirstImpact poi                  = repository.gw.enums.PointOfFirstImpact.LeftRear;

  

    public YesOrNo getWasVehicleParked() {
        return wasVehicleParked;
    }

    public void setWasVehicleParked(YesOrNo wasVehicleParked) {
        this.wasVehicleParked = wasVehicleParked;
    }

    public YesOrNo getSafeToDrive() {
        return safeToDrive;
    }

    public void setSafeToDrive(YesOrNo safeToDrive) {
        this.safeToDrive = safeToDrive;
    }

    public YesOrNo getDidAirbagDeploy() {
        return didAirbagDeploy;
    }

    public void setDidAirbagDeploy(YesOrNo didAirbagDeploy) {
        this.didAirbagDeploy = didAirbagDeploy;
    }

    public YesOrNo getEquipmentFailure() {
        return equipmentFailure;
    }

    public void setEquipmentFailure(YesOrNo equipmentFailure) {
        this.equipmentFailure = equipmentFailure;
    }

    

    public repository.gw.enums.PointOfFirstImpact getPoi() {
        return poi;
    }

    public void setPoi(repository.gw.enums.PointOfFirstImpact poi) {
        this.poi = poi;
    }

    public BackendIncidentDetails() {

    }
    
    // Use When Vehicle isn't towed to somewhere
    public BackendIncidentDetails(YesOrNo wasVehicleParked, YesOrNo safeToDrive, YesOrNo didAirbagDeploy, YesOrNo equipmentFailure, PointOfFirstImpact poi) {
        this.wasVehicleParked = wasVehicleParked;
        this.safeToDrive = safeToDrive;
        this.didAirbagDeploy = didAirbagDeploy;
        this.equipmentFailure = equipmentFailure;
        this.poi = poi;
    }
    
}
