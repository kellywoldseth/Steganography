/**
 * Steganography Lab
 * 
 * Author: @kellywoldseth
 * Date: April 2022
 * 
 * Description:   Steganography is the study of hiding a message within another message. In this lab, we will hide pictures within 
 * pictures by manipulating pixel information.
 * 
 *  This class is written entirely by the author.
 * All other files and classes were written by CollegeBoard for use in AP Computer Science A. 
 */

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Steganography
{

    /**
     * Clear the lower (rightmost) two bits in a pixel.
     * @param Pixel p - a single pixel in a picture
     */
     public static void clearLow(Pixel p)
     {
        p.setRed((p.getRed()/4*4));
        p.setGreen((p.getGreen()/4*4));
        p.setBlue((p.getBlue()/4*4));
     } 


     /**
      * Method to test clearLow on all pixels in a Picture
      * @param pic - a Picture
      * @return - new picture with rightmost two bits cleared from each pixel
      */
      public static Picture testClearLow(Picture pic)
      {
        Picture newPic = new Picture(pic); //make a copy of the picture to modify and return
        Pixel[][] pixels = pic.getPixels2D(); //crate a 2D array of pixels

        //iterate through each pixel, clearing the rightmost two bits of each
        for (int r = 0; r<pixels.length; r++)
        {
           for (int c = 0; c<pixels[0].length; c++)
           {
                clearLow(pixels[r][c]);
            }
        }
        return newPic;
      }


    /**
     * Set the lower 2 bits in a pixel to the highest 2 bits in c, effectively hiding c inside the pixel.
     * @param p - a single pixel in a Picture
     * @param c - a color to hide inside Pixel p
    */
    public static void setLow(Pixel p, Color c)
    {
        //first, clear the rightmost two pixels
        clearLow(p);

        //second, add the leftmost two pixels from color to the current pixel's color amount
        p.setRed(p.getRed() + c.getRed()/64);
        p.setGreen(p.getGreen() + c.getGreen()/64);
        p.setBlue(p.getBlue() + c.getBlue()/64);
    }

     /**
      * Method to test setLow on all pixels in a Picture
      * @param pic - a Picture
      * @param col - a color to hide within the picture pic
      * @return - new picture with hidden color inside it
      */
      public static Picture testSetLow(Picture pic, Color col)
      {
        Picture newPic = new Picture(pic); //make a copy of the picture to modify and return
        Pixel[][] pixels = newPic.getPixels2D(); //crate a 2D array of pixels

        //iterate through each pixel, setting the righmtmost two pits of each pixel to the leftmost two bits of the color
        for (int r = 0; r<pixels.length; r++)
        {
           for (int c = 0; c<pixels[0].length; c++)
           {
                setLow(pixels[r][c], col);
            }
        }
        return newPic;
      }

    /**
     * Undo the hiding methods to reveal the hidden picture
     * @param pic - picture to reveal message on
     * @return new picture with revealed picture
     */
    public static Picture revealPicture(Picture pic)
    {
        Picture newPic = new Picture(pic); //make a copy of the picture to modify and return
        Pixel[][] pixels = newPic.getPixels2D(); //crate a 2D array of pixels

        //iterate through each pixel, setting the righmtmost two pits of each pixel to the leftmost two bits of the color
        for (int r = 0; r<pixels.length; r++)
        {
           for (int c = 0; c<pixels[0].length; c++)
           {
               //Take the rightmost two pixels with a %4 and then set them to the leftmost two pixels with *64
               int red = pixels[r][c].getRed()%4*64;
               int green = pixels[r][c].getGreen()%4*64;
               int blue = pixels[r][c].getBlue()%4*64;

                pixels[r][c].setColor(new Color(red, green, blue));//set pixel to revealed color

            }
        }
        return newPic;
    }

    /**
     * Hide a picture within another of the same size
     * @param original - the picture to hide within; cannot be null
     * @param hidden - the picture to hide; cannot be null
     * @return - a new picture that looks like the original to the naked eye but actually has "hidden" within it
     * @preconditions - original and hidden must be the same size
     */
    public static Picture hidePicture(Picture original, Picture hiddenPicture)
    {
        Picture newPic = new Picture(original); //make a copy of the picture to modify and return
        Pixel[][] originalPixels = newPic.getPixels2D(); //crate a 2D array of pixels from original
        Pixel[][] hiddenPixels = hiddenPicture.getPixels2D(); //crate a 2D array of pixels from hidden

        //iterate through each pixel, hiding the color of hidden in the original pixels
        for (int r = 0; r<originalPixels.length; r++)
        {
           for (int c = 0; c<originalPixels[0].length; c++)
           {
                setLow(originalPixels[r][c], hiddenPixels[r][c].getColor());
            }
        }
        return newPic;
    }

    /**
     * Determines if p2 can be hidden in p1
     * 
     * @param p1 - Picture 1; cannot be null
     * @param p2 - Picture 2; cannot be null
     * @return true if pictures are the same size or if p2 is smaller than p1, false otherwise
     */
    public static boolean canHide(Picture p1, Picture p2)
    {
        Pixel[][] p1Pixels = p1.getPixels2D(); //crate a 2D array of pixels from p1
        Pixel[][] p2Pixels = p2.getPixels2D(); //crate a 2D array of pixels from p2
        if(p1Pixels.length >= p2Pixels.length && p1Pixels[0].length >= p2Pixels[0].length)
            return true;
        return false;

    }
    /**
     * Hide a smaller picture within a bigger picture at designated startRow and startCol 
     * @param original - the picture to hide within; cannot be null
     * @param hidden - the picture to hide; cannot be null; must be smaller than or equal to the original in size
     * @param startRow - the row to start hiding "hidden" in
     * @param startCol - the column to start hiding "hidden" in
     * @return - a new picture that looks like the original to the naked eye but actually has "hidden" within it
     * @preconditions - original and hidden must be the same size
     */
    public static Picture hidePicture(Picture original, Picture hiddenPicture, int startRow, int startCol)
    {
        Picture newPic = new Picture(original); //make a copy of the picture to modify and return
        Pixel[][] originalPixels = newPic.getPixels2D(); //crate a 2D array of pixels from original
        Pixel[][] hiddenPixels = hiddenPicture.getPixels2D(); //crate a 2D array of pixels from hidden
        
        //if canot hide, return newPic without modifications (copy of original)
        if(!canHide(original, hiddenPicture))
            return newPic;

        //iterate through each pixel, hiding the color of hidden in the original pixels
        for (int r1 = startRow, r2=0; r1<originalPixels.length && r2<hiddenPixels.length; r1++, r2++)
        {
           for (int c1 = startCol, c2=0; c1<originalPixels[0].length && c2<hiddenPixels[0].length; c1++, c2++)
           {
                setLow(originalPixels[r1][c1], hiddenPixels[r2][c2].getColor());
            }
        }
        return newPic;
    }

    /**
     * Determines if two pictures are the same
     * @param p1 - Picture 1
     * @param p2 - Picture 2
     * @return true or false if they are the same picture
     */
    public static boolean isSame(Picture p1, Picture p2)
    {
        Pixel[][] p1Pixels = p1.getPixels2D(); //crate a 2D array of pixels from original
        Pixel[][] p2Pixels = p2.getPixels2D(); //crate a 2D array of pixels from hidden   
        
        if(p1Pixels.length != p2Pixels. length || p1Pixels[0].length != p2Pixels[0].length)
            return false;
        
        for (int r = 0; r<p1Pixels.length; r++)
        {
            for (int c = 0; c<p1Pixels[0].length; c++)
            {
                if(!p1Pixels[r][c].getColor().equals(p2Pixels[r][c].getColor()))
                    return false;
            }
        }
        return true;
    }

    /**
     * Finds differences between two colors and stores the differences in an ArrayList
     * @param p1 - Picture 1
     * @param p2 - Picture 2
     * @return - ArrayList containing list of points that have different colors
     * @precondition - p1 and p2 must be the same size or else it will return 0
     */
    public static ArrayList<Point> findDifferences(Picture p1, Picture p2)
    {
        ArrayList<Point> arr = new ArrayList<Point>();
        Pixel[][] p1Pixels = p1.getPixels2D(); //crate a 2D array of pixels from original
        Pixel[][] p2Pixels = p2.getPixels2D(); //crate a 2D array of pixels from hidden   
        Point pointToAdd = null;


        //check if pictures are identical or if they are different sizes - both cases returns a blank ArrayList
        if(isSame(p1,p2) || (p1Pixels.length!=p2Pixels.length && p1Pixels[0].length != p2Pixels[0].length))
            return arr;
        
        for (int r = 0; r<p1Pixels.length; r++)
        {
            for (int c = 0; c<p1Pixels[0].length; c++)
            {
                if(!p1Pixels[r][c].getColor().equals(p2Pixels[r][c].getColor()))
                {
                    pointToAdd = new Point(r,c);
                    arr.add(pointToAdd);
                }
            }
        }
        return arr;
    }


    

    //SHOW DIFFERENCES - TODO
    public static Picture showDifferences(Picture p1, ArrayList<Point> arr)
    {
        Picture newPic = new Picture(p1); //make a copy of the picture to modify and return
        Pixel[][] pixels = newPic.getPixels2D(); //crate a 2D array of pixels from original
        
        //set initial min/max values to first point's x and y values
        int maxRow=(int)(arr.get(0).getX());
        int minRow=(int)(arr.get(0).getX());
        int maxCol=(int)(arr.get(0).getY());
        int minCol=(int)(arr.get(0).getY());

        //iterate through arr to find min and max for rows and columns
        for(int i = 1; i<arr.size(); i++)
        {
            if(arr.get(i).getX()>maxCol)
                maxCol=(int)(arr.get(i).getY());
            if(arr.get(i).getX()<minCol)
                minCol=(int)(arr.get(i).getY());
            if(arr.get(i).getY()>maxRow)
                maxRow=(int)(arr.get(i).getX());
            if(arr.get(i).getY()<minRow)
                minRow=(int)(arr.get(i).getX());
        }
        //change the boundary of the differences area to the colo red
        for (int r = 0; r<pixels.length; r++)
        {
           for (int c = 0; c<pixels[0].length; c++)
           {
               //color top line red
               if(r==maxRow && c>minCol && c<maxCol)
                pixels[r][c].setColor(Color.red);
            
                //color bottom line red
                if(r==minRow && c>minCol && c<maxCol)
                    pixels[r][c].setColor(Color.red);
                //color left line red
                if(c==minCol && r>minRow && r<maxRow)
                    pixels[r][c].setColor(Color.red);
                //color right line red
                if(c==maxCol && r>minRow && r<maxRow)
                    pixels[r][c].setColor(Color.red);


            }
        }
        return newPic;
    }

{}    public static void main(String[] args)
    {
        //Picture beach = new Picture("beach.jpg");
        //beach.explore();

        //test clearLow by exploring the pictures and comparing the RGB values of each pixel
        /*
        Picture beachClearLow = testClearLow(beach);
        beachClearLow.explore();
        ----------------------------------------------------------------
        */

        //test setLow by hiding the color pink in the beach picture
        /*
        Picture beachSetLow = testSetLow(beach, Color.pink);
        beachSetLow.explore();

        Picture beachRevealed = revealPicture(beachSetLow);
        beachRevealed.explore();
        ----------------------------------------------------------------
        */

        //test hidePicture
        /*
        Picture redMotorcycle = new Picture("redMotorcycle.jpg");
        redMotorcycle.explore();

        Picture motorCycleInBeach = hidePicture(beach, redMotorcycle);
        motorCycleInBeach.explore();

        Picture redMotoRevealed = revealPicture(motorCycleInBeach);
        redMotoRevealed.explore();
        ----------------------------------------------------------------
        */

        //test hidePicture (smaller pic hidden in larger pic)
        /*
        Picture robot = new Picture("robot.jpg");
        robot.explore();

        Picture robotInBeach = hidePicture(beach, robot, 66, 31);
        robotInBeach.explore();

        Picture robotRevealed = revealPicture(robotInBeach);
        robotRevealed.explore();
        ----------------------------------------------------------------
        */

        //test isSame
        /*
        Picture beachCopy = new Picture("beach.jpg");
        Picture robotCopy = new Picture("robot.jpg");
        System.out.println(isSame(beach, beachCopy));
        System.out.println(isSame(beach, robotCopy));
        ----------------------------------------------------------------
        */

        //test findDifferences
        /*
        Picture beachCopy2 = new Picture("beach.jpg");
        ArrayList<Point> pointList = findDifferences(beach, beachCopy2); 
        System.out.println("PointList after comparing two identical pictures has a size of " + pointList.size());
        

        Picture robotCopy2 = new Picture("robot.jpg");
        pointList = findDifferences(beach, robotCopy2); 
        System.out.println("PointList after comparing two different sized pictures has a size of " + pointList.size());

        Picture arch = new Picture("arch.jpg");
        Picture arch2 = hidePicture(arch, robotCopy2, 65, 102);
        pointList = findDifferences(arch, arch2); 
        System.out.println("PointList after hiding a picture has a size of " + pointList.size());
        ----------------------------------------------------------------
        */

        //test showDifferences
        Picture robotCopy3 = new Picture("robot.jpg");
        Picture archCopy = new Picture("arch.jpg");
        Picture archCopy2 = hidePicture(archCopy, robotCopy3, 65, 102);
        ArrayList<Point> pointList2 = findDifferences(archCopy, archCopy2); 
        Picture show1 = revealPicture(archCopy2);
        show1.explore();

        Picture show2 = showDifferences(archCopy, pointList2);
        show2.explore();
        /*
        ----------------------------------------------------------------
        */

    }       

}