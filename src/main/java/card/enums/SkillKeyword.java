package card.enums;

public enum SkillKeyword {
	/** Deals double damage to AIR monsters. **/
	AIR_DOUBLE,
	/** Damages all other monsters. **/
	ALL,
	/** Damages all other GRD monsters. **/
	ALL_GRD,
	/** Damages all opponent's monsters. **/
	AOE,
	/** Damages all opponent's AIR monsters. **/
	AOE_AIR,
	/** Damages all opponent's GRD monsters. **/
	AOE_GRD,
	/** All monsters on attacking team lose 1 life point at the end of the attack phase. **/
	ARID_LAND,
	/** All cards cost 1 more GUTS. **/
	AWE,
	/** All monsters become GRD. All monsters return to their default type when this is removed. **/
	BAD_WEATHER,
	/** This monster can use multiple skill cards this turn. **/
	BERSERK,
	/** Reduce damage from POW or INT attack by X. **/
	BLOCK,
	/** Reduce damage from INT attack by X. **/
	BLOCK_INT,
	/** Reduce damage from POW attack by X. **/
	BLOCK_POW,
	/** Your other monsters become AIR. **/
	BLOW_HOLE,
	/** Deflect all damage from a POW attack. This does not prevent special effects. **/
	BOUNCE,
	/** Return as many cards as you have GUTS from the top of your discards to your deck. Your deck gets shuffled. **/
	BUSY_TIME,
	/** Players discard all cards in their hand after their GUTS phase. **/
	CLOSE_UP,
	/** This monster becomes restricted to only using "Emerge". **/
	COCOON,
	/** Must combo with "Tail Attack" and "Bite". These cards GUTS costs become 0. Damages all opponent's monsters. **/
	COMBO_DRAGON,
	/** Can combo with "Kick". Two card combo deals 4 damage. **/
	COMBO_HENGER,
	/** Can combo "Left Claw" and "Right Claw" and "Horn". Two card combo deals 3 damage. Three card combo deals 7 damage. **/
	COMBO_TIGER,
	/** Must combo with a POW attack. This attack can't be dodged. **/
	COMBO_UNDGE,
	/** Can combo with "Tail Lash". Two card combo deals 6 damage. **/
	COMBO_WORM,
	/** Select one card from your hand. Discard the selected card. Draw 2 cards. **/
	COMPUTING,
	/** Must combo with a POW or INT attack. Any DGE or BLK that costs 0 GUTS can't be used against this attack. **/
	CONCENTRATE,
	/** Discard two cards from the top of each player's deck on their draw phase. **/
	CONFUSION,
	/** Draw one card. **/
	COOL_JUDGE,
	/** Must combo with a POW attack. This attack becomes any POW card from your deck. **/
	COPY_CAT,
	/** Deflect half damage from a POW attack. This does not prevent special effects. **/
	COUNTER,
	/** Must combo with a POW or INT attack. Add 2 damage to this attack. **/
	CRITICAL,
	/** Deflect all damage from a INT attack. This does not prevent special effects. **/
	DEFLECT,
	/** Dodges a POW or INT attack. **/
	DGE,
	/** Dodges a POW or INT attack that costs more than X GUTS. */
	DGE_GUTS,
	/** Dodges a INT attack. **/
	DGE_INT,
	/** Dodges a POW attack. **/
	DGE_POW,
	/** Dodge a POW or INT attack. You lose all of your GUTS. **/
	DODGE,
	/** Select one DGE card from your discards. Put this card in your hand. **/
	DODGE_PROGRAM,
	/** Prevent damage to this monster until your next turn. **/
	DOLL,
	/** Can't be used unless "Cocoon" was already played. Replace this monster with a monster whose sub-lineage is "Worm". **/
	EMERGE,
	/** Opponent's monsters can't attack this monster on their next turn. **/
	ENCHANT,
	/** All cards cost 1 less GUTS. **/
	ENCOURAGE,
	/** Can't play SPE cards. **/
	ENSNARE,
	/** All INT attacks deal 2 less damage. **/
	EXCITED,
	/** Both player's GUTS are reduced to zero. **/
	FAINTED,
	/** Opponent loses 1 GUTS. **/
	FAKE_CRY,
	/** Dodges a POW attack. All POW and INT attacks made by this monster deal double damage next turn. **/
	FLY_AWAY,
	/** Opponent's monsters can't dodge this turn. **/
	GLARE,
	/** The top cards of your deck become GUTS equal to this monster's life. This monster's life becomes 0. **/
	GOOD_LUCK,
	/** Can't be used if there are more than 0 cards in your deck. Must combo with a POW or INT card. This attack deals triple damage. **/
	GRAND_END,
	/** Can only be used once per monster. If this monster would be reduced to 0 life, reduce this monster to 1 life instead. **/
	GRIT,
	/** If this attack deals damage, opponent loses X GUTS. **/
	GUTS_DMG,
	/** If dodged, deal half damage to the target monster. **/
	HALF_DODGE,
	/** Can't reduce a monster below 1 life. **/
	HELP,
	/** Transfer damage from a POW or INT attack to another monster on your team. **/
	HIDE,
	/** Combo with any number of cards. Add one damage for each card combo. **/
	HIT_CARD,
	/** All POW and INT attacks deal 1 more damage. **/
	HOT_BATTLE,
	/** Opponent's monsters can only attack this monster on their next turn. Prevent all damage to your other monsters. **/
	IN_YOUR_FACE,
	/** Take damage from a POW or INT attack for another monster on your team. **/
	INTERCEPT,
	/** Dodges a POW attack. All POW attacks made by this monster deal double damage next turn. This monster becomes AIR until next turn. **/
	JUMP,
	/** Double damage received from a POW attack. All POW attacks made by this monster deal triple damage next turn. **/
	JUMP_IN,
	/** Can't be used if there is more than one monster on your team with more than 0 life. This monster may use multiple attacks this turn. **/
	LAST_MAN,
	/** Select one card from your hand. Shuffle that card into your deck. Draw one card. **/
	LAST_TRUMP,
	/** Change the damage received from a POW or INT attack to half of this monster's life. **/
	LEAF_ZERO,
	/** Can't be used if this monster's current life is greater than 2. **/
	LIFE_THRESHOLD,
	/** This monster gains life equal to damage dealt by this attack. **/
	LIFE_STEAL,
	/** POW or INT attacks where the unblocked damage is more than 3 will deal 0 damage. **/
	LOCK_TIGHT,
	/** Players can only convert up to 2 cards to GUTS on their GUTS phase. **/
	LONG_BATTLE,
	/** Opponent discards one card from their hand. **/
	LOUD_VOICE,
	/** Prevent GUTS damage until your next turn. **/
	MEDITATE,
	/** Can be used multiple times this turn. **/
	MULTI,
	/** Select one card from your discards. Put this card on the bottom of your deck. **/
	MYSTERY_POWER,
	/** POW or INT attacks where the unblocked damage is less than 4 will deal 0 damage. **/
	NICE_FIGHT,
	/** Deals regular damage. **/
	NORMAL,
	/** Target monster on your team can attack again this turn. **/
	NUTS_OIL,
	/** Prevent GUTS damage from an attack. **/
	PERSISTANCE,
	/** Can't use DGE or BLK cards on opponent's next turn. All POW or INT attacks made by this monster deal double damage next turn. **/
	PO,
	/** Deal 1 damage to a monster on your team. Draw 1 card. **/
	POWDER,
	/** Must combo with a POW or INT attack. This attack costs 2 less GUTS. **/
	POWER_OF_SUN,
	/** Prevent ENV card from being played. **/
	PRAY,
	/** Mixed breed monsters can't use DGE defense against purebreed monster's attacks. **/
	PRIDE,
	/** Opponent's monsters can only attack this monster on their next turn. **/
	PROVOKE,
	/** Restore a "Pheonix" on your team with 0 life to its starting life. **/
	REBORN,
	/** All POW attacks made by this monster deal double damage next turn. **/
	RECHARGE,
	/** Deal X damage to this monster after attacking. **/
	RECOIL,
	/** If dodged, deal X damage to this monster after attacking. **/
	RECOIL_DGE,
	/** If not dodged, deal X damage to this monster after attacking. **/
	RECOIL_HIT,
	/** Discard all cards from your hand. Draw that many cards. **/
	REFRESH,
	/** Restore X life to a monster on your team. **/
	RESTORE,
	/** All monsters in your team dodge an attack that would damage all of the monsters in your team. **/
	RETREAT,
	/** Opponent's hand gets placed on the bottom of their deck. Your opponent draws 4 cards. **/
	RIDDLER,
	/** Opponent's monsters can't attack next turn. **/
	ROAR,
	/** All POW attacks made by this monster deal double damage next turn. This effect ends if this monster is damaged. **/
	ROOTONE,
	/** Reveal opponent's hand. Draw a card. **/
	SCOUTING,
	/** All INT attacks made by this monster deal double damage next turn. This effect ends if this monster is damaged. **/
	SILENT_STANCE,
	/** Can use multiple breeder cards this turn. **/
	SINGING_CAT,
	/** Dodges a POW attack. All POW attacks made by this monster deal double damage next turn. **/
	SOAR,
	/** All INT attacks deal 2 more damage. **/
	SPARK,
	/** Reduce damage from POW or INT attack by half. **/
	SPONGE,
	/** If not dodged, defending monster can't attack next turn. **/
	STUN,
	/** Must combo with a breeder card that costs less than 4 GUTS. The breeder card costs 0 GUTS to play. **/
	SUPPORT,
	/** Select one card from your discards. Add selected card to your hand. **/
	TACTICS,
	/** This monster's life becomes 0. Restore a monster on your team with 0 life to its starting life.  **/
	TAKE_OVER,
	/** Can't target AIR monsters. **/
	TARGET_GRD,
	/** Opponent's monsters become AIR for this turn. This monster may attack again this turn. **/
	THROW_AWAY,
	/** If this monster is GRD, this monster becomes AIR. If this monster is AIR, this monster becomes GRD. Draw one card. **/
	TRANSFORM,
	/** Can't be used if there is more than one monster on your team with more than 0 life. All cards in your deck are made into GUTS. **/
	TWINKLING,
	/** Damage is not reduced by BLK defense. **/
	UNBLOCKABLE,
	/** Can't be dodged. **/
	UNDODGABLE,
	/** All monsters become AIR. All monsters return to their default type when this is removed. **/
	WARP,
	/** Can't use after playing another card on your turn. The top 5 cards of your deck become GUTS. Your attack phase ends. **/
	WILD_RUSH,
	/** Can't use if current life is greater than 2. Must combo with a POW or INT card. This attack deals double damage. **/
	WILL_POWER,
	/** Skip opponent's next turn. **/
	YODEL
}