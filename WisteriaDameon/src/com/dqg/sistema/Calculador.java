package com.dqg.sistema;
import org.apache.log4j.Logger;

import com.dqg.tipos.EstadoRele;
import com.dqg.tipos.ModoSistema;


public class Calculador 
{
	//static final float diferencia_temperatura=0.2f;

	private static final Logger log = Logger.getLogger("Dameon");
	SistemaDomotico sistema;
	float diferencia_temperatura;
	
	public Calculador(SistemaDomotico sis, float diferencia)
	{
		this.sistema = sis;
		this.diferencia_temperatura = diferencia;
	}

	public EstadoRele calcularNuevoEstado()	
	{
		EstadoRele nuevo_estado = null;


		if (sistema.getModoSistema()==ModoSistema.CLIMATIZADOR)
		{			
			float temperatura_referencia =0;			
			float tempertura_objetivo = sistema.getTemperatura_Climatizador();

				

			switch(sistema.get_opcionesModo())
			{
			case SALON:
				temperatura_referencia = sistema.getTemperatura();				
				break;			
			case DORMITORIO:
				temperatura_referencia = sistema.getTemperatura_dormitorio();
				break;			
			case HABITACION1:
				temperatura_referencia = sistema.getTemperatura_habitacion1();
				break;
			case HABITACION2:
				temperatura_referencia = sistema.getTemperatura_habitacion1();
			case MEDIA:
				temperatura_referencia = sistema.calcularTemperaturaMedia();				
				break;						
			case MINIMO:
				temperatura_referencia = sistema.calcularTemperaturaMinima();
				break;
			case MAXIMO:
				temperatura_referencia = sistema.calcularTemperaturaMaxima();
				break;
			}

			//log.debug("temperatura_referencia " + temperatura_referencia );

			
			if (temperatura_referencia==0)
			{
				log.debug("Sin inicializar todavía");
				nuevo_estado = EstadoRele.CERRADO;
			}
				
			else if (temperatura_referencia < tempertura_objetivo)
			{								

				if (!sistema.getAlcanzoTemperatura())					
					nuevo_estado = EstadoRele.ABIERTO;
				else
				{
					// Cuando se alcanzó la temperatura, pero ha bajado ligeramente
					float diferencia = tempertura_objetivo - temperatura_referencia;

					log.debug ("Alcanzó temperatura recientemente. La diferencia es + "+ diferencia);


					if (diferencia>diferencia_temperatura)					
					{						
						nuevo_estado = EstadoRele.ABIERTO;
						sistema.setAlcanzoTemperatura(false);
						log.debug ("Arranca nuevamente y inicializa ");
					}
					else
					{
						log.debug(("La diferencia de temperatura es baja, no arranca el sistema"));
						nuevo_estado = EstadoRele.CERRADO;
					}
				}

			}
			else
			{										
				// Temperatura mayor que la solicitad
				sistema.setAlcanzoTemperatura(true);
				nuevo_estado = EstadoRele.CERRADO;			
			}

		}			
		else if (sistema.getModoSistema() == ModoSistema.OFF)
		{
			log.debug ("Modo apagado");
			nuevo_estado = EstadoRele.CERRADO;
		}
		else if (sistema.getModoSistema() == ModoSistema.ON_FIJO)
		{
			log.debug ("Modo siempre encendido");
			nuevo_estado = EstadoRele.ABIERTO;
		}

		return nuevo_estado;

	}
}
