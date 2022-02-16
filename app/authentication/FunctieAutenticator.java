package authentication;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public abstract class FunctieAutenticator extends Security.Authenticator {
    @Override
    public abstract String getUsername(Http.Context ctx);

    @Override
    public final Result onUnauthorized(Http.Context ctx) {
        ctx.flash().put("error-message", "U heeft geen toegang tot deze pagina");
        return redirect(controllers.routes.Application.home());
    }
}
