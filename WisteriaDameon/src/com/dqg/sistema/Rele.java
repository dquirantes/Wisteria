package com.dqg.sistema;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.dqg.config.Configuracion;
import com.dqg.tipos.ErroresSistema;
import com.dqg.tipos.EstadoRele;



public class Rele {

	private static final Logger log = Logger.getLogger("Dameon");


	//ProgramaExterno programa = new ProgramaExterno();


	private Configuracion configuracion;
	private SistemaDomotico sistema;


	public Rele (Configuracion config, SistemaDomotico sis)	
	{
		configuracion = config;
		sistema = sis;		

	}



	public boolean abrir ()
	{		

		if (sistema.getEstadoRele()!=EstadoRele.ABIERTO)
		{
			log.debug ("Abrir rele");


			/*try {
				programa.ejecutar(configuracion.getProgramaAbrir());
			} catch (Exception e) {
				e.printStackTrace();
			}*/

			try
			{
				URL url = new URL(configuracion.getShellyEnceder());

				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

				String result = "";
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
					sistema.setEstadoRele(EstadoRele.ABIERTO);
					log.debug("Resultado shelly: " + result);
					return true;
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				log.error(e);
				sistema.setErrorSistema(ErroresSistema.SHELLY);
	
			}

		}
		return false;
	}


	public boolean cerrar ()
	{

		if (sistema.getEstadoRele()!=EstadoRele.CERRADO)
		{
			log.debug ("Cerrar rele");

			try
			{
				URL url = new URL(configuracion.getShellyApagar());

				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

				String result = "";
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
					sistema.setEstadoRele(EstadoRele.CERRADO);
					log.debug("Resultado shelly: " + result);
					return true;
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				log.error(e);
				sistema.setErrorSistema(ErroresSistema.SHELLY);				
	
			}

		}
		return false;
	}



}
