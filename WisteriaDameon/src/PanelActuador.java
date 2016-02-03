import java.util.TimerTask;

import org.apache.log4j.Logger;

import sistema.BaseDatos;
import sistema.TipoPosicion;


public class PanelActuador extends TimerTask {

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private BaseDatos basedatos;

	private int codigo_instruccion = 0;


	public PanelActuador (SistemaDomotico sistema, BaseDatos basedatos)
	{
		this.sistema = sistema;
		this.basedatos = basedatos;
	}

	@Override
	public void run() 
	{
		log.debug("Run PanelActuador");

		String res;		

		//TODO: comprobar esto antes de subir
		res = basedatos.obtenerOrden();		
		//res = "CLIMATIZADOR;10;5;true;DORMITORIO";

		if (res=="")
		{
			sistema.setErrorSistema(ErroresSistema.ACTUADOR);
		}
		else
		{
			try
			{
				String [] partes = res.split(";");

				log.debug("Recibido: " + res);

				int codigo_instruccion_nuevo= Integer.parseInt(partes[2]);
				float temp = Float.parseFloat(partes[1]);

			

				if (codigo_instruccion_nuevo!=codigo_instruccion)
				{
					sistema.setModoSistema(ModoSistema.valueOf(partes[0]));
					sistema.setTempclimatizador(temp);
					sistema.setEnviarNotificaciones(Boolean.parseBoolean(partes[3]));
					sistema.set_opcionesModo(OpcionesModo.valueOf(partes[4]));

					
					log.debug("Se ha recibido actualizacion " + codigo_instruccion_nuevo);
					codigo_instruccion = codigo_instruccion_nuevo;
					sistema.setAlcanzoTemperatura(false);

					log.debug ("Instruccion: " + codigo_instruccion_nuevo);
					log.debug ("Climatizador modo: " + sistema.getModoSistema());
					log.debug ("Opciones  modo: " + sistema.get_opcionesModo());
					log.debug ("Climatizador temp: " + sistema.getTemperatura_Climatizador());
					log.debug ("Notificaciones: " + sistema.getEnviarNotificaciones());
				}
			}catch (Exception e)
			{
				log.error("Exception actuador");
				sistema.setErrorSistema(ErroresSistema.ACTUADOR);
			}
			


		}

		TipoPosicion posicion = basedatos.obtenerPosicion();
		log.debug ("Posicion recibida: " + posicion.cod_instruccion + " lat: " + posicion.latitud + " long: " + posicion.longitud);
		
		
		// C/Wisteria 
		double long_casa = -3.9254872000000205;
		double lat_casa = 40.51844359999999;
		
		double distancia = DistanceCalculator.distance(lat_casa, long_casa, posicion.latitud, posicion.longitud, "K");
		
		log.debug ("Distancia: " + distancia);
		log.debug("FIN PanelActuador");

	}
}