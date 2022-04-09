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
      * Method to test setLow on all pixels in a Picture
      * @param pic - a Picture
      * @param col - a color to hide within the picture pic
      */
      public static Picture testSetLow(Picture pic, Color col)
      {
        Picture newPic = new Picture(pic); //make a copy of the picture to modify and return
        Pixel[][] pixels = pic.getPixels2D(); //crate a 2D array of pixels

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
     * Undo the hiding methods to reveal the hidden picture
     * @param 
     */
    public static Picture revealPicture(Picture pic)
    {
        Picture newPic = new Picture(pic); //make a copy of the picture to modify and return
        Pixel[][] pixels = pic.getPixels2D(); //crate a 2D array of pixels

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



//TODO
    /**
     * Hide a picture within another of the same size
     */

    /**
    * Hide a smaller picture within a bigger picture at designated startRow and startCol 
    */

    public static void main(String[] args)
    {
        Picture beach = new Picture("beach.jpg");
        beach.explore();

        //test clearLow by exploring the pictures and comparing the RGB values of each pixel
        /*
        Picture beachClearLow = testClearLow(beach);
        beachClearLow.explore();
        */

        //test setLow by hiding the color pink in the beach picture
        Picture beachSetLow = testSetLow(beach, Color.pink);
        beachSetLow.explore();

        Picture becahRevealed = revealPicture(beach);
        becahRevealed.explore();


    }       

}