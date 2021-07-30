package pl.mrcwojcik.faction_torn.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "reports")
public class Report implements Comparable<Report>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;
    private Integer hits;
    private Integer xanUsed;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getXanUsed() {
        return xanUsed;
    }

    public void setXanUsed(Integer xanUsed) {
        this.xanUsed = xanUsed;
    }

    @Override
    public int compareTo(Report o) {
        if (getHits() == null || o.getHits() == null) {
            return 0;
        }
        return getHits().compareTo(o.getHits());
    }
}
