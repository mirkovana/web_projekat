package rest;

import static spark.Spark.port;

import com.google.gson.Gson;

public class VMApp {
	
	private static Gson gson = new Gson();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		port(8080);
	}

}
