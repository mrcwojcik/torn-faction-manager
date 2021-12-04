package pl.mrcwojcik.faction_torn.utils;

import org.json.JSONObject;

public interface RequestToTornApi {

    JSONObject makeRequestToApi(String apiUrl, String key);

}
