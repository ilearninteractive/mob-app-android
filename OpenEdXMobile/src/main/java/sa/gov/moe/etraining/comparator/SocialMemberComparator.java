package sa.gov.moe.etraining.comparator;

import java.text.Collator;
import java.util.Comparator;

import sa.gov.moe.etraining.social.SocialMember;

public class SocialMemberComparator implements Comparator<SocialMember> {

    Collator collator;

    public SocialMemberComparator(){

        collator = Collator.getInstance();

    }

    @Override
    public int compare(SocialMember lhs, SocialMember rhs) {

        return collator.compare(lhs.getFullName(), rhs.getFullName());

    }
}
