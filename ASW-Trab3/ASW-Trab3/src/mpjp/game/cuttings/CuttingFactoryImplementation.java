package mpjp.game.cuttings;

import java.util.HashSet;
import java.util.Set;

import mpjp.shared.MPJPException;

/**
 * A concrete creator implementing a CuttingFactory. This class is part of 
 * Factory Method design pattern. 
 */
public class CuttingFactoryImplementation implements CuttingFactory {
    
    /**
     * Create a cutting with given name.
     * 
     * @param name
     */
	public Cutting createCutting​(String name) throws MPJPException {
		name = name.toUpperCase();
		switch (name) {
		case "ROUND":
			return new RoundCutting();
		case "STANDARD":
			return new StandardCutting();
		case "STRAIGHT":
			return new StraightCutting();
		case "TRIANGULAR":
			return new TriangularCutting();
		default:
			throw new MPJPException("Not a valid cutting name " + name);
		}
	}

    /**
     * Set of valid cutting names.
     */
	public Set<java.lang.String> getAvaliableCuttings() {
		Set<String> cuttings = new HashSet<String>();
		cuttings.add("Round");
		cuttings.add("Standard");
		cuttings.add("Straight");
		cuttings.add("Triangular");
		return cuttings;
	}
}