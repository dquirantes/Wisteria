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

	private static final Logger log = Logger.getLogger("Dameon");



	SistemaDomotico sistema;
	EnviarNotificaciones notifiaciones;
	Configuracion configuracion;
	BaseDatos basedatos;
	
	ServerSocket skServidor;

	public Servidor(SistemaDomotico sistema, EnviarNotificaciones notificaciones, Configuracion configuracion, BaseDatos bbdd) 
	{
		this.sistema= sistema;
		this.notifiaciones = notificaciones;
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
		//String respuesta = "";
		String recibido;

		try 
		{
			log.info("Arrancar servidor puerto: " + configuracion.getPuerto());
			skServidor = new ServerSocket(configuracion.getPuerto());


			while (true)
			{
				Socket skCliente = skServidor.accept(); 


				log.debug("Petición cliente recibida");
				DataInputStream in = new DataInputStream(skCliente.getInputStream());

				recibido = in.readUTF();
				log.debug("recibido mensaje: " + recibido); 


				OutputStream aux = skCliente.getOutputStream();
				DataOutputStream flujo= new DataOutputStream( aux );			

				if (recibido.toLowerCase().equals(INFO))
				{
					// Envia el estado del sistema por notificaciones
					notifiaciones.enviar(sistema.toString_info());										
				}
				else if (recibido.toLowerCase().equals(SALON))
				{				
					notifiaciones.enviar("Salón: " + sistema.getTemperatura());										
				}
				else if (recibido.toLowerCase().equals(DORMITORIO))
				{			
					notifiaciones.enviar("Dormitorio: " + sistema.getTemperatura_dormitorio());										
				}			
				else if (recibido.toLowerCase().equals(HABITACION1))
				{
					notifiaciones.enviar("Habitación1: " + sistema.getTemperatura_habitacion1());										
				}
				else if (recibido.toLowerCase().equals(HABITACION2))
				{				
					notifiaciones.enviar("Habitación2: " + sistema.getTemperatura_habitacion2());										
				}
				else if (recibido.toLowerCase().equals(RASPBERRY))
				{					
					notifiaciones.enviar("Raspberry: " + sistema.getTemperatura_raspi());				
				}
				else if (recibido.toLowerCase().equals(EXTERNA))
				{					
					notifiaciones.enviar("Las Rozas: " + sistema.getTempExterna());				
				}
				else if (recibido.toLowerCase().equals("calefaccion_dormitorio"))
				{					
					basedatos.insertarInstruccion(1, 21, "david", true, "DORMITORIO");									
					//notifiaciones.enviar("Las Rozas: " + sistema.getTempExterna());				
				}
				else if (recibido.toLowerCase().equals("calefaccion_salon"))
				{					
					basedatos.insertarInstruccion(1, 20.5f, "david", true, "SALON");									
					//notifiaciones.enviar("Las Rozas: " + sistema.getTempExterna());				
				}
				else if (recibido.toLowerCase().equals("apagar_calefaccion"))
				{					
					basedatos.insertarInstruccion(2, 21, "david", true, "SALON");									
					//notifiaciones.enviar("Las Rozas: " + sistema.getTempExterna());				
				}
				
				else if (recibido.toLowerCase().equals(SALIR))
				{
					File FLAG_FILE = new File(configuracion.getFicheroTemporal());

					if (FLAG_FILE.exists())
					{										
						notifiaciones.enviar("Comienza el proceso de apagado por Telegram recibido");
						// Borrar el fichero temporal para salir
						FLAG_FILE.delete();						
					}	
				}

				else
				{
					log.error("Opción incorrecta");				
				}


				flujo.writeUTF("OK");
				skCliente.close();				
			}
		} catch( Exception e ) 
		{
			log.error("Error Servidor "  + e.getMessage());
			//System.out.println( e.getMessage() );
		}		


	}


}
