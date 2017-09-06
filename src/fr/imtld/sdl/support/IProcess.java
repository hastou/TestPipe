package fr.imtld.sdl.support;

/**
 * Une interface Process SDL simple.
 * 
 * @author: C. TOMBELLE
 */
public interface IProcess {
    /**
	 * D�poser un signal dans la fifo de cette instance de process SDL.
	 * 
	 * @param oSignal
	 *            Le signal � d�poser.
     */
    public void add( Object oSignal );
	/**
	 * D�finir le parent de ce Pipe et d�marrer son thread SDL.
	 * 
	 * @param procParent
	 *            Processus parent
	 */
    public void setParent( IProcess procParent );
}
