import java.util.TimerTask;

import org.apache.log4j.Logger;

import sistema.EnviarNotificaciones;

public class CambioDia  extends TimerTask {
	
	private static final Logger log = Logger.getLogger("Dameon");

	//Hola Mundo 
	EnviarNotificaciones notificaciones;
	SistemaDomotico sistema;
	
	public CambioDia(SistemaDomotico sistema, EnviarNotificaciones notificaciones) 
	{
		this.notificaciones = notificaciones;
		this.sistema = sistema;
	}


	public void run() 
	{		
		log.debug("Ejecutar clase de cambio de día");
		
		log.debug("Arranques caldera: " + sistema.getArranques());
		notificaciones.enviar("Arranques caldera: " + sistema.getArranques());
		
		// Convierte a horas en float
		float tiempo_funcionando = (float)(sistema.getTiempoFuncionando()/3600000);
		
		log.debug("Tiempo funcionamiento: " + tiempo_funcionando);
		notificaciones.enviar("Tiempo funcionamiento: " + tiempo_funcionando);
		
		
		sistema.inicializarArranques();
		
					
	}

}
