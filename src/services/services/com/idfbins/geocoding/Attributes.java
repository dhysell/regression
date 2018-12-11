package services.services.com.idfbins.geocoding;

import java.util.HashMap;
import java.util.Map;

public class Attributes {

	private Double Score;
	private String MatchAddr;
	private String Side;
	private String StAddr;
	private String AddNum;
	private String AddNumFrom;
	private String AddNumTo;
	private String StPreDir;
	private String StPreType;
	private String StName;
	private String StType;
	private String StDir;
	private String City;
	private String Region;
	private String RegionAbbr;
	private String Postal;
	private String Country;
	private String LangCode;
	private String AddrType;
	private Double Distance;
	private Double X;
	private Double Y;
	private Double DisplayX;
	private Double DisplayY;
	private Double Xmin;
	private Double Xmax;
	private Double Ymin;
	private Double Ymax;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Double getScore() {
		return Score;
	}

	public void setScore(Double Score) {
		this.Score = Score;
	}

	public String getMatchAddr() {
		return MatchAddr;
	}

	public void setMatchAddr(String MatchAddr) {
		this.MatchAddr = MatchAddr;
	}

	public String getSide() {
		return Side;
	}

	public void setSide(String Side) {
		this.Side = Side;
	}

	public String getStAddr() {
		return StAddr;
	}

	public void setStAddr(String StAddr) {
		this.StAddr = StAddr;
	}

	public String getAddNum() {
		return AddNum;
	}

	public void setAddNum(String AddNum) {
		this.AddNum = AddNum;
	}

	public String getAddNumFrom() {
		return AddNumFrom;
	}

	public void setAddNumFrom(String AddNumFrom) {
		this.AddNumFrom = AddNumFrom;
	}

	public String getAddNumTo() {
		return AddNumTo;
	}

	public void setAddNumTo(String AddNumTo) {
		this.AddNumTo = AddNumTo;
	}

	public String getStPreDir() {
		return StPreDir;
	}

	public void setStPreDir(String StPreDir) {
		this.StPreDir = StPreDir;
	}

	public String getStPreType() {
		return StPreType;
	}

	public void setStPreType(String StPreType) {
		this.StPreType = StPreType;
	}

	public String getStName() {
		return StName;
	}

	public void setStName(String StName) {
		this.StName = StName;
	}

	public String getStType() {
		return StType;
	}

	public void setStType(String StType) {
		this.StType = StType;
	}

	public String getStDir() {
		return StDir;
	}

	public void setStDir(String StDir) {
		this.StDir = StDir;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String City) {
		this.City = City;
	}

	public String getRegion() {
		return Region;
	}

	public void setRegion(String Region) {
		this.Region = Region;
	}

	public String getRegionAbbr() {
		return RegionAbbr;
	}

	public void setRegionAbbr(String RegionAbbr) {
		this.RegionAbbr = RegionAbbr;
	}

	public String getPostal() {
		return Postal;
	}

	public void setPostal(String Postal) {
		this.Postal = Postal;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String Country) {
		this.Country = Country;
	}

	public String getLangCode() {
		return LangCode;
	}

	public void setLangCode(String LangCode) {
		this.LangCode = LangCode;
	}

	public String getAddrType() {
		return AddrType;
	}

	public void setAddrType(String AddrType) {
		this.AddrType = AddrType;
	}

	public Double getDistance() {
		return Distance;
	}

	public void setDistance(Double Distance) {
		this.Distance = Distance;
	}

	public Double getX() {
		return X;
	}

	public void setX(Double X) {
		this.X = X;
	}

	public Double getY() {
		return Y;
	}

	public void setY(Double Y) {
		this.Y = Y;
	}

	public Double getDisplayX() {
		return DisplayX;
	}

	public void setDisplayX(Double DisplayX) {
		this.DisplayX = DisplayX;
	}

	public Double getDisplayY() {
		return DisplayY;
	}

	public void setDisplayY(Double DisplayY) {
		this.DisplayY = DisplayY;
	}

	public Double getXmin() {
		return Xmin;
	}

	public void setXmin(Double Xmin) {
		this.Xmin = Xmin;
	}

	public Double getXmax() {
		return Xmax;
	}

	public void setXmax(Double Xmax) {
		this.Xmax = Xmax;
	}

	public Double getYmin() {
		return Ymin;
	}

	public void setYmin(Double Ymin) {
		this.Ymin = Ymin;
	}

	public Double getYmax() {
		return Ymax;
	}

	public void setYmax(Double Ymax) {
		this.Ymax = Ymax;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
