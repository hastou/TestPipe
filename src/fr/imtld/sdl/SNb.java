package fr.imtld.sdl;

import fr.imtld.sdl.support.IProcess;
import fr.imtld.sdl.support.Signal;

/**
 * Implantation du signal SDL SNb.
 */
public class SNb extends Signal {
	/** Nombre v�hicul� par ce signal */
	private int nb;

	/**
	 * Cr�er une instance du signal SNb.
	 * 
	 * @param sender
	 *            PId de l'instance du process �metteur.
	 * @param iNb
	 *            Nombre que le signal doit v�hiculer.
	 */
	public SNb(IProcess sender, int iNb) {
		super(sender);
		nb = iNb;
	}

	/**
	 * Fournir le nombre v�hicul� par ce signal.
	 * 
	 * @return le nombre.
	 */
	public int nb() {
		return nb;
	}
}
