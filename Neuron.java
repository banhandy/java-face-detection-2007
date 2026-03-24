/**
 * Class untuk komputasi JST
 * 
 * @author (Ban Handy) 
 * @version (1.0 Desember 2007)
 */

public class Neuron
{
	double out;
	Neuron[] nIn;
	Neuron[] nOut;
	Weight[] wIn;
	Weight[] wOut;
	int id;
	double error;
	
	/**
	* Konstruktor untuk objek dari class Neuron
	* @param id   nomor urut neuron
	*/
	public Neuron(int id)
	{
		this.id=id;
	}
	
	/**
	* Method untuk menciptakan koneksi antar objek neuron dan objek weight
	* @param nIn   objek-objek neuron yang masuk
	* @param nOut   objek-objek neuron yang keluar
	* @param wIn   objek-objek weight yang keluar
	* @param wOut   objek-objek weight yang keluar
	*/
	public void connection(Neuron[] nIn, Neuron[] nOut, Weight[] wIn, Weight[] wOut)
	{
		this.nIn	= nIn;
		this.nOut	= nOut;
		this.wIn	= wIn;
		this.wOut	= wOut;
	}
	
	/**
	* method untuk mencari nilai output neuron
	* @param bias   nilai bias neuron
	*/
	public void simOut(double bias)
	{
		double activation = 0;
		for(int i=0;i<nIn.length;i++)
		{
			activation+=nIn[i].out*wIn[i].weight;
		}
		activation+=bias;
		out = ( 2 / ( 1 + Math.exp( - activation / 1.2 ) ) ) - 1;
	}
	
	/**
	* method untuk melakukan pelatihan lapisan tersembunyi
	* @param rate   nilai learning rate
	* @param momentum   nilai momentum
	*/
	public void HiddenIncrementalTrain (double rate,double momentum) {
		// first compute the error
		double temp_diff = 0;
		for (int i = 0; i < nOut.length; i++) {
			temp_diff += nOut[i].error * wOut[i].prevweight;
		}
		error = temp_diff * Derivative();
		IncrementalUpdateWeights(rate,momentum);
	}
	
	/**
	* method untuk melakukan pelatihan lapisan keluaran
	* @param rate   nilai learning rate
	* @param momentum   nilai momentum
	* @param target   nilai target yang ingin dicapai
	*/
	public void OutputIncrementalTrain (double rate, double target, double momentum) {
		this.error = (target - out) * Derivative();
		IncrementalUpdateWeights(rate,momentum);
	}
	
	/**
	* method untuk melakukan update weight
	* @param rate   nilai learning rate
	* @param momentum   nilai momentum
	*/
	private void IncrementalUpdateWeights (double rate,double momentum) {
		double temp_weight;
		for (int i = 0; i < wIn.length; i++) {
			temp_weight = wIn[i].weight;
			wIn[i].weight += (rate * 0.1 * error * nIn[i].out) + ( momentum * ( wIn[i].weight - wIn[i].prevweight ) );
			wIn[i].prevweight = temp_weight;
	}
	}
	
	/**
	* method untuk melakukan perhitungan derivatif dari fungsi aktivasi
	* @return     hasil nilai derivatif output 
	*/
	public double Derivative () {
		double temp_derivative;
		temp_derivative = ( 1 - Math.pow( out , 2 ) ) / ( 2 * 1.2 ); 
		return temp_derivative;
	}
	
	/**
	* method untuk melakukan menghitung nilai update bias
	* @param rate   nilai learning rate
	* @return  nilai bias yang harus diupdate
	*/
	public double updateBias(double rate)
	{
		double tempBias;
		tempBias = error*rate;
		return tempBias;
	}
}