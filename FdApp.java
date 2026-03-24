import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;


public class FdApp extends JFrame implements ActionListener,Runnable
{
	private final static String title="Simulasi Komponen Pendeteksian Wajah";
	private Image modImg;
	private JButton createSetBtn,loadButton,filterButton,tranButton,saveWBtn,loadWBtn,stopBtn,setPBtn;
  	private int fWidth=0,fHeight=0;
  	private JTextField tAlpha,tMomen,tTrainSite,tJumFd,tJumNFd,tNFd,tLevel,tFactor,tErrToleran,tTh;
  	private JLabel lAlpha,lMomen,lTrainSite,lJumFd,lJumNFd,lNFd,lLevel,lFactor,lErrToleran,lTh;
  	FaceDetector fd;
  	private JImagePanel pImg;
  	private JPanel pTrain,pCreateSet,pScan,pSystem,pPane,pState;
  	private Thread myThread;
  	private JLabel lStatus,lErr;
  	private double err;
  	private String state;
  	
	public FdApp()
	{
		super(title);
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		state="Ready";
		createUI();
		fd = new FaceDetector(Double.parseDouble(tAlpha.getText()),Double.parseDouble(tMomen.getText()),Double.parseDouble(tErrToleran.getText()));
		modImg=loadImage("junk.gif");
		this.setResizable(false);
		setVisible(true);
		pack();
		center();
		validate();
	}
  	
  	public void run()
  	{
  		while (fd.run)
		{
		lStatus.setText("Status: Training");
		lErr.setText("Sum Squared Error : "+fd.getSumErr());
  		}
  		while (fd.scan)
		{
		lStatus.setText("Status: Scanning");
		repaint();
  		}
  		filterButton.setEnabled(true);
  		loadButton.setEnabled(true);
		tranButton.setEnabled(true);
		saveWBtn.setEnabled(true);
		loadWBtn.setEnabled(true);
		stopBtn.setEnabled(true);
  		lStatus.setText("Status: Ready");
  	}
  	
  	private void center()
	{
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension app=getSize();
		int x=(screen.width-app.width)/2;
		int y=(screen.height-app.height)/2;
		setLocation(x,y);
	}
	
	private Image loadImage(String path) {
		fd.clearFace();
        return fd.loadScanImage(path);
    }
	
	private void createUI()
	{
		loadButton=new JButton("Load Image");
		filterButton =new JButton("Scan");
		tranButton =new JButton("Start Training");
		saveWBtn =new JButton("Save Weight");
		loadWBtn =new JButton("Load Weight");
		stopBtn=new JButton("Stop Training");
		setPBtn =new JButton("Set Parameter");
		createSetBtn = new JButton("Create Train Set");
				
		filterButton.addActionListener(this);
		loadButton.addActionListener(this);
		tranButton.addActionListener(this);
		saveWBtn.addActionListener(this);
		loadWBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		setPBtn.addActionListener(this);
		createSetBtn.addActionListener(this);
		
		tAlpha = new JTextField(5);
		tAlpha.setText("0.1");
		tMomen = new JTextField(5);
		tMomen.setText("0.7");
		tTrainSite = new JTextField(5);
		tTrainSite.setText("train set");
		tJumFd = new JTextField(5);
		tJumFd.setText("41");
		tJumNFd = new JTextField(5);
		tJumNFd.setText("100");
		tNFd = new JTextField(5);
		tNFd.setText("3");
		tLevel = new JTextField(5);
		tLevel.setText("6");
		tFactor = new JTextField(5);
		tFactor.setText("1.2");
		tErrToleran = new JTextField(5);
		tErrToleran.setText("0.05");
		tTh = new JTextField(5);
		tTh.setText("0.7");
		
		lStatus = new JLabel("Status : "+state);
		lErr = new JLabel("Sum Squared Error JST : "+err);
		lAlpha = new JLabel("Learning Rate");
		lMomen = new JLabel("Momentum");
		lTrainSite = new JLabel("Folder Citra Pelatihan");
		lJumFd = new JLabel("Jumlah Citra Wajah");
		lJumNFd = new JLabel("Jumlah Citra Bukan Wajah Untuk Tiap File");
		lNFd = new JLabel("Banyak File Citra Bukan Wajah");
		lLevel = new JLabel("Scale Level");
		lFactor = new JLabel("Scale Factor");
		lErrToleran = new JLabel("Toleransi Error");
		lTh = new JLabel("Threshold");
		
		
		pTrain = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		pTrain.add(lAlpha,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		pTrain.add(tAlpha,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 1;
		pTrain.add(lMomen,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 1;
		pTrain.add(tMomen,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 2;
		pTrain.add(lErrToleran,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 2;
		pTrain.add(tErrToleran,c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		pTrain.add(setPBtn,c);
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		pTrain.add(tranButton,c);
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		pTrain.add(stopBtn,c);
		pTrain.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Training"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		pScan = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		pScan.add(lLevel,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		pScan.add(tLevel,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 1;
		pScan.add(lFactor,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 1;
		pScan.add(tFactor,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 2;
		pScan.add(lTh,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 2;
		pScan.add(tTh,c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		pScan.add(filterButton,c);
		pScan.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Scan"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		pSystem = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		pSystem.add(loadWBtn,c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		pSystem.add(saveWBtn,c);
		pSystem.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("System"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		
		pCreateSet = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		pCreateSet.add(lTrainSite,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		pCreateSet.add(tTrainSite,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 1;
		pCreateSet.add(lJumFd,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 1;
		pCreateSet.add(tJumFd,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 2;
		pCreateSet.add(lNFd,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 2;
		pCreateSet.add(tNFd,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 3;
		pCreateSet.add(lJumNFd,c);
		c.gridx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 3;
		pCreateSet.add(tJumNFd,c);
		c.gridx = 2;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.gridheight=2;
		pCreateSet.add(loadButton,c);
		c.gridx = 2;
		c.insets = new Insets(5,5,5,5);
		c.gridy = 2;
		c.gridheight=2;
		pCreateSet.add(createSetBtn,c);
		pCreateSet.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Create Set"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		pPane=new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		pPane.add(pTrain,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 1;
		pPane.add(pScan,c);
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 2;
		pPane.add(pSystem,c);
		
		pImg=new JImagePanel(70,35);
		pImg.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Citra"),BorderFactory.createEmptyBorder(5,5,5,5)));
		pImg.setLayout(new BorderLayout());
		pImg.add(pCreateSet,BorderLayout.SOUTH);
		
		pState = new JPanel();
		pState.add(lStatus);
		pState.add(new JLabel("                 "));
		pState.add(lErr);
				
		this.add(pState,BorderLayout.NORTH);
		this.add(pImg,BorderLayout.CENTER);
		this.add(pPane,BorderLayout.EAST);
		
		addWindowListener(new WindowAdapter() {
      	public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });
	}
	
	public void scan()
	{
		fd.clearFace();
		fd.scanner(modImg,Double.parseDouble(tTh.getText()),Integer.parseInt(tLevel.getText()),Float.parseFloat(tFactor.getText()));
		myThread = new Thread(this);
		myThread.start();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String msg = ae.getActionCommand();
		if (msg.equals("Load Image"))
		{
			lStatus.setText("Status : Loading Image");
			FileDialog openDialog=new FileDialog(this,"Select Image");
			openDialog.show();
			if (openDialog.getFile() == null) return;
			String path = openDialog.getDirectory() + openDialog.getFile();
        	modImg=loadImage(path);
        	setTitle(title + "-" + path);
        	pack();
        	center();
    		validate();
    		repaint();
    		lStatus.setText("Status : Ready");
		}
		else if (msg.equals("Scan"))
		{
			lStatus.setText("Status : Scanning");
			filterButton.setEnabled(false);
			loadButton.setEnabled(false);
			tranButton.setEnabled(false);
			saveWBtn.setEnabled(false);
			loadWBtn.setEnabled(false);
			stopBtn.setEnabled(false);
			scan();
		}
		else if (msg.equals("Start Training"))
		{
			tranButton.setEnabled(false);
			saveWBtn.setEnabled(false);
			loadWBtn.setEnabled(false);
			filterButton.setEnabled(false);
			lStatus.setText("Status : Training");
			fd.training();
			myThread=new Thread(this);
			myThread.start();
		}
		else if (msg.equals("Stop Training"))
		{
			lStatus.setText("Status : Ready");
			fd.stopTrain();
			myThread.stop();
			filterButton.setEnabled(true);
  			loadButton.setEnabled(true);
			tranButton.setEnabled(true);
			saveWBtn.setEnabled(true);
			loadWBtn.setEnabled(true);
			stopBtn.setEnabled(true);
		}
		else if (msg.equals("Save Weight"))
		{
			FileDialog openDialog=new FileDialog(this,"Select Image");
			openDialog.setMode(FileDialog.SAVE);
			openDialog.show();
			if (openDialog.getFile() == null) return;
			String path = openDialog.getDirectory() + openDialog.getFile();
			if (fd.saveWeight(path+".dat")) System.out.println("Saved");
		}
		else if (msg.equals("Load Weight"))
		{
			FileDialog openDialog=new FileDialog(this,"Select Image");
			openDialog.show();
			if (openDialog.getFile() == null) return;
			String path = openDialog.getDirectory() + openDialog.getFile();
			if (fd.loadWeight(path))  System.out.println("Loaded");
		}
		else if (msg.equals("Set Parameter"))
		{
			lStatus.setText("Status : Set Parameter");
			fd.setParam(Double.parseDouble(tAlpha.getText()),Double.parseDouble(tMomen.getText()),Double.parseDouble(tErrToleran.getText()));
			lStatus.setText("Status : Ready");
		}
		else
		{
			lStatus.setText("Status : Creating Data Set");
			fd.createTrainSet(tTrainSite.getText(),Integer.parseInt(tJumFd.getText()),Integer.parseInt(tNFd.getText()),Integer.parseInt(tJumNFd.getText()));
			lStatus.setText("Status : Ready");
		}
	}
    
    public static void main(String[] args)
    {
    	new FdApp();
    }
    
    public class JImagePanel extends JPanel{
	int x, y;
	public JImagePanel(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(modImg, x, y, null);
		
		int faces=fd.getFacesCount();
		//System.out.println(faces);
		if (faces!=0)
		{
			for (int i=0;i<faces;i++)
			{
			g.setColor(Color.BLUE);
			g.drawRect(x+fd.getPosX(i),y+fd.getPosY(i),fd.getPosWidth(i),fd.getPosHeight(i));
			}
		}
		
	}
}

	
}
	