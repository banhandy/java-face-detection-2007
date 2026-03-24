import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

/**
 * Class yang menyediakan layanan atau fungsi-fungsi untuk pendeteksian wajah pada citra
 * 
 * @author (Ban Handy) 
 * @version (1.0 Desember 2007)
 */
public class FaceDetector implements Runnable {
	
	private Vector faceWindow;
	private ImageFilter[] imgProc;
	private int step=2;
	private MyMlp myNet;
	private Thread myThread;
	public boolean run=false,scan=false;	
	private double[][] inputS;
	private double[] target;
	private double alpha;
	private double m;
	private double errToleran;
	private double sumErr;
	private Image img;
	private float sFac;
	private int level;
	private double th;
	private int[] mean={ 
						0  ,0  ,0  ,0  ,0  ,0  ,114,112,111,112,0  ,0  ,0  ,0  ,0  ,0  ,
						0  ,0  ,0  ,0  ,120,122,122,122,121,120,121,119,0  ,0  ,0  ,0  ,
						0  ,0  ,113,121,125,129,128,130,130,129,128,124,119,116,0  ,0  ,
						0  ,0  ,118,125,131,134,133,135,137,136,134,127,122,119,0  ,0  ,
						0  ,119,121,128,136,137,135,137,137,136,136,131,125,121,119,0  ,
						0  ,118,122,130,137,139,137,139,139,138,138,134,127,122,120,0  ,
						0  ,120,124,131,135,137,140,143,142,142,139,132,126,123,120,0  ,
						115,121,122,124,125,128,137,144,144,139,130,124,120,120,121,116,
						122,124,120,120,118,120,128,140,140,128,122,117,118,119,123,121,
						129,124,114,111,111,116,125,137,137,126,117,111,111,116,125,128,
						132,124,118,117,117,120,125,138,138,127,121,116,117,120,126,131,
						135,132,133,132,131,126,128,139,139,130,130,130,132,133,133,134,
						137,138,141,140,138,131,132,141,140,135,135,138,139,141,139,135,
						134,136,140,142,138,132,132,142,143,134,135,139,140,141,136,132,
						134,135,139,142,138,130,133,139,139,135,133,137,140,140,133,130,
						130,135,139,143,137,129,121,124,123,124,132,137,141,138,132,128,
						129,133,136,137,133,125,119,120,120,122,129,134,135,134,131,125,
						126,129,133,131,128,124,124,127,126,128,127,126,129,131,128,124,
						0  ,129,129,126,119,117,119,124,124,121,119,120,125,129,127,0  ,
						0  ,128,130,125,117,111,113,119,119,116,115,120,127,132,128,0  ,
						0  ,126,127,129,126,122,120,123,123,123,125,127,129,129,126,0  ,
						0  ,0  ,125,127,127,125,125,125,126,129,130,127,127,125,0  ,0  ,
						0  ,0  ,123,124,126,125,128,131,132,133,131,128,125,123,0  ,0  ,
						0  ,0  ,0  ,0  ,125,126,129,134,135,134,130,127,0  ,0  ,0  ,0  ,
						0  ,0  ,0  ,0  ,0  ,0  ,123,128,128,128,0  ,0  ,0  ,0  ,0  ,0							
						};
							
	private int[] std={
							0 ,0 ,0 ,0 ,0 ,0 ,51,51,52,52,0 ,0 ,0 ,0 ,0 ,0 ,
							0 ,0 ,0 ,0 ,46,44,44,42,43,44,42,45,0 ,0 ,0 ,0 ,
							0 ,0 ,48,45,43,42,43,42,42,42,40,42,43,44,0 ,0 ,
							0 ,0 ,45,42,40,40,43,44,45,43,40,41,42,41,0 ,0 ,
							0 ,44,42,39,37,39,43,44,45,43,41,38,39,41,42,0 ,
							0 ,42,42,38,39,43,48,46,48,46,43,40,37,41,41,0 ,
							0 ,41,42,42,41,46,50,49,50,48,46,43,41,41,40,0 ,
							46,37,40,39,41,42,45,47,47,43,42,43,39,39,37,44,
							38,36,39,42,45,44,46,45,45,44,43,47,43,39,36,36,
							36,35,40,42,47,45,46,43,43,45,44,47,44,40,33,33,
							35,37,37,39,40,40,43,43,44,42,39,40,39,37,35,33,
							35,34,35,37,37,40,41,43,44,39,37,38,36,35,32,35,
							35,39,40,40,40,40,42,44,46,41,39,41,41,40,37,35,
							36,38,43,43,41,43,41,47,46,41,41,43,44,41,37,38,
							38,36,40,42,41,41,44,44,45,42,40,42,43,40,36,36,
							38,36,37,38,40,40,45,43,43,44,38,39,39,37,35,36,
							39,35,35,38,39,41,42,39,40,41,38,39,39,34,33,39,
							41,37,35,38,38,38,41,39,40,38,36,37,37,36,36,42,
							0 ,41,39,38,39,41,44,41,42,42,41,38,37,38,39,0 ,
							0 ,43,37,38,40,42,45,42,43,43,42,40,38,36,39,0 ,
							0 ,43,38,37,39,40,39,35,36,37,38,37,37,37,42,0 ,
							0 ,0 ,38,37,38,38,40,39,36,37,38,37,35,38,0 ,0 ,
							0 ,0 ,39,38,38,38,40,38,38,37,37,38,37,39,0 ,0 ,
							0 ,0 ,0 ,0 ,40,38,41,40,39,41,39,40,0 ,0 ,0 ,0 ,
							0 ,0 ,0 ,0 ,0 ,0 ,42,39,39,42,0 ,0 ,0 ,0 ,0 ,0
						 };
	
	/**
	* Konstruktor untuk objek dari class FaceDetector
	* @param alpha   nilai learning rate
	* @param m   nilai momentum
	* @param errToleran   nilai toleransi error
	*/
	public FaceDetector(double alpha, double m,double errToleran)
	{
		faceWindow= new Vector();
		myNet = new MyMlp();
		this.alpha = alpha;
		this.m=m;
		this.errToleran = errToleran;
	}
	
	/**
	* method untuk mengubah nilai learning rate, momentum dan toleransi error JST
	* @param alpha   nilai learning rate
	* @param m   nilai momentum
	* @param errToleran   nilai toleransi error
	*/
	public void setParam(double alpha, double m,double errToleran)
	{
		this.alpha=alpha;
		this.m=m;
		this.errToleran = errToleran;
	}
	
	/**
	* method yang melakukan proses pendeteksian dan pelatihan JST dengan thread
	* 
	*/
	public void run()
	{
		try
		{
		while (run)
		{
			
			while (myNet.sumSquaredError(inputS,target)>errToleran)
			{
			for (int j=0;j<inputS.length;j++)
			{
				myNet.IncrementalTrain(alpha,inputS[j],target[j],m);
			}
			sumErr=myNet.sumSquaredError(inputS,target);
			}
			run=false;
		}
		
		while (scan)
		{
			int imgCols = img.getWidth(null);
			int imgRows = img.getHeight(null);
			imgProc = new ImageFilter[level];
			imgProc[0]= new ImageFilter(img,imgCols,imgRows);
			scanWind(imgRows,imgCols,0,th,sFac);
    		for (int i=1;i<level;i++) 
			{
			int w=Math.round(imgCols/(sFac*i));
			int h=Math.round(imgRows/(sFac*i));
			imgProc[i]= new ImageFilter(img,w,h);
			scanWind(h,w,i,th,sFac);
    		}
    		scan=false;
		}
		}
		catch(Exception e)
		{
			System.err.print("err "+ e );
			run=false;
			myThread.stop();
		}
	}
	
	private void scanWind(int h,int w,int lvl,double th,float scaleFactor)
	{
		double[] in;
		for (int row=0;row<h-step-27;row=row+step)
    	{
    		for (int col=0;col<w-step-18;col=col+step)
    		{
    			 in = newNorm(imgProc[lvl].getProcWindow(row,col));
    			 double result = myNet.simmulate(in);
    			 if (result>th)
    			 {
    			 	 if (lvl==0)
    			 	 {
    			 	 int[] pos = new int[4];
    			 	 pos[0]=col;
    			 	 pos[1]=row;
    			 	 pos[2]=18;
    			 	 pos[3]=27;
    			 	 faceWindow.add(pos);
    			 	 }
    			 	 else
    			 	 {
    			 	 	int[] pos = new int[4];
    			 		pos[0]= Math.round(col*scaleFactor*lvl);
    			 		pos[1]= Math.round(row*scaleFactor*lvl);
    			 		pos[2]= Math.round(18*scaleFactor*lvl);
    			 		pos[3] =Math.round(27*scaleFactor*lvl);
    			 		faceWindow.add(pos);
    			 	}
    			 }
    			 	 			
    		}
    	}
	}
	
	/**
	* method untuk menghapus wajah yang terdeteksi
	*/
	public void clearFace()
	{
		faceWindow.clear();
	}
	
	/**
	* method untuk mendapatkan koordinat x wajah pada citra
	* @param i   urutan wajah
	* @return  nilai koordinat x wajah pada citra
	*/
	public int getPosX(int i)
	{
		return ((int[])faceWindow.get(i))[0];
	}
	
	/**
	* method untuk mendapatkan koordinat y wajah pada citra
	* @param i   urutan wajah
	* @return  nilai koordinat y wajah pada citra
	*/
	public int getPosY(int i)
	{
		return ((int[])faceWindow.get(i))[1];
	}
	
	/**
	* method untuk mendapatkan koordinat lebar wajah pada citra
	* @param i   urutan wajah
	* @return  nilai koordinat lebar wajah pada citra
	*/
	public int getPosWidth(int i)
	{
		return ((int[])faceWindow.get(i))[2];
	}
	
	/**
	* method untuk mendapatkan koordinat tinggi wajah pada citra
	* @param i   urutan wajah
	* @return  nilai koordinat tinggi wajah pada citra
	*/
	public int getPosHeight(int i)
	{
		return ((int[])faceWindow.get(i))[3];
	}
	
	/**
	* method untuk melakukan proses pendeteksian wajah
	* @param img   citra masukan
	* @param th   nilai threshold
	* @param level   tahapan pengecilan
	* @param sFac   skala pengecilan
	*/
	public void scanner(Image img,double th, int level,float sFac)
	{
		this.img=img;
		this.th=th;
		this.level=level;
		this.sFac=sFac;
		scan=true;
		myThread=new Thread(this);
		myThread.start();
	}
   
	/**
	* method untuk menyimpan nilai bobot dan bias ke file
	* @param url   url file bobot
	* @return status keberhasilan proses penyimpanan
	*/
   public boolean saveWeight(String url)
   {
   		if (myNet.saveWeight(url)) return true;
   		return false;
   }
   
   /**
	* method untuk mengambil nilai bobot dan bias ke JST
	* @param url   url file bobot
	* @return status keberhasilan proses pemuatan
	*/
   public boolean loadWeight(String url)
   {
   		if (myNet.loadWeight(url)) return true;
   		return false;
   }
   
   /**
	* method untuk mengambil jumlah wajah terdeteksi
	* @return banyak wajah yang terdeteksi
	*/
   public int getFacesCount()
	{
		int i=0;
		try
		{
			i=faceWindow.size();
		}
		catch(Exception e)
		{
			
		}
		return i;
	}
	
	
	private Image loadImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        int status = icon.getImageLoadStatus();
        if(status != MediaTracker.COMPLETE)
            System.out.print("fd");
        return icon.getImage();
    }
   
    /**
	* method untuk mengambil nilai sum squared error JST
	* @return nilai sum squared error JST
	*/
    public double getSumErr()
    {
    	return sumErr;
    }
    
    /**
	* method untuk merubah ukuran citra bila lebar atau tinggi citra lebih dari 300 piksel 
	* @return citra yang mau dilakukan pendeteksian wajah
	*/
    public Image loadScanImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        int status = icon.getImageLoadStatus();
        if(status != MediaTracker.COMPLETE)
            System.out.print("fd");
        int w = icon.getImage().getWidth(null);
        int h = icon.getImage().getHeight(null);
        if ((w>250)||(h>250))
        {
        	if (w>250)
        	{
        	h=(int)Math.round(h/(w/300.0));
        	w=300;
        	}
        	if (h>300)
        	{
        	w=(int)Math.round(300/(h/300.0));
        	h=300;
        	}
        	Image img = icon.getImage().getScaledInstance(w,h,icon.getImage().SCALE_SMOOTH);
        	MediaTracker mt = new MediaTracker(new Component() {});
        	mt.addImage(img, 0);
        	try {
            mt.waitForID(0);
        	} catch(InterruptedException e) {
            System.out.println("mt interrupted");
        	}
        	return img;
        }
        return icon.getImage();
    }
    
    /**
	* method untuk menciptakan himpunan citra pelatihan
	* @param dir   nama folder citra pelatihan
	* @param fD   jumlah citra wajah
	* @param nFd   jumlah citra bukan wajah
	* @param numNFd   jumlah potongan citra bukan wajah yang diambil
	*/
    public void createTrainSet(String dir, int fD,int nFd,int numNFd)
    {
    	try
    	{
    	target= new double[fD*2+numNFd*nFd];
		inputS= new double[fD*2+numNFd*nFd][400];
						
		String path="";
		Random rand = new Random();
		for (int i=0;i<fD;i++)
		{
			path=dir+"\\s"+(i+1)+".jpg";
			ImageFilter imgFil = new ImageFilter(loadImage(path));
			inputS[i] = imgFil.getProcWindow();
			target[i] = 0.9;
		}
		for (int i=0;i<fD;i++)
		{
		inputS[i+fD]=ImageFilter.mirrorX(inputS[i]);
		target[i+fD] = 0.9;
		}
		for (int i=0;i<nFd;i++)
		{
			path=dir+"\\"+(i+1)+".jpg";
			Image img=loadImage(path);
			int imgCols = img.getWidth(null);
			int imgRows = img.getHeight(null);
			ImageFilter imgFil = new ImageFilter(img,imgCols,imgRows);
			for (int j=0;j<numNFd;j++)
			{
			int x = rand.nextInt(imgFil.imgCols-18);
			int y = rand.nextInt(imgFil.imgRows-27);
			inputS[(i*numNFd)+j+(fD*2)] = imgFil.getProcWindow(y,x);
			target[(i*numNFd)+j+(fD*2)] = -0.9;
			}
		}
		//calMean(inputS);
		inputS = newNorm(inputS);
		sumErr=myNet.sumSquaredError(inputS,target);
		}
		catch (Exception e)
		{
		}
    }
    
    /**
	* method untuk memulai proses pelatihan JST
	*/
    public void training()
	{
		
		run=true;
		myThread=new Thread(this);
		myThread.start();
		
	}
	
	/**
	* method untuk menghentikan proses pelatihan JST
	*/
	public void stopTrain()
	{
		myThread.stop();
		run=false;
	}
	
	private void calMean(double[][] input)
	{
		int sum;
		mean = new int[400];
		std = new int[400];
		try
		{
			
		File outputFile = new File("mean.txt");
		FileWriter outF = new FileWriter(outputFile);
		
		for (int j=0;j<400;j++)
		{
			sum=0;
			for (int i=0;i<input.length;i++)
			{
			sum+=input[i][j];
			}
			mean[j]=Math.round(sum/input.length);
			outF.write(mean[j]+",");
			if ((j%16==0)&&(j!=0)) 
			outF.write("\n");
		}
		outF.write("\n");
		for (int j=0;j<400;j++)
		{
			sum=0;
			for (int i=0;i<input.length;i++)
			{
			sum+=Math.pow(mean[j]-input[i][j],2);
			}
			std[j]=(int)Math.sqrt(sum/input.length);
			outF.write(std[j]+",");
			if ((j%16==0)&&(j!=0)) 
			outF.write("\n");
		}
		outF.close();
		}
		catch(Exception e)
		{
			System.err.print(e);
		}
		
	}
	
	private double[] newNorm(double[] input)
	{
		double[] nInput=new double[400];
		for (int i=0;i<input.length;i++)
		{
			if (std[i]!=0)
			nInput[i] = (input[i]-mean[i])/std[i];
			else
			nInput[i] = (input[i]-mean[i]);
		}
		return nInput;
	}
	
	private double[][] newNorm(double[][] input)
	{
		double[][] nInput=new double[input.length][400];
		for (int i=0;i<input.length;i++)
		{
			nInput[i]=newNorm(input[i]);
		}
		return nInput;
	}
	
}