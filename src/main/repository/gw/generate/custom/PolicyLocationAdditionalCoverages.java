package repository.gw.generate.custom;

public class PolicyLocationAdditionalCoverages {

	private boolean moneyAndSecuritiesCoverage = false;
	private int moneySecNumMessengersCarryingMoney = 1;
	private boolean moneySecDepositDaily = true;
	private String moneySecHowOftenDeposit = "Often enough";
	private long moneySecOnPremisesLimit = 5000;
	private long moneySecOffPremisesLimit = 5000;
	private boolean outdoorSignsCoverage = false;
	private long outdoorSignsLimit = 10000;
	private boolean waterBackupAndSumpOverflow = false;

	public PolicyLocationAdditionalCoverages() {

	}

	public boolean isMoneyAndSecuritiesCoverage() {
		return moneyAndSecuritiesCoverage;
	}

	public void setMoneyAndSecuritiesCoverage(boolean moneyAndSecuritiesCoverage) {
		this.moneyAndSecuritiesCoverage = moneyAndSecuritiesCoverage;
	}

	public int getMoneySecNumMessengersCarryingMoney() {
		return moneySecNumMessengersCarryingMoney;
	}

	public void setMoneySecNumMessengersCarryingMoney(int moneySecNumMessengersCarryingMoney) {
		this.moneySecNumMessengersCarryingMoney = moneySecNumMessengersCarryingMoney;
	}

	public boolean isMoneySecDepositDaily() {
		return moneySecDepositDaily;
	}

	public void setMoneySecDepositDaily(boolean moneySecDepositDaily) {
		this.moneySecDepositDaily = moneySecDepositDaily;
	}

	public String getMoneySecHowOftenDeposit() {
		return moneySecHowOftenDeposit;
	}

	public void setMoneySecHowOftenDeposit(String moneySecHowOftenDeposit) {
		this.moneySecHowOftenDeposit = moneySecHowOftenDeposit;
	}

	public long getMoneySecOnPremisesLimit() {
		return moneySecOnPremisesLimit;
	}

	public void setMoneySecOnPremisesLimit(long moneySecOnPremisesLimit) {
		this.moneySecOnPremisesLimit = moneySecOnPremisesLimit;
	}

	public long getMoneySecOffPremisesLimit() {
		return moneySecOffPremisesLimit;
	}

	public void setMoneySecOffPremisesLimit(long moneySecOffPremisesLimit) {
		this.moneySecOffPremisesLimit = moneySecOffPremisesLimit;
	}

	public boolean isOutdoorSignsCoverage() {
		return outdoorSignsCoverage;
	}

	public void setOutdoorSignsCoverage(boolean outdoorSignsCoverage) {
		this.outdoorSignsCoverage = outdoorSignsCoverage;
	}

	public long getOutdoorSignsLimit() {
		return outdoorSignsLimit;
	}

	public void setOutdoorSignsLimit(long outdoorSignsLimit) {
		this.outdoorSignsLimit = outdoorSignsLimit;
	}

	public boolean isWaterBackupAndSumpOverflow() {
		return waterBackupAndSumpOverflow;
	}

	public void setWaterBackupAndSumpOverflow(boolean waterBackupAndSumpOverflow) {
		this.waterBackupAndSumpOverflow = waterBackupAndSumpOverflow;
	}

}
