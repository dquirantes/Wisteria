package sistema;

import java.io.*;
import java.net.*;

class Cliente 
{
	/*static final String HOST = "localhost";
	static final int PUERTO=6001;*/
	
	public Cliente(String host, String puerto, String mensaje) 
	{
		try{
			
			
			int port = Integer.parseInt(puerto);
			
			
			Socket skCliente = new Socket(host , port);
			
			InputStream aux = skCliente.getInputStream();
									
			
			 OutputStream outToServer = skCliente.getOutputStream();
	         DataOutputStream out = new DataOutputStream(outToServer);
	         out.writeUTF(mensaje);
	                      			
	         
			DataInputStream flujo = new DataInputStream( aux );							
			System.out.println( flujo.readUTF() );
			skCliente.close();
		} catch( Exception e ) 
		{
			System.out.println( e.getMessage() );
		}
	}
	
	
	public static void main( String[] arg ) 
	{		
		if (arg.length!=3)
		{
			System.out.println ("Uso: cliente host puerto mensaje");
			
		}
		else
		{
			System.out.println ("Host: " + arg[0]);
			System.out.println ("Puerto: " + arg[1]);		
			System.out.println ("Mensaje: " + arg[2]);
			new Cliente(arg[0],arg[1],arg[2]);
		}					
	}
}