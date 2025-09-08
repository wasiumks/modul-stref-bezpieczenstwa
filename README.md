# modul-stref-bezpieczenstwa

## Geofencing – Simulation Guide (QA)
- **Endpoint**: `GET /api/geofence/events/{zoneId}?count=N`
- **What it does**: returns N random ENTER/EXIT mock events around the zone center for its devices (or a virtual one if none assigned)
- **How to run**:
  - Open browser: `http://localhost:8080/api/geofence/events/1?count=5`
  - Or Chrome DevTools (Console):
    ```javascript
    fetch('/api/geofence/events/1?count=3')
      .then(r => r.json())
      .then(d => { console.log('Mock events', d); alert('Generated ' + d.length + ' geofence events'); });
    ```
- **Logging**: backend logs each event at INFO with event type, zone, device, lat/lng, radius, timestamp

---
