package fr.imtld.sdl;

import fr.imtld.sdl.support.IProcess;
import fr.imtld.sdl.support.Signal;

/**
 * Implantation du signal SDL SKill.
 */
public class SKill extends Signal {
	/**
	 * Cr�er une instance du signal SKill.
	 * 
	 * @param sender
	 *            PId de l'instance du process �metteur.
	 */
	public SKill(IProcess sender) {
		super(sender);
	}
}
