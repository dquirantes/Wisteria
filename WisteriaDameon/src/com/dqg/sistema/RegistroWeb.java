package com.dqg.sistema;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;




public class RegistroWeb extends TimerTask {
	private static final Logger log = Logger.getLogger("Dameon");


	private SistemaDomotico sistema;	

	private String fichero_web;


	public RegistroWeb (SistemaDomotico sistema, String web)
	{
		this.sistema = sistema;		
		this.fichero_web = web;
	}

	@Override
	public void run() {
		log.debug ("Registrar informacion Fichero Web");


		if (sistema.getTempExterna()!=null)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String res2 = dateFormat.format(date) + "," + sistema.getTemperatura() 
			+ "," + sistema.getTemperatura_dormitorio()
			+ "," + sistema.getTemperatura_habitacion2()
			+ "," + sistema.getTempExterna() +			
			System.getProperty("line.separator");
			
			try
			{

				File archivo = new File(fichero_web);
				BufferedWriter bw;
				if(archivo.exists()) 
				{
					bw = new BufferedWriter(new FileWriter(archivo,true));	            
					bw.append(res2);
				} else {
					bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(res2);
				}
				bw.close();
			}catch (Exception e)
			{
				log.error("Fallo registro web: " + e);
				//e.printStackTrace();
			}
		}	

	}
}

