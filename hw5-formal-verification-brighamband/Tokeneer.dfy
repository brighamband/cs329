class Token {
  // Invalid state (userId: -1, clearance: -1)
  var userId : int; // Fingerprint data -- if userId == -1, it's a null token
  var clearance : int;  // 1 is Low, 2 is Medium, 3 is High       -1 for invalid

  predicate isInRange()
    reads `userId, `clearance
  {
    userId >= -1 &&
    -1 <= clearance <= 3
  }

  predicate isNull()
    reads `userId, `clearance
  {
    userId == -1 && clearance == -1
  }

  constructor(id: int, cl : int)
    requires id >= -1
    requires -1 <= cl <= 3
    ensures isInRange()
    ensures userId == id
    ensures clearance == cl
  {
    userId := id;
    clearance := cl;
  }
}

class IdStation {
  var doorNum : int;
  var doorSecurityLevel : int;
  var doorIsOpen : bool;
  var alarmIsOn : bool;
  var tokenIsInSystem : bool;

  predicate isValid()
    reads `doorNum, `doorSecurityLevel
  {
    doorNum >= 0 &&
    1 <= doorSecurityLevel <= 3
  }

  constructor(num : int, level : int)
    requires num >= 0
    requires 1 <= level <= 3
    ensures isValid()
    ensures doorNum == num
    ensures doorSecurityLevel == level
    ensures !doorIsOpen
    ensures !alarmIsOn
  {
    doorNum := num;
    doorSecurityLevel := level;
    doorIsOpen := false;
    alarmIsOn := false;
    tokenIsInSystem := false;
  }

  // Pass it to the enrollment station to see if they have access
  method openDoor(enrollStation : EnrollmentStation, token : Token) returns (doorIsOpen : bool)  // Must have valid token to open door
    requires isValid()
    requires token.isInRange()
    ensures isValid()
    ensures token.isInRange()
    ensures tokenIsInSystem && token.clearance >= doorSecurityLevel ==> !alarmIsOn && doorIsOpen
    ensures !tokenIsInSystem ==> alarmIsOn && !doorIsOpen
    ensures token.clearance < doorSecurityLevel ==> alarmIsOn && !doorIsOpen
    modifies `tokenIsInSystem, `alarmIsOn, `doorIsOpen
  {
    // Check token
    tokenIsInSystem := enrollStation.tokenIsEnrolled(token);

    if (tokenIsInSystem && token.clearance >= doorSecurityLevel) { // Open the door
      alarmIsOn := false; // Turn off alarm if it was on
      doorIsOpen := true;
    } 
    else {  // Close the door
      enrollStation.invalidateToken(token);
      alarmIsOn := true;
      doorIsOpen := false;  // Should stay closed
    }
  }

  method closeDoor(token : Token) returns (doorIsOpen : bool)  // Anyone can close the door
    requires isValid()
    requires token.isInRange()
    ensures isValid()
    ensures token.isInRange()
    ensures !doorIsOpen
    modifies `doorIsOpen
  {
    doorIsOpen := false;
  }
}

class EnrollmentStation {
  var tokens : set<Token>;
  var currId : int;

  constructor()
    ensures tokens == {}
    ensures currId == 0
  {
    tokens := {};
    currId := 0;
  }

  method issueToken(cl : int) returns (token : Token)  // Probably have a user as a param
    requires 1 <= cl <= 3
    requires currId >= 0
    ensures token.isInRange() && !token.isNull()
    ensures token in tokens
    ensures currId == old(currId) + 1
    ensures fresh(token)
    modifies `tokens, `currId
  {
    // Set up new token
    token := new Token(currId, cl);

    // Add to set of tokens
    var newSet := {token};
    tokens := tokens + newSet;

    // Increment currId
    currId := currId + 1;
  }

  method tokenIsEnrolled(token : Token) returns (tokenIsInSystem : bool)
    requires token.isInRange()
    ensures token.isInRange()
    ensures token in tokens ==> tokenIsInSystem
    ensures token !in tokens ==> !tokenIsInSystem
  {
    tokenIsInSystem := false;
    if (token in tokens) {
      tokenIsInSystem := true;
    }
  }

  method invalidateToken(token : Token)
    requires token.isInRange()
    ensures token.isInRange()
    ensures token !in tokens
    modifies `tokens
  {
    // Remove token from set
    tokens := tokens - {token};
  }
}

// Test method
method main()
{
  var enrollStation := new EnrollmentStation();  // Global variable for the single enrollment station
  var nullToken := new Token(-1, -1);  // Token that starts out null
  var idStationLvl1 := new IdStation(55, 1);

  // Test open door with no token
  var doorIsOpen := idStationLvl1.openDoor(enrollStation, nullToken); // Try opening door without a token
  assert !doorIsOpen;

  // Test close door with no token (should close door)
  doorIsOpen := idStationLvl1.closeDoor(nullToken);
  assert !doorIsOpen;

  // Test issue token
  var tokenLvl2 := enrollStation.issueToken(2);
  assert tokenLvl2.isInRange() && !tokenLvl2.isNull();
  var tokenIsInSystem := enrollStation.tokenIsEnrolled(tokenLvl2);
  assert tokenIsInSystem;

  // // Test opening door with clearance 1 (should open)
  doorIsOpen := idStationLvl1.openDoor(enrollStation, tokenLvl2);
  assert doorIsOpen;

  // Test opening door with clearance 3 (should not open)
  var idStationLvl3 := new IdStation(99, 3);
  doorIsOpen := idStationLvl3.openDoor(enrollStation, tokenLvl2);
  assert !doorIsOpen;

  // // Test close door with token (should close door)
  doorIsOpen := idStationLvl1.closeDoor(tokenLvl2);
  assert !doorIsOpen;

  // Test invalidate token
  enrollStation.invalidateToken(tokenLvl2);
  tokenIsInSystem := enrollStation.tokenIsEnrolled(tokenLvl2);
  assert !tokenIsInSystem;
}
