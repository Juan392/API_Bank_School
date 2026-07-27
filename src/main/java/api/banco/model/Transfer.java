package api.banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transactions")
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransfer;
    @ManyToOne
    @JoinColumn(name = "id_account_origin")
    private Account accountOrigin;
    @ManyToOne
    @JoinColumn(name = "id_account_destiny")
    private Account accountDestiny;
    private BigDecimal amount;
    @Column(name = "transaction_date")
    private LocalDateTime transferDate;
}
