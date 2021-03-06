package solver;

import puzzle.PuzzleCharacter;
import puzzle.Tile;

/**
 * @author Tesser Paolo
 * @version 0.1
 */
public class RowThread extends BasicThread {
    private final int totalRowThread;

    /**
     *
     * @param start il numero della prima riga da comporre
     * @param stop il numero dell'ultima riga da comporre
     * @param p il Puzzle generale
     * @param s l'oggetto condiviso con gli altri thread per scambiarsi informazioni
     */
    public RowThread(int start, int stop, PuzzleCharacter p, SearchStatus s, int t){
        super(start,stop,p,s);
        this.totalRowThread = t;
    }

    public void run(){
        /* Parto dal primo elemento delle riga e ad ogni iterazione cerco l'elemento successivo e lo inserisco nell'apposita posizione nell'array */
        for (int z = 0; z < (this.getPuzzleSolve().getNumCol()-1); z++){
            Tile nowTile = this.getPuzzleSolve().getPuzzleElementSolved()[this.getStart()][z];
            String idTileEast = nowTile.getIdEast();
            Tile newTileEast = this.getPuzzleSolve().getPuzzleElementToSolve().get(idTileEast);
            this.getPuzzleSolve().setPuzzleElementSolved(this.getStart(),z+1,newTileEast);
        }

        /* Sezione critica in quanto devo aumentare il contatore dell'oggetto condiviso che mi dice quanti task hanno completato il loro compito */
        synchronized (this.getSharedStatus()){
            int count = this.getSharedStatus().getCountRowThread();
            this.getSharedStatus().setCountRowThread(++count);
            if (this.getSharedStatus().getCountRowThread() == this.totalRowThread)
                this.getSharedStatus().notifyAll();
        }


    }
}
