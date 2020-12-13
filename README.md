
***Authors:** Matan Ben Nagar & Reut Maslansky*


## **About this project**
This project represents a game that is based on the implementation of an directional weighted graph with different methods and algorithms.

## **Pokemons Game**
In this game the goal is to collect as many pokemons as possible through one or more of your pokeballs.<br />
The game has few different scenarios, based on different graphs with different time limits, in various levels.<br />
The pokemons remain static in their places and have different values. one pokemon can be more valuable than the other.

There are two types of pokemons:
- Picachua <a href="https://imgbb.com/"><img src="https://i.ibb.co/YLQJhPn/pok.png" alt="pok" border="0"></a>
- Balbazor  <a href="https://imgbb.com/"><img src="https://i.ibb.co/zQS7Hd0/balb.png" alt="balb" border="0"></a>

The pokeballs are our players and the more they catch pokemons the more value they gain.
They can also increase their speed if they catch enough pokemons.
<a href="https://imgbb.com/"><img src="https://i.ibb.co/HxbVLvs/pu.png" alt="pu" border="0"></a> <a href="https://imgbb.com/"><img src="https://i.ibb.co/Z1Y8RLb/re.png" alt="re" border="0"></a><a href="https://imgbb.com/"> <img src="https://i.ibb.co/CQ5qMv4/yel.png" alt="yel" border="0"></a>

Our game is connected to a server that provides the arenas as a JSON file converted into a string.

## Our Packages & Classes:

**api:**
* directed weighted graph- this is the data structure that base this game on.
* directed weighted graph algorithms- these are the main functions used to create the arena (load, save, init) and also responsible for finding the path from each pokeball to a pokemon
* Ex2- In order to start playing the game you need to run this class, which is responsible for the initial of the arena, pokemons and pokeballs.


**GameClient**
* util- This folder contains the classes which are responsible for the game's graphics (Using Jframe & JPanel).
* Arena- The arena is based on a directed weighted graph and includes the pokaballs and the pokemons.
* CL_Pokemon- Class representing the pokemon object.
* CL_Agent- Class representing the agent object.

**Data:**
* In this folder you will find the graphs files and the elements images for the graphical presentation.

### *A screenshot of the game play:*
<a href="https://imgbb.com/"><img src="https://i.ibb.co/fvFfNFq/Screenshot-2020-12-13-212540.jpg" alt="Screenshot-2020-12-13-212540" border="0"></a>
<a href="https://ibb.co/M9HpDxn"><img src="https://i.ibb.co/Fn1JbL6/Screenshot-2020-12-13-114057.jpg" alt="Screenshot-2020-12-13-114057" border="0"></a>

## **Main algoritms to construct the game:**
- ***shortestPathDist-*** return the length of the shortest path between source vertex to the target vertex.
- ***shortestPath-*** return the shortest path between source vertex to the target vertex- as an ordered list of nodes:
*source--> n1--> n2--> ... -->target*

## **Dijkstra's algorithm**
In the development of the algorithm of shortestPath in the weighted graph, we based on Dijkstra's algorithm,<br />
in order to find the quickest path from a pokeball to a pokemon.

More about- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

## **For clone down this repository**
Write in your Git Bash the follow:

```
$ git clone https://github.com/Reut-Maslansky/ex2.git
```

מסך כניסה
בחירה של משתמש איזה משחק להריץ
jar
אלגוריתם של המשחק- סוכן אחד הולך לפוקימון ולא כמה

