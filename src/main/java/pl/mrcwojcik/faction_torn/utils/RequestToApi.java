package pl.mrcwojcik.faction_torn.utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestToApi {

    private final ReadJson readJson;
    private final String apiKey;

    public RequestToApi(ReadJson readJson, @Value("${torn.api.koziolkuj}") String apiKey) {
        this.readJson = readJson;
        this.apiKey = apiKey;
    }

    public JSONObject getAttackNews(long fromTime, long toTime){
        try {
            return readJson.readJsonFromUrl("https://api.torn.com/faction/?selections=attacksfull&from="
                    + fromTime
                    + "&to=" + toTime
                    + "&key=" + apiKey);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getXanaxNews(long fromTimeInMilis, long toTimeInMilis){
        try {
            return readJson.readJsonFromUrl("https://api.torn.com/faction/?selections=armorynews&from="
                    + fromTimeInMilis
                    + "&to=" + toTimeInMilis
                    + "&key=" + apiKey);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getChainList(){
        try {
            return readJson.readJsonFromUrl("https://api.torn.com/faction/?selections=chains&key=" + apiKey);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
