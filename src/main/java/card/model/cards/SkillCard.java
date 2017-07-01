package card.model.cards;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SkillCard extends BattleCard {

	protected String userId;
	protected int gutsCost;
	protected int damage;
	protected String type;
	protected String targeting;
	protected String keyword;
	protected int keyValue;

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
	public String getKeyword() {
		return keyword;
	}
	@XmlElement
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getKeyValue() {
		return keyValue;
	}
	@XmlElement
	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}
	
	//----- TYPES -----//
	
	public boolean anyUser() { return "Any".equals(getUserId()); }
	
	public boolean typePOW() { return "POW".equals(getType()); }
	public boolean typeINT() { return "INT".equals(getType()); }
	public boolean typeSPE() { return "SPE".equals(getType()); }
	public boolean typeENV() { return "ENV".equals(getType()); }
	public boolean typeDGE() { return "DGE".equals(getType()); }
	public boolean typeBLK() { return "BLK".equals(getType()); }
	public boolean typeAttack() { return typePOW() || typeINT() || typeSPE() || typeENV(); }
	public boolean typeDefense() { return typeDGE() || typeBLK(); }
	
	public boolean targetEnemy() { return "ENEMY".equals(getTargeting()); }
	public boolean targetFriend() { return "FRIEND".equals(getTargeting()); }
	
	// ----- SPECIAL EFFECTS ----- //
	
	public boolean keywordNormal() { return "NORMAL".equals(getKeyword()); }
	
	public boolean keywordAirDouble() { return "AIR_DOUBLE".equals(getKeyword()); }
	public boolean keywordAll() { return "ALL".equals(getKeyword()); }
	public boolean keywordAllGround() { return "ALL_GRD".equals(getKeyword()); }
	public boolean keywordAOE() { return "AOE".equals(getKeyword()); }
	public boolean keywordAOEAir() { return "AOE_AIR".equals(getKeyword()); }
	public boolean keywordAOEGround() { return "AOE_GRD".equals(getKeyword()); }
	public boolean keywordDodge() { return "DGE".equals(getKeyword()); }
	public boolean keywordDodgeINT() { return "DGE_INT".equals(getKeyword()); }
	public boolean keywordDodgePOW() { return "DGE_POW".equals(getKeyword()); }
	public boolean keywordHalfDodge() { return "HALF_DODGE".equals(getKeyword()); }
	public boolean keywordIntercept() { return "INTERCEPT".equals(getKeyword()); }
	public boolean keywordLife() { return "LIFE".equals(getKeyword()); }
	public boolean keywordLifeSteal() { return "LIFE_STEAL".equals(getKeyword()); }
	public boolean keywordStun() { return "STUN".equals(getKeyword()); }
	public boolean keywordTargetGround() { return "TARGET_GRD".equals(getKeyword()); }
	public boolean keywordUnblockable() { return "UNBLOCKABLE".equals(getKeyword()); }
	public boolean keywordUndodgable() { return "UNDODGABLE".equals(getKeyword()); }
	
	public boolean keywordComboTiger() { return "COMBO_TIGER".equals(getKeyword()); }
	
	public int findBlockAmount() { return "BLOCK".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findBlockINTAmount() { return "BLOCK_INT".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findBlockPOWAmount() { return "BLOCK_POW".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findDodgeGUTSThreshold() { return "BLOCK_POW".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findGUTSDamage() { return "GUTS_DMG".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findLifeThreshold() { return "LIFE".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findRecoilDamage() { return "RECOIL".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findDodgeRecoilDamage() { return "RECOIL_DGE".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findHitRecoilDamage() { return "RECOIL_HIT".equals(getKeyword()) ? getKeyValue() : 0; }
	public int findRestoreAmount() { return "RESTORE".equals(getKeyword()) ? getKeyValue() : 0; }
	
	public boolean isAOE() { return keywordAll() || keywordAllGround() || keywordAOE() || keywordAOEAir() || keywordAOEGround(); }
	public boolean isCombo() { return false; }
}
