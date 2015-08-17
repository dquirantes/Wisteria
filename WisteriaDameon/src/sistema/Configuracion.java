package sistema;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class Configuracion {

	private static final Logger log = Logger.getLogger("Dameon");
	
	private String programa_abrir;
	private String programa_cerrar;
	private String programa_sensor;
	private String programa_notificaciones;
	private String programa_placa;
	private String url_tiempo;

	
	private String fichero_temporal;
	
	private String bd_name;
	private String bd_servidor;
	private String bd_puerto;
	private String bd_usuario;
	private String bd_password;

	private boolean enviarCorreo;
	private String correo_from;
	private String correo_to;	
	private String correo_usuario;
	private String correo_password;
	private String correo_host;
	private String correo_puerto;
	private String correo_asunto;
	

	private int gpio_salon;
	private int gpio_dormitorio;
	private int gpio_habitacion1;
	private int gpio_habitacion2;
	
	private float tempMaximaPlaca;
	
	private long tActuador;
	private long tSensor;	
	private long tExterno;
	private long tBucle;
	private long tRegistro;

	private float tmp_Margen;
	
	
	
	
	
	public Configuracion(String pathProperties) {

		try
		{
			Properties prop = new Properties();
			prop.load(new FileInputStream(pathProperties));


			programa_abrir = prop.getProperty("programa_abrir");	
			programa_cerrar = prop.getProperty("programa_cerrar");
			programa_sensor = prop.getProperty("programa_sensor");
			programa_notificaciones = prop.getProperty("programa_notificaciones");
			programa_placa = prop.getProperty("programa_placa");
			
			
			url_tiempo = prop.getProperty("url_tiempo");

			fichero_temporal = prop.getProperty("fichero_temporal");
						
			bd_name = prop.getProperty("bd_name");
			bd_servidor = prop.getProperty("bd_servidor");
			bd_puerto = prop.getProperty("bd_puerto");
			bd_usuario = prop.getProperty("bd_usuario");
			bd_password = prop.getProperty("bd_password");

			enviarCorreo = Boolean.parseBoolean(prop.getProperty("enviar_correo"));			
			correo_from = prop.getProperty("correo_from");
			correo_to = prop.getProperty("correo_to");
			correo_usuario = prop.getProperty("correo_usuario");
			correo_password = prop.getProperty("correo_password");
			correo_host = prop.getProperty("correo_host");			
			correo_puerto = prop.getProperty("correo_puerto");			
			correo_asunto = prop.getProperty("correo_asunto");			
			
			
			gpio_salon = Integer.parseInt(prop.getProperty("gpio_salon"));
			gpio_dormitorio = Integer.parseInt(prop.getProperty("gpio_dormitorio"));
			gpio_habitacion1 = Integer.parseInt(prop.getProperty("gpio_habitacion1"));
			gpio_habitacion2 = Integer.parseInt(prop.getProperty("gpio_habitacion2"));
			
			tempMaximaPlaca = Float.parseFloat(prop.getProperty("tempMaximaPlaca"));
			tmp_Margen = Float.parseFloat(prop.getProperty("tmp_Margen"));
			
			
			tActuador= Long.parseLong(prop.getProperty("tActuador"));
			tSensor= Long.parseLong(prop.getProperty("tSensor"));
			tExterno= Long.parseLong(prop.getProperty("tExterno"));
			tBucle= Long.parseLong(prop.getProperty("tBucle"));
			tRegistro= Long.parseLong(prop.getProperty("tRegistro"));
			
			
			
			
		}catch (Exception e)
		{
			log.error ("Fallo en la configuración. Revise el fichero: " + pathProperties);
			e.printStackTrace();
			System.exit (-1);
		}


	}

	public boolean getEnviarCorreo()
	{
		return enviarCorreo;
	}
	
	public Float gettmp_Margen()
	{
		return tmp_Margen;
	}
	public long gettActuador()
	{
		return tActuador;
	}

	public long gettSensor()
	{
		return tSensor;
	}
	
	public long gettExterno()
	{
		return tExterno;
	}

	public long gettBucle()
	{
		return tBucle;
	}
	
	public long gettRegistro()
	{
		return tRegistro;
	}
	
	public Float get_tempMaximaPlaca()
	{
		return tempMaximaPlaca;
	}
	public int get_Gpio_Salon()
	{
		return gpio_salon;
	}
	
	public int get_Gpio_Dormitorio()
	{
		return gpio_dormitorio;
	}
	
	public int get_Gpio_habitacion1()
	{
		return gpio_habitacion1;
	}
	

	public int get_Gpio_habitacion2()
	{
		return gpio_habitacion2;
	}

	
	public String getProgramaNotificaciones()
	{
		return this.programa_notificaciones;
	}
	
	public String getCorrreoAsunto()
	{
		return correo_asunto;
	}
	
	public String getCorrreoTo()
	{
		return correo_to;
	}
	
	public String getCorrreoFrom()
	{
		return correo_from;
	}
	

	
	public String getCorrreoUsuario()
	{
		return correo_usuario;
	}
	
	public String getCorrreoPuerto()
	{
		return correo_puerto;
	}	
	
	public String getCorrreoPassword()
	{
		return correo_password;
	}	
	public String getCorrreoHost()
	{
		return correo_host;
	}

	public String getProgramaAbrir()
	{
		return programa_abrir;
	}

	public String getProgramaCerrar()
	{
		return programa_cerrar;
	}

	public String getProgramaSensor()
	{
		return programa_sensor;
	}

	public String getURLTiempo()
	{
		return url_tiempo;	
	}
	
	public String getBDPassword()
	{
		return bd_password;		
	}
	
	public String getBDUsuario()
	{
		return bd_usuario;		
	}


	public String getBDPuerto()
	{
		return bd_puerto;		
	}

	public String getBDName()
	{
		return bd_name;		
	}

	public String getBDServidor()
	{
		return bd_servidor;		
	}

	public String getFicheroTemporal()
	{
		return fichero_temporal;		
	}
	
	public String getProgramaPlaca()
	{
		return programa_placa;
	}	

}


