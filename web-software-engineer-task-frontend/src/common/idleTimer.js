import {useIdleTimer} from "react-idle-timer";

function useIdleTimeout({onIdle, onActive, idleTime = 5}) {

    const idleTimer = useIdleTimer({
  
      timeout: 1000 * idleTime,
  
      onIdle: onIdle,
  
      onActive: onActive,
  
      debounce: 500,
  
      events: ['mousemove', 'keydown', 'wheel']
  
    });
  
  
    return idleTimer;
  
  }

  export default useIdleTimeout;