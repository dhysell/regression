package scratchpad.jon;

public class SQL_Scripts {
	
	
//	SELECT GLClassCodeBAK2.*
//	from GLClassCodeBAK2
//	where GLClassCodeBAK2.HasQuestions is NULL
	
	
//	 select GLClassCodeBAK2.*
//	 from GLClassCodeBAK2
//	 where
//	 GLClassCodeBAK2.Code in (SELECT GLUWQuestions.ClassCode from GLUWQuestions where GLUWQuestions.ClassCode = GLClassCodeBAK2.Code)
	
	
	//10/19/2016
//	  UPDATE GLClassCodeBAK2
//	  set UseCounter = '0'
	
	
	//7/14/2016
	//gets the first instance of every unique element.
	//SELECT * FROM (SELECT * , row_number() over (partition by AgentUserName order by ID) as RowNumber from GeneratedPolicies) SOURCE where RowNumber = 1
	
	
	
	//7/22/2016
	//COPY ONE COLUMN FROM ONE TABLE TO ANOTHER TABLE
//	SELECT AddressesTemp.Address, AddressesTemp.Zip,AddressesTemp.TeritoryCode, AddressesBAK.Address, AddressesBAK.ZIP, AddressesBAK.TerritoryCode
//	FROM AddressesTemp INNER JOIN AddressesBAK
//	ON AddressesTemp.Zip = AddressesBAK.Zip
//
//	update AddressesBAK
//	SET AddressesBAK.TerritoryCode = AddressesTemp.TeritoryCode 
//	FROM AddressesTemp INNER JOIN AddressesBAK
//	ON AddressesTemp.Zip = AddressesBAK.Zip
//	where AddressesBAK.TerritoryCode is NULL
	
	

}
