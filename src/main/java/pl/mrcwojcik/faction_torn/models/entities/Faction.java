package pl.mrcwojcik.faction_torn.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "faction")
public class Faction {

    @Id
    @Column(unique = true)
    private String torn_id;
    private String name;
    private String respect;
    private String age;
    private String bestchain;

    public String getTorn_id() {
        return torn_id;
    }

    public void setTorn_id(String torn_id) {
        this.torn_id = torn_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRespect() {
        return respect;
    }

    public void setRespect(String respect) {
        this.respect = respect;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBestchain() {
        return bestchain;
    }

    public void setBestchain(String bestchain) {
        this.bestchain = bestchain;
    }
}
