package neural.mnist;

/**
 * Interface to load the MedMNIST data.
 */
public interface IMedLoader {
    public record Normal(double[][] pixels, double[][] labels) {
    }

    /**
     * Gets the pixel and label data in row-major order from their respective files.
     * @param type The type of data to be returned Options: (training, testing)
     * @return Data in row-major order.
     */
    public MDigit[] load(String type, String mode);

    /**
     * Normalizes the data.
     */
    public  Normal normalize(); 
}
