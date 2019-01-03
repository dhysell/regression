package repository.gw.generate.custom;

public class CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket {

	private CPPCommercialPropertyProperty property;
	private int camerasBlanketLimit = 1000;
	private int musicalInstrumentsBlanketLimit = 1000;
	
	public CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public int getCamerasBlanketLimit() {
		return camerasBlanketLimit;
	}

	public void setCamerasBlanketLimit(int camerasBlanketLimit) {
		this.camerasBlanketLimit = camerasBlanketLimit;
	}

	public int getMusicalInstrumentsBlanketLimit() {
		return musicalInstrumentsBlanketLimit;
	}

	public void setMusicalInstrumentsBlanketLimit(int musicalInstrumentsBlanketLimit) {
		this.musicalInstrumentsBlanketLimit = musicalInstrumentsBlanketLimit;
	}

	public CPPCommercialPropertyProperty getProperty() {
		return property;
	}

	public void setProperty(CPPCommercialPropertyProperty property) {
		this.property = property;
	}
	
}
