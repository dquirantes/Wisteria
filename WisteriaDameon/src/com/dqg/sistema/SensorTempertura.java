package com.dqg.sistema;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dqg.config.Configuracion;
import com.dqg.sistema.datos.Medicion;
import com.dqg.tipos.ErroresSistema;
import com.dqg.utilidades.ProgramaExterno;



public class SensorTempertura extends TimerTask{

	static final int ERRORES_MAXIMOS= 5;

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private ProgramaExterno programa_externo = new ProgramaExterno(); 
	private String programa;	
	private Configuracion config;


	private int errores1 = 0;
	//private int errores2 = 0;
	//private int errores3 = 0;
	//private int errores4 = 0;
	private int errores5 = 0;


	public SensorTempertura (SistemaDomotico sis,String programa, Configuracion config)
	{		
		sistema = sis;
		this.programa = programa;
		this.config = config;
	}


	private Medicion medicionGPIO (String info)
	{
		String res ="";
		String partes[];


		Medicion medicion = new Medicion();		

		try
		{
			res= programa_externo.ejecutar(programa + " " + info);
			partes = res.split(",");			
			medicion.humedad = Float.parseFloat(partes[0]);							
			medicion.temperatura= Float.parseFloat(partes[1]);

		}catch (Exception e)
		{
			medicion = null;
			log.error("Fallo a ejecutar programa " + e);		
		}



		return medicion;
	}

	private Medicion medicionWeb(String info)
	{
		Medicion medicion = new Medicion();
		log.debug ("Conectado Web " + info);


		try
		{
			URL url = new URL(info);						
			URLConnection con = (URLConnection) url.openConnection();
			//Cambio de timeout
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));


			String linea;
			String []partes;


			if ((linea = in.readLine()) != null) 
			{			
				partes = linea.split(",");

				medicion.humedad=Float.parseFloat(partes[1]);
				medicion.temperatura = Float.parseFloat(partes[2]);
			}
		}
		catch(Exception e)
		{
			medicion = null;
			log.error("Error lectura sensor web " + info + " " + e);
		}
		return medicion;

	}

	private Medicion medicionTemperatura(String info)
	{			

		String partes[];

		if (info != null)
		{
			partes = info.split("@");

			if (partes.length==2)
			{

				if (partes[0].toLowerCase().equals("web"))			
				{
					return medicionWeb(partes[1]);
				}
				else if (partes[0].toLowerCase().equals("gpio"))
				{			
					return medicionGPIO(partes[1]);
				}				
			}
		}

		return null;

	}

	@Override
	public void run() {


		log.debug("Sensor temperatura INICIO");


		String res;									

		float temp_placa;
		String partes[];





		Medicion medicion = medicionTemperatura(config.getSensorSalon());
		// 09/02/2017 descarta las menores que cero
		if (medicion!=null && medicion.temperatura>0)
		{			
			sistema.setTemp(medicion.temperatura);
			sistema.setHumedad(medicion.humedad);
			errores1 =0;
		}
		else
		{
			errores1++;
			log.error("Fallo sensor tempertura salon");

			if (errores1>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSORES);		
		}


		/*medicion = medicionTemperatura(config.getSensorDormitorio());
		// 09/02/2017 descarta las menores que cero
		if (medicion!=null  && medicion.temperatura>0)
		{						
			sistema.setTempdormitorio(medicion.temperatura);
			sistema.setHumedaddormitorio(medicion.humedad);
			errores2=0;

		}
		else
		{
			errores2++;
			log.error("Fallo sensor dormitorio");

			if (errores2>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSORES);
		}*/



		/*medicion = medicionTemperatura(config.getSensorHabitacion1());
		// 09/02/2017 descarta las menores que cero
		if (medicion!=null && medicion.temperatura>0)
		{
			sistema.setTemp_habitacion1(medicion.temperatura);
			sistema.setHumedad_habitacion1(medicion.humedad);
			errores3=0;

		}else			
		{			
			errores3++;
			log.error("Fallo sensor habitación1 ESP8266");


			if (errores3>ERRORES_MAXIMOS)
			{
				sistema.setTemp_habitacion1(0f);
				sistema.setHumedad_habitacion1(0f);
			}

		}


		medicion = medicionTemperatura(config.getSensorHabitacion2());
		// 09/02/2017 descarta las menores que cero
		if (medicion!=null && medicion.temperatura>0)
		{
			sistema.setTemp_habitacion2(medicion.temperatura);
			sistema.setHumedad_habitacion2(medicion.humedad);
			errores4=0;

		}else			
		{			
			errores4++;
			log.error("Fallo sensor habitación2 ESP8266");


			if (errores4>ERRORES_MAXIMOS)
			{
				sistema.setTemp_habitacion2(0f);
				sistema.setHumedad_habitacion2(0f);
			}

		}


		 */
		try
		{
			res= programa_externo.ejecutar(config.getProgramaPlaca());


			partes = res.split("=");
			partes = partes[1].split("'");


			temp_placa = Float.parseFloat(partes[0]);							


			sistema.setTemp_raspi(temp_placa);
			errores5=0;

			// Si la tempertura de la placa excede 
			if (temp_placa>config.get_tempMaximaPlaca())
			{
				sistema.setErrorSistema(ErroresSistema.TEMP_PLACA);
			}

		}catch (Exception e)
		{			
			errores5++;
			log.error("Fallo lectura temperatura de la placa");


			if (errores5>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSOR_PLACA);

		}

		// Medición Shelly
		try {
			URL urlShelly= new URL(config.getShellyEstado());

			HttpURLConnection httpURLConnection = (HttpURLConnection) urlShelly.openConnection();

			String result="";
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				InputStreamReader inputStreamReader =
						new InputStreamReader(httpURLConnection.getInputStream());
				BufferedReader bufferedReader =
						new BufferedReader(inputStreamReader, 8192);
				String line = null;
				while((line = bufferedReader.readLine()) != null){
					result += line;
				}

				bufferedReader.close();

				//ParseResult(result, sistema);

				JSONObject jsonObject = new JSONObject(result);
				double temperature = jsonObject.getDouble("temperature");
				log.debug("Temperatura Shelly: " + temperature);


				JSONArray relays = jsonObject.getJSONArray("relays");

				if(relays.length() > 0)
				{
					JSONObject JSONObject_weather = relays.getJSONObject(0);
					Boolean ison = JSONObject_weather.getBoolean("ison");
					log.debug("ISON Shelly: " + ison);
				}


			} else {
				log.error("Error in httpURLConnection.getResponseCode()!!!");
			}

		} catch (Exception e)
		{
			log.error ("Fallo Shelly " + e);
		}

		log.debug("Sensor temperatura FIN");
	}


}
