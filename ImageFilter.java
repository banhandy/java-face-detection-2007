import java.awt.*; 
import java.awt.image.*;
import java.util.*;

/**
 * Class untuk melakukan proses preprocessing pada citra masukan
 * 
 * @author (Ban Handy) 
 * @version (1.0 Desember 2007)
 */

public class ImageFilter
{
	private Image rawImg;
	private int[] mask = {  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      				        0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
      				        0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
      				        0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
      				        0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
      				        0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
      				        0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
      				        0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      			 	        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
      				        0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
      				        0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
      				        0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
      			 	        0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
      				        0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
      				        0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
      				        0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
      				        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  	private double[][] invA1xAxA1 =
   	{
   		{
   		-0.0013,   -0.0013,   -0.0013,   -0.0013,   -0.0013,   -0.0013,   -0.0013,   -0.0013,   -0.0013,
   		-0.0013,   -0.0013,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,
   		-0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,   -0.0011,
   		-0.0011,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,
   		-0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0010,
   		-0.0010,   -0.0010,   -0.0010,   -0.0010,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,
   		-0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,
  		-0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0008,   -0.0006,   -0.0006,
		-0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,
		-0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,   -0.0006,
		-0.0006,   -0.0006,   -0.0006,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,
    	-0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,
   		-0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0004,   -0.0003,
    	-0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,
  		-0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,
  		-0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0003,   -0.0001,   -0.0001,   -0.0001,
		-0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,
  		-0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,   -0.0001,
    	-0.0001,   -0.0001,   -0.0001,   -0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,
    	 0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,
  		 0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,    0.0001,
         0.0001,    0.0001,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,
    	 0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,
		 0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,    0.0003,
    	 0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,
         0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0004,
   		 0.0004,    0.0004,    0.0004,    0.0004,    0.0004,    0.0006,    0.0006,    0.0006,    0.0006,
    	 0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,
  		 0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,    0.0006,
    	 0.0006,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,
         0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,    0.0008,
   		 0.0008,    0.0008,    0.0008,    0.0008,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,
         0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,
         0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0010,    0.0011,    0.0011,
   		 0.0011,    0.0011,    0.0011,    0.0011,    0.0011,    0.0011,    0.0011,    0.0011,    0.0011,
  		 0.0011,    0.0011,    0.0011,    0.0011,    0.0011,    0.0011,    0.0013,    0.0013,    0.0013,
    	 0.0013,    0.0013,    0.0013,    0.0013,    0.0013,    0.0013,    0.0013,    0.0013
  		},
   		{
   			 -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,    0.0000,    0.0001,    0.0001,    0.0002,
   			  0.0003,    0.0004,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,   -0.0001,
   			 -0.0001,    0.0000,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,    0.0005,
   			  0.0006,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,
   			 -0.0001,   -0.0001,    0.0000,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,
    		  0.0005,    0.0006,    0.0007,    0.0007,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,
    		 -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,    0.0000,    0.0001,    0.0001,    0.0002,
    		  0.0003,    0.0004,    0.0004,    0.0005,    0.0006,    0.0007,    0.0007,   -0.0008,   -0.0007,
    		 -0.0007,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,
    		  0.0000,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,    0.0005,    0.0006,
			  0.0007,    0.0007,    0.0008,   -0.0008,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,
  		     -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,    0.0000,    0.0001,    0.0001,    0.0002,
    		  0.0003,    0.0004,    0.0004,    0.0005,    0.0006,    0.0007,    0.0007,    0.0008,   -0.0009,
    		 -0.0008,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,
    		 -0.0001,   -0.0001,    0.0000,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,
    		  0.0005,    0.0006,    0.0007,    0.0007,    0.0008,    0.0009,   -0.0009,   -0.0008,   -0.0007,
		     -0.0007,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,
    		  0.0000,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,    0.0005,    0.0006,
    		  0.0007,    0.0007,    0.0008,    0.0009,   -0.0009,   -0.0008,   -0.0007,   -0.0007,   -0.0006,
  			 -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,         0,    0.0001,
   			  0.0001,    0.0002,    0.0003,    0.0004,    0.0004,    0.0005,    0.0006,    0.0007,    0.0007,
    		  0.0008,    0.0009,   -0.0009,   -0.0008,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,
  			 -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,         0,    0.0001,    0.0001,    0.0002,
    		  0.0003,    0.0004,    0.0004,    0.0005,    0.0006,    0.0007,    0.0007,    0.0008,    0.0009,
  			 -0.0008,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,
             -0.0001,   -0.0001,         0,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,
    		  0.0005,    0.0006,    0.0007,    0.0007,    0.0008,   -0.0008,   -0.0007,   -0.0007,   -0.0006,
   			 -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,         0,    0.0001,
              0.0001,    0.0002,    0.0003,    0.0004,    0.0004,    0.0005,    0.0006,    0.0007,    0.0007,
   			  0.0008,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,   -0.0004,   -0.0003,   -0.0002,
             -0.0001,   -0.0001,         0,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004,    0.0004,
   			  0.0005,    0.0006,    0.0007,    0.0007,   -0.0007,   -0.0007,   -0.0006,   -0.0005,   -0.0004,
             -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,         0,    0.0001,    0.0001,    0.0002,
              0.0003,    0.0004,    0.0004,    0.0005,    0.0006,    0.0007,    0.0007,   -0.0006,   -0.0005,
  			 -0.0004,   -0.0004,   -0.0003,   -0.0002,   -0.0001,   -0.0001,         0,    0.0001,    0.0001,
  			  0.0002,    0.0003,    0.0004,    0.0004,    0.0005,    0.0006,   -0.0004,   -0.0003,   -0.0002,
             -0.0001,   -0.0001,   -0.0000,    0.0001,    0.0001,    0.0002,    0.0003,    0.0004
   		},
   		{
   			 0.0206,    0.0195,    0.0185,    0.0175,    0.0164,    0.0154,    0.0143,    0.0133,    0.0122,
   			 0.0112,    0.0101,    0.0221,    0.0210,    0.0200,    0.0190,    0.0179,    0.0169,    0.0158,
   			 0.0148,    0.0137,    0.0127,    0.0116,    0.0106,    0.0095,    0.0085,    0.0074,    0.0064,
   			 0.0053,    0.0225,    0.0215,    0.0204,    0.0194,    0.0184,    0.0173,    0.0163,    0.0152,
   			 0.0142,    0.0131,    0.0121,    0.0110,    0.0100,    0.0089,    0.0079,    0.0068,    0.0058,
   			 0.0047,    0.0037,    0.0026,    0.0016,    0.0209,    0.0198,    0.0188,    0.0178,    0.0167,
   			 0.0157,    0.0146,    0.0136,    0.0125,    0.0115,    0.0104,    0.0094,    0.0083,    0.0073,
   			 0.0062,    0.0052,    0.0041,    0.0031,    0.0020,    0.0010,   -0.0001,    0.0203,    0.0192,
   			 0.0182,    0.0172,    0.0161,    0.0151,    0.0140,    0.0130,    0.0119,    0.0109,    0.0098,
   			 0.0088,    0.0077,    0.0067,    0.0056,    0.0046,    0.0035,    0.0025,    0.0014,    0.0004,
   		    -0.0007,   -0.0017,   -0.0027,    0.0187,    0.0176,    0.0166,    0.0155,    0.0145,    0.0134,
   		     0.0124,    0.0113,    0.0103,    0.0092,    0.0082,    0.0071,    0.0061,    0.0050,    0.0040,
  			 0.0029,    0.0019,    0.0008,   -0.0002,   -0.0013,   -0.0023,   -0.0033,   -0.0044,    0.0181,
   			 0.0170,    0.0160,    0.0149,    0.0139,    0.0128,    0.0118,    0.0107,    0.0097,    0.0086,
   			 0.0076,    0.0065,    0.0055,    0.0044,    0.0034,    0.0023,    0.0013,    0.0002,   -0.0008,
   			-0.0019,   -0.0029,   -0.0039,   -0.0050,   -0.0060,   -0.0071,    0.0164,    0.0154,    0.0143,
 			 0.0133,    0.0122,    0.0112,    0.0101,    0.0091,    0.0080,    0.0070,    0.0059,    0.0049,  		
   			 0.0038,    0.0028,    0.0017,    0.0007,   -0.0004,   -0.0014,   -0.0025,   -0.0035,   -0.0045,
   			-0.0056,   -0.0066,   -0.0077,   -0.0087,    0.0148,    0.0137,    0.0127,    0.0116,    0.0106,
   			 0.0095,    0.0085,    0.0074,    0.0064,    0.0053,    0.0043,    0.0032,    0.0022,    0.0011,
   			 0.0001,   -0.0010,   -0.0020,   -0.0030,   -0.0041,   -0.0051,   -0.0062,   -0.0072,   -0.0083,
   			-0.0093,   -0.0104,    0.0131,    0.0121,    0.0110,    0.0100,    0.0089,    0.0079,    0.0068,
   			 0.0058,    0.0047,    0.0037,    0.0026,    0.0016,    0.0005,   -0.0005,   -0.0016,   -0.0026,
   			-0.0036,   -0.0047,   -0.0057,   -0.0068,   -0.0078,   -0.0089,   -0.0099,   -0.0110,   -0.0120,
   			 0.0104,    0.0094,    0.0083,    0.0073,    0.0062,    0.0052,    0.0041,    0.0031,    0.0020,
   			 0.0010,   -0.0001,   -0.0011,   -0.0022,   -0.0032,   -0.0042,   -0.0053,   -0.0063,   -0.0074,
   			-0.0084,   -0.0095,   -0.0105,   -0.0116,   -0.0126,    0.0088,    0.0077,    0.0067,    0.0056,
   			 0.0046,    0.0035,    0.0025,    0.0014,    0.0004,   -0.0007,   -0.0017,   -0.0027,   -0.0038,
   			-0.0048,   -0.0059,   -0.0069,   -0.0080,   -0.0090,   -0.0101,   -0.0111,   -0.0122,   -0.0132,
   			-0.0143,    0.0061,    0.0050,    0.0040,    0.0029,    0.0019,    0.0008,   -0.0002,   -0.0013,
   			-0.0023,   -0.0033,   -0.0044,   -0.0054,   -0.0065,   -0.0075,   -0.0086,   -0.0096,   -0.0107,
			-0.0117,   -0.0128,   -0.0138,   -0.0149,    0.0044,    0.0034,    0.0023,    0.0013,    0.0002,
			-0.0008,   -0.0019,   -0.0029,   -0.0039,   -0.0050,   -0.0060,   -0.0071,   -0.0081,   -0.0092,
   			-0.0102,   -0.0113,   -0.0123,   -0.0134,   -0.0144,   -0.0155,   -0.0165,    0.0007,   -0.0004,
			-0.0014,   -0.0025,   -0.0035,   -0.0045,   -0.0056,   -0.0066,   -0.0077,   -0.0087,   -0.0098,
   			-0.0108,   -0.0119,   -0.0129,   -0.0140,   -0.0150,   -0.0161,   -0.0041,   -0.0051,   -0.0062,
			-0.0072,   -0.0083,   -0.0093,   -0.0104,   -0.0114,   -0.0125,   -0.0135,   -0.0146
   		}
   
   	};
	
	private int[][][] threeDPix;
	private int[][][] threeDPixMod;
  	private int[] oneDPix;
  	int imgCols,imgRows;
  	private int minGLvl=255;
  	private int maxGLvl=0;
  	
  	/**
	* Konstruktor untuk objek dari class ImageFilter
	* @param rawImg   citra masukan
	*/  	
   	public ImageFilter(Image rawImg)
  	{
  		this.rawImg=rawImg;
    	imgCols = this.rawImg.getWidth(null);
   		imgRows = this.rawImg.getHeight(null);
   	    splitPiksel();
   	    threeDPixMod = grayScale();
   	    masking();
   	    shading();
   	    histogram();
   	    oneDPix = convertToOneDim(
                  threeDPixMod,imgCols,imgRows);
   	}
   	
   	/**
	* Konstruktor untuk objek dari class ImageFilter
	* @param rawImge   citra masukan
	* @param width   lebar citra masukan
	* @param height   tinggi citra masukan
	*/ 
   	public ImageFilter(Image rawImg,int width,int height)
  	{
  		Image tempImg=rawImg.getScaledInstance(width,height,rawImg.SCALE_SMOOTH);
  		MediaTracker mt = new MediaTracker(new Component() {});
        	mt.addImage(tempImg, 0);
        	try {
            mt.waitForID(0);
        	} catch(InterruptedException e) {
            System.out.println("mt interrupted");
        	}
    		
  		this.rawImg=tempImg;
    	imgCols = this.rawImg.getWidth(null);
   		imgRows = this.rawImg.getHeight(null);
   		splitPiksel();
   		threeDPixMod = grayScale();
   		oneDPix = convertToOneDim(
                  threeDPixMod,imgCols,imgRows);
    }
    
    private int[][][] getWindow(int row,int col)
    {
    	int[][][] wPiksel = new int[27][18][4];
    	int min=256;
    	int max=0;
    	for (int wRow=0;wRow<27;wRow++)
    		{
    			for (int wCol=0;wCol<18;wCol++)
    			{
    				wPiksel[wRow][wCol]=threeDPixMod[row+wRow][col+wCol];
    			}
    		}
    	wMasking(wPiksel);
    	wShading(wPiksel);
    	wHistogram(wPiksel);
    	return wPiksel;
    }
    
    /**
	* method untuk mendapatkan piksel dari citra hasil preprocesing dengan koordinat x,y
	* @param y   koordinat y pada citra
	* @param x   koordinat x pada citra
	* @return piksel hasil preprocessing
	*/ 
    public double[] getProcWindow(int y,int x)
    {
    	int[][][] wPiksel=getWindow(y,x);
    	double[] g = new double[25*16];
    	for (int row=0;row<25;row++)
  		{
  			for (int col=0; col<16;col++)
  			{
  				g[col+row*16]=wPiksel[row+1][col+1][1];
  			}
  		}
  		return g;
    }
    
    /**
	* method untuk mendapatkan piksel dari citra hasil preprocesing 
	* @return piksel hasil preprocessing
	*/ 
    public double[] getProcWindow()
    {
    	double[] g = new double[25*16];
    	for (int row=0;row<25;row++)
  		{
  			for (int col=0; col<16;col++)
  			{
  				g[col+row*16]=threeDPixMod[row+1][col+1][1];
  			}
  		}
  		return g;
    }
    
    private int[][][] convertToThreeDim(int[] oneDPix,int imgCols,int imgRows)
    {    
    int[][][] data =
                    new int[imgRows][imgCols][4];

    for(int row = 0;row < imgRows;row++){
      
      int[] aRow = new int[imgCols];
      for(int col = 0; col < imgCols;col++){
        int element = row * imgCols + col;
        aRow[col] = oneDPix[element];
      }
      for(int col = 0;col < imgCols;col++){
        //Alpha data
        data[row][col][0] = (aRow[col] >> 24)
                                          & 0xFF;
        //Red data
        data[row][col][1] = (aRow[col] >> 16)
                                          & 0xFF;
        //Green data
        data[row][col][2] = (aRow[col] >> 8)
                                          & 0xFF;
        //Blue data
        data[row][col][3] = (aRow[col])
                                          & 0xFF;
      }
    }
    return data;
  }
   	
   	private int[] convertToOneDim(int[][][] data,int imgCols,int imgRows)
   	{
   
    int[] oneDPix = new int[
                          imgCols * imgRows ];

    
    for(int row = 0,cnt = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        oneDPix[cnt] = ((data[row][col][0] << 24)
                                   & 0xFF000000)
                     | ((data[row][col][1] << 16)
                                   & 0x00FF0000)
                      | ((data[row][col][2] << 8)
                                   & 0x0000FF00)
                           | ((data[row][col][3])
                                   & 0x000000FF);
        cnt++;
      }

    }

    return oneDPix;
  }
   	
   	private int[][][] grayScale(){

    
    int[][][] temp3D =
                    new int[imgRows][imgCols][4];
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        temp3D[row][col][0] =
                          threeDPix[row][col][0];
        temp3D[row][col][1] =
                          threeDPix[row][col][1];
        temp3D[row][col][2] =
                          threeDPix[row][col][2];
        temp3D[row][col][3] =
                          threeDPix[row][col][3];
      }
    }

     for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
      	                   
        temp3D[row][col][1] =
                          (temp3D[row][col][1]+temp3D[row][col][2]+temp3D[row][col][3])/3;
        
        temp3D[row][col][2] =
                          temp3D[row][col][1];
        temp3D[row][col][3] =
                          temp3D[row][col][1];
      }
    }
      
    return temp3D;
  }
  
    private void wShading(int[][][] wPiksel)
  {
  	int[][] mx = new int[27][18];
  	int[][] my = new int[27][18];
  	
  	int size=0;
  	for(int row = 0;row < 27;row++)
  	{
      for(int col = 0;col < 18;col++)
      {
      	mx[row][col]=col+1;
      	my[row][col]=row+1;
      	if (inOval(row,col))
    		{
    			size++;
    		}
      }
    }
    
  	int[][] A = new int[size][3];
  	int[] B = new int[size];
  	for(int col = 0,cnt=0;col < 18;col++)
      {
      	for(int row = 0;row < 27;row++)
  		{
      			if (inOval(row,col))
      			{
    			A[cnt][0]=col+1;
    			A[cnt][1]=row+1;
    			A[cnt][2]=1;
    			B[cnt] = wPiksel[row][col][1];
    			cnt++;	
    			}
    		
      }
      
    }
    
   	
   	double[] gradient= new double[3];
   	for (int j=0;j<size;j++)
   		{
   			gradient[0]=gradient[0]+(invA1xAxA1[0][j]*B[j]);
   			gradient[1]=gradient[1]+(invA1xAxA1[1][j]*B[j]);
   			gradient[2]=gradient[2]+(invA1xAxA1[2][j]*B[j]);
   		}
   	int a,b,c;	
   		a=(int)gradient[0];
   		b=(int)gradient[1];	
   		c=(int)gradient[2];   		
   		
     	
   	for(int row = 0;row < 27;row++)
  		{
  			for(int col = 0;col < 18;col++)
      		{
      			if (inOval(row,col))
      			{
      				    		
    				wPiksel[row][col][1]-=(mx[row][col]*a)+(my[row][col]*b)+c;
    				wPiksel[row][col][2]=wPiksel[row][col][1];
      				wPiksel[row][col][3]=wPiksel[row][col][1];
      				if (minGLvl>wPiksel[row][col][1])
    				minGLvl=wPiksel[row][col][1];
    				if (maxGLvl<wPiksel[row][col][1])
    				maxGLvl=wPiksel[row][col][1];
    			}
    			
    		
      		}
      		
    	}
   	
   	
  }
  
  private void shading()
  {
  	int[][] mx = new int[27][18];
  	int[][] my = new int[27][18];
  	
  	int size=0;
  	for(int row = 0;row < imgRows;row++)
  	{
      for(int col = 0;col < imgCols;col++)
      {
      	mx[row][col]=col+1;
      	my[row][col]=row+1;
      	if (inOval(row,col))
    		{
    			size++;
    		}
      }
    }
    
  	int[][] A = new int[size][3];
  	int[] B = new int[size];
  	for(int col = 0,cnt=0;col < imgCols;col++)
      {
      	for(int row = 0;row < imgRows;row++)
  		{
      			if (inOval(row,col))
      			{
    			A[cnt][0]=col+1;
    			A[cnt][1]=row+1;
    			A[cnt][2]=1;
    			B[cnt] = threeDPixMod[row][col][1];
    			cnt++;	
    			}
    		
      }
      
    }
    
   	
   	double[] gradient= new double[3];
   	for (int j=0;j<size;j++)
   		{
   			gradient[0]=gradient[0]+(invA1xAxA1[0][j]*B[j]);
   			gradient[1]=gradient[1]+(invA1xAxA1[1][j]*B[j]);
   			gradient[2]=gradient[2]+(invA1xAxA1[2][j]*B[j]);
   		}
   	int a,b,c;	
   		a=(int)gradient[0];
   		b=(int)gradient[1];	
   		c=(int)gradient[2];   		
   		//System.out.println(a+" "+b+" "+c);
     	
   	for(int row = 0;row < imgRows;row++)
  		{
  			for(int col = 0;col < imgCols;col++)
      		{
      			if (inOval(row,col))
      			{
      				    		
    			threeDPixMod[row][col][1]-=(mx[row][col]*a)+(my[row][col]*b)+c;
    			threeDPixMod[row][col][2]=threeDPixMod[row][col][1];
      			threeDPixMod[row][col][3]=threeDPixMod[row][col][1];
      				if (minGLvl>threeDPixMod[row][col][1])
    				minGLvl=threeDPixMod[row][col][1];
    				if (maxGLvl<threeDPixMod[row][col][1])
    				maxGLvl=threeDPixMod[row][col][1];
    			}
    		
      }
      
    }
   	
   	
  }
  
  
  
  private void splitPiksel()
    {
    	oneDPix = new int[imgCols * imgRows];

       try{
      
      	PixelGrabber pgObj = new PixelGrabber(
                      rawImg,0,0,imgCols,imgRows,
                              oneDPix,0,imgCols);
      
      	if(pgObj.grabPixels() &&
                           ((pgObj.getStatus() &
                           ImageObserver.ALLBITS)
                                          != 0)){

        
        threeDPix = convertToThreeDim(
                        oneDPix,imgCols,imgRows);
        }
      	else System.out.println(
                    "Pixel grab not successful");
    	}catch(InterruptedException e){
      	e.printStackTrace();
    	}
    }
    
    private void masking()
  	{
  	for (int row=0; row<27; row++)
    {
    	for (int col=0; col<18; col++)
    	{
    		threeDPixMod[row][col][1]=threeDPixMod[row][col][1]*mask[row*18+col];
      		threeDPixMod[row][col][2]=threeDPixMod[row][col][2]*mask[row*18+col];
      		threeDPixMod[row][col][3]=threeDPixMod[row][col][3]*mask[row*18+col];
      	}
    }
  	}
  	
  	private void wMasking(int[][][] wPiksel)
  	{
  	for (int row=0; row<27; row++)
    {
    	for (int col=0; col<18; col++)
    	{
    		wPiksel[row][col][1]=wPiksel[row][col][1]*mask[row*18+col];
      		wPiksel[row][col][2]=wPiksel[row][col][2]*mask[row*18+col];
      		wPiksel[row][col][3]=wPiksel[row][col][3]*mask[row*18+col];
      	}
    }
    }
  	
  	private void wHistogram(int[][][] wPiksel)
  	{
  		for(int row = 0;row < 27;row++)
  			{
      		for(int col = 0;col < 18;col++)
      			{
      				if (inOval(row,col))
    				{
    				if ((maxGLvl-minGLvl)!=0)
    				wPiksel[row][col][1]=(wPiksel[row][col][1]-minGLvl)*255/(maxGLvl-minGLvl);
    				else
    				wPiksel[row][col][1]=255;
      				wPiksel[row][col][2]=wPiksel[row][col][1];
      				wPiksel[row][col][3]=wPiksel[row][col][1];
      				}
      			}
  			}
  	}
  	
  	private void histogram()
  	{
  		for(int row = 0;row < imgRows;row++)
  			{
      		for(int col = 0;col < imgCols;col++)
      			{
      				if (inOval(row,col))
    				{
    				if ((maxGLvl-minGLvl)!=0)
    				threeDPixMod[row][col][1]=(threeDPixMod[row][col][1]-minGLvl)*255/(maxGLvl-minGLvl);
    				else
    				threeDPixMod[row][col][1]=255;
    				threeDPixMod[row][col][2]=threeDPixMod[row][col][1];
      				threeDPixMod[row][col][3]=threeDPixMod[row][col][1];
      				}
      			}
  			}
  	}
  	
  	private boolean inOval(int row,int col)
   	{
   		boolean stat = false;
   		if (mask[row * 18 + col]==1) stat=true;
   		return stat;	 	
   	}
   	
   	/**
	* method untuk melakukan mirror x piksel citra 
	* @param dPix   piksel citra
	* @return piksel hasil mirror x
	*/ 
   	public static double[] mirrorX(double[] dPix)
  	{		
  		double[] mirCnt=new double[dPix.length];
  		for(int row = 0,cnt = 0;row < 25;row++){
      	for(int col = 0;col < 16;col++){
      	mirCnt[cnt]=dPix[(16-col-1)+(16*row)];
  		cnt++;
  		}
  		}	
  		return mirCnt;
    }
    
    /**
	* method untuk melakukan mirror y piksel citra 
	* @param dPix   piksel citra
	* @return piksel hasil mirror y
	*/ 
    public static double[] mirrorY(double[] dPix)
    {
    	double[] mirCnt=new double[dPix.length];
  		for(int row = 0,cnt = 0;row < 25;row++){
      	for(int col = 0;col < 16;col++){
      	mirCnt[cnt]=dPix[(16*(25-row-1))+col];
  		cnt++;
  		}
  		}	
  		return mirCnt;
    }
    	
}