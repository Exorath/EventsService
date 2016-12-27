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
- meta (json object): Event specific data (see Event meta's for more info)

###/subscribe [WEBSOCKET]:
####Creates a websocket that streams event data.

**Outbound Messages**:
- subscribe (string): Subscribes the service to the specified events.
```json
{"subscribe": {"events": ["join", "leave"], "serviceId": "MessageService", "instanceId": "05b39a97-a9e4-4a17-8741-bedf13201f2f"}}
```

**Inbound Messages**:
- event (string): 
```json
{"eventId": "join", "meta": {"uuid": "05b39a97-a9e4-4a17-8741-bedf13201f2f", "bungeeId": "24"}}
```


##Event meta's