import java.io.*;

/**
 * Class untuk memnciptakan JST
 * 
 * @author (Ban Handy) 
 * @version (1.0 Desember 2007)
 */
public class MyMlp
{
	private final int inUnit	=400;
	private final int outUnit	=1;
	private final int hidUnit	=23;
	private Neuron[] nIn;
	private Neuron[] nOut;
	private Neuron[] nHid;
	private Weight[] inHidW;
	private Weight[] hidOutW;
	private double[] hidBias;
	private double outBias;
	
	/**
	* method untuk melakukan proses inisialisasi JST
	*/
	private void init()
	{
		nIn		=new Neuron[inUnit];
		nOut	=new Neuron[outUnit];
		nHid	=new Neuron[hidUnit];
		inHidW	=new Weight[inUnit*hidUnit];
		hidOutW	=new Weight[hidUnit*outUnit];
		hidBias = new double[hidUnit];
		
		java.util.Random  rand = new java.util.Random();
		for (int i=0;i<hidUnit;i++)
		hidBias[i] = rand.nextDouble()*2-1;
		outBias = rand.nextDouble()*2-1;
	
		for (int i=0;i<(inUnit+hidUnit+outUnit);i++)
		{
			if (i<inUnit)
				nIn[i]=new Neuron(i);
			else if (i<inUnit+hidUnit)
				nHid[i-inUnit]=new Neuron(i);
			else 
				nOut[i-inUnit-hidUnit]=new Neuron(i);
		}
		
		Weight[] tempW1		= new Weight[hidUnit];
		Weight[][] tempW2	= new Weight[hidUnit][inUnit];
		Weight[] tempW3		= new Weight[outUnit];
				
				
		for (int i=0;i<inUnit;i++)
		{
			for(int j=0;j<hidUnit;j++)
			{
			inHidW[i*hidUnit+j]=new Weight(nIn[i],nHid[j]);
			tempW1[j]=inHidW[i*hidUnit+j];
			tempW2[j][i]=inHidW[i*hidUnit+j];
			}
			nIn[i].connection(new Neuron[0],nHid,new Weight[0],tempW1);
		}
		
				
		for (int i=0;i<hidUnit;i++)
		{
			hidOutW[i]= new Weight(nHid[i],nOut[0]);
			tempW3[0] = hidOutW[i];
			nHid[i].connection(nIn,nOut,tempW2[i],tempW3);
		}
			nOut[0].connection(nHid,new Neuron[0],hidOutW,new Weight[0]);
		
	}
	
	/**
	* Konstruktor untuk objek dari class MyMlp
	*/
	public MyMlp()
	{
		init();
	}
	
	/**
	* Konstruktor untuk objek dari class MyMlp
	* @param name   nama file bobot
	*/
	public MyMlp(String name)
	{
		init();
		loadWeight(name);
	}
	
	/**
	* method untuk melakukan proses feedforward
	* @param input   himpunan nilai untuk lapisan masukan
	*/
	private void feedForward(double[] input)
	{
		for (int i=0;i<nIn.length;i++)
			nIn[i].out=input[i];
		for (int i=0;i<nHid.length;i++)
			nHid[i].simOut(hidBias[i]);
		for (int i=0;i<nOut.length;i++)
			nOut[i].simOut(outBias);
	}
	
	/**
	* Konstruktor untuk menghitung nilai output JST
	* @param input   himpunan nilai untuk lapisan masukan
	*/
	public double simmulate(double[] input)
	{
		feedForward(input);
		return nOut[0].out;
	}
	
	/**
	* method untuk menyimpan nilai bobot dan bias ke file
	* @param name   nama file bobot
	* @return status keberhasilan proses penyimpanan
	*/
	public boolean saveWeight(String name) 
	{
		if(name == null) return false;
		try
		{
		File outputFile = new File(name);
		FileWriter out = new FileWriter(outputFile);
		for (int i=0;i<inHidW.length;i++)
		{
			out.write(inHidW[i].weight+";");
			out.write("\n");
		}
		for (int i=0;i<hidOutW.length;i++)
		{
			out.write(hidOutW[i].weight+";");
			out.write("\n");
		}
		
		for (int i=0;i<hidBias.length;i++)
		{
			out.write(hidBias[i]+";");
			out.write("\n");
		}
		out.write(outBias+";");
		
		out.close();
		}
		catch(IOException ioe)
		{
			System.err.print(ioe);
		}
		return true;
	}
	
	/**
	* method untuk mengambil nilai bobot dan bias ke JST
	* @param name   nama file bobot
	* @return status keberhasilan proses pemuatan
	*/
	public boolean loadWeight(String name)
	{
		if(name == null) return false;
		try
		{
		int counter=0;
		double weight;
		int strbufferlen = 50;	
		StringBuffer temp_weight = new StringBuffer(strbufferlen);
		File inputFile = new File(name);
		FileReader in = new FileReader(inputFile);
		int c;
		while ((c = in.read()) != -1) 
		{
			if (c != (int)';' && c != 10 && c != 13 && c != 20) {
				temp_weight.append((char)c);
			} 
			else if (c == (int)';') 
			{
				weight = Double.parseDouble(temp_weight.toString());
				temp_weight.delete(0,strbufferlen);
				if (counter<inUnit*hidUnit)
				{
					inHidW[counter++].weight=weight;
				}
				else if(counter<inUnit*hidUnit+outUnit*hidUnit)
				{
					hidOutW[(counter++)-(inUnit*hidUnit)].weight=weight;
				}
				else if(counter<inUnit*hidUnit+outUnit*hidUnit+hidUnit)
				{
					hidBias[(counter++)-(inUnit*hidUnit+outUnit*hidUnit)]=weight;
				}
				else
				outBias = weight;
			}
			
		}
		
		in.close();
		}
		catch(IOException ioe)
		{
			System.err.print(ioe);
		}
		return true;
	}
	
	/**
	* method untuk mengambil nilai sum squared error JST
	* @param input   himpunan nilai untuk lapisan masukan
	* @param target   himpunan nilai target yang diinginkan
	* @return nilai sum squared error JST
	*/
	public double sumSquaredError(double[][] input, double[] target)
	{
		double sum=0;
		for (int i=0;i<input.length;i++)
		{
			sum+=Math.pow(target[i]-simmulate(input[i]),2);
		}
		return sum*0.5;
	}
	
	/**
	* method untuk mengambil nilai bobot dan bias dari JST
	* @return himpunan nilai bobot dan bias
	*/
	public double[] getWeight()
	{
		double[] weight = new double[inUnit*hidUnit+hidUnit+hidUnit+1];
		for (int i=0;i<weight.length;i++)
		{
			if (i<inUnit*hidUnit)
			weight[i]=inHidW[i].weight;
			else if (i<inUnit*hidUnit+hidUnit)
			weight[i]=hidOutW[i-inUnit*hidUnit].weight;
			else if (i<inUnit*hidUnit+hidUnit+hidUnit)
			weight[i]=hidBias[i-inUnit*hidUnit-hidUnit];
			else
			weight[i]=outBias;
		}
		return weight;
	}
	
	/**
	* method untuk menset nilai bobot dan bias ke JST
	* @param weight himpunan nilai bobot dan bias
	*/
	public void setWeight(double[] weight)
	{
		for (int i = 0; i<inUnit*hidUnit+hidUnit+hidUnit+1;i++)
		{
			if (i<inUnit*hidUnit)
			{
				inHidW[i].weight=weight[i];
			}
			else if(i<inUnit*hidUnit+outUnit*hidUnit)
			{
				hidOutW[i-(inUnit*hidUnit)].weight=weight[i];
			}
			else if(i<inUnit*hidUnit+outUnit*hidUnit+hidUnit)
			{
				hidBias[i-(inUnit*hidUnit+outUnit*hidUnit)]=weight[i];
			}
			else
			outBias = weight[i];
		}
	}
	
	/**
	* method untuk melatih JST
	* @param input   himpunan nilai untuk lapisan masukan
	* @param target   himpunan nilai target yang diinginkan
	* @param rate   nilai learning rate
	* @param momentum   nilai momentum
	*/	
	public void IncrementalTrain(double rate, double[] input, double target, double momentum) {
		// feed fw
		feedForward(input);
		
		// train the output layer first
		for (int i=0; i<nOut.length;i++)
		{
		nOut[i].OutputIncrementalTrain(rate,target,momentum);
		outBias+=nOut[i].updateBias(rate);
		}
		// train hidden layers
		for (int i=0; i<nHid.length;i++)
		{
		nHid[i].HiddenIncrementalTrain(rate,momentum);
		hidBias[i] += nHid[i].updateBias(rate);
		}
	}
	
}