import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import sistema.Configuracion;
import sistema.ProgramaExterno;



public class SensorTempertura extends TimerTask{

	static final int ERRORES_MAXIMOS= 3;

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private ProgramaExterno programa_externo = new ProgramaExterno(); 
	private String programa;	
	private Configuracion config;


	private int errores1 = 0;
	private int errores2 = 0;
	//private int errores3 = 0;
	//private int errores4 = 0;
	private int errores5 = 0;


	public SensorTempertura (SistemaDomotico sis,String programa, Configuracion config)
	{		
		sistema = sis;
		this.programa = programa;
		this.config = config;
	}


	@Override
	public void run() {


		log.debug("Sensor temperatura");



		String res;
		float humedad;
		float temp;
		float humedad_dormitorio;
		float temp_dormitorio;
		float humedad_habitacion1;
		float temp_habitacion1;
		//float humedad_habitacion2;
		//float temp_habitacion2;				

		float temp_placa;
		String partes[];
		String linea;



		try
		{
			res= programa_externo.ejecutar(programa + " " + config.get_Gpio_Salon());
			partes= res.split(",");
			humedad = Float.parseFloat(partes[0]);							
			temp = Float.parseFloat(partes[1]);

			sistema.setTemp(temp);
			sistema.setHumedad(humedad);

			errores1 =0;
		}catch (Exception e)
		{
			errores1++;
			log.error("Fallo sensor tempertura salon");

			if (errores1>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSORES);				
		}


		try
		{
			res= programa_externo.ejecutar(programa + " " + config.get_Gpio_Dormitorio());
			partes = res.split(",");
			humedad_dormitorio = Float.parseFloat(partes[0]);							
			temp_dormitorio = Float.parseFloat(partes[1]);

			sistema.setTempdormitorio(temp_dormitorio);
			sistema.setHumedaddormitorio(humedad_dormitorio);

			errores2=0;

		}catch (Exception e)
		{
			errores2++;
			log.error("Fallo sensor dormitorio");

			if (errores2>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSORES);
		}


		try
		{

			log.debug ("Conectado ESP8266 habitacion1");
			URL url = new URL(config.get_Gpio_habitacion1());
			URLConnection con = (URLConnection) url.openConnection();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));


			log.debug ("Recibido ESP8266");
			
			if ((linea = in.readLine()) != null) 
			{
				log.debug (linea);
				partes = linea.split(",");

				humedad_habitacion1 = Float.parseFloat(partes[1]);
				temp_habitacion1 = Float.parseFloat(partes[2]);

				sistema.setTemp_habitacion1(temp_habitacion1);
				sistema.setHumedad_habitacion1(humedad_habitacion1);

				//errores3=0;

			}
		}catch (Exception e)
		{
			//e.printStackTrace();
			//errores3++;


			log.error("Fallo sensor habitación1 ESP8266");

			// No es necesario que funcione ese sensor
			/*if (errores3>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSORES);*/
		}



		/*try
		{

			log.debug ("Conectado ESP8266 habitacion2");
			URL url = new URL(config.get_Gpio_habitacion2());
			URLConnection con = (URLConnection) url.openConnection();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));


			if ((linea = in.readLine()) != null) 
			{
				log.debug ("Recibido: " + linea);
				partes = linea.split(",");

				humedad_habitacion2 = Float.parseFloat(partes[1]);
				temp_habitacion2 = Float.parseFloat(partes[2]);

				sistema.setTemp_habitacion2(temp_habitacion2);
				sistema.setHumedad_habitacion2(humedad_habitacion2);

				errores4=0;

			}
		}catch (Exception e)
		{
			e.printStackTrace();
			
			errores4++;


			log.error("Fallo sensor habitación2 ESP8266");

			if (errores4>ERRORES_MAXIMOS)
				sistema.setErrorSistema(ErroresSistema.SENSORES);
		}*/


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
