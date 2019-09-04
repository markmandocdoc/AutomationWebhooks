import java.util.List;
import MAP_API.a_Tools;
import csv_reader.CSVHelper;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class WebHooks {
	
	private String base = CSVHelper.getApiUrl();
	
	/********************************************************************
	 * Description: Cancels subscription for current set of credentials 
	 * 				through API call for any scenarios tagged as RU2RS
	 * Param:NA
	 * Returns:NA
	 * Status: Completed
	 ********************************************************************/
	@After("@RU2RS")
	public void CancelSubscriptionForRS(){
		String email = CSVHelper.getRuEmail();
		String muid;
		List<String> productID;
				
		muid = a_Tools.SearchByEmail_GetMuid(base, email);	
		productID = a_Tools.SearchByEmail_GetMapProductID(base,email);	
			
		for(int i=0;i<productID.size();i++){
			System.out.println("Canceling subscription for muid: " + muid + " and Product id: " + productID.get(i));
			a_Tools.CancelUsersEntitlements(base, muid,productID.get(i));
		}
		
		System.out.println("Completed cancelation");
	}
	
	/********************************************************************
	 * Description: Cancels subscription for current set of credentials 
	 * 				through API call for any scenarios tagged as RU2RSMonthly
	 * Param:NA
	 * Returns:NA
	 * Status: Completed
	 ********************************************************************/
	@After("@RU2RSMonthly")
	public void CancelSubscriptionForRSMonthly(){
		String email = CSVHelper.getRuMonthlyEmail();
		String muid;
		List<String> productID;
				
		muid = a_Tools.SearchByEmail_GetMuid(base, email);	
		productID = a_Tools.SearchByEmail_GetMapProductID(base,email);	
			
		for(int i=0;i<productID.size();i++){
			System.out.println("Canceling subscription for muid: " + muid + " and Product id: " + productID.get(i));
			a_Tools.CancelUsersEntitlements(base, muid,productID.get(i));
		}
		
		System.out.println("Completed cancelation");
	}
	
	/********************************************************************
	 * Description: Resets user info after test cases marked with tag 
	 * 				@userInfoChange
	 * Param:NA
	 * Returns:NA
	 * Status: Completed
	 ********************************************************************/
	@After("@UserInfoChange")
	public void ResetUserInfo(){
		String email = CSVHelper.getRuEmail();
		String firstName = CSVHelper.getRuFirstName();
		String lastName = CSVHelper.getRuLastName();
		String phoneNumber = "";
		String password = CSVHelper.getRuPassword();
		String muid;
				
		muid = a_Tools.SearchByEmail_GetMuid(base, email);	
		
		System.out.println("Reseting User Info");
		a_Tools.UpdateMapUserInfo(base, muid, email, firstName, lastName, phoneNumber, password);
		System.out.println("Completed User Info Reset");
	}
	
	/********************************************************************
	 * Description: Creates user for scenarios tagged @UserRequired
	 * Param:NA
	 * Returns:NA
	 * Status: Completed
	 ********************************************************************/
	@Before("@UserRequired")
	public void CreateUser(){
		String email = CSVHelper.getRuEmail();
		String password = CSVHelper.getRuPassword();
		String firstName = CSVHelper.getRuFirstName();
		String lastName = CSVHelper.getRuLastName();
		String phoneNumber = "";
		
		a_Tools.authenticateUser(base, email, password);
		String muid = a_Tools.SearchByEmail_GetMuid(base, email);
		
		if (muid == null)
		{
			System.out.println("Creating User: " + email);
			a_Tools.CreateUser(base, email, password, firstName, lastName, phoneNumber);
			System.out.println("Completed User Creation");
		}
	}

	/********************************************************************
	 * Description: Deletes user for scenarios tagged @UserRequired
	 * Param:NA
	 * Returns:NA
	 * Status: Completed
	 ********************************************************************/
	@After("@DeleteUserAfter")
	public void DeleteUser(){
		String email = CSVHelper.getRuEmail();
		String muid = a_Tools.SearchByEmail_GetMuid(base, email);
		
		System.out.println("Deleting User: " + email + " - MUID: " + muid);
		a_Tools.DeleteMapUser(base, muid, email);
		System.out.println("Completed User Deletion");
	}
	
}
