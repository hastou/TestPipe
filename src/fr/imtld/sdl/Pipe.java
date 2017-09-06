package fr.imtld.sdl;

import fr.imtld.sdl.support.Fifo;
import fr.imtld.sdl.support.IProcess;
import fr.imtld.sdl.support.Signal;

/* *************************************************************************
* Le code SDL textuel pour le process Pipe de la sp?cification du crible
* d'Erathost?ne.
* Conseil : copier / coller des parties de cette sp?cification comme
* commentaire du code Java qui implante le comportement correspondant ?
* chaque partie comme si un g?n?rateur avait produit le code Java.

* Ne pas formater le code de tout le fichier pour ne pas perdre la mise en
* forme qui suit !

PROCESS Pipe(1,);
  SIGNALSET SNb, SPrime, SKill, SOver;
  DCL p, n integer;
  DCL prec PID;

  START ;
    NEXTSTATE Initial;

  STATE Initial ;
    INPUT SNb(p);
      TASK prec := sender;
      OUTPUT SPrime(p) TO prec;
      NEXTSTATE First;
  ENDSTATE;

  STATE First;
    INPUT SNb( n );
      DECISION n mod p;
        ( 0 ):
          NEXTSTATE -;
        ELSE:
          CREATE Pipe;
  lblSNb :
          OUTPUT SNb( n ) TO offspring;
          NEXTSTATE Child;
      ENDDECISION;
    INPUT SKill;
  lblStop:
      OUTPUT SOver TO prec;
      DECISION parent;
        ( null ):
          NEXTSTATE Initial;
        ELSE:
          STOP ;
      ENDDECISION;
  ENDSTATE;

  STATE Child
    COMMENT 'offspring existe !';
    INPUT SNb( n );
      DECISION n mod p;
        ( 0 ):
          NEXTSTATE -;
        ELSE:
          JOIN lblSNb;
      ENDDECISION;
    INPUT SPrime( n );
      OUTPUT SPrime( n ) to prec;
      NEXTSTATE -;
    INPUT SKill;
      OUTPUT SKill TO offspring;
      NEXTSTATE -;
    INPUT SOver;
      JOIN lblStop;
  ENDSTATE;
ENDPROCESS;
***************************************************************************/

/**
 * Implante le process SDL Pipe (un ?tage de pipeline d'Erathosth?ne) ;
 * IProcess correspond au type PId de SDL.
 */
public class Pipe implements IProcess {

	/* Implantation des concepts SDL. */
    /**
     * Variable d'?tat de l'automate mod?lisant le comportement du process Pipe.
     */
    private int state;
    /**
     * Thread pour une instance de process SDL.
     */
    private Thread _thread;
    /**
     * Fifo pour une instance de process SDL.
     */
    private Fifo _fifo;
    /**
     * Contr?le la terminaison de l'instance de process SDL.
     */
    private boolean _bRun;

    /* Implantation des expressions SDL pr?d?finies ; TODO penser à leur donner
     * une valeur aux moments appropri?s ! */

    /**
     * Implantation de l'expression SDL parent.
     */
    private IProcess parent;
    /**
     * Implantation de l'expression SDL offspring.
     */
    private IProcess offspring;
    /**
     * Implantation de l'expression SDL sender.
     */
    private IProcess sender;
    /**
     * Implantation de l'expression SDL self.
     */
    private IProcess self;

    /* Implantation des variables SDL du process Pipe. */

    /**
     * Etats du process Pipe.
     */
    private static final int INITIAL = 0, FIRST = 1, CHILD = 2;
    /**
     * Implantation de DCL n integer;
     */
    private int n;
    /**
     * Implantation de DCL p integer;
     */
    private int p;
    /**
     * Implantation de DCL prec PId;
     */
    private IProcess prec;
    /**
     * Runnable pour le point d'entr?e du thread de l'instance de process SDL.
     */
    protected Runnable runnable = new Runnable() {
        /**
         * Point d'entr?e du thread de l'instance de process SDL.
         */
        public void run() {
            while (Pipe.this._bRun) {
                while (Pipe.this._fifo.isEmpty()) {
                    synchronized (Pipe.this._fifo) {
                        try {
                            Pipe.this._fifo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (Pipe.this.state == INITIAL) {
                    initial();
                } else if (Pipe.this.state == FIRST) {
                    first();
                } else if (Pipe.this.state == CHILD) {
                    child();
                }
            }
            // TODO coder une boucle :
            //   Attendre que la fifo soit non vide
            //   Obtenir un signal de la fifo
            //   selon l'?tat courant, aiguiller vers une m?thode correspondante (? ?crire) :
            //     INITIAL -> initial()
            //     FIRST -> first()
            //     CHILD -> child()
            //   Dans chaque m?thode/?tat, aiguiller ensuite selon le signal re?u en se
            //   laissant guider par les INPUT de la sp?cification en SDL textuel.
            //   Conseil : sous chaque ligne de SDL textuel, ?crire le code Java correspondant
            //   comme si un g?n?rateur avait produit ce code Java.
            // fin de boucle
        }
    };

    private void initial() {
        Object o = this._fifo.get();
        if (o instanceof Signal) {
            Signal s = (Signal) o;
            if (s instanceof SNb) {
                this.prec = s.getSender();
                SNb snb = (SNb) s;
                this.p = snb.nb();
                this.prec.add(new SPrime(this, this.p));
                this.state = FIRST;
            }
        }
    }

    private void first() {
        Object o = this._fifo.get();
        if (o instanceof Signal) {
            Signal s = (Signal) o;
            if (s instanceof SNb) {
                SNb snb = (SNb) s;
                this.n = snb.nb();
                if (n % p == 0) {
                    this.state = FIRST;
                } else {
                    this.offspring = new Pipe();
                    this.offspring.setParent(this);
                    lblSNb();
                }
            } else if (s instanceof SKill) {
                lblStop();
            }
        }
    }

    private void lblStop() {
        prec.add(new SOver(this));
        this.state = INITIAL;
        this._bRun = false;
    }

    private void lblSNb() {
        this.offspring.add(new SNb(this, n));
        this.state = CHILD;
    }


    //    INPUT SPrime( n );
//    OUTPUT SPrime( n ) to prec;
//    NEXTSTATE -;
//    INPUT SKill;
//    OUTPUT SKill TO offspring;
//    NEXTSTATE -;
//    INPUT SOver;
//    JOIN lblStop;
//    ENDSTATE;
    private void child() {
        Object o = this._fifo.get();
        if (o instanceof Signal) {
            Signal s = (Signal) o;
            if (s instanceof SNb) {
                SNb snb = (SNb) s;
                this.n = snb.nb();
                if (n % p == 0) {
                    this.state = CHILD;
                } else {
                    lblSNb();
                }
            } else if (s instanceof SKill) {
                offspring.add(new SKill(this));
                this.state = CHILD;
            } else if (s instanceof SPrime) {
                prec.add(new SPrime(this, ((SPrime) s).prime()));
                this.state = CHILD;
            } else if (s instanceof SOver) {
                lblStop();
            }
        }
    }

    public Pipe() {
        // TODO Faire ce qu'on doit faire en g?n?ral dans un constructeur !
        this._bRun = true;
        this.state = INITIAL;
        this.self = this;
        this._fifo = new Fifo();
    }

    /**
     * D?poser un signal dans la fifo de cette instance de process SDL.
     *
     * @param signal Le signal ? d?poser.
     */
    public void add(Object signal) {
        // TODO : Implanter le comportement appropri?.
        synchronized (this._fifo) {
            this._fifo.add(signal);
            this._fifo.notify();
        }
    }

    /**
     * D?finir le parent de ce Pipe et d?marrer son thread SDL.
     *
     * @param procParent Processus parent
     */
    public void setParent(IProcess procParent) {
        parent = procParent;
        // TODO : d?marrer le thread de la m?thode run ici
        _thread = new Thread(this.runnable);
        _thread.start();
    }

    /*
     * Terminer l'instance de process SDL (support pour la construction SDL
     * STOP).
     */
    private void stop() {
        // TODO s'arranger pour que la boucle dans la m?thode run se termine
        this.add(new SKill(this));
    }
}
