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
	static final String MEDIA = "media";
	
	static final String RASPBERRY = "raspberry";
	static final String EXTERNA = "externa";
	static final String SALIR = "salir";

	static final String CALEFACCION_DORMITORIO = "calefaccion_dormitorio";
	static final String CALEFACCION_SALON = "calefaccion_salon";
	static final String CALEFACCION_APAGAR = "calefaccion_apagar";
	
	static final String NOTIFICACIONES = "notificaciones";
	static final String NOTIFICACIONES_SI = "notificaciones_si";
	static final String NOTIFICACIONES_NO = "notificaciones_no";
	
	static final String USO = "uso";
	
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


				log.debug("Petici�n cliente recibida");
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
					respuesta = "Sal�n: " + sistema.getTemperatura() + "�";										
				}
				else if (recibido.toLowerCase().equals(DORMITORIO))
				{			
					respuesta = "Dormitorio: " + sistema.getTemperatura_dormitorio() + "�";										
				}			
				else if (recibido.toLowerCase().equals(HABITACION1))
				{
					respuesta = "Habitaci�n1: " + sistema.getTemperatura_habitacion1() + "�";										
				}
				else if (recibido.toLowerCase().equals(HABITACION2))
				{				
					respuesta = "Habitaci�n2: " + sistema.getTemperatura_habitacion2() + "�";										
				}
				else if (recibido.toLowerCase().equals(MEDIA))
				{				
					respuesta = "Media: " + sistema.calcularTemperaturaMedia() + "�";										
				}
				else if (recibido.toLowerCase().equals(RASPBERRY))
				{					
					respuesta = "Raspberry: " + sistema.getTemperatura_raspi() + "�";;				
				}
				else if (recibido.toLowerCase().equals(EXTERNA))
				{					
					respuesta = "Las Rozas: " + sistema.getTempExterna() + "�";;				
				}
				else if (recibido.toLowerCase().equals(CALEFACCION_DORMITORIO))
				{					
					basedatos.insertarInstruccion(1, sistema.getTemperatura_Climatizador(), "david", sistema.getEnviarNotificaciones(), "DORMITORIO");															
				}
				else if (recibido.toLowerCase().equals(CALEFACCION_SALON))
				{					
					basedatos.insertarInstruccion(1, sistema.getTemperatura_Climatizador(), "david", sistema.getEnviarNotificaciones(), "SALON");																
				}
				else if (recibido.toLowerCase().equals(CALEFACCION_APAGAR))
				{					
					basedatos.insertarInstruccion(2, sistema.getTemperatura_Climatizador(), "david", sistema.getEnviarNotificaciones(), sistema.get_opcionesModo().toString());									
				}
				else if (recibido.toLowerCase().equals(NOTIFICACIONES))
				{					
					respuesta = "Notificaciones: " + sistema.getEnviarNotificaciones();
				}
				else if (recibido.toLowerCase().equals(NOTIFICACIONES_SI))
				{					
					respuesta = "Activar notificaciones";
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
				else if (recibido.toLowerCase().equals(USO))
				{					
					// Convierte a horas en float
					//float tiempo_funcionando = (float)(sistema.getTiempoFuncionando()/3600000);					
					respuesta = "Uso caldera: " + sistema.getTiempoFuncionando();									
				}
				else
				{
					log.error("Opci�n incorrecta servidor: " + recibido);
					respuesta = "Opci�n incorrecta: '" + recibido +"'";
				}

				// Devuelve OK al cliente
				flujo.writeUTF("OK");
				skCliente.close();
				
				if (respuesta!="")
					notificaciones.enviar(respuesta);
					
			}
		} catch( Exception e ) 
		{
			e.printStackTrace();
			log.error("Error Servidor "  + e.getMessage());
		}		
	}


}
