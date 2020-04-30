---
title: openHAB
subcategory: Home Control
featured: true
---

# openHAB

Component Type: Processors (Subcategory: Home Control)

The openHAB component interfaces to an openHAB instance, which is used to configure and use a home control environment. Usually, openHAB is stand-alone. Therefore it has its own GUI, many many possible interfacing solutions (called bindings) and a configuration tool (based on Eclipse). More information is available here: [openHAB wiki][1].

This component uses the provided REST API of openHAB to read and write the state of different nodes (called items) within the openHAB system.

![Screenshot: openHAB demo with different options (./light, heating, temperature, ...)](./img/openhab_overview.png "Screenshot: openHAB demo with different options (light, heating, temperature, ...)")

openHAB demo with different options (light, heating, temperature, ...)

## Requirements

A functional openHAB installation, which is accessible via the web interface (the plugin connects via HTTP REST API). You can run either HTTP or HTTPS, in order to fulfill any security requirements. In addition, it is also possible to provide HTTP basic authentication with username/password. Please note, that any saved password in the AsTeRICS model is stored in the model file in PLAINTEXT!  
To run openHAB without password authentication, start openHAB with this command:

```sh
./start_debug.sh
```

To start with password authentication, use following command:

```sh
bash ./start_debug.sh -Djava.security.auth.login.config=./etc/login.conf
```

The user configuration is handled via this file:

```sh
openHAB_runtime/configurations/users.cfg
```

Please note, that the first line is necessary, so do not remove it!  
Further information on configuration and usage of openHAB is available on the openHAB GitHub page ([openHAB wiki][2]).

## Input Port Description

- **item1in \[string\]:** New state for item1 (the corresponding name is set in the property item1in). For example: set the property item1in to Light_GF_Bed_Ceiling and send "ON" to the input port to switch on the light which is connected to this item.
- **item2in \[string\]:** New state for item2 (the corresponding name is set in the property item2in). Example: see input port item1in
- **item3in \[string\]:** New state for item3 (the corresponding name is set in the property item3in). Example: see input port item1in
- **item4in \[string\]:** New state for item4 (the corresponding name is set in the property item4in). Example: see input port item1in
- **item5in \[string\]:** New state for item5 (the corresponding name is set in the property item5in). Example: see input port item1in
- **item6in \[string\]:** New state for item6 (the corresponding name is set in the property item6in). Example: see input port item1in
- **actionString \[string\]:** NOT IMPLEMENTED YET: more flexible input, where a random item (referenced by the name) can be set.

## Output Port Description

- **item1 \[string\] - item6 \[string\]:** The current state of the items1 to items6, corresponding to the item names of properties item1out to item6out

## Event Trigger Description

- **item1change - item6change:** This event is triggered if the corresponding item (set by the properties item1event to item6event) changes its state. The initial value is NOT raising an event.

## Properties

- **updaterate \[integer\]:** Time in milliseconds, which will ellapse between each status update. Default: 1s (1000ms)
- **hostname \[string\]:** Hostname to connect to. It is possible to use a hostname, an IP adress or a FQDN
- **port \[string\]:** Port of the openHAB installation. Defaults: 8080 for HTTP, 8443 for HTTPS. Please take care of any blocking firewall.
- **protocol \[string\]:** Protocol to connect to openHAB. Either http or https may be used (recommended: https).
- **lazyCertificates \[boolean\]:** If this property is set, any SSL related certificate check will be removed for the given hostname. This affects the hole ARE.
- **username \[string\]:** This property is used, if the HTTP basic authentication of openHAB is used. Provide the username here.
- **password \[string\]:** This property is used, if the HTTP basic authentication of openHAB is used. Provide the password here.
- **item1in \[string\]:** Item name, which is used for the input port 1 (set an openHAB item)
- **item2in \[string\]:** Item name, which is used for the input port 2 (set an openHAB item)
- **item3in \[string\]:** Item name, which is used for the input port 3 (set an openHAB item)
- **item4in \[string\]:** Item name, which is used for the input port 4 (set an openHAB item)
- **item5in \[string\]:** Item name, which is used for the input port 5 (set an openHAB item)
- **item6in \[string\]:** Item name, which is used for the input port 6 (set an openHAB item)
- **item1out \[string\]:** Item name, which is used for the output port 1 (fetch an openHAB item with the given updaterate)
- **item2out \[string\]:** Item name, which is used for the output port 2 (fetch an openHAB item with the given updaterate)
- **item3out \[string\]:** Item name, which is used for the output port 3 (fetch an openHAB item with the given updaterate)
- **item4out \[string\]:** Item name, which is used for the output port 4 (fetch an openHAB item with the given updaterate)
- **item5out \[string\]:** Item name, which is used for the output port 5 (fetch an openHAB item with the given updaterate)
- **item6out \[string\]:** Item name, which is used for the output port 6 (fetch an openHAB item with the given updaterate)
- **item1event \[string\]:** Item name, which is used to raise an event if the state is changed (item1change)
- **item2event \[string\]:** Item name, which is used to raise an event if the state is changed (item2change)
- **item3event \[string\]:** Item name, which is used to raise an event if the state is changed (item3change)
- **item4event \[string\]:** Item name, which is used to raise an event if the state is changed (item4change)
- **item5event \[string\]:** Item name, which is used to raise an event if the state is changed (item5change)
- **item6event \[string\]:** Item name, which is used to raise an event if the state is changed (item6change)

## Additional hints

- The model will stop with an error message, if one of the item names in the properties is not found.
- There is no feedback for checking a successful state change. E.g.: if your write to a read-only item (temperature sensor), nothing will happen
- Use the _lazyCertificates_ property with care, it will disable a major part of the SSL handshaking for the whole Java session. It should be limited to the given hostname only, but without warranty.
- The username/password combination from the properties is saved in PLAINTEXT in the model file, so handle it with care.

[1]: https://github.com/openhab/openhab/wiki
[2]: https://github.com/openhab/openhab/wiki
