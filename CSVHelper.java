import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class CSVHelper {

	/*********************Initialization Variables***********************/
	private static String target;
	private static HashMap<String, String> data;
	private static String baseCsvFilePath = "src/test/resources/dataSources/";
	private static String nativeAppCsv;
	private static String currentRuEmail = "";

	/********************************************************************
	 * Description: Generates data hash map and target filename from
	 * 				CSV files if not yet initialized. Then locates the
	 * 				"baseURL" key in the map and returns the value
	 * Param: NA
	 * Returns: String baseURL
	 * Status: Complete
	 ********************************************************************/
	public static String getBaseUrl() {
		return getKey("baseURL");
	}

	/********************************************************************
	 * Description: Generates data hash map and target filename from
	 * 				CSV files if not yet initialized. Then locates the
	 * 				"apiURL" key in the map and returns the value
	 * Param: NA
	 * Returns: String apiURL
	 * Status: Complete
	 ********************************************************************/
	public static String getApiUrl() {
		return getKey("apiURL");
	}
	
	/********************************************************************
	 * Description: Generates data hash map and target filename from
	 * 				CSV files if not yet initialized. Then locates the
	 * 				"ruEmail" key in the map and returns the value
	 * Note: "ru" must be lower case or the API can't match the email
	 * to the MUID. This was found through failures on Jenkins.
	 * Param: NA
	 * Returns: String ruEmail
	 * Status: Complete
	 ********************************************************************/
	public static String getRuEmail() {
		if(!currentRuEmail.isEmpty()) return currentRuEmail;
		String timestamp = DateTimeFormatter.ofPattern("MMddyyyyHHmmss").format(java.time.LocalDateTime.now());
		currentRuEmail = "ru" + timestamp + "@wri.com";	
		if (!getKey("ruEmail").equals("use-dynamic-email")) currentRuEmail = getKey("ruEmail");
		return currentRuEmail;
	}
	
	/********************************************************************
	 * Description: Resets the currentEmail
	 * Param: NA
	 * Returns: String ruEmail
	 * Status: Complete
	 ********************************************************************/
	public static void deleteRuEmail() {
		if (!currentRuEmail.equals(getKey("ruEmail"))) currentRuEmail = "";
	}

	/********************************************************************
	 * Description: Generates data hash map and target filename from
	 * 				CSV files if not yet initialized. Then locates the
	 * 				"ruPassword" key in the map and returns the value
	 * Param: NA
	 * Returns: String ruPassword
	 * Status: Complete
	 ********************************************************************/
	public static String getRuPassword() {
		return getKey("ruPassword");
	}

	/********************************************************************
	 * Description: Generates data hash map and target filename from
	 * 				CSV files if not yet initialized. Then locates the
	 * 				key in the hash map and returns the string value
	 * Param: String key
	 * Returns: String value
	 * Status: Complete
	 ********************************************************************/
	public static String getKey(String key) {
		
		if (target == null) target = baseCsvFilePath + getTargetFile();
		if (data == null) loadCSV();
		
		if (data.get(key) == null) return "ERROR: Key not found";
		
		return data.get(key);
		
	}

	/********************************************************************
	 * Description: Sets CSV for native app automation
	 * Param: String
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	public static void setNativeAppCsv(String name) {
		if (nativeAppCsv == null) nativeAppCsv = name;
	}
	
	/********************************************************************
	 * Description: Iterates through a CSV file and populates hash map
	 * Param: NA
	 * Returns: NA
	 * Status: Complete
	 ********************************************************************/
	public static void loadCSV() {
		
		data = new HashMap<String, String>();
		String line;
		String [] split;
		
		if (nativeAppCsv != null)
			target = baseCsvFilePath + nativeAppCsv;
		
		if (System.getProperty("csvTargetFile") != null)
			target = baseCsvFilePath + System.getProperty("csvTargetFile");
		
		try (BufferedReader br = new BufferedReader(new FileReader(target))) {
		
			System.out.println("Reading from " + target);
			while ((line = br.readLine()) != null) {
				split = line.split(",");
				String key = split[0];
				String value = split[1];
				
				if (System.getProperty(key) != null) {
					value = System.getProperty(key);
					System.out.println("Overriding key '" + key + "' with value '" + value + "'");
				}
				
				data.put(key,value);
			}
			
		} catch (IOException e) {
			System.out.println("\n***** ERROR: CSV not found. Verify file name in externalization.properties *****\n");
		}
		
	}

	/********************************************************************
	 * Description: Reads externalization.properties and returns the
	 * 				filename for the target CSV file
	 * Param: NA
	 * Returns: String filename
	 * Status: Complete
	 ********************************************************************/
	public static String getTargetFile() {
		
		String line;
		
		try (BufferedReader br = new BufferedReader(new FileReader("externalization.properties"))) {
			if ((line = br.readLine()) != null) {
				String [] split = line.split("=");
				if (split[1] != null)
					return split[1];
			}
		} catch (IOException e) {
			System.out.println("\n***** ERROR: Externalization properties missing. Verify externalization.properties is in the root folder. *****\n");
		}
		
		return "";
		
	}

}