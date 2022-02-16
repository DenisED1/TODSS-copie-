package authentication;

import model.Gebruiker;
import play.mvc.Http;

public class Leraar extends FunctieAutenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        if(
            ctx.session().get("functie") != null &&
            ctx.session().get("functie").equals(Gebruiker.Functie.leraar.name())
        ) {
            return "authorized";
        }

        return null;
    }
}
