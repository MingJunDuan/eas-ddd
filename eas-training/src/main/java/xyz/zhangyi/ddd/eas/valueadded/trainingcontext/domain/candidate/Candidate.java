package xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.candidate;

import xyz.zhangyi.ddd.core.stereotype.Aggregate;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.ticket.Nominee;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.tickethistory.TicketOwner;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.tickethistory.TicketOwnerType;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.training.TrainingId;

import java.util.UUID;

@Aggregate
public class Candidate {
    private String id;
    private String employeeId;
    private String name;
    private String email;
    private TrainingId trainingId;

    public Candidate(String EmployeeId, String name, String email, TrainingId trainingId) {
        this.id = UUID.randomUUID().toString();
        this.employeeId = EmployeeId;
        this.name = name;
        this.email = email;
        this.trainingId = trainingId;
    }

    public String employeeId() {
        return employeeId;
    }

    public String name() {
        return this.name;
    }

    public String email() {
        return email;
    }

    public TrainingId trainingId() {
        return trainingId;
    }

    public TicketOwner toOwner() {
        return new TicketOwner(employeeId, TicketOwnerType.Nominee);
    }

    public Nominee toNominee() {
        return new Nominee(employeeId, name, email);
    }
}