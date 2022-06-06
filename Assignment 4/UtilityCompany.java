import java.io.*;
import java.util.*;
public class UtilityCompany
{
	public static void main (String [] args) throws IOException
	{
		Scanner input = new Scanner (new FileReader (new File ("input.txt")));
		Graph graph = new Graph (input.nextInt());
		
		while (input.hasNextLine())
		{
			int start = input.nextInt();
			int end = input.nextInt();
			int weight = input.nextInt();
			graph.addEdge(new Edge(start, end, weight));
		}

		runPrims(7, 14, 25, 28, 40, graph);
		runPrims(14, 7, 25, 28, 40, graph);
		runPrims(25, 7, 14, 28, 40, graph);
		runPrims(28, 7, 14, 25, 40, graph);
		runPrims(40, 7, 14, 25, 28, graph);
	}
	
	public static void runPrims (int station, int delete1, int delete2, int delete3, int delete4, Graph graph)
	{
		ArrayList <Integer> stations = new ArrayList <Integer> ();
		
		stations.add(station);
		
		ArrayList <Integer> delete = new ArrayList <Integer> ();
		
		delete.add(delete1);
		delete.add(delete2);
		delete.add(delete3);
		delete.add(delete4);
		
		Prims answer = new Prims (graph, stations, delete);
		System.out.println("If " + station + " is the only station:");
		System.out.println("The minimum cable length needed to power all the customers is " + answer.totalCableLength() + " units. \n");
	}
}

class Prims
{
	public boolean [] visited;
	public ArrayList <Edge> minimumSpanningTree;
	public PriorityQueue <Edge> nextEdge;

	public Prims (Graph graph, ArrayList <Integer> stations, ArrayList <Integer> delete)
	{
		visited = new boolean [graph.numberVertices];
		minimumSpanningTree = new ArrayList <Edge> ();
		nextEdge = new PriorityQueue <Edge> ();

		for (int node : delete)
		{
			graph.removeNode(node);
		}
		
		for (int start = 0; start < stations.size(); start++)
		{
			for (int end = start + 1; end < stations.size(); end++)
			{
				graph.addEdge(new Edge(start, end, 0));
			}
		}

		visit (graph, 0);

		while (nextEdge.isEmpty() == false)
		{
			Edge next = nextEdge.poll();
			int one = next.one;
			int two = next.two;

			if (visited[one] && visited[two])
			{
				continue;
			}

			minimumSpanningTree.add(next);

			if (visited[one])
			{
				visit (graph, two);
			}

			else
			{
				visit (graph, one);
			}
		}
	}

	public void visit (Graph graph, int start)
	{
		visited[start] = true;
		for (Edge edge : graph.edges.get(start))
		{
			if (!visited[edge.other(start)])
			{
				nextEdge.add(edge);
			}
		}
	}

	public int totalCableLength ()
	{
		int totalCableLength = 0;
		for (int index = 0; index < minimumSpanningTree.size(); index++)
		{
			totalCableLength += minimumSpanningTree.get(index).weight;
		}
		return totalCableLength;
	}

	public void printMinimumSpanningTree ()
	{
		for (Edge edge : minimumSpanningTree)
		{
			System.out.println(edge);
		}
	}
}

class Graph
{
	public int numberVertices;
	public int numberEdges;

	public ArrayList <ArrayList<Edge>> edges;

	public Graph (int numberVertices)
	{
		this.numberVertices = numberVertices;
		this.numberEdges = 0;

		edges = new ArrayList <ArrayList<Edge>> ();
		for (int index = 0; index < numberVertices; index++)
		{
			edges.add(new ArrayList <Edge> ());
		}
	}

	public void addEdge (Edge edge)
	{
		int one = edge.one;
		int two = edge.two;
		edges.get(one).add(edge);
		edges.get(two).add(edge);
		numberEdges++;
	}
	
	public void removeNode (int node) // If you want to remove seven from the graph
	{
		for (Edge connected : edges.get(node))
		{
			int other = connected.other(node); // Go to every node seven is directly linked to
			
			Edge find = null;
			
			for (Edge next : edges.get(other))
			{
				if (next.other(other) == node)
				{
					find = next;
					break;
				}
			}
			
			edges.get(other).remove(find);
		}
		edges.get(node).clear(); // Clear the adjacency list of seven
	}
}

class Edge implements Comparable <Edge>
{
	public int one;
	public int two;
	public int weight;

	public Edge (int one, int two, int weight)
	{
		this.one = one;
		this.two = two;
		this.weight = weight;
	}

	public int other (int vertex)
	{
		if (one == vertex)
		{
			return two;
		}
		else
		{
			return one;
		}
	}

	public int compareTo (Edge other)
	{
		if (this.weight < other.weight)
		{
			return -1;
		}
		else if (this.weight > other.weight)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public String toString ()
	{
		return one + " - " + two;
	}
}
