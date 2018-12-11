package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineTripTransit {

	private int limit = 50000;
	private InlandMarineCPP.TripTransitCoverageForm_IH_00_78_Deductible deductible = InlandMarineCPP.TripTransitCoverageForm_IH_00_78_Deductible.FiveHundred;
	private InlandMarineCPP.TripTransitCoverageForm_IH_00_78_DistanceInMiles distanceInMiles = InlandMarineCPP.TripTransitCoverageForm_IH_00_78_DistanceInMiles.From_0_To_250;
	private List<InlandMarineCPP.InlandMarine_Cargo> cargoList = new ArrayList<InlandMarineCPP.InlandMarine_Cargo>();
	private LocalDate coverageBegins = LocalDate.now(); //TODO get system date
	private LocalDate coverageEnds = coverageBegins.plusDays(7);
	
	private boolean contractCarrier = true;
	private boolean byMessanger = true;
	private boolean byRailRoad = true;
	private boolean byAirCarrier = true;
	private boolean byYourVehicle = true;
	
	private List<CPPInlandMarineTripTransit_Trip> trips = new ArrayList<CPPInlandMarineTripTransit_Trip>();
	
	public CPPInlandMarineTripTransit(List<InlandMarineCPP.InlandMarine_Cargo> cargoList) {
		this.cargoList = cargoList;
		//Add two trips by default
		this.trips.add(new CPPInlandMarineTripTransit_Trip());
		this.trips.add(new CPPInlandMarineTripTransit_Trip());
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public InlandMarineCPP.TripTransitCoverageForm_IH_00_78_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.TripTransitCoverageForm_IH_00_78_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.TripTransitCoverageForm_IH_00_78_DistanceInMiles getDistanceInMiles() {
		return distanceInMiles;
	}

	public void setDistanceInMiles(InlandMarineCPP.TripTransitCoverageForm_IH_00_78_DistanceInMiles distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}

	public List<InlandMarineCPP.InlandMarine_Cargo> getCargoList() {
		return cargoList;
	}

	public void setCargoList(List<InlandMarineCPP.InlandMarine_Cargo> cargoList) {
		this.cargoList = cargoList;
	}

	public LocalDate getCoverageBegins() {
		return coverageBegins;
	}

	public void setCoverageBegins(LocalDate coverageBegins) {
		this.coverageBegins = coverageBegins;
	}

	public LocalDate getCoverageEnds() {
		return coverageEnds;
	}

	public void setCoverageEnds(LocalDate coverageEnds) {
		this.coverageEnds = coverageEnds;
	}

	public boolean isContractCarrier() {
		return contractCarrier;
	}

	public void setContractCarrier(boolean contractCarrier) {
		this.contractCarrier = contractCarrier;
	}

	public boolean isByMessanger() {
		return byMessanger;
	}

	public void setByMessanger(boolean byMessanger) {
		this.byMessanger = byMessanger;
	}

	public boolean isByRailRoad() {
		return byRailRoad;
	}

	public void setByRailRoad(boolean byRailRoad) {
		this.byRailRoad = byRailRoad;
	}

	public boolean isByAirCarrier() {
		return byAirCarrier;
	}

	public void setByAirCarrier(boolean byAirCarrier) {
		this.byAirCarrier = byAirCarrier;
	}

	public boolean isByYourVehicle() {
		return byYourVehicle;
	}

	public void setByYourVehicle(boolean byYourVehicle) {
		this.byYourVehicle = byYourVehicle;
	}

	public List<CPPInlandMarineTripTransit_Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<CPPInlandMarineTripTransit_Trip> trips) {
		this.trips = trips;
	}

}
