package authentication;

import model.Gebruiker;
import play.mvc.Http;

public class Administrator extends FunctieAutenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        String functie = ctx.session().get("functie");
        if (
            functie != null &&
            functie.equals(Gebruiker.Functie.administrator.name())
        ) {
            return "authorized";
        }

        return null;
    }
}
