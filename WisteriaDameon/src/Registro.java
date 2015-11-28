import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import sistema.BaseDatos;


public class Registro extends TimerTask {
	private static final Logger log = Logger.getLogger("Dameon");
	
	
	private SistemaDomotico sistema;
	private BaseDatos basedatos;

	private String fichero_web;
	

	public Registro (SistemaDomotico sistema, BaseDatos basedatos, String web)
	{
		this.sistema = sistema;
		this.basedatos = basedatos;
		this.fichero_web = web;
	}
	
	@Override
	public void run() {
		log.debug ("Registrar informacion en la BBDD");
		
		Boolean res = 
		basedatos.insertarSistema(sistema.getTemperatura(),sistema.getHumedad(),
				sistema.getTempExterna(),sistema.getEstadoRele().toString(),
				sistema.getTemperatura_Climatizador(),sistema.getTemperatura_dormitorio(),sistema.getHumedad_dormitorio(),
				sistema.getTemperatura_habitacion1(),sistema.getTemperatura_habitacion1(),
				sistema.getTemperatura_habitacion2(),sistema.getTemperatura_habitacion2(),
				sistema.getTemperatura_raspi(),
				sistema.get_opcionesModo().toString());
		
		if (!res)
		{
			//sistema.setErrorSistema(ErroresSistema.REGISTRO);
		}
		

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String res2 = dateFormat.format(date) + "," + sistema.getTemperatura() 
		+ "," + sistema.getTemperatura_dormitorio()
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
			e.printStackTrace();
		}
			
		
	}
}
	
