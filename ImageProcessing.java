
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 *
 * @author lakkhana
 */    
public class ImageProcessing {

    /**
     * @param args the command line arguments
     */
    int width,height,minLevelR,maxLevelR,minLevelG,maxLevelG,minLevelB,maxLevelB,range,num;
    int[] countR,countG,countB,pix,newpix;
    boolean foundMinR = false;
    boolean foundMinG = false;
    boolean foundMinB = false;
    BufferedImage img2;
    
    ImageProcessing(BufferedImage img) {
        
        width = img.getWidth();
        height = img.getHeight();
        num = width*height;
        range = 256*256*256;
        countR = new int[256];
        countG = new int[256];
        countB = new int[256];
        
        pix = new int[num];
        img.getRGB(0, 0, width, height, pix, 0, width);
        int px;
        for(int i=0;i<num;i++) {
            
            //convert RGB value to Grayscale
            px = (range+pix[i])/(256*256);
            //System.out.println("R : "+px);           
            countR[px]++;
            px = ((range+pix[i])/256)%256;
            //System.out.println("G : "+px);            
            countG[px]++;
            px = ((range+pix[i])%256)%256;
            //System.out.println("B : "+px);            
            countB[px]++;
        }
        
        for(int i=0;i<256;i++) {
            if(!foundMinR && countR[i]!=0) {
                foundMinR = true;
                minLevelR = i;
            }
            if(countR[i]!=0)
                maxLevelR = i;
            if(!foundMinG && countG[i]!=0) {
                foundMinG = true;
                minLevelG = i;
            }
            if(countG[i]!=0)
                maxLevelG = i;
            if(!foundMinB && countB[i]!=0) {
                foundMinB = true;
                minLevelB = i;
            }
            if(countB[i]!=0)
                maxLevelB = i;
                
        }
        System.out.println("RED - min: "+minLevelR+"\nmax: "+maxLevelR);
        System.out.println("Green - min: "+minLevelG+"\nmax: "+maxLevelG);
        System.out.println("Blue - min: "+minLevelB+"\nmax: "+maxLevelB);
    }
    
    void contrastStrech() {
        
        newpix = new int[num];
        int px,npxR,npxG,npxB;
        for(int i=0;i<num;i++) {
            px = (range+pix[i])/(256*256);
            npxR = (px-minLevelR)*255/(maxLevelR-minLevelR);
            px = ((range+pix[i])/256)%256;
            npxG = (px-minLevelR)*255/(maxLevelR-minLevelR);
			px = ((range+pix[i])%256)%256;
			npxB = (px-minLevelR)*255/(maxLevelR-minLevelR);
            newpix[i] = range+npxR*(256*256)+npxG*256+npxB;
        }
        writeImage(1);
        
    }
    
    
    void histogramEqalization() {
    	int[] newLevelR = new int[256];
    	int[] newLevelG = new int[256];
    	int[] newLevelB = new int[256];
    	newpix = new int[num];
    	float sumR,sumG,sumB;
    	for(int i=0;i<256;i++) {
    	
    		sumR = 0;
    		sumG = 0;
    		sumB = 0;
    		for(int j=0;j<=i;j++) {	
    			sumR += (float)(countR[j])/num;
    			sumG += (float)(countG[j])/num;
    			sumB += (float)(countB[j])/num;
    		}
    			
    		newLevelR[i] = (int)(sumR*256);
    		newLevelG[i] = (int)(sumG*256);
    		newLevelB[i] = (int)(sumB*256);
    		
    	}
    	int px,npxR,npxG,npxB;
    	for(int i=0;i<num;i++) {
    	
            npxR = newLevelR[(range+pix[i])/(256*256)];
            npxG = newLevelG[((range+pix[i])/256)%256];
            npxB = newLevelB[((range+pix[i])%256)%256];
            

            newpix[i] = range+npxR*(256*256)+npxG*256+npxB;
        }
        writeImage(2);
    	
    }
    
    void writeImage(int i) {
    	
    	try {
            img2 = ImageIO.read(getClass().getResource("test.jpg"));
            img2.setRGB(0, 0, width, height, newpix, 0, width);

            File outputfile = new File("test"+i+".jpg");
        
            ImageIO.write(img2, "jpg", outputfile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
            
    }
}
