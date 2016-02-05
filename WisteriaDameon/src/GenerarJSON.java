import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sistema.TipoPosicion;

public class GenerarJSON{

	private SistemaDomotico sistema;
	public GenerarJSON (SistemaDomotico sis)
	{
		this.sistema = sis;
	}


	public static void run() {

		//JSONObject outerObject = new JSONObject();
		//JSONArray outerArray = new JSONArray();
		JSONObject objeto = new JSONObject();
		JSONArray habitacionesArray = new JSONArray();

		TipoPosicion posicion = new TipoPosicion ();
		posicion.cod_instruccion = 1014;
		posicion.latitud = 1000;
		posicion.longitud= 2000;
		try{
			habitacionesArray.put("Salón");
			habitacionesArray.put("Dormtitorio");
			habitacionesArray.put("Habitación1");
			habitacionesArray.put("Habitación2");
			objeto.put("Habitaciones", habitacionesArray);

			objeto.put("Rasperry", 50);
			objeto.put("Modo", "CLIMATIZADOR");
			objeto.put("OpcionesModo", "Salón");
			objeto.put("TemperaturaClimatizador", "22");
			
			objeto.put("Posicion", posicion);
			
			System.out.println(objeto.toString());

		}
		catch(Exception e)
		{

		}

	}

	public static void main (String[] args) throws java.lang.Exception
	{
		System.out.println ("mainnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
		run();
	}
}
