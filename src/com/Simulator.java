package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Simulator implements Runnable {
	private static OkHttpClient httpClient = new OkHttpClient();
	volatile static List<Alarm> alarmslist = new ArrayList<>();
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		Simulator sm = new Simulator();
		
		System.out.println("FireAlarm SIMULATION----------------");
		System.out.println("Press y and enter to start----------");
		String choice = sc.nextLine();
		
		if(choice.equalsIgnoreCase("y")) {
			Thread thread = new Thread(sm);
			thread.start();
		}
	}

	@Override
	public void run() {
		for(;;) {
			try {
				System.out.println("Loading alarms...");
				Thread.sleep(5000);
				List<Alarm> list = new ArrayList<>();
				try {
					list.addAll(setAlarmsList());
					
					System.out.println("Setting values...");
					Thread.sleep(5000);
					
					int smokeRandom = getRandomInteger(10);
					int co2Random = getRandomInteger(10);
					int index = getRandomInteger(alarmslist.size());
					
					Alarm a = alarmslist.get(index);	
					
					System.out.println("Random Smoke value = "+smokeRandom);
					System.out.println("Random CO2 value   = "+co2Random);
					System.out.println("Selected alarm     = "+a.getAid());
					
					if(smokeRandom > 5 || co2Random > 5) a.setIsActive(1);
					else a.setIsActive(0);
						
					Thread.sleep(5000);
					
					a.setSmokeLevel(smokeRandom);
					a.setCo2Level(co2Random);
					
					Thread.sleep(5000);
					
					System.out.println("Saving the data...");
					updateAlarm(a);
					sendEmail(a);
					
					Thread.sleep(5000);
					
					System.out.println("\nPlease Wait for next...\n");
				} 
				catch (JSONException | IOException e) {
					e.printStackTrace();
				}
				Thread.sleep(5000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendEmail(Alarm a) {
		if(a.getSmokeLevel() > 5) {
			System.out.println("\n-------------------------------------------------------\n");
			System.out.println("Notification by FireAlarm------------------------------\n");
			System.out.println("Fire Alert due to smoke levels in floor of "+a.getLid().substring(0, 5)+" and room "+a.getLid().substring(5, 10)+". Please go to a safer area.");
			System.out.println("-------------------------------------------------------\n");
			System.out.println("Sending email to "+a.getEmail());			
		}
		if(a.getCo2Level() > 5) {
			System.out.println("\n-------------------------------------------------------\n");
			System.out.println("Notification by FireAlarm------------------------------\n");
			System.out.println("High CO2 levels in floor of "+a.getLid().substring(0, 5)+" and room "+a.getLid().substring(5, 10)+". Please go to a safer area.");
			System.out.println("-------------------------------------------------------\n");
			System.out.println("Sending email to "+a.getEmail());
		}
	}

	public static int getRandomInteger(int max) {
		int i = new Random().nextInt(max);
		if(i != 0)
			return i;
		else
			return getRandomInteger(max);
	}
	
	public static JSONArray getAlarms() throws IOException, JSONException {
		JSONArray alarms = null;
		Request request = new Request.Builder()
        .url("http://localhost:8080/FireAlarmRest/rest/AlarmService/getAlarms")
        .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) 
            	throw new IOException("AlarmService not responding" + response);
            
            String resBody = response.body().string();
            alarms = new JSONArray(resBody);
        }
		
        return alarms;
	}
	
	public static List<Alarm> setAlarmsList() throws JSONException, IOException {
		if(!alarmslist.isEmpty())
			alarmslist.clear();
		
		JSONArray alarmsJsArr = getAlarms();
		
		for (int i=0;i<alarmsJsArr.length();i++) {
			JSONObject o = alarmsJsArr.getJSONObject(i);
			Alarm a = new Alarm();

			a.setAid(o.getString("aid"));
			a.setCo2Level(o.getInt("co2Level"));
			a.setEmail(o.getString("email"));
			a.setIsActive(o.getInt("isActive"));
			a.setIsWorking(o.getInt("isWorking"));
			a.setLid(o.getString("lid"));
			a.setSmokeLevel(o.getInt("smokeLevel"));

			if(a.getIsWorking() == 1)
				alarmslist.add(a);
		}
		
		return alarmslist;
	}
	
	public boolean updateAlarm(Alarm a) throws IOException {
		String json = new StringBuilder()
		.append("{"
			+ "\"aid\":\""+a.getAid()+"\","
			+ "\"email\":\""+a.getEmail()+"\","
			+ "\"lid\":\""+a.getLid()+"\","
			+ "\"smokeLevel\":\""+a.getSmokeLevel()+"\","
			+ "\"co2Level\":\""+a.getCo2Level()+"\","
			+ "\"isActive\":\""+a.getIsActive()+"\","
			+ "\"isWorking\":\""+a.getIsWorking()+"\""
			+ "}").toString();

		RequestBody requestBody = RequestBody.create (
			MediaType.parse("application/json; charset=UTF-8"), json
		);

		Request request = new Request.Builder()
		.url("http://localhost:8080/FireAlarmRest/rest/AlarmService/updateAlarm")
		.put(requestBody)
		.build();

		try (Response response = httpClient.newCall(request).execute()) {
			int code = response.code();

			if(code == 201)
				return true;
			else
				return false;
		}
	}
}
