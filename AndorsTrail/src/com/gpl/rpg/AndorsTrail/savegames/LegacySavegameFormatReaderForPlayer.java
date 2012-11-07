package com.gpl.rpg.AndorsTrail.savegames;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.util.FloatMath;
import android.util.SparseIntArray;

import com.gpl.rpg.AndorsTrail.context.WorldContext;
import com.gpl.rpg.AndorsTrail.controller.ActorStatsController;
import com.gpl.rpg.AndorsTrail.controller.Constants;
import com.gpl.rpg.AndorsTrail.model.ability.ActorCondition;
import com.gpl.rpg.AndorsTrail.model.ability.ActorConditionEffect;
import com.gpl.rpg.AndorsTrail.model.ability.SkillInfo;
import com.gpl.rpg.AndorsTrail.model.actor.Player;
import com.gpl.rpg.AndorsTrail.model.item.Inventory;
import com.gpl.rpg.AndorsTrail.model.item.ItemType;
import com.gpl.rpg.AndorsTrail.model.quest.QuestProgress;
import com.gpl.rpg.AndorsTrail.util.Coord;
import com.gpl.rpg.AndorsTrail.util.CoordRect;
import com.gpl.rpg.AndorsTrail.util.Range;
import com.gpl.rpg.AndorsTrail.util.Size;

public final class LegacySavegameFormatReaderForPlayer {
	/*
	public static Player readFromParcel_pre_v34(DataInputStream src, WorldContext world, int fileversion) throws IOException {
		LegacySavegameData_Player savegameData = readPlayerDataPreV34(src, world, fileversion);
		return new Player(savegameData);
	}
	
	private static LegacySavegameData_Player readPlayerDataPreV34(DataInputStream src, WorldContext world, int fileversion) throws IOException {
		LegacySavegameData_Player result = new LegacySavegameData_Player();
		result.isImmuneToCriticalHits = false;
		
		boolean readCombatTraits = true;
		if (fileversion >= 25) readCombatTraits = src.readBoolean();
		if (readCombatTraits) {
			result.attackCost = src.readInt();
			result.attackChance = src.readInt();
			result.criticalSkill = src.readInt();
			if (fileversion <= 20) {
				result.criticalMultiplier = src.readInt();
			} else {
				result.criticalMultiplier = src.readFloat();
			}
			result.damagePotential = new Range(src, fileversion);
			result.blockChance = src.readInt();
			result.damageResistance = src.readInt();
		}
		
		result.iconID = src.readInt();
		result.tileSize = new Size(src, fileversion);
		result.maxAP = src.readInt();
		result.maxHP = src.readInt();
		result.name = src.readUTF();
		result.moveCost = src.readInt();
		
		result.baseAttackCost = src.readInt();
		result.baseAttackChance = src.readInt();
		result.baseCriticalSkill = src.readInt();
		if (fileversion <= 20) {
			result.baseCriticalMultiplier = src.readInt();
		} else {
			result.baseCriticalMultiplier = src.readFloat();
		}
		result.baseDamagePotential = new Range(src, fileversion);
		result.baseBlockChance = src.readInt();
		result.baseDamageResistance = src.readInt();
		
		if (fileversion <= 16) {
			result.baseMoveCost = result.moveCost;
		} else {
			result.baseMoveCost = src.readInt();
		}
				
		if (!readCombatTraits) {
			result.attackCost = result.baseAttackCost;
			result.attackChance = result.baseAttackChance;
			result.criticalSkill = result.baseCriticalSkill;
			result.criticalMultiplier = result.baseCriticalMultiplier;
			result.damagePotential = result.baseDamagePotential;
			result.blockChance = result.baseBlockChance;
			result.damageResistance = result.baseDamageResistance;
		}

		result.ap = new Range(src, fileversion);
		result.health = new Range(src, fileversion);
		result.position = new Coord(src, fileversion);
		result.rectPosition = new CoordRect(result.position, result.tileSize);
		if (fileversion > 16) {
			final int n = src.readInt();
			for(int i = 0; i < n ; ++i) {
				result.conditions.add(new ActorCondition(src, world, fileversion));
			}
		}
		
		result.lastPosition = new Coord(src, fileversion);
		result.nextPosition = new Coord(src, fileversion);
		result.level = src.readInt();
		result.totalExperience = src.readInt();
		result.inventory = new Inventory(src, world, fileversion);
		
		if (fileversion <= 13) readQuestProgressPreV13(result, src, world, fileversion);

		result.useItemCost = src.readInt();
		result.reequipCost = src.readInt();
		final int size2 = src.readInt();
		for(int i = 0; i < size2; ++i) {
			if (fileversion <= 21) {
				result.skillLevels.put(i, src.readInt());
			} else {
				final int skillID = src.readInt();
				result.skillLevels.put(skillID, src.readInt());
			}
		}
		result.spawnMap = src.readUTF();
		result.spawnPlace = src.readUTF();

		if (fileversion > 13) {
			final int numquests = src.readInt();
			for(int i = 0; i < numquests; ++i) {
				final String questID = src.readUTF();
				result.questProgress.put(questID, new HashSet<Integer>());
				final int numprogress = src.readInt();
				for(int j = 0; j < numprogress; ++j) {
					int progress = src.readInt();
					result.questProgress.get(questID).add(progress);
				}
			}
		}
		
		result.availableSkillIncreases = 0;
		if (fileversion > 21) {
			result.availableSkillIncreases = src.readInt();
		}
		
		if (fileversion >= 26) {
			final int size3 = src.readInt();
			for(int i = 0; i < size3; ++i) {
				final String faction = src.readUTF();
				final int alignment = src.readInt();
				result.alignments.put(faction, alignment);
			}
		}
		
		return result;
	}
	
	public static final class LegacySavegameData_Player extends LegacySavegameData_Actor {
		// from Player
		public Coord lastPosition;
		public Coord nextPosition;
		public int level;
		public int totalExperience;
		public Inventory inventory;
		public final HashMap<String, HashSet<Integer> > questProgress = new HashMap<String, HashSet<Integer> >();
		public final HashMap<String, Integer> alignments = new HashMap<String, Integer>();
		public int useItemCost;
		public int reequipCost;
		public final SparseIntArray skillLevels = new SparseIntArray();
		public String spawnMap;
		public String spawnPlace;
		public int availableSkillIncreases = 0;
	}
	*/
	
	public static class LegacySavegameData_Actor {
		// from Actor
		public boolean isImmuneToCriticalHits;
		public int iconID;
		public Size tileSize;
		public Range ap;
		public Range health;
		public Coord position;
		public CoordRect rectPosition;
		public final ArrayList<ActorCondition> conditions = new ArrayList<ActorCondition>();
		
		// from ActorTraits
		public int maxAP;
		public int maxHP;
		public String name;
		public int moveCost;
		public int baseMoveCost;
		public int baseAttackCost;
		public int baseAttackChance;
		public int baseCriticalSkill;
		public float baseCriticalMultiplier;
		public Range baseDamagePotential;
		public int baseBlockChance;
		public int baseDamageResistance;
		
		// from CombatTraits
		public int attackCost;
		public int attackChance;
		public int criticalSkill;
		public float criticalMultiplier;
		public Range damagePotential;
		public int blockChance;
		public int damageResistance;
	}
	
	public static void readQuestProgressPreV13(Player player, DataInputStream src, WorldContext world, int fileversion) throws IOException {
		final int size1 = src.readInt();
		for(int i = 0; i < size1; ++i) {
			String keyName = src.readUTF();
			if ("mikhail_visited".equals(keyName)) addQuestProgress(player, "andor", 1);
			else if ("qmikhail_bread_complete".equals(keyName)) addQuestProgress(player, "mikhail_bread", 100);
			else if ("qmikhail_bread".equals(keyName)) addQuestProgress(player, "mikhail_bread", 10);
			else if ("qmikhail_rats_complete".equals(keyName)) addQuestProgress(player, "mikhail_rats", 100);
			else if ("qmikhail_rats".equals(keyName)) addQuestProgress(player, "mikhail_rats", 10);
			else if ("oromir".equals(keyName)) addQuestProgress(player, "leta", 20);
			else if ("qleta_complete".equals(keyName)) addQuestProgress(player, "leta", 100);
			else if ("qodair".equals(keyName)) addQuestProgress(player, "odair", 10);
			else if ("qodair_complete".equals(keyName)) addQuestProgress(player, "odair", 100);
			else if ("qleonid_bonemeal".equals(keyName)) {
				addQuestProgress(player, "bonemeal", 10);
				addQuestProgress(player, "bonemeal", 20);
			}
			else if ("qtharal_complete".equals(keyName)) addQuestProgress(player, "bonemeal", 30);
			else if ("qthoronir_complete".equals(keyName)) addQuestProgress(player, "bonemeal", 100);
			else if ("qleonid_andor".equals(keyName)) addQuestProgress(player, "andor", 10);
			else if ("qgruil_andor".equals(keyName)) addQuestProgress(player, "andor", 20);
			else if ("qgruil_andor_complete".equals(keyName)) addQuestProgress(player, "andor", 30);
			else if ("qleonid_crossglen".equals(keyName)) addQuestProgress(player, "crossglen", 1);
			else if ("qjan".equals(keyName)) addQuestProgress(player, "jan", 10);
			else if ("qjan_complete".equals(keyName)) addQuestProgress(player, "jan", 100);
			else if ("qbucus_thieves".equals(keyName)) addQuestProgress(player, "andor", 40);
			else if ("qfallhaven_derelict".equals(keyName)) addQuestProgress(player, "andor", 50);
			else if ("qfallhaven_drunk".equals(keyName)) addQuestProgress(player, "fallhavendrunk", 10);
			else if ("qfallhaven_drunk_complete".equals(keyName)) addQuestProgress(player, "fallhavendrunk", 100);
			else if ("qnocmar_unnmir".equals(keyName)) addQuestProgress(player, "nocmar", 10);
			else if ("qnocmar".equals(keyName)) addQuestProgress(player, "nocmar", 20);
			else if ("qnocmar_complete".equals(keyName)) addQuestProgress(player, "nocmar", 200);
			else if ("qfallhaven_tavern_room2".equals(keyName)) addQuestProgress(player, "fallhaventavern", 10);
			else if ("qarcir".equals(keyName)) addQuestProgress(player, "arcir", 10);
			else if ("qfallhaven_oldman".equals(keyName)) addQuestProgress(player, "calomyran", 10);
			else if ("qcalomyran_tornpage".equals(keyName)) addQuestProgress(player, "calomyran", 20);
			else if ("qfallhaven_oldman_complete".equals(keyName)) addQuestProgress(player, "calomyran", 100);
			else if ("qbucus".equals(keyName)) addQuestProgress(player, "bucus", 10);
			else if ("qthoronir_catacombs".equals(keyName)) addQuestProgress(player, "bucus", 20);
			else if ("qathamyr_complete".equals(keyName)) addQuestProgress(player, "bucus", 40);
			else if ("qfallhaven_church".equals(keyName)) addQuestProgress(player, "bucus", 50);
			else if ("qbucus_complete".equals(keyName)) addQuestProgress(player, "bucus", 100);
		}
	}

	private static void addQuestProgress(Player player, String questID, int progress) {
		player.addQuestProgress(new QuestProgress(questID, progress));
		/*if (!player.questProgress.containsKey(questID)) player.questProgress.put(questID, new HashSet<Integer>());
		else if (player.questProgress.get(questID).contains(progress)) return;
		player.questProgress.get(questID).add(progress);*/
	}
	
	public static void upgradeSavegame(Player player, WorldContext world, int fileversion) {
		
		if (fileversion <= 12) {
			player.useItemCost = 5;
			player.health.max += 5;
			player.health.current += 5;
			player.baseTraits.maxHP += 5;
		}

		if (fileversion <= 21) {
			int assignedSkillpoints = 0;
			for(SkillInfo skill : world.skills.getAllSkills()) {
				assignedSkillpoints += player.getSkillLevel(skill.id);
			}
			player.availableSkillIncreases = getExpectedNumberOfSkillpointsForLevel(player.getLevel()) - assignedSkillpoints;
		}
		
		if (fileversion <= 21) {
			if (player.hasExactQuestProgress("prim_hunt", 240)) player.addQuestProgress(new QuestProgress("bwm_agent", 250));
			if (player.hasExactQuestProgress("bwm_agent", 240)) player.addQuestProgress(new QuestProgress("prim_hunt", 250));
		}
		
		if (fileversion <= 27) {
			correctActorConditionsFromItemsPre0611b1(player, "bless", world, "elytharan_redeemer");
			correctActorConditionsFromItemsPre0611b1(player, "blackwater_misery", world, "bwm_dagger");
			correctActorConditionsFromItemsPre0611b1(player, "regen", world, "ring_shadow0");
		}
		
		if (fileversion <= 30) {
			player.baseTraits.attackCost = Player.DEFAULT_PLAYER_ATTACKCOST;
		}
	}
	
	public static int getExpectedNumberOfSkillpointsForLevel(int level) {
		level -= Constants.FIRST_SKILL_POINT_IS_GIVEN_AT_LEVEL;
		if (level < 0) return 0;
		return 1 + (int) FloatMath.floor((float) level / Constants.NEW_SKILL_POINT_EVERY_N_LEVELS);
	}

	private static void correctActorConditionsFromItemsPre0611b1(Player player, String conditionTypeID, WorldContext world, String itemTypeIDWithCondition) {
		if (!player.hasCondition(conditionTypeID)) return;
		boolean hasItemWithCondition = false;
		for (ItemType t : player.inventory.wear) {
			if (t == null) continue;
			if (t.effects_equip == null) continue;
			if (t.effects_equip.addedConditions == null) continue;
			for(ActorConditionEffect e : t.effects_equip.addedConditions) {
				if (!e.conditionType.conditionTypeID.equals(conditionTypeID)) continue;
				hasItemWithCondition = true;
				break;
			}
		}
		if (hasItemWithCondition) return;
		
		ActorStatsController.removeConditionsFromUnequippedItem(player, world.itemTypes.getItemType(itemTypeIDWithCondition));
	}

	public static void readCombatTraitsPreV034(DataInputStream src, int fileversion) throws IOException {
		if (fileversion < 25) return;
		if (!src.readBoolean()) return;
		
		/*attackCost = */src.readInt();
		/*attackChance = */src.readInt();
		/*criticalSkill = */src.readInt();
		if (fileversion <= 20) {
			/*criticalMultiplier = */src.readInt();
		} else {
			/*criticalMultiplier = */src.readFloat();
		}
		/*damagePotential = */new Range(src, fileversion);
		/*blockChance = */src.readInt();
		/*damageResistance = */src.readInt();
	}
}
