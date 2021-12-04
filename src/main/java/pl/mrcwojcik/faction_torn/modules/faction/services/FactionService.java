package pl.mrcwojcik.faction_torn.modules.faction.services;

import org.json.JSONObject;
import pl.mrcwojcik.faction_torn.modules.faction.domain.Faction;

public interface FactionService {

    Faction updateFaction();
    Faction getFactionInfo();
    Faction saveFactionData(JSONObject json);

}
