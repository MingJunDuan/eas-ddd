package xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.ticket;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.candidate.Candidate;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.exception.TicketException;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.training.TrainingId;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.tickethistory.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TicketTest {
    private TrainingId trainingId;
    private Candidate candidate;
    private Nominator nominator;

    @Before
    public void setUp() {
        trainingId = TrainingId.next();
        candidate = new Candidate("200901010110", "Tom", "tom@eas.com", trainingId);
        nominator = new Nominator("200901010007", "admin", "admin@eas.com", TrainingRole.Coordinator);
    }

    @Test
    public void should_generate_ticket_history_after_ticket_was_nominated() {
        Ticket ticket = new Ticket(TicketId.next(), trainingId);
        TicketHistory ticketHistory = ticket.nominate(candidate, nominator);
        assertTicketHistory(ticket, ticketHistory);
    }

    @Test
    public void ticket_status_should_be_WAIT_FOR_CONFIRM_after_ticket_was_nominated() {
        Ticket ticket = new Ticket(TicketId.next(), trainingId);

        ticket.nominate(candidate, nominator);

        assertThat(ticket.status()).isEqualTo(TicketStatus.WaitForConfirm);
        assertThat(ticket.nomineeId()).isEqualTo(candidate.employeeId());
    }

    @Test
    public void should_throw_TicketException_given_ticket_is_not_AVAILABLE() {
        Ticket ticket = new Ticket(TicketId.next(), trainingId, TicketStatus.WaitForConfirm);

        assertThatThrownBy(() -> ticket.nominate(candidate, nominator))
                .isInstanceOf(TicketException.class)
                .hasMessageContaining("ticket is not available");
    }

    private void assertTicketHistory(Ticket ticket, TicketHistory ticketHistory) {
        Assertions.assertThat(ticketHistory.ticketId()).isEqualTo(ticket.id());
        assertThat(ticketHistory.operationType()).isEqualTo(OperationType.Nomination);
        assertThat(ticketHistory.getTicketOwner()).isEqualTo(new TicketOwner(candidate.employeeId(), TicketOwnerType.Nominee));
        assertThat(ticketHistory.getStateTransit()).isEqualTo(StateTransit.from(TicketStatus.Available).to(TicketStatus.WaitForConfirm));
        assertThat(ticketHistory.operatedBy()).isEqualTo(new Operator(nominator.employeeId(), nominator.name()));
        assertThat(ticketHistory.operatedAt()).isEqualToIgnoringSeconds(LocalDateTime.now());
    }
}