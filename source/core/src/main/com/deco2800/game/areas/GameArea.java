package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(GameArea.class);

    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;
    protected Entity player;
    protected int numEnemy = 0;
    protected int numBoss = 0;
    protected Map map;
    protected static final float WALL_WIDTH = 0.1f;
    protected static String[] tileTextures = null;
    protected static final String[] textures = {
            "images/tree.png",
            "images/trap.png",
            "images/test.png",
            "images/arrow_normal.png",
            "images/crown.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/mud.png",
            "images/player.png",
            "images/player_axe.png",
            "images/player_hammer.png",
            "images/player_scepter.png",
            "images/player_longsword.png",
            "images/blast.png",
            "images/hammer_projectile",
            "images/health_left.png",
            "images/health_middle.png",
            "images/health_right.png",
            "images/health_frame_left.png",
            "images/health_frame_middle.png",
            "images/health_frame_right.png",
            "images/hp_icon.png",
            "images/dash_icon.png",
            "images/prisoner.png",
            "images/rock.png",
            "images/enemy_health_bar.png",
            "images/enemy_health_border.png",
            "images/enemy_health_bar_decrease.png",
            "images/vortex.png",
            "images/aiming_line.png",
            "images/bossAttack.png",
            "images/meleeElf.png",
            "images/guardElf.png",
            "images/rangedElf.png",
            "images/fireball/fireballAnimation.png",
            "images/rangedFixed.png",
            "images/bossFixed.png",
            "images/meleeAnimationsTextured.png",
            "images/meleeFinal.png",
            "images/assassinFinal.png",
            "images/guardFinal.png",
            "images/rangedAllFinal.png",
            "images/bossFinal.png",
            "player_scepter.png",
            "player_hammer.png",
            "player_axe.png",
            "portal.png",
            "Odin/odin.png",
            "Assets/gametile-127.png",
            "images/boss_health_middle.png",
            "images/boss_health_left.png",
            "images/boss_health_right.png",
            "images/viking.png",
            "images/explosion/explosion.png",
            "images/hellViking.png",
            "images/outdoorArcher.png",
            "images/asgardWarrior.png",
            "images/lokiBoss.png",
            "thor/aoe_attck.png",
            "thor/up_attck.png",
            "thor/down_attck.png",
            "thor/left_attck.png",
            "thor/right_attck.png",
            "thor/walk_left.png",
            "thor/walk_right.png",
            "images/firePillar.png",
            "healthRegen/healthPotion_placeholder.png",
            "crate/crateHitBreak.png"
    };
    protected static final String[] textureAtlases = {
            "images/outdoorArcher.atlas", "images/terrain_iso_grass.atlas", "crate/crateHitBreak.atlas", "images/elf.atlas",
            "images/player.atlas", "images/bossAttack.atlas", "images/meleeElf.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAnimation.atlas",
            "end/portal.atlas", "Odin/odin.atlas", "images/player_scepter.atlas", "images/player_hammer.atlas",
            "images/player_longsword.atlas", "images/hammer_projectile.atlas", "images/outdoorWarrior.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAnimation.atlas",
            "images/player_scepter.atlas", "images/player_hammer.atlas", "images/newArrowBroken/atlas/arrow.atlas",
            "images/viking.atlas", "images/meleeAnimationsTextured.atlas",
            "images/meleeFinal.atlas", "images/assassinFinal.atlas", "images/guardFinal.atlas", "images/rangedAllFinal.atlas", "images/bossFinal.atlas",
            "images/explosion/explosion.atlas", "images/hellViking.atlas", "images/outdoorArcher.atlas", "images/asgardWarrior.atlas",
            "images/lokiBoss.atlas", "thor/thor.atlas", "images/firePillar.atlas"
    };
    protected static final String[] sounds = {
            "sounds/Impact4.ogg", "sounds/impact.ogg", "sounds/swish.ogg",
            "sounds/arrow_disappear.mp3",
            "sounds/arrow_shoot.mp3",
            "sounds/death_2.mp3",
            "sounds/death_1.mp3",
            "sounds/boss_death.mp3"
    };
    protected static final String backgroundMusic = "sounds/RAGNAROK_MAIN_SONG_76bpm.mp3";

    protected TerrainFactory terrainFactory = null;
    protected int playerHealth = 300;

    protected GameArea() {
        areaEntities = new ArrayList<>();
    }

    /**
     * Create the game area in the world.
     */
    public void create() {
        ServiceLocator.registerGameArea(this);
    }

    /**
     * Use for teleport, track the current map player in
     */
    public int getLevel() {
        return 0;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * increase number of enemy on the map (keep track) - when the enemy is create and spawn
     */
    public void incNum() {
        numEnemy++;
    }

    /**
     * decrease number of enemy on the map (keep track) - when the enemy died
     */
    public void decNum() {
        numEnemy--;
    }

    /**
     * increase the number of boss
     */
    public void incBossNum() {
        numBoss++;
    }

    /**
     * decrease number of boss - spawn the teleport portal to another map
     */
    public void decBossNum() {
        numBoss--;
        System.out.println(numBoss);
        if (numBoss == 0) {
            logger.info("Number of Bosses is now at 0");
            if (getLevel() == 9) {
                //tutorial level
                logger.info("Spawning the teleport object");
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float> teleportPos = map.getTeleportObjects()[0];
                GridPoint2 fixedPos = new GridPoint2(teleportPos.get("x").intValue(),
                        (map.getDimensions().get("n_tiles_heihgt") - teleportPos.get("y").intValue()));
                this.spawnEntityAt(teleport, fixedPos, true, true);
            } else if (getLevel() == 0) {
                //gama area 0
                logger.info("Spawning the teleport object");
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                        (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 2));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 1) {
                //gama area 1
                logger.info("Spawning the teleport object");
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                        (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 2));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 2) {
                //gama area 2
                logger.info("Spawning the teleport object");
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                        (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 2));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 3) {
                //gama area 3
                logger.info("Spawning the teleport object");
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                        (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 2));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 4) {
                //gama area 4
                logger.info("Spawning the teleport object");
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                        (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 2));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            }

        }
    }

    /**
     * get the number of enemy on the map
     *
     * @return int number of enemy
     */
    public int getNumEnemy() {
        return numEnemy;
    }

    /**
     * Returns the player entity that is created.
     *
     * @return player entity - main character being controlled
     */
    public Entity getPlayer() {
        return player;
    }


    /**
     * Spawn entity at its current position
     *
     * @param entity Entity (not yet registered)
     */
    protected void spawnEntity(Entity entity) {
        areaEntities.add(entity);
        ServiceLocator.getEntityService().register(entity);
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity  Entity (not yet registered)
     * @param tilePos tile position to spawn at
     * @param centerX true to center entity X on the tile, false to align the bottom left corner
     * @param centerY true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
        float tileSize = terrain.getTileSize();

        if (centerX) {
            worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(worldPos);
        spawnEntity(entity);
    }

    /**
     * Spawns a create object which will reveal a health potion at the positions specified
     * within the Tiled JSON file.
     */
    protected void spawnHealthCrateObject() {
        HashMap<String, Float>[] crates = map.getHealthCrateObjects();
        if (crates == null) {
            return;
        }
        for (HashMap<String, Float> crate : crates) {
            int x = crate.get("x").intValue();
            int y = crate.get("y").intValue();

            spawnEntityAt(
                    ObstacleFactory.createHealthCrate(),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity    Entity (not yet registered)
     * @param entityPos world position to spawn at
     * @param centerX   true to center entity X on the tile, false to align the bottom left corner
     * @param centerY   true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, Vector2 entityPos, boolean centerX, boolean centerY) {
        float tileSize = terrain.getTileSize();

        if (centerX) {
            entityPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            entityPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(entityPos);
        spawnEntity(entity);
    }

    protected void spawnHellWarriorObject() {
        HashMap<String, Float>[] warriors = map.getHellMeleeObjects();
        for (HashMap<String, Float> warrior : warriors) {
            int x = warrior.get("x").intValue();
            int y = warrior.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createMeleeHellViking(player),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
    }

    protected void spawnAsgardWarriorObject() {
        HashMap<String, Float>[] crates = map.getAsgardMeleeObjects();
        for (HashMap<String, Float> crate : crates) {
            int x = crate.get("x").intValue();
            int y = crate.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createMeleeAsgardViking(player),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
    }

    protected void spawnOutdoorWarriorObject() {
        HashMap<String, Float>[] warriors = map.getOutdoorMeleeObjects();
        for (HashMap<String, Float> warrior : warriors) {
            int x = warrior.get("x").intValue();
            int y = warrior.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createMeleeViking(player),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
    }

    protected void spawnOutdoorArcherObject() {
        HashMap<String, Float>[] crates = map.getRangeObjects();
        for (HashMap<String, Float> crate : crates) {
            int x = crate.get("x").intValue();
            int y = crate.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createOutdoorArcher(player),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
    }

    protected void spawnMovementCutscenes() {
        HashMap<String, Float>[] lefts = map.getMoveLeftObjects();
        if (lefts != null) {
            for (HashMap<String, Float> left : lefts) {
                int x = left.get("x").intValue();
                int y = left.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createLeftMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }

        HashMap<String, Float>[] rights = map.getMoveRightObjects();
        if (rights != null) {
            for (HashMap<String, Float> right : rights) {
                int x = right.get("x").intValue();
                int y = right.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createRightMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }

        HashMap<String, Float>[] downs = map.getMoveDownObjects();
        if (downs != null) {
            for (HashMap<String, Float> down : downs) {
                int x = down.get("x").intValue();
                int y = down.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createDownMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }

        HashMap<String, Float>[] ups = map.getMoveUpObjects();
        if (ups != null) {
            for (HashMap<String, Float> up : ups) {
                int x = up.get("x").intValue();
                int y = up.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createUpMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Load the texture from files
     */
    protected void loadAssets() {

        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(textures);
        resourceService.loadTextures(tileTextures);
        resourceService.loadTextureAtlases(textureAtlases);
        resourceService.loadSounds(sounds);
        resourceService.loadMusic(new String[] {backgroundMusic});
        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Unload the assets (include image and sound)
     */
    protected void unloadAssets() {
        logger.debug("Unloading assets");
        if (ServiceLocator.getResourceService() != null) {
            ResourceService resourceService = ServiceLocator.getResourceService();
            resourceService.unloadAssets(textures);
            resourceService.unloadAssets(tileTextures);
            resourceService.unloadAssets(textureAtlases);
            resourceService.unloadAssets(sounds);
            resourceService.unloadAssets(new String[]{backgroundMusic});
        }
    }

    public void dispose() {
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
        if (ServiceLocator.getResourceService() != null
                && ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class) != null) {
            ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        }
        this.unloadAssets();
    }

    protected void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

    }
}
