package fr.imtld.sdl.support;

/**
 * Base pour l'implantation de signaux SDL.
 */
public class Signal {
	/** PId de l'�metteur du signal. */
	protected IProcess sender;

	/**
	 * Cr�er une instance du signal SDL.
	 * 
	 * @param sender
	 *            PId de l'instance du process �metteur.
	 */
	protected Signal(IProcess sender) {
		this.sender = sender;
	}

	/**
	 * Fournir le PId de l'instance du process �metteur.
	 * 
	 * @return Le PId.
	 */
	public IProcess getSender() {
		return sender;
	}
}
