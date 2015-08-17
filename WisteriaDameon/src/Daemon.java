import java.io.File;
import java.io.IOException;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sistema.BaseDatos;
import sistema.Configuracion;
import sistema.EnviarCorreo;
import sistema.EnviarNotificaciones;



public class Daemon {
	

	private static final Logger log = Logger.getLogger("Dameon");


	


	public static void main(String[] args) {

		String path_aplicacion = System.getProperty("path_aplicacion");		
		String path_configuracion =path_aplicacion + "/cfg/daemon.cfg"; 		
		String ruta_log4j = path_aplicacion + "/cfg/log4j.properties";
		


		PropertyConfigurator.configure(ruta_log4j);


		Configuracion configuracion = new Configuracion (path_configuracion);
		EnviarNotificaciones notificaciones = new EnviarNotificaciones (configuracion.getProgramaNotificaciones());


		log.info("Arrancando Wisteria Daemon");


		
		log.info ("script abrir: " + configuracion.getProgramaAbrir());
		log.info ("script cerrar: " + configuracion.getProgramaCerrar());
		log.info ("script temperatura: " + configuracion.getProgramaSensor());
		log.info ("script notificaciones: " + configuracion.getProgramaNotificaciones());
		log.info ("script placa: " + configuracion.getProgramaPlaca());
		log.info ("BBDD: " + configuracion.getBDName());		
		log.info ("URL Tiempo: " + configuracion.getURLTiempo());



		SistemaDomotico sistema = new SistemaDomotico();
		BaseDatos basedatos = new BaseDatos(configuracion);
		Calculador calculador = new Calculador (sistema,configuracion.gettmp_Margen());



		Rele rele = new Rele(configuracion,sistema);

		SensorTempertura sensor = new SensorTempertura(sistema,configuracion.getProgramaSensor(),configuracion);  
		Timer timer_sensor = new Timer(true);       
		timer_sensor.scheduleAtFixedRate(sensor, 0, configuracion.gettSensor()*1000);


		TemperaturaExterna temp_externa = new TemperaturaExterna (configuracion.getURLTiempo(),sistema);
		Timer timer_externa = new Timer(true);       
		timer_externa.scheduleAtFixedRate(temp_externa , 0, configuracion.gettExterno()*1000);


		PanelActuador actuador = new PanelActuador(sistema, basedatos);  
		Timer timer_actuador = new Timer(true);       
		timer_actuador.scheduleAtFixedRate(actuador , 0, configuracion.gettActuador() * 1000);

		Registro registro = new Registro (sistema, basedatos);  
		Timer timer_registro = new Timer(true);       
		timer_registro .scheduleAtFixedRate(registro, configuracion.gettRegistro()*1000, configuracion.gettRegistro()* 1000);




		File FLAG_FILE = new File(configuracion.getFicheroTemporal());


		EnviarCorreo correo = new EnviarCorreo(configuracion.getCorrreoFrom(), configuracion.getCorrreoUsuario(), configuracion.getCorrreoPassword(), configuracion.getCorrreoHost(),configuracion.getCorrreoPuerto());
		
		
		if (configuracion.getEnviarCorreo())
			correo.enviarCorreo(configuracion.getCorrreoTo(),configuracion.getCorrreoAsunto(),"Arrancando el sistema de calefacción " + sistema);

		
		notificaciones.enviar("Arrancando Sistema");


		try {
			log.debug("Creando fichero " + configuracion.getFicheroTemporal());
			FLAG_FILE.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		while (FLAG_FILE.exists() && sistema.getErrorSistema()==null)
		{
			// Imprime el estado del sistema
			log.info(sistema);
			
			
			EstadoRele estado_old = sistema.getEstadoRele();
			
			// Calcular el nuevo estado
			EstadoRele estado_nuevo = calculador.calcularNuevoEstado();	
			


			if (estado_old!=estado_nuevo)
			{


				if (estado_nuevo == EstadoRele.ABIERTO)			
				{
					log.debug ("Encender caldera");
					rele.abrir();
					basedatos.abrir(sistema.getTemperatura());
					
					if (sistema.getEnviarNotificaciones())
						notificaciones.enviar("Encender la caldera. Climatizador: " + sistema.getTemperatura_Climatizador());
				}
				else if (estado_nuevo == EstadoRele.CERRADO)					
				{
					rele.cerrar();
					log.debug ("Apagar caldera");
					if (sistema.getEnviarNotificaciones())
						notificaciones.enviar("Apagar la caldera. Climatizador: " + sistema.getTemperatura());
					basedatos.cerrar((sistema.getTemperatura()));
				}
				

				// Si hay cambio de estado actualizar en BBDD
				registro.run();

			}

			try {
				Thread.sleep(configuracion.gettBucle()*1000);
			} catch (InterruptedException e) {
				log.error ("Exception Daemon " + e);			
			}

		}
		
		if (sistema.getErrorSistema()!=null)
		{
			log.error ("Finaliza debido al error: " + sistema.getErrorSistema());
			notificaciones.enviar("Error Sistema Domótico " + sistema.getErrorSistema());
		}
		
		if (sistema.getEstadoRele()==EstadoRele.ABIERTO)
		{
			log.info("Apagar caldera antes de salir");
			rele.cerrar();
			basedatos.cerrar((sistema.getTemperatura()));

		}

		log.info("Sale de la aplicacion");

	}




}


