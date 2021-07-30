package pl.mrcwojcik.faction_torn.models;

public class Xanax implements Comparable<Xanax>{

    public String memberName;
    public Integer deposited;
    public Integer used;

    public Integer getDeposited() {
        return deposited;
    }

    public void setDeposited(Integer deposited) {
        this.deposited = deposited;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public int compareTo(Xanax o) {
        if (getUsed() == null || o.getUsed() == null) {
            return 0;
        }
        return getUsed().compareTo(o.getUsed());
    }
}
