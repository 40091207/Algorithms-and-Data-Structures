import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TSPlidLoad {

	public static void main(String[] args) {
		ArrayList<Point2D> cities = new	ArrayList<Point2D>();
		ArrayList<Point2D> result = new	ArrayList<Point2D>();

		cities = loadTSPLib("C:\\vm1748.tsp");
		result.addAll(cities);

		double distance = routeLength(cities);
		System.out.println(distance);

		long counter=0;
		int runs = 1000;
		for(int i=0;i<runs;i++) {
			cities.clear();
			cities.addAll(result);
			long start = System.nanoTime();
			cities = TSPalgorithm(cities);
			long time = System.nanoTime() - start;
			counter = counter + time;
		}
		System.out.printf("Each iteration took an average of %,d ns%n", counter/(runs));

		distance = routeLength(cities);
		System.out.println(distance);

	}

	public static ArrayList<Point2D> TSPalgorithm(ArrayList<Point2D> cities){
		//cities – a data structure containing all cities
		//result – a list of cities in the order they are to be visited
		//Nearest Neighbour Algorithm
		ArrayList<Point2D> result = new	ArrayList<Point2D>();

		//rand used to create a random start point
		Random rand = new Random();
		int  n = rand.nextInt(cities.size());

		Point2D currentcity = cities.get(n);
		cities.remove(n);

		//Until all cities are removed
		while (cities.size() >0){
			//move the current city to the newly ordered array
			result.add(currentcity);
			//reset distance and the closest city on each loop
			double distance = 999999999;
			Point2D closestcity = null;
			
			//compare the distance to every city remaining in the list not added, add the closest
			for	(Point2D city: cities){
				if(city.distance(currentcity) < distance){
					closestcity = city;
					distance = currentcity.distance(city);
				}
			}
			//remove the city after it has been added to the new ordered array
			cities.remove(closestcity);
			//update the current city
			currentcity = closestcity;
		}

		return result;
	}


	public	static	double	routeLength(ArrayList<Point2D> 	cities){
		//Calculate the length of a TSP route held in 	an ArrayList 	as a set 	of Points
		double 	result=0;
		//Holds the route length
		Point2D prev = 	cities.get(cities.size()-1);
		//Set the previous city to the last city in theArrayList as we need to measure the length of the entire loop
		for	(Point2D city: cities){
			//Go through each city in turn
			result	+=	city.distance(prev);
			//get distance from the previous city
			prev = 	city;
			//current city will be the previous city next time
		}
		return	result	;
	}

	public static ArrayList<Point2D> loadTSPLib(String fName){
		//Load in a TSPLib instance. This example assumes that the Edge weight type is EUC_2D.
		//It will work for examples such as rl5915.tsp	. Other files such as 
		//fri26.tsp .To use a different format, you will have to modify the this code 
		ArrayList<Point2D> result = new	ArrayList<Point2D>();
		BufferedReader br = null;

		try	{
			String 	currentLine	;
			int	dimension =0;//Hold the dimension of the problem
			boolean	readingNodes = false;
			br = new BufferedReader(new	FileReader(fName));
			while((currentLine = br.readLine()) != 	null) {
				//Read the file until the end;
				if	(currentLine.contains("EOF")){
					//EOF should be the last line
					readingNodes = false;
					//Finished reading nodes

					if	(result.size() != 	dimension){
						//Check to see if the expected 	number	of cities 	have been loaded
						System.out.println("Error loading cities");
						System.exit	(-1);
					}
				}

				if	(readingNodes){
					//If reading in the node data
					String[] tokens	= currentLine.split(" ");
					//Split the line by spaces.
					//tokens[0] is the city id 	and not needed in this 	example
					float x = Float.parseFloat(tokens[1].trim());
					float y	= Float.parseFloat(tokens[2].trim());
					//Use Java's built in Point2D type to hold a city
					Point2D city = new Point2D.Float(x,y);
					//Add this city into the arraylist	
					result.add(city);
				}
				if(currentLine.contains("DIMENSION")){
					//Note the expected problem dimension (number of cities)
					String[] tokens	= currentLine.split(":"	);
					dimension = Integer.parseInt(tokens[1].trim());
				}
				if(currentLine.contains("NODE_COORD_SECTION")){
					//Node data follows this line
					readingNodes = 	true;
				}
			}

		} 	catch (IOException e) {	
			e.printStackTrace();
		} 	finally	{	
			try	{	
				if	(br	!= 	null)br.close();
			} 	catch	(IOException 	ex	) {
				ex	.printStackTrace();
			}					
		}

		return	result;

	}
}

