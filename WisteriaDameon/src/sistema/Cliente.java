package sistema;

import java.io.*;
import java.net.*;
class Cliente {
	static final String HOST = "localhost";
	static final int PUERTO=6001;
	public Cliente(String arg ) {
		try{
			Socket skCliente = new Socket( HOST , PUERTO );
			
			//InputStream aux = skCliente.getInputStream();
			
			
			
			
			 OutputStream outToServer = skCliente.getOutputStream();
	         DataOutputStream out = new DataOutputStream(outToServer);
	         out.writeUTF(arg);
	                      			
	         
			//DataInputStream flujo = new DataInputStream( aux );							
			//System.out.println( flujo.readUTF() );
			skCliente.close();
		} catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}
	public static void main( String[] arg ) {
		new Cliente(arg[0]);
	}
}