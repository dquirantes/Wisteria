
import org.apache.log4j.Logger;

import sistema.Configuracion;
import sistema.ProgramaExterno;



public class Rele {

	private static final Logger log = Logger.getLogger("Dameon");
	

	ProgramaExterno programa = new ProgramaExterno();


	private Configuracion configuracion;
	private SistemaDomotico sistema;


	public Rele (Configuracion config, SistemaDomotico sis)	
	{
		configuracion = config;
		sistema = sis;		

	}



	public boolean abrir ()
	{		

		if (sistema.getEstadoRele()!=EstadoRele.ABIERTO)
		{
			log.debug ("Abrir rele");


			try {
				programa.ejecutar(configuracion.getProgramaAbrir());
			} catch (Exception e) {
				e.printStackTrace();
			}
			sistema.setEstadoRele(EstadoRele.ABIERTO);
			return true;
		}

		return false;

	}

	public boolean cerrar ()
	{

		if (sistema.getEstadoRele()!=EstadoRele.CERRADO)
		{
			log.debug ("Cerrar rele");

			try {
				programa.ejecutar(configuracion.getProgramaCerrar());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sistema.setEstadoRele(EstadoRele.CERRADO);
			return true;
		}
		return false;

	}



}
