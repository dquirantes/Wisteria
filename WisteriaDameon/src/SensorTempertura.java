import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import sistema.Configuracion;
import sistema.ProgramaExterno;



public class SensorTempertura extends TimerTask{

	static final int ERRORES_MAXIMOS= 5;

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private ProgramaExterno programa_externo = new ProgramaExterno(); 
	private String programa;	
	private Configuracion config;


	private int errores1 = 0;
	private int errores2 = 0;
	private int errores3 = 0;
	private int errores4 = 0;
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
		log.debug ("Conectado Web" + info);


		try
		{
			URL url = new URL(info);
			URLConnection con = (URLConnection) url.openConnection();

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
			log.error("Error lectura sensor web " + e);
		}
		return medicion;

	}

	private Medicion medicionTemperatura(String info)
	{			


		if (info.toLowerCase().startsWith("http"))			
		{
			return medicionWeb(info);
		}else
		{
			return medicionGPIO(info);
		}		
	}

	@Override
	public void run() {


		log.debug("Sensor temperatura");


		String res;									

		float temp_placa;
		String partes[];
		




		Medicion medicion = medicionTemperatura(config.get_Gpio_Salon());
		if (medicion!=null)
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


		medicion = medicionTemperatura(config.get_Gpio_Dormitorio());
		if (medicion!=null)
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
		}


		
		medicion = medicionTemperatura(config.get_Gpio_habitacion1());
		if (medicion!=null)
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


		medicion = medicionTemperatura(config.get_Gpio_habitacion2());
		if (medicion!=null)
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
	}
}
