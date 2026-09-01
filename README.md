# LifeMatch

## Description

**LifeMatch** is an information system designed for the **simulated management of organ transplants**. It allows users to register and manage transplant candidates, donors, and available organs.

The system helps organize the transplant waiting list and supports the matching of available organs with eligible candidates. It uses **simulated compatibility and priority criteria** to rank candidates and identify the most suitable candidate for an available organ.

## Main Entities (Data Base)

- **Candidates:** Stores information about candidates on the transplant waiting list, such as identification, blood type, required organ, priority level, waiting list entry date, and status.

- **Donors:** Stores relevant donor information, including identification, blood type, and donation-related information.

- **Organs:** Stores information about donated organs, including organ type, availability date, and status (available, reserved, or transplanted).


## Main Business Rules

The main business logic of **LifeMatch** is the **simulated matching and ranking of candidates for an available organ**.

When a new organ is registered, the system searches for candidates who require that type of organ and checks their eligibility.

For each eligible candidate, the system calculates a **compatibility and priority score** based on simulated criteria such as:

- Blood type compatibility;
- Candidate priority level;
- Time spent on the waiting list;
- Match between the required organ and the available organ.

The candidates are then ranked according to their score, allowing the system to identify the candidate with the highest priority based on the defined rules.

When an organ is assigned to a candidate, the system updates the corresponding statuses and prevents the same organ from being assigned to more than one candidate at the same time. 
