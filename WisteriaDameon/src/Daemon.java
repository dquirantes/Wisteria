import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sistema.BaseDatos;
import sistema.Configuracion;
import sistema.EnviarCorreo;
import sistema.EnviarNotificaciones;



public class Daemon {


	private static final Logger log = Logger.getLogger("Dameon");





	public static void main(String[] args) 
	{

		String path_aplicacion = System.getProperty("path_aplicacion");		
		String path_configuracion =path_aplicacion + "/cfg/daemon.cfg"; 		
		String ruta_log4j = path_aplicacion + "/cfg/log4j.properties";


		long ms_inicio = System.currentTimeMillis();
		long ms_arranque = System.currentTimeMillis();
		PropertyConfigurator.configure(ruta_log4j);


		Configuracion configuracion = new Configuracion (path_configuracion);



		log.info("Arrancando Wisteria Daemon David");


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





		File FLAG_FILE = new File(configuracion.getFicheroTemporal());

		if (FLAG_FILE.exists())
		{
			log.info ("El fichero temporal existe");
		}

		EnviarCorreo correo = new EnviarCorreo(configuracion.getCorrreoFrom(), configuracion.getCorrreoUsuario(), configuracion.getCorrreoPassword(), configuracion.getCorrreoHost(),configuracion.getCorrreoPuerto());


		EnviarNotificaciones notificaciones = new EnviarNotificaciones (configuracion.getProgramaNotificaciones());
		notificaciones.enviar("Arrancando Sistema Domotico");

		NotificacionesInformacion notificaciones_info = new NotificacionesInformacion (notificaciones,sistema);
		Timer timer_notificaciones = new Timer(true);
		timer_notificaciones.scheduleAtFixedRate(notificaciones_info, 30000, configuracion.gettNotificaciones()* 1000);


		GenerarXML generarxml = new GenerarXML (sistema, configuracion.getFicheroEstado()); 
		Timer timer_xml = new Timer(true);
		timer_xml.scheduleAtFixedRate(generarxml, 0, configuracion.getTestado()* 1000);


		Servidor servidor = new Servidor(sistema, notificaciones,configuracion, basedatos);
		servidor.start();


		if (configuracion.getEnviarCorreo())
			correo.enviarCorreo(configuracion.getCorrreoTo(),configuracion.getCorrreoAsunto(),"Arrancando Sistema Domotico");




		// Arrancando el resto de servicios

		SensorTempertura sensor = new SensorTempertura(sistema,configuracion.getProgramaSensor(),configuracion);  
		Timer timer_sensor = new Timer(true);       
		timer_sensor.scheduleAtFixedRate(sensor, 0, configuracion.gettSensor()*1000);


		TemperaturaExterna temp_externa = new TemperaturaExterna (configuracion.getURLTiempo(),sistema);
		Timer timer_externa = new Timer(true);       
		timer_externa.scheduleAtFixedRate(temp_externa , 0, configuracion.gettExterno()*1000);


		PanelActuador actuador = new PanelActuador(sistema, basedatos, configuracion);  
		Timer timer_actuador = new Timer(true);       
		timer_actuador.scheduleAtFixedRate(actuador , 0, configuracion.gettActuador() * 1000);


		Registro registro = new Registro (sistema, basedatos);  
		Timer timer_registro = new Timer(true);       
		timer_registro.scheduleAtFixedRate(registro, configuracion.gettRegistro()*1000, configuracion.gettRegistro()* 1000);

		RegistroWeb registro_web = new RegistroWeb (sistema, configuracion.getFicheroWeb());
		Timer timer_registro_web = new Timer(true);       
		timer_registro_web.scheduleAtFixedRate(registro_web, configuracion.gettRegistroWeb()*1000, configuracion.gettRegistroWeb()* 1000);





		//Thread cambio de d�a
		Date horaCambioDia = new Date(System.currentTimeMillis());

		Calendar c = Calendar.getInstance();
		c.setTime(horaCambioDia);

		//c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 0);
		horaCambioDia = c.getTime();
		Timer timer_cambio_dia = new Timer();
		log.debug ("Planificaci�n cambio de hora: " + horaCambioDia);
		timer_cambio_dia.schedule(new CambioDia(sistema,notificaciones), horaCambioDia, 86400000);

		try 
		{
			log.debug("Creando fichero temporal: " + configuracion.getFicheroTemporal());
			FLAG_FILE.createNewFile();
		} catch (IOException e1) 
		{
			log.error(e1);
		}



		while (FLAG_FILE.exists() && sistema.getErrorSistema()==null)
		{
			// Imprime el estado del sistema en cada bucle
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
						notificaciones.enviar("Encender la caldera");

					// Cuenta de ciclos de funcionamiento
					sistema.aumentarArranques();
					
					
					ms_arranque = System.currentTimeMillis();
					log.debug("Tiempo arranque: " + ms_arranque);
				}
				else if (estado_nuevo == EstadoRele.CERRADO)					
				{
					rele.cerrar();
					log.debug ("Apagar caldera");
					if (sistema.getEnviarNotificaciones())
						notificaciones.enviar("Apagar la caldera");
					basedatos.cerrar((sistema.getTemperatura()));
					
					long ms_parada = System.currentTimeMillis();
					
					long duracion = ms_parada - ms_arranque;
					log.debug("Tiempo parada: " + duracion);
					
					sistema.incrementarTiempoFuncionando(duracion);
					
					log.debug("Acumulado sistema: " + sistema.getTiempoFuncionando());
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
			notificaciones.enviar("Error Sistema Dom�tico " + sistema.getErrorSistema());
		}

		if (sistema.getEstadoRele()==EstadoRele.ABIERTO)
		{
			log.info("Apagar caldera antes de salir");
			rele.cerrar();
			basedatos.cerrar((sistema.getTemperatura()));
		}


		// Cancela los timer
		log.debug("Cancela los timer");
		timer_sensor.cancel();
		timer_externa.cancel();
		timer_actuador.cancel();		
		timer_registro.cancel();
		timer_registro_web.cancel();
		timer_notificaciones.cancel();
		timer_xml.cancel();
		timer_cambio_dia.cancel();
		
		// Cierra el servidor
		servidor.cerrar();
		long ms_fin = System.currentTimeMillis();

		log.info ("Tiempo ejecucion: " + (ms_fin-ms_inicio)/1000 + " sg");
		notificaciones.enviar("Parada sistema dom�tico");
		log.info("Sale de la aplicacion");


	}




}


