package authentication;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class NietIngelogd extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String functie = ctx.session().get("functie");
        if(functie == null) {
            return "authorized";
        }

        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(controllers.routes.Application.home());
    }
}
