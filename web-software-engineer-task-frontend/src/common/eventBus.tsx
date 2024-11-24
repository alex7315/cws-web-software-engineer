/**
 * Global event-driven system (Publish-Subscribe pattern based system)
 * allows to listen and dispatch events from components.
 *
 * on() attachs an EventListener to the document object.
 *
 * dispatch() fires an event using the CustomEvent API
 *
 * remove() removes attached eventfrom document object
 */
const eventBus = {
  on(event: string, callback: EventListener) {
    console.log('attach event: ' + event)

    document.addEventListener(event, (e) => callback(e))
  },
  dispatch(event: string, data?: any) {
    console.log('dispatch event: ' + event)
    document.dispatchEvent(new CustomEvent(event, { detail: data }))
  },
  remove(event: string, callback: EventListener) {
    console.log('detach event: ' + event)
    document.removeEventListener(event, callback)
  },
}

export default eventBus
