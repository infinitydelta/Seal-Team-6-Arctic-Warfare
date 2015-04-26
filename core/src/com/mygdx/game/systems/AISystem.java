package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.*;
import com.mygdx.game.utility.ComparableVector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.utility.Factory;

import java.util.*;

/**
 * Created by McLean on 4/23/2015.
 */
public class AISystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<AIControllerComponent> aim = ComponentMapper.getFor(AIControllerComponent.class);
    private ComponentMapper<MovementComponent> cm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<VisualComponent> vm = ComponentMapper.getFor(VisualComponent.class);
    HashMap<ComparableVector2,ComparableVector2> paths;

    float timeElapsed =0;

    ComparableVector2 dir = new ComparableVector2(0,1);
    ComparableVector2 nextPos =  new ComparableVector2(0,1);


    private ImmutableArray<Entity> players;

    public AISystem() {
        super(Family.getFor(AIControllerComponent.class, PositionComponent.class, MovementComponent.class));
        paths = new HashMap<ComparableVector2,ComparableVector2>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        /*
        if(timeElapsed>4) {
            timeElapsed=0;
            ai.xIndex = (int) position.x;
            ai.yIndex = (int) position.y;
            ai.xTarIndex = (int) players.get(0).getComponent(PositionComponent.class).x;
            ai.yTarIndex = (int) players.get(0).getComponent(PositionComponent.class).y;
            paths.clear();
            GenPath(new ComparableVector2(ai.xIndex, ai.yIndex), new ComparableVector2(ai.xTarIndex, ai.yTarIndex));
            //ComparableVector2 dir = new ComparableVector2();
            nextPos = paths.get(new ComparableVector2(ai.xIndex, ai.yIndex));
        }

        System.out.println(nextPos);
        if(ai.xIndex>nextPos.x){
            dir.set(-1,0);
        }else if(ai.xIndex<nextPos.x){
            dir.set(1,0);
        }else if(ai.yIndex>nextPos.y){
            dir.set(0,-1);
        }else{
            dir.set(0,1);
        }
        */

        AIControllerComponent ai = aim.get(entity);
        PositionComponent position = pm.get(entity);
        MovementComponent collision = cm.get(entity);
        VisualComponent visual = vm.get(entity);

        for(int i=0; i<players.size(); i++){
            float px = players.get(i).getComponent(PositionComponent.class).x;
            float py = players.get(i).getComponent(PositionComponent.class).y;
            ai.xIndex = (int) position.x;
            ai.yIndex = (int) position.y;
            float dx=px-position.x;
            float dy=py-position.y;
            if(dx*dx+dy*dy < 50){
                visual.setAnimation(Factory.seal_walk_anim);
                ai.xTarIndex = (int)px;
                ai.yTarIndex = (int)py;

                    if(dx>0) {
                        if (!visual.sprite.isFlipX()) visual.sprite.flip(true, false);
                    }else{
                        if (visual.sprite.isFlipX()) visual.sprite.flip(true, false);
                    }

                dir.set(ai.speed * dx, ai.speed * dy);
                collision.body.setLinearVelocity(dir);
            }else{
                collision.body.setLinearVelocity(0,0);
                visual.setAnimation(Factory.seal_idle_anim);
            }
            ai.lastdx = dx;
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
