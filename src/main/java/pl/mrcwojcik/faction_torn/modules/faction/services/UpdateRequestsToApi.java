package pl.mrcwojcik.faction_torn.modules.faction.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.mrcwojcik.faction_torn.utils.ReadJson;
import pl.mrcwojcik.faction_torn.utils.RequestToTornApi;

@Component
public class UpdateRequestsToApi implements RequestToTornApi {

    private final ReadJson readJson;
    private final String apiUrl;

    public UpdateRequestsToApi(ReadJson readJson, @Value("${torn.api.url}") String apiUrl) {
        this.readJson = readJson;
        this.apiUrl = apiUrl;
    }


    @Override
    public JSONObject makeRequestToApi(String partOfApi, String key) {
        try {
            return readJson.readJsonFromUrl(apiUrl + partOfApi + key);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
