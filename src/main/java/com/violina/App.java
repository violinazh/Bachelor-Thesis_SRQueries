package com.violina;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.Transaction;

import java.text.MessageFormat;

import static org.neo4j.driver.v1.Values.parameters;

/**
 *
 *
 */
public class App implements AutoCloseable
{

    private final Driver driver;

    public App( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    // For tests on the Neo4j server
    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }

    public void findNeighbors( final int distance )
    {
        try ( Session session = driver.session() )
        {

            String query = session.writeTransaction( new TransactionWork<String>()
            {
                // For every c.id as parameter while count(poitype) < 6 iterate over the endpoint distance parameter
                String string = MessageFormat.format("MATCH poipath = (c1:CROSSROAD)-[r:ROAD*0..{0}]->(c2:CROSSROAD)-->(p:POI) " +
                        "WHERE c1.id = 178732 " +
                        "WITH c1, p, reduce(total=0, h in relationships(poipath) | total + h.distance) AS totalCost " +
                        "WITH c1, p.type AS poitype, min(totalCost) AS minCost " +
                        "RETURN toString(count(poitype))", distance);

                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( string );
                    return result.single().get( 0 ).asString();
                }
            } );

            System.out.println(query);
        }
    }

    public static void main( String[] args ) throws Exception {

        String database = "resources/berlin.db";

        System.out.println("Hello!");
        // Testing the Neo4j server
        try ( App greeter = new App( "bolt://localhost:7687", "neo4j", "botle" ) )
        {
            //greeter.printGreeting( "hello, world" );
            greeter.findNeighbors(30);
        }




    }

}