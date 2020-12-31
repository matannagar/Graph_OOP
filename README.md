# **Directed Weighted Graph**

## **About this project**
This project represents an implementation of an directed weighted graph with different methods and algorithms using Python.
The graph is based on nodes and directed edges, enables printing the graph using plotlibrary, an implementation of Dijkstra's algorithm and BFS algorithm.

## **Main algoritms:**
- ***ConnectedComponents-*** Finds the Strongly Connected Components(SCC) of the graph. 
- ***shortestPath-*** return the length of the shortest path between source vertex to the target vertex and also a Node list: src-->...-->dest
- ***save-*** saves this weighted graph to the given file name using json method.
- ***load-*** load a graph from the given file name to to a graph on which we'll operates the algorithms above.
- ***getGraph-*** returns the graph on which we operate the algorithms

## **Dijkstra's algorithm**
In the development of the algorithm of shortestPath in the weighted graph, we based on Dijkstra's algorithm,<br />
in order to find the quickest path from a source node to a dest node.
Dijkstra's Shortest Path First algorithm is an algorithm for finding the shortest paths between nodes in a graph, which may represent, for example, road networks. 
It was conceived by computer scientist Edsger W. Dijkstra in 1956 and published three years later.

Let the node at which we are starting be called the initial node. Let the distance of node Y be the distance from the initial node to Y. Dijkstra's algorithm will assign some initial distance values and will try to improve them step by step.

1.Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
2.Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes. Set the initial node as current.[16]
3.For the current node, consider all of its unvisited neighbours and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one. For example, if the current node A is marked with a distance of 6, and the edge connecting it with a neighbour B has length 2, then the distance to B through A will be 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise, the current value will be kept.
4.When we are done considering all of the unvisited neighbours of the current node, mark the current node as visited and remove it from the unvisited set. A visited node will never be checked again.
5.If the destination node has been marked visited (when planning a route between two specific nodes) or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes), then stop. The algorithm has finished.
6.Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as the new "current node", and go back to step 3.
When planning a route, it is actually not necessary to wait until the destination node is "visited" as above: the algorithm can stop once the destination node has the smallest tentative distance among all "unvisited" nodes (and thus could be selected as the next "current")<br />
<a href="https://ibb.co/Brw6Psk"><img src="https://i.ibb.co/LJvgPpK/dijkstra1.png" alt="dijkstra1" border="0"></a><br />

More about- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

## **To clone this repository**
Write in your Git Bash the follow:

```
$ git clone https://github.com/Reut-Maslansky/ex3.git
```
### *Example of Weighted Graph:*
![Example of Weighted Graph](https://media.geeksforgeeks.org/wp-content/uploads/graphhh.png)
