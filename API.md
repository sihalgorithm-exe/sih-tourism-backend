
````markdown
# Tourism API

## Base URL

```text
/api
````

## Authentication

Protected endpoints use JWT authentication.

Include the JWT token in the request header:

```text
Authorization: Bearer <JWT_TOKEN>
```

The JWT token is returned after successful registration or login.

---

# 1. Authentication

## Register

Creates a new tourist account.

### Endpoint

```http
POST /api/auth/register
```

### Request Body

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Response

**Status:** `201 Created`

```json
{
  "token": "<JWT_TOKEN>",
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "TOURIST"
}
```

### Errors

* `400 Bad Request` — Invalid request data
* `409 Conflict` — An account with this email already exists

---

## Login

Authenticates an existing user.

### Endpoint

```http
POST /api/auth/login
```

### Request Body

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

### Response

**Status:** `200 OK`

```json
{
  "token": "<JWT_TOKEN>",
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "TOURIST"
}
```

### Errors

* `400 Bad Request` — Invalid request data
* `401 Unauthorized` — Invalid email or password

---

# 2. Destinations

## Get All Destinations

Returns all available destinations.

### Endpoint

```http
GET /api/destinations
```

### Response

**Status:** `200 OK`

Returns a JSON array of `Destination` objects.

---

## Get Destination by ID

Returns a destination by its ID.

### Endpoint

```http
GET /api/destinations/{id}
```

### Path Parameters

| Parameter | Type | Description    |
| --------- | ---- | -------------- |
| `id`      | Long | Destination ID |

### Response

**Status:** `200 OK`

Returns a `Destination` object.

### Errors

* `404 Not Found` — Destination does not exist

---

# 3. Food

## Get All Food Places

Returns all available food places.

### Endpoint

```http
GET /api/food
```

### Response

**Status:** `200 OK`

Returns a JSON array of `FoodPlace` objects.

---

## Get Food Place by ID

Returns a food place by its ID.

### Endpoint

```http
GET /api/food/{id}
```

### Path Parameters

| Parameter | Type | Description   |
| --------- | ---- | ------------- |
| `id`      | Long | Food place ID |

### Response

**Status:** `200 OK`

Returns a `FoodPlace` object.

### Errors

* `404 Not Found` — Food place does not exist

---

# 4. Travel Groups

## Create Group

Creates a new travel group.

The authenticated user automatically becomes the group leader and is added as a member.

### Endpoint

```http
POST /api/groups
```

### Authentication

Required.

### Request Body

```json
{
  "name": "Weekend Trip",
  "radiusMeters": 500
}
```

### Response

**Status:** `201 Created`

```json
{
  "id": 1,
  "name": "Weekend Trip",
  "leaderId": 10,
  "leaderName": "John Doe",
  "radiusMeters": 500
}
```

The exact response fields are determined by `GroupResponse`.

---

## Add Member

Adds an existing user to a travel group.

Only the group leader can add members.

### Endpoint

```http
POST /api/groups/{groupId}/members
```

### Authentication

Required.

### Path Parameters

| Parameter | Type | Description     |
| --------- | ---- | --------------- |
| `groupId` | Long | Travel group ID |

### Request Body

```json
{
  "userId": 15
}
```

### Response

**Status:** `201 Created`

No response body.

### Errors

* `400 Bad Request` — Invalid request data
* `401 Unauthorized` — Requester is not the group leader
* `404 Not Found` — Group or user does not exist
* `409 Conflict` — User is already a member of the group

---

## Get Group

Returns information about a travel group.

### Endpoint

```http
GET /api/groups/{groupId}
```

### Path Parameters

| Parameter | Type | Description     |
| --------- | ---- | --------------- |
| `groupId` | Long | Travel group ID |

### Response

**Status:** `200 OK`

```json
{
  "id": 1,
  "name": "Weekend Trip",
  "leaderId": 10,
  "leaderName": "John Doe",
  "radiusMeters": 500
}
```

### Errors

* `404 Not Found` — Group does not exist

---

# 5. Group Guard

## Submit Location

Records the authenticated user's location within a travel group.

Both the leader and regular group members can submit locations.

For regular members, the system compares their location with the leader's latest known location.

### Endpoint

```http
POST /api/groups/{groupId}/locations
```

### Authentication

Required.

### Path Parameters

| Parameter | Type | Description     |
| --------- | ---- | --------------- |
| `groupId` | Long | Travel group ID |

### Request Body

```json
{
  "latitude": 16.5062,
  "longitude": 80.6480
}
```

### Response

**Status:** `201 Created`

No response body.

### Behavior

For a regular member:

1. The location is recorded.
2. The leader's latest known location is retrieved.
3. The distance between the member and leader is calculated using the Haversine formula.
4. The calculated distance is compared with the group's configured radius.
5. If the member crosses outside the radius, a safety alert is created.
6. While the member remains outside the radius, duplicate alerts are not created.
7. When the member returns inside the radius, the out-of-bounds state is reset.

If the leader has not submitted a location yet, the distance check is skipped.

### Errors

* `400 Bad Request` — Invalid location data
* `401 Unauthorized` — Authentication required
* `403 Forbidden` — User is not a member of the group
* `404 Not Found` — Group or leader membership does not exist

---

## Get Safety Alerts

Returns the safety alerts generated for a travel group.

Only the group leader can access the alert feed.

### Endpoint

```http
GET /api/groups/{groupId}/alerts
```

### Authentication

Required.

### Path Parameters

| Parameter | Type | Description     |
| --------- | ---- | --------------- |
| `groupId` | Long | Travel group ID |

### Response

**Status:** `200 OK`

```json
[
  {
    "id": 1,
    "groupMemberId": 25,
    "userId": 15,
    "userName": "Member Name",
    "distanceMeters": 742.5,
    "triggeredAt": "2026-08-27T10:30:00"
  }
]
```

The alerts are returned with the newest alerts first.

### Errors

* `401 Unauthorized` — Authentication required
* `403 Forbidden` — User is not the group leader
* `404 Not Found` — Group does not exist

---

# 6. Hotels

## Get All Hotels

Returns all available hotels.

### Endpoint

```http
GET /api/hotels
```

### Response

**Status:** `200 OK`

Returns a JSON array of `Hotel` objects.

---

## Get Hotel by ID

Returns a hotel by its ID.

### Endpoint

```http
GET /api/hotels/{id}
```

### Path Parameters

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| `id`      | Long | Hotel ID    |

### Response

**Status:** `200 OK`

Returns a `Hotel` object.

### Errors

* `404 Not Found` — Hotel does not exist

---

# 7. Recommendations

## Get My Recommendations

Returns personalized destination recommendations for the authenticated user.

The user ID is obtained from the JWT.

### Endpoint

```http
GET /api/recommendations
```

### Authentication

Required.

### Response

**Status:** `200 OK`

Returns a JSON array of `Destination` objects.

### Recommendation Logic

The recommendation system is rule-based and does not use machine learning.

If the user has no saved preferences:

* All destinations are returned.
* Destinations are sorted by popularity.

If the user has saved preferences:

* Destinations are filtered by budget level when a budget level is set.
* Destinations are filtered by preferred category when a preferred category is set.
* Matching destinations are sorted by popularity.

The user ID is not accepted as a query parameter. The authenticated user's identity is obtained from the JWT.

---

# 8. Shopping

## Get All Shopping Places

Returns all available shopping places.

### Endpoint

```http
GET /api/shopping
```

### Response

**Status:** `200 OK`

Returns a JSON array of `ShoppingPlace` objects.

---

## Get Shopping Place by ID

Returns a shopping place by its ID.

### Endpoint

```http
GET /api/shopping/{id}
```

### Path Parameters

| Parameter | Type | Description       |
| --------- | ---- | ----------------- |
| `id`      | Long | Shopping place ID |

### Response

**Status:** `200 OK`

Returns a `ShoppingPlace` object.

### Errors

* `404 Not Found` — Shopping place does not exist

---

# 9. Transport

## Get All Transport Options

Returns all available transport options.

### Endpoint

```http
GET /api/transport
```

### Response

**Status:** `200 OK`

Returns a JSON array of `TransportOption` objects.

---

## Get Transport Option by ID

Returns a transport option by its ID.

### Endpoint

```http
GET /api/transport/{id}
```

### Path Parameters

| Parameter | Type | Description         |
| --------- | ---- | ------------------- |
| `id`      | Long | Transport option ID |

### Response

**Status:** `200 OK`

Returns a `TransportOption` object.

### Errors

* `404 Not Found` — Transport option does not exist

---

# 10. User Preferences

## Get My Preferences

Returns the preferences of the authenticated user.

The user ID is obtained from the JWT.

### Endpoint

```http
GET /api/preferences
```

### Authentication

Required.

### Response

**Status:** `200 OK`

Returns a `UserPreference` object.

If the user has not configured preferences yet, no preference record is returned.

---

## Update My Preferences

Creates or updates the authenticated user's preferences.

This operation behaves as an upsert:

* If preferences already exist, they are updated.
* If no preferences exist, a new preference record is created.

### Endpoint

```http
PUT /api/preferences
```

### Authentication

Required.

### Request Body

```json
{
  "interests": "heritage,food,nature",
  "budgetLevel": "MEDIUM",
  "preferredCategory": "Heritage"
}
```

### Response

**Status:** `200 OK`

Returns the saved `UserPreference` object.

### Notes

The user ID is obtained from the JWT and is not supplied by the client.

---

# Authentication Summary

| Endpoint                               | Authentication          |
| -------------------------------------- | ----------------------- |
| `POST /api/auth/register`              | No                      |
| `POST /api/auth/login`                 | No                      |
| `GET /api/destinations`                | Not explicitly required |
| `GET /api/destinations/{id}`           | Not explicitly required |
| `GET /api/food`                        | Not explicitly required |
| `GET /api/food/{id}`                   | Not explicitly required |
| `POST /api/groups`                     | Required                |
| `POST /api/groups/{groupId}/members`   | Required                |
| `GET /api/groups/{groupId}`            | Not explicitly required |
| `POST /api/groups/{groupId}/locations` | Required                |
| `GET /api/groups/{groupId}/alerts`     | Required                |
| `GET /api/hotels`                      | Not explicitly required |
| `GET /api/hotels/{id}`                 | Not explicitly required |
| `GET /api/recommendations`             | Required                |
| `GET /api/shopping`                    | Not explicitly required |
| `GET /api/shopping/{id}`               | Not explicitly required |
| `GET /api/transport`                   | Not explicitly required |
| `GET /api/transport/{id}`              | Not explicitly required |
| `GET /api/preferences`                 | Required                |
| `PUT /api/preferences`                 | Required                |

---

# Common HTTP Status Codes

| Status             | Meaning                                     |
| ------------------ | ------------------------------------------- |
| `200 OK`           | Request completed successfully              |
| `201 Created`      | A new resource was created                  |
| `400 Bad Request`  | Invalid request data or validation failure  |
| `401 Unauthorized` | Authentication is missing or invalid        |
| `403 Forbidden`    | Authenticated user does not have permission |
| `404 Not Found`    | Requested resource does not exist           |
| `409 Conflict`     | Resource conflicts with existing data       |

---

# JWT Usage

For protected endpoints, include the JWT returned by the register or login endpoint.

```http
Authorization: Bearer <JWT_TOKEN>
```

The authenticated user's identity is derived from the JWT for operations such as:

* Creating travel groups
* Adding group members
* Submitting locations
* Viewing safety alerts
* Getting personalized recommendations
* Reading user preferences
* Updating user preferences

Client applications should not provide another user's ID to perform operations on behalf of that user.

```
```
