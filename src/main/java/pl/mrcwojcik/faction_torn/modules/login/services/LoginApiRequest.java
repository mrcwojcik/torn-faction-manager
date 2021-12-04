package pl.mrcwojcik.faction_torn.modules.login.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.mrcwojcik.faction_torn.utils.ReadJson;
import pl.mrcwojcik.faction_torn.utils.RequestToTornApi;

@Component
public class LoginApiRequest implements RequestToTornApi {

    private final ReadJson readJson;
    private final String apiUrl;

    public LoginApiRequest(ReadJson readJson, @Value("${torn.api.url}") String apiUrl) {
        this.readJson = readJson;
        this.apiUrl = apiUrl;
    }

    @Override
    public JSONObject makeRequestToApi(String requestPart, String apiKey) {
        try {
            return readJson.readJsonFromUrl(apiUrl + requestPart + apiKey);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
