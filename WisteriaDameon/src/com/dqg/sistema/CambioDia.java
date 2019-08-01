package com.dqg.sistema;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.dqg.utilidades.EnviarNotificaciones;

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
			
		
		log.debug("Tiempo funcionamiento: " + sistema.getTiempoFuncionando());
		notificaciones.enviar("Tiempo funcionamiento: " + sistema.getTiempoFuncionando());
		
		log.debug("Inicializar tiempo funcionando");
		// Inicializar		
		sistema.inicializarArranques();		
		sistema.inicializarTiempoFuncionando();		
		log.debug("Tiempo funcionamiento después: " + sistema.getTiempoFuncionando());
		
	}

}
