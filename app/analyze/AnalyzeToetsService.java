package analyze;

import model.analyze.Toets.ToetsAntwoord;
import play.db.jpa.Transactional;

public class AnalyzeToetsService {
    AnalyzeToetsController analyzeToetsController;

    @Transactional
    public void VoegAntwoordToe(ToetsAntwoord toetsAntwoord) throws Exception {
        if(analyzeToetsController == null){
            throw new Exception("Geen opdracht geselecteerd");
        }

        analyzeToetsController.voegWoordToe(toetsAntwoord);
    }

    public void StopOpdracht(){
        analyzeToetsController.stopOpdracht();
    }

    public void resetController(){
        analyzeToetsController = null;
    }

    public void nieuweOpdracht(int spellingregelid) {
        analyzeToetsController = new AnalyzeToetsController(spellingregelid);
    }
}
