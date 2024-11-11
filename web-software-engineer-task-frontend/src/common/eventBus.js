/**
 * Global event-driven system (Publish-Subscribe pattern based system)
 * allows to listen and dispatch events from components
 * on() attachs an EventListener to the document object.
 * dispatch() fires an event using the CustomEvent API
 * remove() removes attached eventfrom document object
 */
const eventBus = {
    on(event, callback) {
      document.addEventListener(event, (e) => callback(e.detail));
    },
    dispatch(event, data) {
      document.dispatchEvent(new CustomEvent(event, { detail: data }));
    },
    remove(event, callback) {
      document.removeEventListener(event, callback);
    },
  };
  
  export default eventBus;