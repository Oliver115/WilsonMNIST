package neural.mnist;

/**
 * Container of MNIST digits
 * @param no Digit number in the database
 * @param pixels 8-bit pixels
 * @param label Corresponding label
 * @author Mike Volchko & Oliver Wilson
 */
public record MDigit(int no, double[] pixels, int label) {
    public String toString()
    {
        String output = "";
        output += "--- Raster Output\n" +
                "#" + no + " label: " + label;
        output += "\n   0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7";
        for(int row = 0; row < 28; row++)
        {
            output += "\n" + (row % 10) + "  ";
            for(int column = 0; column < 28; column++)
            {
                String currentPixel = ".  ";
                if(pixels[row*28 + column] != 0.0)
                    currentPixel = Integer.toHexString(
                            0x100 | ((int)pixels[row*28 + column])).substring(1) + " ";
                output += currentPixel;
            }//for
        }//for
        return output;
    }//toString
}
