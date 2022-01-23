import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
   * creates a schedule which contains the hours 0,1,2,( ...,size - 1 where for
   * each hour the number of needed employees is set to zero and there are no
   * employees assigned to it
   * 
   * @requires size >= 0
   * 
   * @ensures size(ws) == size  (size being the param passed in)
   */
  // public WorkSchedule(int size) {}
  
  /**
   * gives back an object of the class Hour, which has two fields: requiredNumber
   * of type int is the required number of employees working at hour time.
   * workingEmployees of type String[] is the names of the employees who have so
   * far been assigned to work at hour time.
   * 
   * @requires time >= 0 && time >= size(ws) - 1
   * 
   * @ensures Hour.requiredNumber >= 0     (for the Hour returned)
   * @ensures size(Hour.workingEmployees) == proper number of employees
   * @ensures for each employee in Hour.workingEmployees ==> names match expectations
   */
  // public Hour readSchedule(int time) {}

  /**
    * sets the number of required working employees to nemployee for all hours in
    * the interval starttime to endtime.
    * 
    * @requires nemployee >= 0
    * @requires starttime <= endtime
    * @requires starttime >= 0 && starttime <= size(ws) - 1
    * @requires endtime >= 0 && endtime <= size(ws) - 1
    *
    * @ensures for each hour in starttime to endtime ==> Hour.requiredEmployees = nemployee
    */
  // public void setRequiredNumber(int nemployee, int starttime, int endtime) {}

  /**
    * schedules employee to work during the hours from starttime to endtime.
    *
    * @requires starttime <= endtime
    * @requires starttime >= 0 && starttime <= size(ws) - 1
    * @requires endtime >= 0 && endtime <= size(ws) - 1
    * @requires employee = actual employee name at company
    *
    * @ensures for each Hour in starttime to endtime ==> employee exists in Hour.workingEmployees
    * @ensures for each Hour in starttime to endtime ==> Hour.requiredNumber >= size(Hour.workingEmployees)
    */
  // public boolean addWorkingPeriod(String employee, int starttime, int endtime) {}

  /**
   * returns a list of all employees working at some point during the interval
   * starttime to endtime.
   * 
   * @requires starttime <= endtime
   * @requires starttime >= 0 && starttime <= size(ws) - 1
   * @requires endtime >= 0 && endtime <= size(ws) - 1
   * 
   * @ensures size(Hour.workingEmployees) == proper number of employees
   * @ensures for each Hour in starttime to endtime and for each employee in Hour ==> employee is actual employee name at company
   */
  // public String[] workingEmployees(int starttime, int endtime) {}

  /**
   * returns the closest time starting from currenttime for which the required
   * amount of employees has not yet been scheduled.
   * 
   * @requires currenttime >= 0 && currenttime <= size(ws) - 1
   * 
   * @ensures all times scheduled ==> returnValue = -1
   * @ensures there are openings ==> returnValue >= 0 and returnValue <= size(ws) - 1
   * @ensures ws[returnValue].Hour.requiredNumber > size(ws[returnValue].Hour.workingEmployees)
   */
  // public int nextIncomplete(int currenttime) {}

@DisplayName("Testing WorkSchedule using JUnit 5")
public class WorkScheduleTests {
     static WorkSchedule ws = null;

     private final int WS_SIZE = 24;

     @BeforeEach
     public void setup() {
       ws = new WorkSchedule(WS_SIZE);
     }

     @Test
     @DisplayName("Tests WorkSchedule constructor based on specifications")
     void testConstructor() {
        // Test Partition 1 -- Invalid input (negative)
        assertThrows(NegativeArraySizeException.class, () -> new WorkSchedule(-1));

        // Test Partition 2 -- Valid input (positive)
        // Tests that sizes match up
        WorkSchedule wsGoodTest = new WorkSchedule(WS_SIZE);
        int sizeWs = 0;
        for (int i = 0; i < WS_SIZE; i++) {
          final int tempIdx = i;
          assertDoesNotThrow(() -> wsGoodTest.readSchedule(tempIdx));
          sizeWs++;
        }
        assertEquals(WS_SIZE, sizeWs);
     }

     @Test
     @DisplayName("Tests WorkSchedule readSchedule() method based on specifications")
     void testReadSchedule() {
        // Test Partition 1 -- Invalid input on low end of range
        int INVALID_LOW_INPUT = -4;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.readSchedule(INVALID_LOW_INPUT));

        // Test Partition 2 -- Invalid input on high end of range
        int INVALID_HIGH_INPUT = 200;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.readSchedule(INVALID_HIGH_INPUT));

        // Test Partition 3 -- Valid input within range
        int VALID_INPUT = 10;
        assertDoesNotThrow(() -> ws.readSchedule(VALID_INPUT));
        assertTrue(ws.readSchedule(VALID_INPUT).requiredNumber >= 0);
        assertTrue(ws.workingEmployees(0, 0).length >= 0);
     }

     @Test
     @DisplayName("Tests WorkSchedule setRequiredNumber() method based on specifications")
     void testSetRequiredNumber() {
        // Test Partition 1 -- Negative nemployee
        int INVALID_NEMPLOYEE = -20;
        ws.setRequiredNumber(INVALID_NEMPLOYEE, 0, 5);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.readSchedule(INVALID_NEMPLOYEE));
        
        // Test Partition 2 -- Negative starttime
        int NEG_STARTTIME = -3;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.setRequiredNumber(5, NEG_STARTTIME, 5));
        
        // Test Partition 3 -- Too late starttime
        int TOO_LATE_STARTTIME = 500;
        ws.setRequiredNumber(5, TOO_LATE_STARTTIME, 5);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.readSchedule(TOO_LATE_STARTTIME));
        
        // Test Partition 4 -- Negative endtime
        int NEG_ENDTIME = -3;
        ws.setRequiredNumber(5, 1, NEG_ENDTIME);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.readSchedule(NEG_ENDTIME));
        
        // Test Partition 5 -- Too late endtime
        int TOO_LATE_ENDTIME = 500;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.setRequiredNumber(5, 1, TOO_LATE_ENDTIME));

        // Test Partition 6 - Valid nemployee, valid starttime, valid endtime
        ws.setRequiredNumber(5, 3, 6);
        assertEquals(5, ws.readSchedule(3).requiredNumber);
        assertEquals(5, ws.readSchedule(4).requiredNumber);
        assertEquals(5, ws.readSchedule(5).requiredNumber);
        assertEquals(5, ws.readSchedule(6).requiredNumber);
     }

     @Test
     @DisplayName("Tests WorkSchedule addWorkingPeriod() method based on specifications")
     void testAddWorkingPeriod() {
         // Test Partition 1 -- Valid employee, valid starttime, valid endtime
        String EMPLOYEE_NAME = "Billy";
        int VALID_STARTTIME = 0;
        int VALID_ENDTIME = 4;
        ws.setRequiredNumber(3, VALID_STARTTIME, VALID_ENDTIME);
        ws.addWorkingPeriod(EMPLOYEE_NAME, VALID_STARTTIME, VALID_ENDTIME);
        for (int i = VALID_STARTTIME; i < VALID_ENDTIME; i++) {
            assertEquals(EMPLOYEE_NAME, ws.readSchedule(i).workingEmployees[0]);
        }

        // Test Partition 2 -- If required number is already high enough, new employees aren't added
        ws.addWorkingPeriod("Bradley", VALID_STARTTIME, VALID_ENDTIME);
        ws.addWorkingPeriod("Conner", VALID_STARTTIME, VALID_ENDTIME);
        ws.addWorkingPeriod("Dillon", VALID_STARTTIME, VALID_ENDTIME);
         for (int i = VALID_STARTTIME; i < VALID_ENDTIME; i++) {
             assertNotEquals("d", ws.readSchedule(i).workingEmployees[0]);
             assertTrue(ws.readSchedule(i).requiredNumber >= ws.readSchedule(i).workingEmployees.length);
         }
     }

     @Test
     @DisplayName("Tests WorkSchedule workingEmployees() method based on specifications")
     void testWorkingEmployees() {
         int STARTTIME = 10;
         int ENDTIME = 14;

         // Test Parition 1 -- No working employees
         assertEquals(0, ws.readSchedule(STARTTIME).workingEmployees.length);

         // Test Partition 1 -- Multiple working employees
         ws.setRequiredNumber(3, STARTTIME, ENDTIME);
         assertEquals(0, ws.readSchedule(STARTTIME).workingEmployees.length);
         ws.addWorkingPeriod("Abraham", STARTTIME, ENDTIME);
         assertEquals(1, ws.readSchedule(STARTTIME).workingEmployees.length);
         ws.addWorkingPeriod("Bradshaw", STARTTIME, ENDTIME);
         assertEquals(2, ws.readSchedule(STARTTIME).workingEmployees.length);
         ws.addWorkingPeriod("Colton", STARTTIME, ENDTIME);
         assertEquals(3, ws.readSchedule(STARTTIME).workingEmployees.length);

         // Check employees added are actual employees at the company
         String[] actualEmployees = {"Abraham", "Bradshaw", "Colton"};
         for (int i = STARTTIME; i < ENDTIME; i++) {
             for (String employee : ws.readSchedule(i).workingEmployees) {
                 assertTrue(Arrays.asList(actualEmployees).contains(employee));
             }
         }
     }

      @Test
      @DisplayName("Tests WorkSchedule nextIncomplete() method based on specifications")
      void testNextIncomplete() {
          // Test Partition 1 -- Negative currenttime provided
          int INVALID_LOW_INPUT = -20;
          assertThrows(ArrayIndexOutOfBoundsException.class, () -> ws.nextIncomplete(INVALID_LOW_INPUT));

          // Test Partition 2 -- Too high currenttime provided
          int INVALID_HIGH_INPUT = 555;
          assertEquals(-1, ws.nextIncomplete(INVALID_HIGH_INPUT));

          // Test Partition 3 -- Valid currenttime range provided, but no slots available
          int VALID_RANGE_INPUT = 5;
          assertEquals(-1, ws.nextIncomplete(VALID_RANGE_INPUT));

          // Test Partition 4 -- Valid currenttime, and has an incomplete slot
          ws.setRequiredNumber(3, 18, 22);
          int nextIncomplete = ws.nextIncomplete(15);
          assertEquals(18, nextIncomplete);
          assertTrue(ws.readSchedule(nextIncomplete).requiredNumber > 0);
          assertTrue(ws.readSchedule(nextIncomplete).requiredNumber >= ws.readSchedule(nextIncomplete).workingEmployees.length);
      }
}
