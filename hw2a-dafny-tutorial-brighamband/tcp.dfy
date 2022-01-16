/**
 * This is how to create an enumerated type in Dafny.
 */
datatype STATE = SYN_SENT | ESTABLISHED | FIN_WAIT_1 | FIN_WAIT_2 |
                 CLOSE_WAIT | CLOSING | LAST_ACK | TIME_WAIT | CLOSED

/**
 * This class represents a Client TCP Connection state.
 *   The methods will represent sending and receiving the IP messages
 *   needed to open and close a TCP connection from the client to a server.
 *
 * By modeling the client side in Dafny, we can *prove* that we are in the
 *   correct state after trying to 'connect' or 'close' or 'terminate'.
 */
class ClientTcpConnection {

  var state: STATE

   /**
   * Constructor: state will always start as CLOSED
   */
  constructor init()
    ensures state == CLOSED
  {
    state := CLOSED;
  }

  // Helper methods
  method sendSyn()  
    modifies `state
    ensures state == SYN_SENT
  {
    state := SYN_SENT;
  }

  method recvSynAck()
    modifies `state
    ensures old(state) == SYN_SENT ==> state == ESTABLISHED
    ensures old(state) != SYN_SENT ==> state == old(state)
  {
    if (state == SYN_SENT)
    {
      state := ESTABLISHED;
    }
  }

  /**
   * Sending an ACK does not change the state.
   */
  method sendAck()
  {
    // no update to state
  }

  method sendFin()
    modifies `state
    ensures old(state) == CLOSE_WAIT ==> state == LAST_ACK
    ensures old(state) != CLOSE_WAIT ==> state == FIN_WAIT_1
  {
    if (state == CLOSE_WAIT)
    {
      state := LAST_ACK;
    }
    else 
    {
      state := FIN_WAIT_1;
    }
  }

  method recvAck()
    modifies `state
    ensures old(state) == FIN_WAIT_1 ==> state == FIN_WAIT_2
    ensures old(state) == LAST_ACK ==> state == CLOSED
  {
    if (state == FIN_WAIT_1)
    {
      state := FIN_WAIT_2;
    }
    else if (state == LAST_ACK)
    {
      state := CLOSED;
    }
  }

  method recvFin()
    modifies `state
    ensures old(state) == FIN_WAIT_2 ==> state == TIME_WAIT
    ensures old(state) != FIN_WAIT_2 ==> state == CLOSE_WAIT
  {
    if (state == FIN_WAIT_2)
    {
      state := TIME_WAIT;
    }
    else
    {
      state := CLOSE_WAIT;
    }
  }

  method timeout()
    modifies `state
    ensures state == CLOSED
  {
    state := CLOSED;
  }

  method isClosed() returns (result: bool)
    ensures result == (state == CLOSED)
  {
    if (state == CLOSED)
    {
      result := true;
    }
    else 
    {
      result := false;
    }
  }

  method isEstablished() returns (result: bool)
    ensures result == (state == ESTABLISHED)
  {
    if (state == ESTABLISHED)
    {
      result := true;
    }
    else 
    {
      result := false;
    }
  }

  /**
  * This method represents a client starting the 3-way handshake
  *   with a server to establish a TCP connection.
  */
  method connect()
    modifies `state
    ensures old(state) == CLOSED ==> state == ESTABLISHED
    ensures old(state) != CLOSED ==> state == old(state)
  {
    if (state == CLOSED)
    {
      sendSyn();
      recvSynAck();
      sendAck();
    }
  }

  /**
  * This method represents a client starting the 4-way handshake
  *   with a server to close an established TCP connection.
  */
  method close()
    modifies `state
    ensures state == CLOSED
  {
    if (state != CLOSED)
    {
      sendFin();
      recvAck();
      recvFin();
      sendAck();
      timeout();
    }
  }

  /**
  * This method represents a client terminating an established
  *   TCP connection from a server request to close.
  */
  method terminate()
    modifies `state
    requires state == ESTABLISHED
    ensures state == CLOSED
  {
    recvFin();
    sendAck();
    sendFin();
    recvAck();
  }

  /**
  * Test client initiate connect
  */
  method test_ClientConnect()
  {
    var client := new ClientTcpConnection.init();
    var isClosed: bool := client.isClosed();
    assert (isClosed);
    client.connect();
    var isEstablished: bool := client.isEstablished();
    assert (isEstablished);
  }

  /**
  * Test client initiate close after connect
  */
  method test_ClientConnectThenClientClose()
  {
    var client := new ClientTcpConnection.init();
    var isClosed: bool := client.isClosed();
    assert (isClosed);
    client.connect();
    var isEstablished: bool := client.isEstablished();
    assert (isEstablished);
    client.close();
    isClosed := client.isClosed();
    assert (isClosed);
  }

  /**
  * Test server initiate close after connect
  */
  method test_ClientConnectThenServerClose()
  {
    var client := new ClientTcpConnection.init();
    var isClosed: bool := client.isClosed();
    assert (isClosed);
    client.connect();
    var isEstablished: bool := client.isEstablished();
    assert (isEstablished);
    client.terminate();
    isClosed := client.isClosed();
    assert (isClosed);
  }

  /**
  * Test calling 'close' twice
  */
  method test_ClientCloseTwice()
  {
    var client := new ClientTcpConnection.init();
    var isClosed: bool := client.isClosed();
    assert (isClosed);
    client.close();
    isClosed := client.isClosed();
    assert (isClosed);
  }
}