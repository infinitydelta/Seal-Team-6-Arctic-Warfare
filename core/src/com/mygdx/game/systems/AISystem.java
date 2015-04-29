package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.GameScreen;
import com.mygdx.game.components.*;
import com.mygdx.game.utility.ComparableVector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.utility.Factory;
import com.mygdx.game.utility.RandomInt;

import java.security.spec.DSAGenParameterSpec;
import java.util.*;

/**
 * Created by McLean on 4/23/2015.
 */
public class AISystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<AIControllerComponent> aim = ComponentMapper.getFor(AIControllerComponent.class);
    private ComponentMapper<MovementComponent> cm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<VisualComponent> vm = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
    HashMap<ComparableVector2,ComparableVector2> paths;

    float timeElapsed =0;

    ComparableVector2 dir = new ComparableVector2(0,1);
    ComparableVector2 nextPos =  new ComparableVector2(0,1);


    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> bullets;

    public AISystem() {
        super(Family.getFor(AIControllerComponent.class, PositionComponent.class, MovementComponent.class));
        paths = new HashMap<ComparableVector2,ComparableVector2>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AIControllerComponent ai = aim.get(entity);
        PositionComponent position = pm.get(entity);
        MovementComponent collision = cm.get(entity);
        VisualComponent visual = vm.get(entity);
        EnemyComponent enemy = em.get(entity);

        timeElapsed += deltaTime;
        if (timeElapsed > 4) {
            timeElapsed = 0;
            ai.randWalk = !ai.randWalk;
            Random rand = new Random((long) position.y);
            float xr = (rand.nextFloat() * ai.speed * 4) - ai.speed * 2;
            float yr = (rand.nextFloat() * ai.speed * 4) - ai.speed * 2;
            dir.set(xr, yr);
        }

        if (enemy.health>0) {
            for (int i = 0; i < players.size(); i++) {
                float px = players.get(i).getComponent(PositionComponent.class).x;
                float py = players.get(i).getComponent(PositionComponent.class).y;
                ai.xIndex = (int) position.x;
                ai.yIndex = (int) position.y;
                float dx = px - position.x;
                float dy = py - position.y;
                if (dx * dx + dy * dy < 50) {
                    if (players.get(i).getComponent(PlayerComponent.class).weaponComponent != null) {
                        int fear = players.get(i).getComponent(PlayerComponent.class).weaponComponent.currentclip;
                        //System.out.println(players.get(i).getComponent(PlayerComponent.class).weaponComponent.firetimer);
                        if (fear < 10 && players.get(i).getComponent(PlayerComponent.class).weaponComponent.firetimer == 0) {
                            ai.mode = false;
                        }
                    }
                    visual.setAnimation(Factory.seal_walk_anim);
                    ai.xTarIndex = (int) px;
                    ai.yTarIndex = (int) py;


                    if (!ai.mode) {
                        dir.set(ai.speed * dx, ai.speed * dy);
                    } else {
                        dir.set(-ai.speed * dx, -ai.speed * dy);
                    }
                    collision.body.setLinearVelocity(dir);
                } else {
                    if (!ai.randWalk) {
                        collision.body.setLinearVelocity(0, 0);
                        visual.setAnimation(Factory.seal_idle_anim);
                    } else {
                        collision.body.setLinearVelocity(dir);
                        visual.setAnimation(Factory.seal_walk_anim);
                    }
                }
                ai.lastdx = dx;
            }
        }
    }


    void GenPath(ComparableVector2 a, ComparableVector2 b ){
        int dim = DungeonGenerator.mapSize;

        int[][] gVals = new int[dim][dim];

        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                gVals[i][j] = 99999;
            }
        }

        PriorityQueue<Vector3> needsCheck = new PriorityQueue<Vector3>(4, new Comparator<Vector3>() {
            public int compare(Vector3 v1, Vector3 v2) {
                if(v1.x>v2.x){
                    return 1;
                }else if(v1.x<v2.x){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        ArrayList<ComparableVector2> visited = new ArrayList<ComparableVector2>();
        needsCheck.add(new Vector3(getH((int)a.x,(int)a.y,(int)b.x,(int)b.y),a.x,a.y));
        gVals[(int)a.x][(int)a.y] = 0;
        while(!needsCheck.isEmpty()){
            Vector3 curt = needsCheck.poll();
            ComparableVector2 cur = new ComparableVector2(curt.y,curt.z);
        if(cur.equals(b)){
            return;
        }
            needsCheck.remove(curt);
            visited.add(cur);
            PriorityQueue<Vector3> neig = new PriorityQueue<Vector3>(4, new Comparator<Vector3>() {
                public int compare(Vector3 v1, Vector3 v2) {
                    if(v1.x>v2.x){
                        return 1;
                    }else if(v1.x<v2.x){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });
            for(int i=0; i<4; i++){
                int tpx = (int)cur.x;
                int tpy = (int)cur.y;
                switch(i){
                    case 0:
                        tpx++;
                        break;
                    case 1:
                        tpx--;
                        break;
                    case 2:
                        tpy++;
                        break;
                    case 3:
                        tpy--;
                        break;
                }

                if(((tpx>0 && tpx<dim) && (tpy>0 && tpy<dim)) && DungeonGenerator.map[tpx][tpy] != 2 && !visited.contains(new ComparableVector2(tpx,tpy))){
                    int tg = gVals[(int)cur.x][(int)cur.y]+1;
                    boolean found = false;
                    float foundF = 0;
                    for(Vector3 q : needsCheck){
                        if(q.y==tpx && q.z==tpy){
                            found = true;
                            foundF = q.x;
                        }
                    }
                    if(!found || tg<gVals[(int)cur.x][(int)cur.y]+1) {
                        System.out.println(cur+" "+tpx+" "+tpy);
                        paths.put(cur, new ComparableVector2(tpx, tpy));
                        System.out.println("test:" + paths.get(cur));
                        gVals[tpx][tpy] = tg;
                        if(found) {
                            needsCheck.remove(new Vector3(foundF, tpx, tpy));
                        }
                        needsCheck.add(new Vector3(gVals[tpx][tpy] + getH(tpx, tpy, (int) b.x, (int) b.y), tpx, tpy));

                    }
                }
            }

        }
    }

    int getH(int ax, int ay, int bx, int by){
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    public void setPlayers(Engine engine){
        players = engine.getEntitiesFor(Family.getFor(PlayerComponent.class));
    }


}
