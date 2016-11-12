# EventService
The EventService is responsible for propagating player events.

##Endpoints
###/event [POST]
####Publishes the event to all subscribed services.
Every service type will receive this message only once.

**Body**:
```json
{"eventId": "join", "meta": {"uuid": "05b39a97-a9e4-4a17-8741-bedf13201f2f", "bungeeId": "24"}}
```
- eventId (string): The event identifier (e.g. "join")
- meta (string): Event specific data.
  - uuid (UUID)[OPTIONAL]: The id of the player.
  - bungeeId (string)[OPTIONAL]: The id of the posting bungee server.
  - spigotId (string)[OPTIONAL]: The id of the posting spigot server. 

###/subscribe [WEBSOCKET]:
####Creates a websocket that streams event data.

**Outbound Messages**:
- subscribe (string): Subscribes the service to the specified events.
```json
{"subscribe": ["join", "leave"], "serviceId": "MessageService", "instanceId": "05b39a97-a9e4-4a17-8741-bedf13201f2f"}
```

**Inbound Messages**:
- event (string): 
```json
{"eventId": "join", "meta": {"uuid": "05b39a97-a9e4-4a17-8741-bedf13201f2f", "bungeeId": "24"}}
```
