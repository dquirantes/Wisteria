package sistema;

import org.apache.log4j.Logger;

public class EnviarNotificaciones {
		
	private static final Logger log = Logger.getLogger("Dameon");			
	private String programa; 

	public EnviarNotificaciones (String programa)
	{
		this.programa = programa;


	}
	public void enviar (String texto)
	{
		//String args = programa + " \""  + texto + "\"";
				
		ProgramaExterno programa_externo = new ProgramaExterno();
		
		try
		{
			programa_externo.ejecutar(programa,texto);	
		}catch (Exception e)
		{
			log.error ("Excepcion envio notificaciones " + e);
		}
		
												
		log.debug("Enviar notificacion ");
		//log.debug ("ejecutar: " + argumentos);

	}
}


		
	