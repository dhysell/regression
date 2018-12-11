package scratchpad.steve.utilities;

public class Queries {

/*
	SELECT *
	FROM dbo.AB_Users U
	INNER JOIN dbo.AB_UserRole UR ON UR.ABUser = U.UserName
	INNER JOIN dbo.AB_Role R ON R.RoleID = UR.ABRole
	INNER JOIN [dbo].[AB_PermissionsPerRole] PR ON PR.RoleID = R.RoleID
	INNER JOIN [dbo].[AB_Permissions] P ON P.PermissionID = PR.PermissionID
	
*/	
	
/*
 SELECT Distinct Role, Permission, UserName
	FROM dbo.AB_Users U
	INNER JOIN dbo.AB_UserRole UR ON UR.ABUser = U.UserName
	INNER JOIN dbo.AB_Role R ON R.RoleID = UR.ABRole
	INNER JOIN [dbo].[AB_PermissionsPerRole] PR ON PR.RoleID = R.RoleID
	INNER JOIN [dbo].[AB_Permissions] P ON P.PermissionID = PR.PermissionID
 */
	
	
	
	
}
