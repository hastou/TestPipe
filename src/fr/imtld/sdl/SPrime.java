package fr.imtld.sdl;

import fr.imtld.sdl.support.IProcess;
import fr.imtld.sdl.support.Signal;

/**
 * Implantation du signal SDL SPrime.
 */
public class SPrime extends Signal {
	/** Nombre premier v�hicul� par ce signal. */
	private int prime;

	/**
	 * Cr�er une instance du signal SPrimer.
	 * 
	 * @param sender
	 *            PId de l'instance du process �metteur.
	 * @param iNb
	 *            the carried prime number.
	 */
	public SPrime(IProcess sender, int iNb) {
		super(sender);
		prime = iNb;
	}

	/**
	 * Fournir le nombre premier v�hicul� par ce signal.
	 * 
	 * @return le nombre.
	 */
	public int prime() {
		return prime;
	}

	@Override
	public String toString() {
		return String.valueOf(prime);
	}
}
