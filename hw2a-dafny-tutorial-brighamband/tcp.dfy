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

  // Helper methods
  method sendSyn()  
  {
    // TODO
  }

  method recvSynAck()
  {
    // TODO
  }

  /**
   * Sending an ACK does not change the state.
   */
  method sendAck()
  {
    // no update to state
  }

  method sendFin()
  {
    // TODO
  }

  method recvAck()
  {
    // TODO
  }

  method recvFin()
  {
    // TODO
  }

  method timeout()
  {
    // TODO
  }

  // TODO: Add code from tutorial here.

}