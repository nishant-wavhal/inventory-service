# Production-Oriented Distributed Inventory Service

## Focus Areas:
- Event-Driven Architecture
- CQRS
- Kafka Integration
- Outbox Reliability
- Resilience Foundations
- Observability Foundations
- Saga Readiness
- Distributed Systems Design

 

## Complete Service flow :

```python

ENTRYPOINT (REST / Kafka / Scheduler)

        ↓
        
ExecutionContext
   (traceId + sagaId + operationType)
   
        ↓
        
Observability START (trace + logs)

        ↓
        
Resilience POLICY WRAP (local only)

        ↓
        
Business Logic (Inventory Service)

        ↓
        
Infra (DB / Kafka / Redis)

        ↓
        
Outcome CAPTURE
   - success/failure
   - failureType
   - compensatable flag
          
        ↓
     
Observability END

        ↓
        
Resilience Async Hook
   (local decision only)
   
        ↓
        
IF Saga step:
    Producer publish next event OR compensation event

```
## What is remaining :
 Implementation to get all the implemented systems together.
