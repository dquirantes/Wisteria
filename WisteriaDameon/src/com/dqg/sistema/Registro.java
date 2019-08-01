package com.dqg.sistema;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.dqg.sistema.datos.BaseDatos;



public class Registro extends TimerTask {
	private static final Logger log = Logger.getLogger("Dameon");


	private SistemaDomotico sistema;
	private BaseDatos basedatos;
	

	public Registro (SistemaDomotico sistema, BaseDatos basedatos)
	{
		this.sistema = sistema;
		this.basedatos = basedatos;	
	}

	@Override
	public void run() {
		log.debug ("Registrar informacion en la BBDD");

		Boolean res = 
				basedatos.insertarSistema(sistema.getTemperatura(),sistema.getHumedad(),
						sistema.getTempExterna(),sistema.getEstadoRele().toString(),
						sistema.getTemperatura_Climatizador(),sistema.getTemperatura_dormitorio(),sistema.getHumedad_dormitorio(),
						sistema.getTemperatura_habitacion1(),sistema.getHumedad_Habitacion1(),
						sistema.getTemperatura_habitacion2(),sistema.getHumedad_Habitacion2(),
						sistema.getTemperatura_raspi(),
						sistema.get_opcionesModo().toString(),
						sistema.calcularTemperaturaMedia());

		if (!res)
		{
			//sistema.setErrorSistema(ErroresSistema.REGISTRO);
		}
	}
}

