
# HTTP API

## Contents

- [Contents](#contents)
- [Overview](#overview)
- [ES CORE API](#ES_CORE_API)
   * [currency](#acurrency)
      * [currency/block](#acurrencyblock)
      * [currency/blockstat](#acurrencyblockstat)
      * [currency/peer](#acurrencypeer)
      * [currency/movement](#acurrencymovement)
- [ES USER API](#es_user_api)
   * [user](#user)
      * [user/event](#userevent)
      * [user/profile](#userprofile)
      * [user/settings](#usersettings)
   * [message](#message)
      * [message/inbox](#messageinbox)
      * [message/oubox](#messageoutbox)
   * [invitation](#invitation)
      * [invitation/certification](#invitationcertification)
- [ES SUBSCRIPTION API](#ES SUBSCRIPTION API)
   * [subscription](#subscription)
      * [subscription/record](#subscriptionrecord)

## Overview

Cesium+ Pod offer RESTfull HTTP access:

- `ES CORE API`: BlockChain indexation and statistics;
- `ES USER API`: User data indexation, such as: profiles, private messages, encrypted settings;
- `ES SUBSCRIPTION API`: User service subscription, such as: email notification service;

Data is made accessible through an HTTP API :

```text
    http[s]://node[:port]/...
    |-- <currency_name>/
    |   |-- block
    |   |-- blockstat
    |   |-- peer
    |   `-- movement
    |-- user/
    |   |-- event
    |   |-- profile
    |   `-- settings
    |-- message/
    |   |-- inbox
    |   `-- outbox
    |-- invitation/
    |   `-- certification
    `-- subscription/
        `-- record
```

### Document format
 
All stored documents use a JSON format.

#### Data document

Every document have the following mandatory fields:

- `version` : The document's version.
- `issuer` : The document's emitter
- `hash`: the document's hash
- `signature`: the signature emitted by the issuer. Since `version: 2`, only the `hash` is signed.

#### Deletion

Document deletion use a document with this mandatory fields:

- `index` : The document's index
- `type` : The document's type
- `issuer`: The deletion issuer. Should correspond to the document's `issuer`, or the `recipient` in some special case ([inbox message](#messageinbox) or [invitation](#invitation))
- `time`: the current time
- `hash`
- `signature`

For instance, a deletion on `message/inbox` should send this document:

```json
{
  "version" : 2,
  "index" : "message",
  "type" : "inbox",
  "id" : "AV9VOeOuTvXJwYisNfU6",
  "issuer" : "F13aXKWQPGCjSQAxxTyJYyRyPm5SqzFSsYYWSDEQGi2A",
  "time" : 1509806623,
  "hash" : "61EBBFBCA630E8B715C360DDE1CD6CABD92B9267CA4B724A2F1F36F0FF7E3455",
  "signature" : "FOkYCX1b05LTAbtz72F/LMWZb8F8zhQKEqcvbuiQy1N6AXtCUC5Xmjcn+NeO9sCLdcmA0HxsJx42GnWZOmKCDA=="
}
```
          
## ES CORE API

### `<currency>/*`

#### `<currency>/block`

 - Get the current block: `<currency>/block/current`
 - Get a block by number: `<currency>/block/<number>`
 - Search on blocks: `<currency>/block/_search` (POST or GET)

#### `<currency>/blockstat`

#### `<currency>/peer`

#### `<currency>/movement`

## ES USER API

### `user/*`

#### `user/event`

 - Get events on an account, by pubkey: `user/event/_search?q=issuer:<pubkey>` (GET)
 - Search on events: `user/event/_search` (POST or GET)

#### `user/profile`


 - Get an profile, by public key: `user/profile/<pubkey>`
 - Add a new profile: `user/profile` (POST)
 - Update an existing profile: `user/profile/_update` (POST)
 - Delete an existing invitation: `invitation/certification/_delete` (POST)
 - Search on profiles: `user/profile/_search` (POST or GET)

A profile document is a JSON document. Mandatory fields are:
 
 - `title`: user name (Lastanem, firstname...)
 - `time`: submission time, in seconds
 - `issuer`: user public key
 - `hash`: hash of the JSON document (without fields `hash` and `signature`)
 - `signature`: signature of the JSON document (without fields `hash` and `signature`)

Example with only mandatory fields:

```json
{
    "version" : 2, 
    "title" : "Pecquot Ludovic",
    "description" : "Développeur Java et techno client-serveur\nParticipation aux #RML7, #EIS et #Sou",
    "time" : 1488359903,
    "issuer" : "2v6tXNxGC1BWaJtUFyPJ1wJ8rbz9v1ZVU1E1LEV2v4ss",
    "hash" : "F66D43ECD4D38785F424ADB68B3EA13DD56DABDE275BBE780E81E8D4E1D0C5FA",
    "signature" : "3CWxdLtyY8dky97RZBFLfP6axnfW8KUmhlkiaXC7BN98yg6xE9CkijRBGmuyrx3llPx5HeoGLG99DyvVIKZuCg=="
}
```

Some additional fields are `description`, `socials`, `tags` and `avatar` :

```json
{
    "version" : 2, 
    "title" : "My profile name",
    "description" : "#developer",
    "city" : "Rennes",
    "socials" : [ {
      "type" : "diaspora",
      "url" : "https://diaspora-fr.org/people/f9d13420f9ssqzq97aa01beea1f31e2"
    } ],
    "time" : 1487422234,
    "tags" : [ "developer" ],
    "issuer" : "2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ",
    "avatar" : {
      "_content_type" : "image/png",
      "_content" : "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkC(...)" // base 64 encoding
    }
    "hash" : "85F527077D060E03ECAC6D1AE38A74CCC900ACAF5D52F194BA34F5A5E8A55139",
    "signature" : "WeP7JEwttAoSkHcuiFwo6N4SM0uVakTYBQ09H1+K8/nPFyxO3ak1U9EQ6qaQFoAx9IdDp5qO2EX662wP/pcEAg==",
}
```

#### `user/settings`

 - Get an settings, by wallet pubkey: `user/settings/<pubkey>`
 - Add a new settings: `user/settings` (POST)
 - Delete an existing settings: `user/settings/_delete` (POST)
 - Search on settings: `user/settings/_search` (POST or GET)

Some additional fields are `content` (the settings content, but encrypted) and `nonce` (required for an optimal encryption security level) :

```json
{
    "hash" : "58ECD135719628AA6DCE6DEFE1C2B328B04047B836BC478D0CF9E6F5A515896EC",
    "signature" : "3zP/mOgwnTj6EAfhb9vNfSUoPLZLqMwTP9QDk4wShTXlWnFPmPl2To3VTAoS3aHbLQAKDAWZa6EeVfsYCDVoDg==",
    "issuer" : "EtmXYFdh6WjgKyX3D6s2fXphCDv7jRPRnqnkKFTdMwNr",
    "nonce" : "Lfv5wXbLKF3RY9qbQVgv914ZKbsVi1sAm",
    "time" : 1516311640,
    "content" : "4bW4cL075bLWuTrHRuo69P0glmZJiVKF/AOtRt1e3trcm+Es/E77cYdAL00TCQw8N1kVU6fznCmZyVxtD8gfxpZwcoipWjWeTZTu21SxtPDxTxEvAV4gxbmOk/Li9oMy04WOmpkbsKawmdYW2oaKzz3psJXn4C4/jFQZIL/X863R9sQDGWPHm8MRvCaP7xQT+MMSpb8/1lIgf5443PKBixQbcY4fcqDRK3365xG2jDZEJ/uVZ/bRPJyjclKgBEd8xariJUV+zdh31f/qHhnQlcg/kLmdQ4sja2L/BWE5kTFlajRqOJDGrtuRafWTFamoUKZDE8C9YeivvFR7oGwY0zPE0uFnuZCGAvm3xC13ekpsqDv9YtBmZhou7AZAtw9JV81QuHoorWrka7C3LW12YuOSBKxkZNCi0tPHmF2ArI5WJl7W",
    "version" : 2
}
```

### `message/*`

#### `message/inbox`

Some additional fields are `recipient` (the message recipient), `title` (the encrypted message's title), `content` (the encrypted message's body) and `nonce` (required for an optimal encryption security level) :

```json
{
    "issuer" : "DMwEdBiWuCGkPvtfvGs7fAoyaqbiA3ZXeX5grcNsg5x8",
    "recipient" : "FbvRnCM8gQdDig614qR1y1QY7x7sUN2RzXr3a9D9Rzw4",
    "title" : "jBhFV2kDyUvDxmYtodsO1D9ZpbX+j2vyOjVuMkgWa4vTyvM6VCqZWhutwYpMmXr1vdA=",
    "content" : "sX5/XOwcZe2RUIM2jd7Wlz9NoLTvgxPbNXZtNU8Qa4vT2qApB2rHFloNFdk+mHHo+NOfwk6RZjU3gnmUN0yi3eyUIOr2FYAoltdhx6C4Zmd9JQy5jVMzKg1HfD6K7daYbtN72ZTLfIxwlnAXG8z1+Rf9hZcmRNSIpFJ6lC2IUFcrFbulE8EsZuMPtrfuLlzNoW8HButmBlfbkMlALKcOcGYhVUCFhVAiL5FSgF+sHsUZe9CtubeGPlNT9m1y2joNZ8B4/rBn97XGV5odsaZZBO5gqcRQN4Y9SJSaNEbNFdaeWIFaO4NPT0r48eXKP4OGhYeQl4vCUQGG21U+GmcJiT4hiYJm41Xwp+qyjePlJ+om",
    "time" : 1504199349,
    "nonce" : "CWKAtBXqXu2ZuifeHnRBePw15e36gw3v9",
    "hash" : "965E9C4693C0B63C6F4CC6924A93354F0CE2B16F91BF0243FB5A355B4222D502",
    "signature" : "f0sPHFKukSbIahwkrYzPis9T5fP73QuH6UB76IdXN0JeWfg3Gh9A0oUc/YL78QmcKaM0FrD8JoK8BqYZNd1YAQ=="
}
```

`content` and `title` are encrypted for the issuer _box_ public key.
**Only the message issuer** will be able to decrypt this fields.  

#### `message/outbox`

`content` and `title` are encrypted for the recipient _box_ public key.
**Only the message recipient** will be able to decrypt this fields.

### `invitation/*`

#### `invitation/certification`

 - Get an invitation, by id: `invitation/certification/<id>`
 - Add a new invitation: `invitation/certification` (POST)
 - Delete an existing invitation: `invitation/certification/_delete` (POST)
 - Search on invitations: `invitation/certification/_search` (POST or GET)

## ES SUBSCRIPTION API

### `subscription/*`

#### `subscription/record`

 - Get an subscription, by id: `subscription/record/<id>`
 - Add a new subscription: `subscription/record` (POST)
 - Delete an existing subscription: `subscription/record/_delete` (POST)
 - Search on subscriptions: `subscription/record/_search` (POST or GET)
