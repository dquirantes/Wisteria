package com.dqg.sistema;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.dqg.utilidades.EnviarNotificaciones;

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


		log.debug("Enviar notificaciones");
		notificaciones.enviar(sistema.toString_info());

	}

}
