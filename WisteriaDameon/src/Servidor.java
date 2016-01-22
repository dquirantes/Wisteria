import java.io.* ;
import java.net.* ;

import org.apache.log4j.Logger;

import sistema.EnviarNotificaciones;
import sistema.BaseDatos;
import sistema.Configuracion;


class Servidor extends Thread  
{
	static final String INFO = "info";
	static final String SALON= "salon";
	static final String DORMITORIO = "dormitorio";
	static final String HABITACION1 = "habitacion1";
	static final String HABITACION2 = "habitacion2";
	static final String RASPBERRY = "raspberry";
	static final String EXTERNA = "externa";
	static final String SALIR = "salir";

	static final String CALEFACCION_DORMITORIO = "calefaccion_dormitorio";
	static final String CALEFACCION_SALON = "calefaccion_salon";
	static final String CALEFACCION_APAGAR = "calefaccion_apagar";
	
	static final String NOTIFICACIONES = "notificaciones";
	static final String NOTIFICACIONES_SI = "notificaciones_si";
	static final String NOTIFICACIONES_NO = "notificaciones_no";
	
	private static final Logger log = Logger.getLogger("Dameon");



	SistemaDomotico sistema;
	EnviarNotificaciones notificaciones;
	Configuracion configuracion;
	BaseDatos basedatos;
	
	ServerSocket skServidor;

	public Servidor(SistemaDomotico sistema, EnviarNotificaciones notificaciones, Configuracion configuracion, BaseDatos bbdd) 
	{
		this.sistema= sistema;
		this.notificaciones = notificaciones;
		this.configuracion =configuracion;
		this.basedatos = bbdd;
	}


	public void cerrar()
	{
		log.info("Cerrar servidor");
		try {
			skServidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() 
	{
		
		String recibido;

		try 
		{
			log.info("Arrancar servidor puerto: " + configuracion.getPuerto());
			skServidor = new ServerSocket(configuracion.getPuerto());

			

			while (true)
			{
				String respuesta = "";
				
				Socket skCliente = skServidor.accept(); 


				log.debug("Petición cliente recibida");
				DataInputStream in = new DataInputStream(skCliente.getInputStream());

				recibido = in.readUTF();
				log.debug("recibido mensaje: " + recibido); 


				OutputStream aux = skCliente.getOutputStream();
				DataOutputStream flujo= new DataOutputStream( aux );			

				if (recibido.toLowerCase().equals(INFO))
				{					
					respuesta = sistema.toString_info();										
				}
				else if (recibido.toLowerCase().equals(SALON))
				{				
					respuesta = "Salón: " + sistema.getTemperatura() + "º";										
				}
				else if (recibido.toLowerCase().equals(DORMITORIO))
				{			
					respuesta = "Dormitorio: " + sistema.getTemperatura_dormitorio() + "º";										
				}			
				else if (recibido.toLowerCase().equals(HABITACION1))
				{
					respuesta = "Habitación1: " + sistema.getTemperatura_habitacion1() + "º";										
				}
				else if (recibido.toLowerCase().equals(HABITACION2))
				{				
					respuesta = "Habitación2: " + sistema.getTemperatura_habitacion2() + "º";										
				}
				else if (recibido.toLowerCase().equals(RASPBERRY))
				{					
					respuesta = "Raspberry: " + sistema.getTemperatura_raspi() + "º";;				
				}
				else if (recibido.toLowerCase().equals(EXTERNA))
				{					
					respuesta = "Las Rozas: " + sistema.getTempExterna() + "º";;				
				}
				else if (recibido.toLowerCase().equals(CALEFACCION_DORMITORIO))
				{					
					basedatos.insertarInstruccion(1, 21, "david", true, "DORMITORIO");															
				}
				else if (recibido.toLowerCase().equals(CALEFACCION_SALON))
				{					
					basedatos.insertarInstruccion(1, 20.5f, "david", true, "SALON");																
				}
				else if (recibido.toLowerCase().equals(CALEFACCION_APAGAR))
				{					
					basedatos.insertarInstruccion(2, 21, "david", true, "SALON");									
				}
				else if (recibido.toLowerCase().equals(NOTIFICACIONES))
				{					
					respuesta = "Notificaciones: " + sistema.getEnviarNotificaciones();
				}
				else if (recibido.toLowerCase().equals(NOTIFICACIONES_SI))
				{					
					respuesta = "Activiar notificaciones";
					sistema.setEnviarNotificaciones(true);
				}
				else if (recibido.toLowerCase().equals(NOTIFICACIONES_NO))
				{					
					respuesta = "Desactivar notificaciones";
					sistema.setEnviarNotificaciones(false);
				}
				
				else if (recibido.toLowerCase().equals(SALIR))
				{
					File FLAG_FILE = new File(configuracion.getFicheroTemporal());

					if (FLAG_FILE.exists())
					{										
						respuesta = "Comienza el proceso de apagado por Telegram recibido";
						// Borrar el fichero temporal para salir
						FLAG_FILE.delete();						
					}	
				}
				else
				{
					log.error("Opción incorrecta servidor: " + recibido);
					respuesta = "Opción incorrecta";
				}

				// Devuelve OK al cliente
				flujo.writeUTF("OK");
				skCliente.close();
				
				if (respuesta!="")
					notificaciones.enviar(respuesta);
					
			}
		} catch( Exception e ) 
		{
			log.error("Error Servidor "  + e.getMessage());
		}		
	}


}
