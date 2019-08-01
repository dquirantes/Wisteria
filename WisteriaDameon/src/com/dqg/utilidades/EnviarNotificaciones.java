package com.dqg.utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

public class EnviarNotificaciones {

	private static final Logger log = Logger.getLogger("Dameon");			
	private String url_telegram;
	
	public EnviarNotificaciones (String url)
	{
		this.url_telegram = url;


	}
	public void enviar (String texto)
	{
		//String args = programa + " \""  + texto + "\"";

		/*ProgramaExterno programa_externo = new ProgramaExterno();

		try
		{
			programa_externo.ejecutar(programa,texto);	
		}catch (Exception e)
		{
			log.error ("Excepcion envio notificaciones " + e);
		}


		log.debug("Enviar notificacion ");
		//log.debug ("ejecutar: " + argumentos);

		 */
		try
		{
			URL url = new URL(url_telegram.concat(new String(texto.getBytes(), "UTF-8")));

			
			log.debug(url);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			//String jsonInputString = "{\"on\":false}";
			//try(OutputStream os = con.getOutputStream()) {
				//byte[] input = jsonInputString.getBytes("utf-8");
				//os.write(input, 0, input.length);           
			//}

			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}
}