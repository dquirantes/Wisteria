import java.util.TimerTask;

import org.apache.log4j.Logger;

import sistema.EnviarNotificaciones;

public class NotificacionesInformacion  extends TimerTask {
	
	private static final Logger log = Logger.getLogger("Dameon");

	EnviarNotificaciones notificaciones;
	SistemaDomotico sistema;
	
	public NotificacionesInformacion(EnviarNotificaciones notificaciones, SistemaDomotico sistema) 
	{
		this.notificaciones = notificaciones;
		this.sistema = sistema;
	}


	public void run() {
		
		if (sistema.getEnviarNotificaciones())
		{
			log.debug("Enviar notificaciones!");
			notificaciones.enviar(sistema.toString_info());
		}else
		{
			log.debug("No habilitado el envío de notificaciones!");
		}
		
		
	}

}
