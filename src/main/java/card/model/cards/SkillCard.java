package card.model.cards;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SkillCard extends BattleCard {

	protected String userId;
	protected int gutsCost;
	protected int damage;
	protected String type;
	protected String targeting;
	protected List<String> keywords;

	public int getGutsCost() {
		return gutsCost;
	}
	@XmlElement
	public void setGutsCost(int gutsCost) {
		this.gutsCost = gutsCost;
	}
	public String getUserId() {
		return userId;
	}
	@XmlElement
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	@XmlElement
	public void setType(String type) {
		this.type = type;
	}
	public int getDamage() {
		return damage;
	}
	@XmlElement
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public String getTargeting() {
		return targeting;
	}
	@XmlElement
	public void setTargeting(String targeting) {
		this.targeting = targeting;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	@XmlElement
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	//----- TYPES AND TARGETING -----//
	public final static String ANY_USER = "Any";
	public final static String BREEDER_USER = "Breeder";
	
	public static final String TYPE_POW = "POW";
	public static final String TYPE_INT = "INT";
	public static final String TYPE_SPE = "SPE";
	public static final String TYPE_ENV = "ENV";
	public static final String TYPE_DGE = "DGE";
	public static final String TYPE_BLK = "BLK";
	
	public static final String TARGET_SELF = "SELF";
	public static final String TARGET_FRIEND = "FRIEND";
	public static final String TARGET_ENEMY = "ENEMY";
	public static final String TARGET_AOE = "AOE";
	public static final String TARGET_COMBO = "COMBO";
	public static final String TARGET_MULTI = "MULTI";
	
	public boolean anyUser() {
		return ANY_USER.equals(getUserId());
	}
	public boolean typePOW() {
		return TYPE_POW.equals(getType());
	}
	public boolean typeINT() {
		return TYPE_INT.equals(getType());
	}
	public boolean typeSPE() {
		return TYPE_SPE.equals(getType());
	}
	public boolean typeENV() {
		return TYPE_ENV.equals(getType());
	}
	public boolean typeDGE() {
		return TYPE_DGE.equals(getType());
	}
	public boolean typeBLK() {
		return TYPE_BLK.equals(getType());
	}
	
	public boolean typeAttack() {
		return typePOW() || typeINT() || typeSPE() || typeENV();
	}
	public boolean typeDefense() {
		return typeDGE() || typeBLK();
	}
	
	public boolean targetSelf() {
		return TARGET_SELF.equals(getTargeting());
	}
	public boolean targetFriend() {
		return TARGET_FRIEND.equals(getTargeting());
	}
	public boolean targetEnemy() {
		return TARGET_ENEMY.equals(getTargeting());
	}
	public boolean targetAOE() {
		return TARGET_AOE.equals(getTargeting());
	}
	public boolean targetCombo() {
		return TARGET_COMBO.equals(getTargeting());
	}
	public boolean targetMulti() {
		return TARGET_MULTI.equals(getTargeting());
	}
	
	// ----- SPECIAL EFFECTS ----- //
	
	// keywords
	public static final String STUN = "STUN";
	public static final String HALF_DODGE = "HALF_DODGE";
	public static final String UNDODGABLE = "UNDODGABLE";
	public static final String GUTS_DMG = "GUTS_DMG";
	public static final String HIT_RECOIL = "HIT_RECOIL";
	public static final String ON_POW = "ON_POW";
	public static final String ON_INT = "ON_INT";
	public static final String BLOCK = "BLOCK";
	public static final String COMBO_TIGER = "COMBO_TIGER";

	public boolean keyStun() {
		return getKeywords().contains(STUN);
	}
	public boolean keyHalfDodge() {
		return getKeywords().contains(HALF_DODGE);
	}
	public boolean keyUndodgable() {
		return getKeywords().contains(UNDODGABLE);
	}
	public boolean keyOnPOW() {
		return getKeywords().contains(ON_POW);
	}
	public boolean keyOnINT() {
		return getKeywords().contains(ON_INT);
	}
	public boolean keyComboTiger() {
		return getKeywords().contains(COMBO_TIGER);
	}
	public int findGUTSDamage() {
		return findKeyValue(getKeywords(),GUTS_DMG);
	}
	public int findHitRecoilDamage() {
		return findKeyValue(getKeywords(),HIT_RECOIL);
	}
	public int findBlockAmount() {
		return findKeyValue(getKeywords(),BLOCK);
	}

	
	public int findKeyValue(List<String> keywords, String key) {
		for (String keyword : keywords) {
			String[] split = keyword.split(":");
			if (key.equals(split[0])) {
				return Integer.parseInt(split[1]);
			}
		}
		return 0;
	}
}
