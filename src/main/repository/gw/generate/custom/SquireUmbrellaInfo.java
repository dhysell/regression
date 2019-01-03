package repository.gw.generate.custom;

import repository.gw.enums.SquireUmbrellaIncreasedLimit;

public class SquireUmbrellaInfo extends PolicyCommon {


	private repository.gw.enums.SquireUmbrellaIncreasedLimit increasedLimit = repository.gw.enums.SquireUmbrellaIncreasedLimit.Limit_1000000;
	private repository.gw.generate.custom.SquireUmbrellaInfoExclusionsConditions exclusionsConditions = null;

	public repository.gw.enums.SquireUmbrellaIncreasedLimit getIncreasedLimit() {
		return increasedLimit;
	}

    public void setIncreasedLimit(SquireUmbrellaIncreasedLimit increasedLimit) {
		this.increasedLimit = increasedLimit;
	}

	public repository.gw.generate.custom.SquireUmbrellaInfoExclusionsConditions getExclusionsConditions() {
		return exclusionsConditions;
	}

	public void setExclusionsConditions(SquireUmbrellaInfoExclusionsConditions exclusionsConditions) {
		this.exclusionsConditions = exclusionsConditions;
	}
}

















































