package neural.matrix;

import neural.matrix.IMop;

import java.util.Arrays;

/**
 * @author Oliver Wilson & Mike Volchko
 * @date 17 Sep 2022
 *
 */

public class Mop implements IMop {

    /**
     * The slice method is used to cut a matrix horizontally.
     * @param src       The provided matrix that will be sliced
     * @param startRow  The starting row of the slice (Included)
     * @param endRow    The ending row of the slice (Excluded)
     */
    @Override
    public double[][] slice(double[][] src, int startRow, int endRow) {
        double[][] slicedMatrix = new double[1][1];
        int srcRowPos;
        // If matrix is not empty or the endRow index is not less than the startRow index
        if(!(src.length <= 0 || src[0].length <=0 || endRow<=startRow))
        {
            slicedMatrix = new double[endRow-startRow][src[0].length];
            for(int column = 0; column < src[0].length; column++) {
                srcRowPos = startRow;
                for (int row = 0; row < slicedMatrix.length; row++) {
                    slicedMatrix[row][column] = src[srcRowPos][column];
                    srcRowPos++;
                }
            }
        }

        return slicedMatrix;
    }

    /**
     * The transpose method takes a matrix and transposes it.
     * @param src   The provided matrix that will be transposed.
     */
    @Override
    public double[][] transpose(double[][] src) {
        double[][] transposedMatrix = new double[1][1];
        if(!(src.length <= 0 || src[0].length <=0)){
            transposedMatrix = new double[src[0].length][src.length];
            for(int column = 0; column < transposedMatrix[0].length; column++)
                for(int row = 0; row < transposedMatrix.length; row++)
                    transposedMatrix[row][column] = src[column][row];
        }

        return transposedMatrix;
    }

    /**
     * The dice method is used to cut a matrix vertically.
     * @param src       The provided matrix that will be sliced
     * @param startCol  The starting column of the slice (Included)
     * @param endCol    The ending column of the slice (Excluded)
     */
    @Override
    public double[][] dice(double[][] src, int startCol, int endCol) {
        double[][] dicedMatrix = new double[1][1];
        int srcColumnPos;
        // If matrix is not empty or the endCol index is not less than the startCol index
        if(!(src.length <= 0 || src[0].length <=0 || endCol<=startCol))
        {
            dicedMatrix = new double[src.length][endCol-startCol];
            for(int row = 0; row < dicedMatrix.length; row++) {
                srcColumnPos = startCol;
                for (int column = 0; column < dicedMatrix[0].length; column++) {
                    dicedMatrix[row][column] = src[row][srcColumnPos];
                    srcColumnPos++;
                }
            }
        }

        return dicedMatrix;
    }

    /**
     * The print method receives a msg and a matrix to print out.
     * The message will be printed first, followed by the matrix.
     * @param msg   A message that will be printed before the matrix
     * @param src   The provided matrix that will be printed
     */
    @Override
    public void print(String msg, double[][] src) {
        System.out.println(msg);
        for(int row = 0; row < src.length; row++)
            System.out.println(Arrays.toString(src[row]));
    }
}
