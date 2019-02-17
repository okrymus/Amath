
package ProjectResources;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author seuneguntola
 */
public class ResultFinderTest {
    
    public ResultFinderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isNotEvenDivision method, of class ResultFinder.
     */
    @Test
    public void testIsNotEvenDivision() {
        System.out.println("isNotEvenDivision");
        boolean expResult = false;
        boolean result = ResultFinder.isNotEvenDivision();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getResult method, of class ResultFinder.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        ResultFinder instance = new ResultFinder("5*3");
        
        Integer expResult = 15;
        Integer result = instance.getResult();
        assertEquals(expResult, result);
      
    }
    
}
