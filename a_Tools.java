import java.util.List;

import org.json.simple.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class a_Tools {
			
	/********************************************************************
	 * Description: Uses the API call SearchByEmail and returns the muid 
	 * 				for the appropriate email
	 * Param: String base, String email
	 * Returns: String muid
	 * Status: Complete
	 ********************************************************************/
	public static String SearchByEmail_GetMuid(String base, String email){
		RestAssured.baseURI = base;
		String SearchByEmailEndPoint = "/services/1/tools/map/users?email="+email;
		String muid = null;
		
		try{
			RequestSpecification httRequest = RestAssured.given();
			Response response = httRequest.get(SearchByEmailEndPoint);
			JsonPath jsonPathEvaluator = response.jsonPath();
			muid = jsonPathEvaluator.get("profile.map_user_id");
		}
		catch(Exception e){
			System.out.println("User not found");
		}
		return muid;
	}
	
	/********************************************************************
	 * Description: Uses the API call SearchByEmail and returns the map 
	 * 				productid for the appropriate email
	 * Param: String base, String email
	 * Returns:String productID
	 * Status: Complete
	 ********************************************************************/
	public static List<String> SearchByEmail_GetMapProductID(String base, String email){
		RestAssured.baseURI = base;
		String SearchByEmailEndPoint = "/services/1/tools/map/users?email="+email;
		List<String> productID;
		
		RequestSpecification httRequest = RestAssured.given();
		Response response = httRequest.get(SearchByEmailEndPoint);
		JsonPath jsonPathEvaluator = response.jsonPath();

		productID = jsonPathEvaluator.get("subscriptions.map_product_id");	
		return productID;
	}
	
	/********************************************************************
	 * Description: Puts desired muid and productID into the CancelUserEntitlement 
	 * 				API call and makes calls
	 * Param: String base, muid, productID
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	@SuppressWarnings("unchecked")
	public static void CancelUsersEntitlements(String base, String muid, String productID){
		RestAssured.baseURI = base;
		String CancelUsersEntitlementsEndPoint = "/services/1/tools/map/users/"+ muid +"/entitlements/";
		
		RequestSpecification httRequest = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		
		requestParams.put("map_product_id",productID);
		httRequest.header("Content-Type", "application/json");
		httRequest.body(requestParams.toJSONString());
		Response response = httRequest.delete(CancelUsersEntitlementsEndPoint);	
		System.out.println(response.body().asString());
	}
		
	/********************************************************************
	 * Description: Puts desired muid and productID into the CancelUserEntitlement 
	 * 				API call and makes calls
	 * Param: String base, muid, productID
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	@SuppressWarnings("unchecked")
	public static void UpdateMapUserInfo(String base, String muid, String email, 
						String firstName, String lastName, String phoneNumber, String password){
		
		RestAssured.baseURI = base;
		String UpdateMapUserInfoEndpoint = "/services/1/tools/map/users/"+ muid +"/";
		
		RequestSpecification httRequest = RestAssured.given();
		JSONObject requestParams = new JSONObject();
			
		requestParams.put("identity_provider","stormpath");
		requestParams.put("first_name",firstName);
		requestParams.put("last_name",lastName);
		requestParams.put("phone_number",phoneNumber);
		requestParams.put("password",password);
		
		httRequest.header("Content-Type", "application/json");
		httRequest.body(requestParams.toJSONString());
		Response response = httRequest.patch(UpdateMapUserInfoEndpoint);	
		System.out.println(response.body().asString());
	}
	
	/********************************************************************
	 * Description: Delete user
	 * Param: String base, muid, email
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	public static void DeleteMapUser(String base, String muid, String email)
	{
		RestAssured.baseURI = base;
		String deleteMapUserEndPoint = "/services/1/tools/map/users/" + muid + "?email=" + email;

		RequestSpecification httRequest = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		
		httRequest.header("Content-Type", "application/json");
		httRequest.body(requestParams.toJSONString());
		Response response = httRequest.delete(deleteMapUserEndPoint);	
		System.out.println(response.body().asString());
	}
	
	/********************************************************************
	 * Description: Authenticate user
	 * Param: String base, email, password
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	@SuppressWarnings("unchecked")
	public static void authenticateUser(String base, String email, String password)
	{
		RestAssured.baseURI = base;
		String createWriUserEndPoint = "/services/1/user_composite/authenticate/";

		RequestSpecification httRequest = RestAssured.given();
		JSONObject requestParams = new JSONObject();

		requestParams.put("email",email);
		requestParams.put("password",password);
		
		httRequest.header("Content-Type", "application/json");
		httRequest.body(requestParams.toJSONString());
		Response response = httRequest.post(createWriUserEndPoint);	
		System.out.println(response.body().asString());
	}
	
	/********************************************************************
	 * Description: Create user
	 * Param: String base, email, password
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	@SuppressWarnings("unchecked")
	public static void CreateUser(String base, String email, 
			String password, String firstName, String lastName,String phoneNumber)
	{
		RestAssured.baseURI = base;
		String createWriUserEndPoint = "/services/1/user_composite/users/";

		RequestSpecification httRequest = RestAssured.given();
		JSONObject requestParams = new JSONObject();

		requestParams.put("email", email);
		requestParams.put("password", password);
		requestParams.put("first_name", firstName);
		requestParams.put("last_name", lastName);
		requestParams.put("phone_number", phoneNumber);

		httRequest.header("Content-Type", "application/json");
		httRequest.body(requestParams.toJSONString());
		Response response = httRequest.post(createWriUserEndPoint);
		System.out.println(response.body().asString());
	}

}
