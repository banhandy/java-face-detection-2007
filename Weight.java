/**
 * Class untuk menghubungkan neuron dan berisikan nilai bobot
 * 
 * @author (Ban Handy) 
 * @version (1.0 Desember 2007)
 */
 
public class Weight
{
	public double weight;
	public Neuron sourceunit;
	public Neuron targetunit;
	public double prevweight;
	
	/**
	* Konstruktor untuk objek dari class Weight
	* @param sourceunit   neuron sumber
	* @param  targetunit   neuron target
	*/
	public Weight (Neuron sourceunit, Neuron targetunit)
	{
		this.sourceunit = sourceunit;
		this.targetunit = targetunit;
		java.util.Random rand = new java.util.Random();
		weight = rand.nextDouble()*2-1;
		prevweight = weight;
	}
		
}