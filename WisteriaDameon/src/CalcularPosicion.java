

/*import java.util.*;
import java.lang.*;
import java.io.*;*/

class CalcularPosicion
{
	public static void main (String[] args) throws java.lang.Exception
	{
		/*double long_a = -9.137313599999999;
		double lat_a =  38.7368843;*/
		
		// Madrid
		double long_a = -3.7037901999999576;
		double lat_a =  40.4167754;
		
		// Cádiz
		double long_b = -6.288596200000029;
		double lat_b =  36.5270612;
;		
		// Aravaca		
		double long_c = -3.7854440999999497;
		double lat_c =  40.458195;

		// Clara CampoAmor
		double long_d = -3.8795036999999866;
		double lat_d =  40.5061796;
		
		// C/Wisteria 
		double long_casa = -3.9254872000000205;
		double lat_casa = 40.51844359999999;
		
		//Recibido
		double long_e = -3.9260249;
		double lat_e =  40.516566499999996;
		
		System.out.println(distance(lat_casa, long_casa, lat_a, long_a, "K") + " Kilometers\n");
		System.out.println(distance(lat_casa, long_casa, lat_b, long_b, "K") + " Kilometers\n");
		System.out.println(distance(lat_casa, long_casa, lat_c, long_c, "K") + " Kilometers\n");
		System.out.println(distance(lat_casa, long_casa, lat_d, long_d, "K") + " Kilometers\n");

		System.out.println(distance(lat_casa, long_casa, lat_e, long_e, "K") + " Kilometers\n");
		
		/*System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles\n");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers\n");		
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles\n");*/
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}