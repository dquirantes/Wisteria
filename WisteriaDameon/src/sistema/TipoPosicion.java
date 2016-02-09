package sistema;


public class TipoPosicion 
{
	private int cod_instruccion;
	private double latitud;
	private double longitud;

	public TipoPosicion(int instruccion, double lat, double lon)
	{
		this.cod_instruccion = instruccion;
		this.latitud = lat;
		this.longitud = lon;
	}
	
	public double getLatitud()
	{
		return this.latitud;
	}
	
	public double getLongitud()
	{
		return this.longitud;
	}
	
	public int getInstruccion()
	{
		return this.cod_instruccion;
	}
	public String toString()
	{
		return latitud + "," + longitud;	
	}
	

}

