# libproject

Project has two implementations:

- Spring Framework
    - Full hexagonal architecture
    - No Spring Boot autoconfiguration used
    - Self made multi-profile actuator
    - Some self-made implementations of event-driven architecture
    - Some custom validation annotations

- Spring Boot (branch boot)
    - Three layer architecture
    - All stages profile support
    - Uses configured actuators
    - Prod Dockerfile