/**
 * Implements simple custom hook encapsulates logic of idle timer.
 */
import {useIdleTimer} from "react-idle-timer";

interface IdleTimeout {
  onIdle: () => void;
  onActive: () => void;
  idleTime: number;
}

function useIdleTimeout(idleTimeout: IdleTimeout) {

    const idleTimer = useIdleTimer({
  
      timeout: 1000 * (idleTimeout.idleTime ? idleTimeout.idleTime : 5),
  
      onIdle: idleTimeout.onIdle,
  
      onActive: idleTimeout.onActive,
  
      debounce: 500,
  
      events: ['mousemove', 'keydown', 'wheel']
  
    });
  
  
    return idleTimer;
  
  }

  export default useIdleTimeout;