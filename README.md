## How to run

# Prerequisites for running:

You need to have Docker, Java 17, and a compatible Gradle installed.
Ensure that ports 5432 and 8081 are available.

# Follow these steps:

1. Create the database and schema:

- Open a terminal in the root project folder.
- Run the command **docker-compose up**.

2. Start the application using your preferred method.
3. To interact with the API, you can use the Postman collection(folder "PostMan-collection" in root
   project folder).

To ensure that the project is functioning correctly, you can run the tests.

## Description of certain assumptions in the task implementation:

I didn't implement user authentication and security methods in my code because they were beyond the
scope of the task. However, I anticipate that there might be some integration with similar services,
involving token-based authentication and safeguards against unauthorized access.

Additionally, I made the assumption that my service would only be used by the frontend layer, which
is why I didn't separate Data Transfer Objects (DTO) into a separate module.

## Original README

## Drones

[[_TOC_]]

---

:scroll: **START**

### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the drone**. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

---

### Task description

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case **the load is medications**.

A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has: 
- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).


Develop a service via REST API that allows clients to communicate with the drones (i.e. **dispatch controller**). The specific communicaiton with the drone is outside the scope of this task. 

The service should allow:

- registering a drone;
- loading a drone with medication items;
- checking loaded medication items for a given drone;
- checking available drones for loading;
- check drone battery level for a given drone;

> Feel free to make assumptions for the design approach. 

---

### Requirements

While implementing your solution **please take care of the following requirements**: 

#### Functional requirements

- There is no need for UI;
- Prevent the drone from being loaded with more weight that it can carry;
- Prevent the drone from being in LOADING state if the battery level is **below 25%**; // ищем дрон
  в лоадинг
- Introduce a periodic task to check drones battery levels and create history/audit event log for
  this.

---

#### Non-functional requirements

- Input/output data must be in JSON format;
- Your project must be buildable and runnable;
- Your project must have a README file with build/run/test instructions (use DB that can be run locally, e.g. in-memory, via container);
- Any data required by the application to run (e.g. reference tables, dummy data) must be preloaded in the database;
- Unit tests;
- Use a framework of your choice, but popular, up-to-date, and long-term support versions are recommended.

---

:scroll: **END** 
