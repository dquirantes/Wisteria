package com.dqg.sistema;
import java.io.* ;
import java.net.* ;

import org.apache.log4j.Logger;

import com.dqg.config.Configuracion;
import com.dqg.sistema.datos.BaseDatos;
import com.dqg.utilidades.EnviarNotificaciones;


class Servidor extends Thread  
{
	static final String USUARIO = "telegram";

	static final String INFO = "info";
	static final String SALON= "salon";
	static final String DORMITORIO = "dormitorio";
	static final String HABITACION1 = "habitacion1";
	static final String HABITACION2 = "habitacion2";
	static final String MEDIA = "media";

	static final String RASPBERRY = "raspberry";
	static final String EXTERNA = "externa";
	static final String SALIR = "salir";

	static final String CLIMA_DORMITORIO = "clima dormitorio";
	static final String CLIMA_SALON = "clima salon";
	static final String CLIMA_HABITACION1 = "clima habitacion1";
	static final String CLIMA_HABITACION2 = "clima habitacion2";
	static final String CLIMA_APAGAR = "clima apagar";

	static final String AYUDA = "ayuda";

	/*static final String NOTIFICACIONES = "notificaciones";
	static final String NOTIFICACIONES_SI = "notificaciones_si";
	static final String NOTIFICACIONES_NO = "notificaciones_no";*/

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
		} catch (IOException e) 
		{
			log.error("Error cerrar servidor " + e);

		}
	}
	public void run() 
	{

		String recibido;

		try 
		{
			log.info("Arrancar servidor puerto: " + 5000);
			skServidor = new ServerSocket(5000);



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
				else if (recibido.toLowerCase().equals(MEDIA))
				{				
					respuesta = "Media: " + sistema.calcularTemperaturaMedia() + "º";										
				}
				else if (recibido.toLowerCase().equals(RASPBERRY))
				{					
					respuesta = "Raspberry: " + sistema.getTemperatura_raspi() + "º";;				
				}
				else if (recibido.toLowerCase().equals(EXTERNA))
				{					
					respuesta = "Las Rozas: " + sistema.getTempExterna() + "º";;				
				}
				else if (recibido.toLowerCase().equals(CLIMA_DORMITORIO))
				{					
					basedatos.insertarInstruccion(1, sistema.getTemperatura_Climatizador(), USUARIO, "DORMITORIO");															
				}
				else if (recibido.toLowerCase().equals(CLIMA_SALON))
				{					
					basedatos.insertarInstruccion(1, sistema.getTemperatura_Climatizador(), USUARIO, "SALON");																
				}
				else if (recibido.toLowerCase().equals(CLIMA_HABITACION1))
				{					
					basedatos.insertarInstruccion(1, sistema.getTemperatura_Climatizador(), USUARIO, "HABITACION1");															
				}
				else if (recibido.toLowerCase().equals(CLIMA_HABITACION2))
				{					
					basedatos.insertarInstruccion(1, sistema.getTemperatura_Climatizador(), USUARIO, "HABITACION2");																
				}
				else if (recibido.toLowerCase().equals(CLIMA_APAGAR))
				{					
					basedatos.insertarInstruccion(2, sistema.getTemperatura_Climatizador(), USUARIO, sistema.get_opcionesModo().toString());									
				}
				/*else if (recibido.toLowerCase().equals(NOTIFICACIONES))
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
				}*/

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
					respuesta = "Horas caldera: " + sistema.getTiempoFuncionando();									
				}
				else if (recibido.toLowerCase().equals(AYUDA))
				{									
					respuesta = String.format("comandos disponibles: %s %s %s %s %s %s %s %s %s %s %s %s %s %s" ,
							INFO,SALON,DORMITORIO,HABITACION1,HABITACION2,MEDIA, RASPBERRY, 
							EXTERNA, SALIR, CLIMA_DORMITORIO,  CLIMA_SALON,CLIMA_HABITACION1, 
							CLIMA_HABITACION2, CLIMA_APAGAR);									
				}
				else
				{
					log.error("Opción incorrecta servidor: " + recibido);
					respuesta = "Opción incorrecta: '" + recibido +"'";
				}

				// Devuelve OK al cliente
				flujo.writeUTF("OK");
				skCliente.close();

				if (respuesta!="")
					notificaciones.enviar(respuesta);

			}
		} catch( Exception e ) 
		{
			//e.printStackTrace();
			log.error("Error Servidor "  + e.getMessage());
		}		
	}


}
