package mpjp.game.cuttings;

import mpjp.shared.MPJPException;

/**
 * Definition of a factory method to produce cuttings. This type must provide
 * a method that returns a Cutting given its name, and a list of available 
 * cutting names. This interface is part of Factory Method design pattern. 
 */
public interface CuttingFactory {
	Cutting createCutting​(java.lang.String name) throws MPJPException;
	java.util.Set<java.lang.String> getAvaliableCuttings();
}
