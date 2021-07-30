package pl.mrcwojcik.faction_torn.service.additionals;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.mrcwojcik.faction_torn.models.Attack;
import pl.mrcwojcik.faction_torn.models.Chain;
import pl.mrcwojcik.faction_torn.repositories.MemberRepository;
import pl.mrcwojcik.faction_torn.utils.ReadJson;
import pl.mrcwojcik.faction_torn.utils.RequestToApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ChainAttacksBuilder {

    private MemberRepository memberRepository;
    private ReadJson readJson;
    private RequestToApi requestToApi;

    public ChainAttacksBuilder(MemberRepository memberRepository, ReadJson readJson, RequestToApi requestToApi) {
        this.memberRepository = memberRepository;
        this.readJson = readJson;
        this.requestToApi = requestToApi;
    }

    public void buildAttackList(List<Chain> chains) {
        try {
            int chainCounter = 0;
            for (Chain chain : chains){
                if (chainCounter > 0){
                    TimeUnit.SECONDS.sleep(30);
                }
                List<Attack> attackList = new ArrayList<Attack>();

                if (attackList.isEmpty()){
                    List<Attack> tempList = getAttackFromChainTime(chain.getStartTime(), chain.getEndTime(), chain);
                    attackList.addAll(tempList);
                }
                while (attackList.size() < chain.getSize()){
                    TimeUnit.SECONDS.sleep(30);
                    Attack lastAttack = attackList.stream()
                            .max(Comparator.comparing(v -> v.getAttackEnd()))
                            .get();
                    List<Attack> tempList = getAttackFromChainTime(lastAttack.getAttackEnd(), chain.getEndTime(), chain);
                    attackList.addAll(tempList);
                }
                chain.setAttacks(attackList);
                chainCounter+=1;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Tworzenie listy ataków */
    private List<Attack> getAttackFromChainTime(Integer startTime, Integer endTime, Chain chain) {
        List<Attack> attackList = new ArrayList<>();
        JSONObject attacksNews = requestToApi.getAttackNews(startTime, endTime);
        JSONObject attacksListObject = attacksNews.getJSONObject("attacks");
        attacksListObject.keySet().forEach(attackKey -> {
            JSONObject singleAttack = attacksListObject.getJSONObject(attackKey);
            if (checkAttackConditions(singleAttack, chain)){
                Attack attack = buildAttack(attackKey, singleAttack);
                attackList.add(attack);
            }
        });

        return attackList;
    }

    private Attack buildAttack(String attackKey, JSONObject singleAttack){
        Attack attack = new Attack();
        attack.setAttackId(attackKey);
        attack.setMemberId(singleAttack.getInt("attacker_id"));
        attack.setAttackStart(singleAttack.getInt("timestamp_started"));
        attack.setAttackEnd(singleAttack.getInt("timestamp_ended"));
        return attack;
    }

    /** Warunki tego, czy atak jest "nasz" i wygrany */
    private boolean checkAttackConditions(JSONObject singleAttack, Chain chain) {
        if (singleAttack.get("attacker_faction").getClass() != Integer.class) {
            return false;
        }

        if (singleAttack.getInt("attacker_faction") == 44865) {
            if (chain.getStartTime() <= singleAttack.getInt("timestamp_started")
                    && chain.getEndTime() >= singleAttack.getInt("timestamp_ended")
                    && attackConditions(singleAttack)) {
                return true;
            }
        }

        return false;
    }

    private boolean attackConditions(JSONObject singleAttack){
        if (singleAttack.getString("result").equals("Mugged")
                || singleAttack.getString("result").equals("Hospitalized")
                || singleAttack.getString("result").equals("Attacked")
        ){
            return true;
        }

        return false;
    }

    /** Building chain list **/
    public List<Chain> buildChainList(long fromTime) {
        JSONObject chainNews = requestToApi.getChainList();
        JSONObject chainsListObject = chainNews.getJSONObject("chains");
        return getChainList(chainsListObject, fromTime);
    }

    private List<Chain> getChainList(JSONObject chainsObject, long fromTime) {
        List<Chain> chains = new ArrayList<>();
        chainsObject.keySet().forEach(keyStr -> {
            JSONObject chainObj = chainsObject.getJSONObject(keyStr);
            if (chainObj.getInt("chain") >= 10){
                if (chainObj.getInt("start")>= fromTime){
                    Chain chain = new Chain();
                    chain.setStartTime(chainObj.getInt("start"));
                    chain.setEndTime(chainObj.getInt("end"));
                    chain.setChainId(Integer.valueOf(keyStr));
                    chain.setCounter(0);
                    chain.setSize(chainObj.getInt("chain"));
                    chains.add(chain);
                }
            }
        });
        return chains;
    }

}
