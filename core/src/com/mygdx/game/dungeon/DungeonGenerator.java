package com.mygdx.game.dungeon;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.utility.Factory;
import com.mygdx.game.utility.RandomInt;

import java.util.ArrayList;


/**
 * Created by KS on 4/23/2015.
 */
public class DungeonGenerator {


    public static int mapSize = 64;
    static Vector2 center;
    public static int[][] map;

    public static Room[] rooms;
    public static int numRooms = 10;
    public static int roomSizeMin = 5;
    public static int roomSizeMax = 15;

    public static int squashTimes = 1;

    public static void generateDungeon(GameScreen gameScreen)
    {
        center = new Vector2(mapSize/2, mapSize/2);
        map = new int[mapSize][mapSize];
        rooms = new Room[numRooms];

        //Random rand = new Random();
        for (int i = 0; i < numRooms; i++)
        {
            rooms[i] = new Room();
        }
        initialize();
        squash(squashTimes);
        createMap();
        connectAllRooms();
        addWalls();
        createTiles(gameScreen.pooledEngine);
    }

    //spawns rooms randomly
    //"bogo randomize" - try until it works (can loop infinitely)
    static void initialize()
    {
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                map[x][y] = 0;
            }
        }

        //add new rooms to rooms array
        int remakes = 0;
        for (int i = 0; i < numRooms; i++) {
            Room newRoom = new Room();
            newRoom.index = i;
            newRoom.width = RandomInt.Range(roomSizeMin, roomSizeMax + 1);
            newRoom.height = RandomInt.Range(roomSizeMin, roomSizeMax + 1);

            newRoom.x = RandomInt.Range(1 + newRoom.width, mapSize - newRoom.width - 1);
            newRoom.y = RandomInt.Range(1 + newRoom.height, mapSize - newRoom.height - 1);
            newRoom.center = new Vector2(newRoom.x + (newRoom.width)/2, newRoom.y + (newRoom.height)/2);
            newRoom.connected = false;

            if (doesCollide(newRoom) && remakes < 10000) { //dont loop forever
                //remake the room
                i--;
                remakes++;
                continue;
            }

            rooms[i] = newRoom;

        }
    }

    //collides with anything
    static boolean doesCollide(Room room) {
        return doesCollide(room, -1);
    }
    static boolean doesCollide(Room room, int ignore) {
        for (int i = 0; i < numRooms; i++) {
            if (i == ignore) {
                continue;
            }
            Room check = rooms[i];
            //de morgan's law
            //-1 +1 -1 +1
            if (!((room.x + room.width + 1< check.x ) || (room.x - 1> check.x + check.width ) || (room.y + room.height < check.y ) || (room.y > check.y + check.height))) {
                return true;
            }
        }
        return false;
    }

    static boolean doesCollideY(Room room)
    {
        return doesCollideY(room, -1);
    }


    static boolean doesCollideY(Room room, int ignore)
    {
        for (int i = 0; i < numRooms; i++)
        {
            if (i == ignore)
            {
                continue;
            }
            Room check = rooms[i];
            //de morgan's law
            //theres no "wall" room in y check
            if (!((room.x + room.width + 1< check.x ) || (room.x - 1> check.x + check.width ) || (room.y + room.height - 1 < check.y) || (room.y + 1 > check.y + check.height)))
            {
                return true;
            }
        }
        return false;
    }

    static void squash(int times)
    {
        for (int j = 0; j < times; j++) {
            for (int i = 0; i < rooms.length; i++) { //for each room
                Room curRoom = rooms[i];
                Vector2 oldPos = new Vector2(curRoom.x, curRoom.y);
                int maxMoves = 5; //only move this amount maximum
                int moveX = 0;
                int moveY = 0;
                //if x causes collision, y doesnt move
                if (curRoom.x + curRoom.width/2 > center.x) {
                    //move to left
                    while (moveX < maxMoves)
                    {
                        //move
                        curRoom.move(-1, 0);
                        moveX++;
                        //if collide, move back one
                        if (doesCollideY(curRoom, i) || curRoom.x + curRoom.width / 2 < center.x)
                        {
                            curRoom.move(1,0);
                            break;
                        }

                    }
                    /*
					while (!doesCollide(curRoom, i) && curRoom.x + curRoom.width/2 > center.x) {
						//print ("room#: " + i + " moving left: " + curRoom.x);
						curRoom.x--;
					}*/
                }
                else if (curRoom.x + curRoom.width/2 < center.x) {
                    //move to right
                    while (moveX<maxMoves)
                    {
                        //move
                        curRoom.move(1,0);
                        moveX++;
                        //if collide, move back one
                        if (doesCollideY(curRoom, i) || curRoom.x + curRoom.width / 2 > center.x)
                        {
                            curRoom.move(-1, 0);
                            break;
                        }
                    }
                    /*
					while (!doesCollide(curRoom, i) && curRoom.x + curRoom.width/2 < center.x) {
						curRoom.x++;
					}*/
                }

                //y
                if (curRoom.y + curRoom.height/2 > center.y) {
                    //move down
                    while (moveY < maxMoves)
                    {
                        //move
                        curRoom.move(0, -1);
                        moveY++;
                        //if collide, move back one
                        if (doesCollide(curRoom, i) || curRoom.y + curRoom.height / 2 < center.y)
                        {
                            curRoom.move(0, 1);
                            break;
                        }

                    }
                    /*
					while (!doesCollideY(curRoom, i) && curRoom.y + curRoom.height/2 > center.y) {
						curRoom.y--;
					}*/
                }
                else if (curRoom.y + curRoom.height/2 < center.y) {
                    //move up
                    while (moveY < maxMoves)
                    {
                        //move
                        curRoom.move(0, 1);
                        moveY++;
                        //if collide, move back one
                        if (doesCollide(curRoom, i) || curRoom.y + curRoom.height / 2 > center.y)
                        {
                            curRoom.move(0, -1);
                            break;
                        }
                    }
                    /*
					while (!doesCollideY(curRoom, i) && curRoom.y + curRoom.height/2 < center.y) {
						curRoom.y++;
					}*/
                }

                curRoom.center = new Vector2(curRoom.x + (curRoom.width) / 2, curRoom.y + (curRoom.height) / 2);
                rooms[i] = curRoom;
            }
        }
    }

    static void createMap()
    {
        for (int i = 0; i < numRooms; i++) //for each room
        {
            Room currentRoom = rooms[i];
            currentRoom.updateCenter();

            //create empty game object room to put tiles in
            //GameObject room = new GameObject("room " + i);
            //room.transform.position = currentRoom.center;
            //room.transform.parent = transform;

            //currentRoom.roomObject = room;

            for (int x = currentRoom.x; x < currentRoom.x + currentRoom.width; x++)
            {
                for (int y = currentRoom.y; y < currentRoom.y + currentRoom.height; y++)
                {
                    //add tiles to empty game object room i
                    //TEST: REMOVE != 1


                    map[x][y] = 1;
                    //GameObject tile = (GameObject)Instantiate(ground, new Vector3(x, y, 5), Quaternion.identity);
                    //tile.transform.parent = room.transform;

                }
            }
        }
    }

    static void connectAllRooms()
    {
        rooms[0].connected = true; //set first room's connected to true

        for (int i = 0; i < rooms.length; i++) //for each room
        {
            int numConnects = RandomInt.Range(1, 3); //# of connects (1 or 2)

            for (int j = 0; j < numConnects; j++) //for each neighbor
            {
                connectRooms( rooms[i], findNeighbors(rooms[i]).get(j) ); //connect 1 or 2 neighbors
            }

            //if still not connected
            if (!rooms[i].connected) //connect to closest room
            {
                connectRooms(rooms[i], findNeighbors(rooms[i]).get(numConnects));
            }

        }
    }
    static void connectRooms(Room a, Room b)
    {
        //2 random points in 2 different rooms, move them towards each other
        boolean thick = false;

        Vector2 pointA = new Vector2(RandomInt.Range(a.x, a.x + a.width + 1), RandomInt.Range(a.y, a.y + a.height + 1));
        Vector2 pointB = new Vector2(RandomInt.Range(b.x, b.x + b.width + 1), RandomInt.Range(b.y, b.y + b.height + 1));
        Vector2 pointC = new Vector2(1 + pointB.x, pointB.y);
        //thicker hallway
        if (RandomInt.Range(0, 10) >= 8)
        {
            thick = true;
        }

        while ((pointB.x != pointA.x) || (pointB.y != pointA.y))
        {
            if (pointB.x != pointA.x)
            {
                if (pointB.x > pointA.x)
                {
                    pointB.x--;
                    if (thick)
                    {
                        pointC.x--;
                    }
                }
                else
                {
                    pointB.x++;
                    if (thick)
                    {
                        pointC.x++;
                    }
                }

            }
            else if (pointB.y != pointA.y)
            {
                if (pointB.y > pointA.y)
                {
                    pointB.y--;
                    if (thick)
                    {
                        pointC.y--;
                    }
                }
                else
                {
                    pointB.y++;
                    if (thick)
                    {
                        pointC.y++;
                    }
                }
            }
            if (pointB.x != 0 && pointB.y != 0)
            {
                //map[(int)pointB.x, (int)pointB.y] = 1;
            }

            //add floor tiles for hallways if it isnt already a floor tile
            //##CONFLICT ADDING HALLS
            if (map[(int)pointB.x][(int)pointB.y] != 1)
            {
                map[(int)pointB.x][(int)pointB.y] = 1;
                //GameObject tile = (GameObject)Instantiate(ground, new Vector3((int)pointB.x, (int)pointB.y, 5), Quaternion.identity);
                //tile.transform.parent = transform;
                if (thick)
                {
                    if (map[(int)pointC.x][(int)pointC.y] != 1)
                    {
                        map[(int)pointC.x][ (int)pointC.y] = 1;
                        //GameObject tile2 = (GameObject)Instantiate(ground, new Vector3((int)pointC.x, (int)pointC.y, 5), Quaternion.identity);
                        //tile2.transform.parent = transform;
                    }
                }

            }
        }

        if (a.connected || b.connected)
        {
            a.connected = true;
            b.connected = true;
        }
    }

    static ArrayList<Room> findNeighbors(Room room)
    {
        ArrayList<Room> neighbors = new ArrayList<Room>();
        //= new List<Room>();

        Vector2 mid = new Vector2(room.x + room.width / 2, room.y + room.height / 2);

        int[] closest_dist = {1000, 1000, 1000, 1000} ;

        //loops thru each room and puts in neighbor list if close enough
        for (int i = 0; i < rooms.length; i++)
        {
            Room check = rooms[i];
            if (check.index == room.index)
            {
                continue;
            }

            Vector2 check_mid = new Vector2(check.x + check.width / 2, check.y + check.height / 2);
            int distance = (int)(Math.abs(mid.x - check_mid.x) + Math.abs(mid.y - check_mid.y));

            if (distance < closest_dist[0])
            {
                neighbors.add(0, rooms[i]);
                closest_dist[0] = distance;
            }
            else if (distance < closest_dist[1])
            {
                neighbors.add(1, rooms[i]);
                closest_dist[1] = distance;
            }
            else if (distance < closest_dist[2])
            {
                neighbors.add(2, rooms[i]);
                closest_dist[2] = distance;
            }
            else if (distance < closest_dist[3])
            {
                neighbors.add(3, rooms[i]);
                closest_dist[3] = distance;
            }
        }


        return neighbors;
    }

    static void addWalls()
    {
        //draw wall tiles around rooms by looping through each tile of map and seeing if it's ground
        //if it is, surround with walls
        //can instantiate here and add to room gameobject if want to
        for (int x = 1; x < mapSize; x++)
        {
            for (int y = 1; y < mapSize; y++)
            {
                if (map[x][y] == 1)
                {
                    for (int xx = x - 1; xx <= x + 1; xx++)
                    {
                        for (int yy = y - 1; yy <= y + 1; yy++)
                        {
                            if (map[xx][yy] == 0 && map[xx][yy] != 3)
                            {
                                map[xx][yy] = 2;

                            }
                        }
                    }
                }
            }
        }
    }

    static void createTiles(PooledEngine pooledEngine)
    {
        for (int x = 0; x < mapSize; x++)
        {
            for (int y = 0; y < mapSize; y++)
            {
                //ground
                if (map[x][y] == 1)
                {
                    //doing it above, dont need to do it again
                    //GameObject tile = (GameObject)Instantiate(ground, new Vector3(x, y, 5), Quaternion.identity);
                    //tile.transform.parent = transform;
                    Entity e = pooledEngine.createEntity();
                    PositionComponent p = new PositionComponent(x, y);
                    e.add(p);
                    TextureRegion t = new TextureRegion(Factory.sandTiles, 0, 0, 32, 32);
                    e.add(new VisualComponent(t));
                    pooledEngine.addEntity(e);
                }
                //wall
                else if (map[x][y] == 2)
                {
                    //GameObject tile = (GameObject)Instantiate(wall, new Vector3(x, y, 5), Quaternion.identity);
                    //tile.transform.parent = transform;
                }

                else if (map[x][y] == 3)
                {
                    //GameObject tile = (GameObject)Instantiate(pink, new Vector3(x, y, 1), Quaternion.identity);
                    //tile.transform.parent = transform;
                }
            }
        }
    }


}
